package com.wellpoint.mobility.aggregation.core.configuration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationManager;
import com.wellpoint.mobility.aggregation.core.configuration.impl.CacheConfig;
import com.wellpoint.mobility.aggregation.core.configuration.impl.ConfigurationManagerImpl;

public class TestLoadConfiguration {
	
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
	public void testLoadConfiguration() {
		CacheConfig cacheConfig = new CacheConfig(configType);
		ConfigurationManager cfgmgr = new ConfigurationManagerImpl();
		cacheConfig = (CacheConfig) cfgmgr.loadConfiguration(cacheConfig);
		assertNotNull(cacheConfig);
		if (cacheConfig != null)
		{
			assertEquals(cacheConfig.getConfigType(), configType);
			assertEquals(cacheConfig.getConfigEntries(), configValues);
		}
	}

}
