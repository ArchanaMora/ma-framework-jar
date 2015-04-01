package com.wellpoint.mobility.aggregation.core.configuration.impl;

import java.util.HashMap;
import java.util.Map;
/**
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * This class was created for testing the configuration manager. 
 * It is a concrete implementation of the ConfigImpl class.
 * 
 * 
 * @author robert.swarr@wellpoint.com
 *
 */
public class CacheConfig extends ConfigImpl
{
	
	public static final String TYPE_USER_CACHE = "USER_CACHE";
	public static final String TYPE_APPLICATION_CACHE = "APPLICATION_CACHE";
	public static final String TYPE_METHOD_CACHE = "METHOD_CACHE";
	
	private String configType;
	private Map<String,String> configEntries = new HashMap<String,String>();
	

	/**
	 * Default, zero argument constructor.
	 */
	public CacheConfig()
	{
		super();
	}


	/**
	 * Optional constructor with one argument.
	 * @param configType
	 */
	public CacheConfig(String configType)
	{
		super();
		this.configType = configType;
	}


	/**
	 * Accessor method for the configType field.
	 * @return String
	 */
	public String getConfigType()
	{
		return configType;
	}


	/**
	 * Mutator method for the configType field.
	 * @param configType
	 */
	public void setConfigType(String configType)
	{
		this.configType = configType;
	}


	/**
	 * Accessor method for the configEntries field.
	 * @return Map<String,String>
	 */
	public Map<String, String> getConfigEntries()
	{
		return configEntries;
	}


	/**
	 * Mutator method for the configEntries field.
	 * @param configEntries
	 */
	public void setConfigEntries(Map<String, String> configEntries)
	{
		this.configEntries = configEntries;
	}
	
}
