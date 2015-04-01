package com.wellpoint.mobility.aggregation.core.configuration;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationManager;
import com.wellpoint.mobility.aggregation.core.configuration.impl.CacheConfig;
import com.wellpoint.mobility.aggregation.core.configuration.impl.ConfigurationManagerImpl;


public class TestMergeConfiguration {
	
	
	private String configType;
	private Map<String,String> configValues;
	
	@Before
	public void setUp() {
		configType = "APPLICATION";
		configValues = new HashMap<String, String>();
		configValues.put("SIZE","4056MB");
		configValues.put("KEY2", "654321");
		configValues.put("KEY3", "777777");
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
