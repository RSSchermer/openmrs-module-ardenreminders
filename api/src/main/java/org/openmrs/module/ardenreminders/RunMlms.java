package org.openmrs.module.ardenreminders;

import arden.compiler.CompilerException;
import arden.runtime.*;
import arden.runtime.evoke.CallTrigger;
import arden.runtime.evoke.Trigger;
import arden.runtime.jdbc.JDBCQuery;
import org.hibernate.jdbc.Work;
import org.openmrs.module.ardenreminders.api.dao.ArdenRemindersDao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RunMlms implements Work {
	
	private ArdenRemindersDao dao;
	
	private List<Mlm> mlms;
	
	private int patientId;
	
	private RunMlmsResults results;
	
	public RunMlms(List<Mlm> mlms, int patientId, ArdenRemindersDao dao) {
		this.mlms = mlms;
		this.patientId = patientId;
		this.dao = dao;
	}
	
	@Override
	public void execute(Connection connection) throws SQLException {
		ArdenExecutionSession session = new ArdenExecutionSession(dao, connection, patientId);
		
		for (Mlm mlm : mlms) {
			session.cacheMlm(mlm);
		}
		
		for (Mlm mlm : mlms) {
			session.runMlm(mlm);
		}
		
		results = session.getResults();
	}
	
	public RunMlmsResults getResults() {
		return results;
	}
	
	private class ArdenExecutionSession extends ExecutionContext {
		
		private ArdenRemindersDao dao;
		
		private Connection connection;
		
		private int patientId;
		
		private ArrayList<String> messages = new ArrayList<String>();
		
		private ArrayList<String> errors = new ArrayList<String>();
		
		private HashMap<String, Mlm> mlmCache = new HashMap<String, Mlm>();
		
		public ArdenExecutionSession(ArdenRemindersDao dao, Connection connection, int patientId) {
			this.dao = dao;
			this.connection = connection;
			this.patientId = patientId;
		}
		
		public void cacheMlm(Mlm mlm) {
			mlmCache.put(mlm.getName(), mlm);
		}
		
		public void runMlm(Mlm mlm) {
			try {
				mlm.compiled().run(this, new ArdenValue[0], new CallTrigger());
			}
			catch (CompilerException e) {
				errors.add("Failed to compile `" + mlm.getName() + "`: " + e.getMessage());
			}
			catch (InvocationTargetException e) {
				errors.add("Error while running `" + mlm.getName() + "`: " + e.getCause().getMessage());
			}
		}
		
		public RunMlmsResults getResults() {
			return new RunMlmsResultsImpl(messages, errors);
		}
		
		public DatabaseQuery createQuery(MedicalLogicModule mlm, String mapping) {
			return new JDBCQuery(mapping.replaceAll("__CURRENT_PATIENT_ID__", Integer.toString(patientId)), connection);
		}
		
		public void write(ArdenValue message, ArdenValue destination, double urgency) {
			messages.add(ArdenString.getStringFromValue(message));
		}
		
		@Override
		public MedicalLogicModule findModule(String name, String institution) {
			Mlm mlm = mlmCache.get(name);
			
			if (mlm == null) {
				mlm = dao.getMlmByName(name);
				
				if (mlm == null) {
					throw new RuntimeException("Could not find MLM `" + name + "`");
				}
				
				cacheMlm(mlm);
			}
			
			try {
				return mlm.compiled();
			}
			catch (CompilerException e) {
				throw new RuntimeException("Failed to compile MLM `" + name + "`: " + e.getMessage());
			}
		}
		
		@Override
		public void call(ArdenRunnable mlm, ArdenValue[] arguments, ArdenValue delay, Trigger callerTrigger, double urgency) {
			long delayMillis = ExecutionContextHelpers.delayToMillis(delay);
			Trigger calleeTrigger = ExecutionContextHelpers.combine(callerTrigger, delayMillis);
			
			try {
				mlm.run(this, arguments, calleeTrigger);
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException("Error while running CALLed MLM: " + e.getCause().getMessage());
			}
		}
		
		private class RunMlmsResultsImpl implements RunMlmsResults {
			
			private List<String> messages;
			
			private List<String> errors;
			
			RunMlmsResultsImpl(List<String> messages, List<String> errors) {
				this.messages = messages;
				this.errors = errors;
			}
			
			public List<String> getMessages() {
				return messages;
			}
			
			public List<String> getErrors() {
				return errors;
			}
		}
	}
}
