/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ardenreminders.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ardenreminders.ArdenRemindersConfig;
import org.openmrs.module.ardenreminders.Mlm;
import org.openmrs.module.ardenreminders.RunMlmsResults;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface ArdenRemindersService extends OpenmrsService {
	
	@Authorized()
	@Transactional(readOnly = true)
	List<Mlm> listMlms() throws APIException;
	
	@Authorized()
	@Transactional(readOnly = true)
	List<Mlm> listEvocableMlms() throws APIException;
	
	@Authorized()
	@Transactional(readOnly = true)
	Mlm getMlmByUuid(String uuid) throws APIException;
	
	@Authorized(ArdenRemindersConfig.MODULE_PRIVILEGE)
	@Transactional
	Mlm saveMlm(Mlm mlm) throws APIException;
	
	@Authorized(ArdenRemindersConfig.MODULE_PRIVILEGE)
	@Transactional
	void deleteMlm(Mlm mlm) throws APIException;
	
	@Authorized(ArdenRemindersConfig.MODULE_PRIVILEGE)
	@Transactional
	void deleteMlmByUuid(String uuid) throws APIException;
	
	RunMlmsResults generateReminders(int patientId) throws APIException;
}
