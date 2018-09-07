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

import arden.compiler.CompilerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ardenreminders.Mlm;
import org.openmrs.module.ardenreminders.api.ArdenRemindersService;
import org.openmrs.module.ardenreminders.web.form.NewMlmForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	
	@RequestMapping(value = "mlms/{uuid}/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public void deleteMlm(@PathVariable String uuid) {
		ardenRemindersService.deleteMlmByUuid(uuid);
	}
	
	@RequestMapping(value = "mlms/new.form", method = RequestMethod.GET)
	public String newMlm(Model model) {
		model.addAttribute("newMlmForm", new NewMlmForm());
		
		return "/module/ardenreminders/manage/mlmNewForm";
	}
	
	@RequestMapping(value = "mlms.list", method = RequestMethod.POST)
	public String createMlm(@ModelAttribute NewMlmForm newMlmForm, BindingResult errors, Model model) {
		if (errors.hasErrors()) {
			return "/module/ardenreminders/manage/mlmNewForm";
		}
		
		String name = newMlmForm.getName();
		
		if (name == null || name.length() == 0) {
			model.addAttribute("error", "Name must not be blank");
			
			return "/module/ardenreminders/manage/mlmNewForm";
		}
		
		if (!name.matches("[a-zA-Z0-9\\-_]+")) {
			model.addAttribute("error", "Name must consist of only letters, numbers, dash and underscores");
			
			return "/module/ardenreminders/manage/mlmNewForm";
		}
		
		if (ardenRemindersService.getMlmByName(name) != null) {
			model.addAttribute("error", "Name is already taken");
			
			return "/module/ardenreminders/manage/mlmNewForm";
		}
		
		Mlm mlm = new Mlm();
		
		String source = mlmTemplate.replaceAll("__MLM_NAME__", name)
		        .replaceAll("__AUTHOR_NAME__", Context.getAuthenticatedUser().getPersonName().getFullName())
		        .replaceAll("__DATE__", new SimpleDateFormat("yyyy-mm-dd").format(new Date()));
		
		mlm.setSource(source);
		
		try {
			mlm.updateFromSource();
		}
		catch (CompilerException e) {
			// Should never happen, indicates bug in resources/template.mlm or the placeholder substitution code above
			// (programmer error)
			throw new RuntimeException(e);
		}
		
		ardenRemindersService.saveMlm(mlm);
		
		return String.format("redirect:/module/ardenreminders/manage/mlms/%s.htm", mlm.getUuid());
	}
	
	@RequestMapping(value = "mlms/{uuid}/enable_evoke.json", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void enableMlmEvoke(@PathVariable String uuid) {
		Mlm mlm = ardenRemindersService.getMlmByUuid(uuid);
		
		mlm.setEvoke(true);
		ardenRemindersService.saveMlm(mlm);
	}
	
	@RequestMapping(value = "mlms/{uuid}/disable_evoke.json", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void disableMlmEvoke(@PathVariable String uuid) {
		Mlm mlm = ardenRemindersService.getMlmByUuid(uuid);
		
		mlm.setEvoke(false);
		ardenRemindersService.saveMlm(mlm);
	}
	
	@RequestMapping(value = "mlms/{uuid}/edit_source.htm", method = RequestMethod.GET)
	public String editMlmSource(@PathVariable String uuid, Model model) {
		model.addAttribute("mlm", ardenRemindersService.getMlmByUuid(uuid));
		
		return "/module/ardenreminders/manage/editMlmSource";
	}
	
	@RequestMapping(value = "mlms/{uuid}/check_source.json", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> checkMlmSource(@PathVariable String uuid, @RequestParam String source) {
		HashMap<String, String> response = new HashMap<String, String>();
		Mlm mlm = ardenRemindersService.getMlmByUuid(uuid);
		
		mlm.setSource(source);
		
		try {
			mlm.updateFromSource();
			response.put("output", "Success!");
			
		}
		catch (Exception e) {
			response.put("output", e.getMessage());
		}
		
		return response;
	}
	
	@RequestMapping(value = "mlms/{uuid}/save_source.json", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Map<String, String> saveMlmSource(@PathVariable String uuid, @RequestParam String source) {
		HashMap<String, String> response = new HashMap<String, String>();
		Mlm mlm = ardenRemindersService.getMlmByUuid(uuid);
		
		String oldName = mlm.getName();
		
		mlm.setSource(source);
		
		try {
			mlm.updateFromSource();
			response.put("output", "Success!");
		}
		catch (Exception e) {
			response.put("output", e.getMessage());
			mlm.setCompiles(false);
		}
		
		try {
			ardenRemindersService.saveMlm(mlm);
		}
		catch (Exception e) {
			response.put("output", "Name already taken!");
			mlm.setName(oldName);
			ardenRemindersService.saveMlm(mlm);
		}
		
		return response;
	}
}
