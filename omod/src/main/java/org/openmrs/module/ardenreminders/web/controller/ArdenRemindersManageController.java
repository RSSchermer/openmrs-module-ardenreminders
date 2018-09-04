/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ardenreminders.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ardenreminders.Mlm;
import org.openmrs.module.ardenreminders.api.ArdenRemindersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller("${rootrootArtifactid}.ArdenRemindersManageController")
@RequestMapping(value = "module/ardenreminders/manage")
public class ArdenRemindersManageController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	ArdenRemindersService ardenRemindersService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	@Qualifier("ardenreminders.MlmTemplate")
	String mlmTemplate;
	
	@RequestMapping(value = "mlms.list", method = RequestMethod.GET)
	public String listMlms(Model model) {
		model.addAttribute("mlms", ardenRemindersService.listMlms());
		
		return "/module/ardenreminders/manage/mlmsList";
	}
	
	@RequestMapping(value = "mlms/{uuid}.htm", method = RequestMethod.GET)
	public String viewMlm(@PathVariable String uuid, Model model) {
		model.addAttribute("mlm", ardenRemindersService.getMlmByUuid(uuid));
		
		return "/module/ardenreminders/manage/mlmView";
	}
	
	@RequestMapping(value = "mlms/new.form", method = RequestMethod.GET)
	public String newMlm(Model model) {
		model.addAttribute("mlm", new Mlm());
		
		return "/module/ardenreminders/manage/mlmNewForm";
	}
	
	@RequestMapping(value = "mlms.list", method = RequestMethod.POST)
	public String createMlm(@Valid @ModelAttribute Mlm mlm, BindingResult errors) {
		
		if (errors.hasErrors()) {
			return "/module/ardenreminders/manage/mlmNew";
		}
		
		String source = mlmTemplate.replaceAll("__MLM_NAME__", mlm.getName())
		        .replaceAll("__AUTHOR_NAME__", Context.getAuthenticatedUser().getPersonName().getFullName())
		        .replaceAll("__DATE__", new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
		
		mlm.setSource(source);
		ardenRemindersService.saveMlm(mlm);
		
		return String.format("redirect:/module/ardenreminders/manage/mlms/%s.htm", mlm.getUuid());
	}
}
