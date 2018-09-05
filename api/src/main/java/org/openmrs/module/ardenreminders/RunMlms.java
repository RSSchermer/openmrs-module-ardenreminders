package org.openmrs.module.ardenreminders;

import arden.compiler.CompilerException;
import arden.runtime.*;
import arden.runtime.evoke.CallTrigger;
import arden.runtime.jdbc.JDBCQuery;
import org.hibernate.jdbc.Work;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RunMlms implements Work {
	
	private List<Mlm> mlms;
	
	private int patientId;
	
	private RunMlmsResults results;
	
	public RunMlms(List<Mlm> mlms, int patientId) {
		this.mlms = mlms;
		this.patientId = patientId;
	}
	
	@Override
	public void execute(Connection connection) throws SQLException {
		ArdenExecutionSession session = new ArdenExecutionSession(connection, patientId);
		
		for (Mlm mlm : mlms) {
			session.runMlm(mlm);
		}
		
		results = session.getResults();
	}
	
	public RunMlmsResults getResults() {
		return results;
	}
	
	private class ArdenExecutionSession extends ExecutionContext {
		
		private Connection connection;
		
		private int patientId;
		
		private ArrayList<String> messages = new ArrayList<String>();
		
		private ArrayList<String> errors = new ArrayList<String>();
		
		public ArdenExecutionSession(Connection connection, int patientId) {
			this.connection = connection;
			this.patientId = patientId;
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
