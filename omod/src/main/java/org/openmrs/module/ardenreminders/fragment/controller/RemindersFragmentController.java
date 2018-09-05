package org.openmrs.module.ardenreminders.fragment.controller;

import org.openmrs.module.ardenreminders.api.ArdenRemindersService;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class RemindersFragmentController {
	
	public void controller(FragmentModel model, @FragmentParam("patientId") int patientId,
	        @SpringBean("ardenreminders.ArdenRemindersService") ArdenRemindersService service) {
		model.addAttribute("results", service.generateReminders(patientId));
	}
}
