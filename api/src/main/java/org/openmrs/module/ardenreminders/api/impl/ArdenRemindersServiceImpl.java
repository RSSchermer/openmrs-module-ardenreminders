/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ardenreminders.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ardenreminders.RunMlms;
import org.openmrs.module.ardenreminders.Mlm;
import org.openmrs.module.ardenreminders.RunMlmsResults;
import org.openmrs.module.ardenreminders.api.ArdenRemindersService;
import org.openmrs.module.ardenreminders.api.dao.ArdenRemindersDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class ArdenRemindersServiceImpl extends BaseOpenmrsService implements ArdenRemindersService {
	
	ArdenRemindersDao dao;
	
	UserService userService;
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(ArdenRemindersDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public List<Mlm> listMlms() throws APIException {
		return dao.listMlms();
	}
	
	@Override
	public List<Mlm> listEvocableMlms() throws APIException {
		return dao.listEvocableMlms();
	}
	
	@Override
	public Mlm getMlmByUuid(String uuid) throws APIException {
		return dao.getMlmByUuid(uuid);
	}
	
	@Override
	public Mlm saveMlm(Mlm mlm) throws APIException {
		return dao.saveMlm(mlm);
	}
	
	@Override
	public void deleteMlm(Mlm mlm) throws APIException {
		dao.deleteMlm(mlm);
	}
	
	@Override
	public void deleteMlmByUuid(String uuid) throws APIException {
		dao.deleteMlmByUuid(uuid);
	}
	
	@Override
	public RunMlmsResults generateReminders(int patientId) throws APIException {
		RunMlms work = new RunMlms(listEvocableMlms(), patientId, dao);
		
		sessionFactory.getCurrentSession().doWork(work);
		
		return work.getResults();
	}
}
