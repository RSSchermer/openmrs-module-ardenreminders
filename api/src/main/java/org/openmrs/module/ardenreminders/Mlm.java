/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ardenreminders;

import arden.compiler.CompiledMlm;
import arden.compiler.Compiler;
import arden.compiler.CompilerException;
import org.openmrs.BaseOpenmrsData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Please note that a corresponding table schema must be created in liquibase.xml.
 */
//Uncomment 2 lines below if you want to make the Item class persistable, see also ArdenRemindersDaoTest and liquibase.xml
@Entity(name = "ardenreminders.Mlm")
@Table(name = "ardenreminders_mlm")
public class Mlm extends BaseOpenmrsData {
	
	@Id
	@GeneratedValue
	@Column(name = "ardenreminders_mlm_id")
	private Integer id;
	
	@Basic
	@Column(name = "name", length = 255, unique = true)
	@NotNull
	private String name;
	
	@Basic
	@Lob
	@Column(name = "source", length = 2 ^ 16)
	private String source;
	
	@Basic
	@Lob
	@Column(name = "byte_code", length = 2 ^ 16)
	private byte[] byteCode;
	
	@Basic
	@Column(name = "evoke")
	private Boolean evoke;
	
	@Transient
	private CompiledMlm compiledCache;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String getUuid() {
		return super.getUuid();
	}
	
	@Override
	public void setUuid(String uuid) {
		super.setUuid(uuid);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getByteCode() {
		return source;
	}
	
	public void updateByteCode() throws CompilerException {
		compiledCache = null;
		
		try {
			CompiledMlm compiled = compile();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			try {
				compiled.saveClassFile(outputStream);
			}
			catch (IOException e) {
				// Should never happen, we're not actually doing IO
				throw new RuntimeException(e);
			}
			
			byteCode = outputStream.toByteArray();
		}
		catch (CompilerException e) {
			byteCode = null;
			
			throw e;
		}
	}
	
	public Boolean getEvoke() {
		return evoke;
	}
	
	public void setEvoke(Boolean evoke) {
		this.evoke = evoke;
	}
	
	public CompiledMlm compiled() throws CompilerException {
		if (compiledCache != null) {
			return compiledCache;
		}
		
		if (byteCode == null) {
			compiledCache = compile();
		} else {
			compiledCache = new CompiledMlm(byteCode, name);
		}
		
		return compiledCache;
	}
	
	private CompiledMlm compile() throws CompilerException {
		Compiler compiler = new Compiler();
		
		compiler.enableDebugging(name);
		
		try {
			return compiler.compileMlm(new StringReader(source));
		}
		catch (IOException e) {
			// Should never happen, we're not actually doing IO
			throw new RuntimeException(e);
		}
	}
}
