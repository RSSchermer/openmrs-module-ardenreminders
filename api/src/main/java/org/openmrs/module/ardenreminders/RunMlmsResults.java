package org.openmrs.module.ardenreminders;

import java.util.List;

public interface RunMlmsResults {
	
	List<String> getMessages();
	
	List<String> getErrors();
}
