package com.wellpoint.mobility.aggregation.core.configuration;
/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * JUnit test for the saveConfiguration method of the ConfigurationManager.
 * 
 * 
 * @author robert.swarr@wellpoint.com
 *
 */

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationManager;
import com.wellpoint.mobility.aggregation.core.configuration.impl.CacheConfig;
import com.wellpoint.mobility.aggregation.core.configuration.impl.ConfigurationManagerImpl;

public class TestPersistConfiguration {
	
	
	private String configType;
	private Map<String,String> configValues;
	
	
	@Before
	public void setUp() {
		configType = "APPLICATION";
		configValues = new HashMap<String, String>();
		configValues.put("SIZE","1024MB");
		configValues.put("KEY2", "123456");
		configValues.put("KEY3", "99999");
		// Q & D initialization of LOG4j
		org.apache.log4j.BasicConfigurator.configure();
	}

	@Test
	public void testSaveConfiguration() {
		ConfigurationManager cfgmgr = new ConfigurationManagerImpl();
		CacheConfig cacheConfig = new CacheConfig(configType);
		cacheConfig.setConfigEntries(configValues);
		boolean success = cfgmgr.saveConfiguration(cacheConfig, configType);
		assertTrue(success);
	}

}
