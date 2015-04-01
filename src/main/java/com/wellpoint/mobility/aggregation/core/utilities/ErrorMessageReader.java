/*
 * Copyright (c) 2013 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.utilities;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

/**
 * class implementation for ErrorMessageReader
 * 
 * @author Aneesh.Arjunan@wellpoint.com
 */
public final class ErrorMessageReader
{
	/**
	 * instance of DIR_PATH
	 */
	private static final String DIR_PATH = "C:\\KMAP-Server-4.0\\install\\middleware\\middleware-bootconfig";

	/**
	 * instance of MIDDLEWARE_HOME
	 */
	private static final String MIDDLEWARE_HOME = "middleware.home";

	/**
	 * instance of ERRORMESSAGE_PROPERTIES
	 */
	private static final String ERRORMESSAGE_PROPERTIES = "ErrorMessages.properties";

	/**
	 * Logs statements to middleware.log file in the server.
	 */
	private static final Logger logger = Logger.getLogger(ErrorMessageReader.class);

	/**
	 * configurable properties e.g. EndPoint URLs, Brand specific props etc.
	 */
	private static PropertiesConfiguration CONFIG_PROP = null;

	/**
	 * initialize wellpointConfigPF properties
	 */
	static
	{
		FileChangedReloadingStrategy reloadingStrategy = null;
		try
		{
			CONFIG_PROP = new PropertiesConfiguration(ErrorMessageReader.getPropertiesDirectory() + ERRORMESSAGE_PROPERTIES);
		}
		catch (ConfigurationException e)
		{
			logger.error(e.getMessage(), e);
		}
		reloadingStrategy = new FileChangedReloadingStrategy();
		reloadingStrategy.setRefreshDelay(1000 * 60 * 10);
		CONFIG_PROP.setReloadingStrategy(reloadingStrategy);
	}

	/**
	 * To suppress instantiation as its utility class with static method.
	 */
	private ErrorMessageReader()
	{
		super();
	}

	/**
	 * @return site minder properties.
	 */
	public static PropertiesConfiguration getConfigProp()
	{
		return CONFIG_PROP;
	}

	/**
	 * @return properties directory location.
	 */
	private static String getPropertiesDirectory()
	{
		
		String dirPath = null;
		logger.info("middleware.home:" + System.getProperty(MIDDLEWARE_HOME));
		if (null != System.getProperty(MIDDLEWARE_HOME))
		{
			dirPath = System.getProperty(MIDDLEWARE_HOME) + "/middleware/middleware-bootconfig/";
		}
		else
		{
			dirPath = DIR_PATH;
		}
		logger.info("Config Dir:" + dirPath);
		return dirPath;
	}
}