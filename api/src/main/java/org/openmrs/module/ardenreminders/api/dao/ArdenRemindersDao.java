/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ardenreminders.api.dao;

import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ardenreminders.Mlm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ardenreminders.ArdenRemindersDao")
public class ArdenRemindersDao {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private DbSession getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public List<Mlm> listMlms() {
		return (List<Mlm>) getSession().createCriteria(Mlm.class).list();
	}
	
	public Mlm getMlmByUuid(String uuid) {
		return (Mlm) getSession().createCriteria(Mlm.class).add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	public Mlm saveMlm(Mlm mlm) {
		getSession().saveOrUpdate(mlm);
		
		return mlm;
	}
	
	public void deleteMlm(Mlm mlm) {
		getSession().delete(mlm);
	}
}
