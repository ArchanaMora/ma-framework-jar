/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.configuration;

import com.wellpoint.mobility.aggregation.core.configuration.pojo.Config;

/**
 * ConfigManager.
 * 
 * @author edward.biton@wellpoint.com
 */
public interface ConfigManager
{
	/**
	 * Add a config listener
	 * 
	 * @param configListener
	 */
	public void addConfigListener(ConfigListener configListener);

	/**
	 * Removes a config listener
	 * 
	 * @param configListener
	 */
	public void removeConfigListener(ConfigListener configListener);

	/**
	 * save the configuration
	 * 
	 * @param config
	 */
	public void saveConfig(Config config);

	/**
	 * load the configuration
	 * 
	 * @param config
	 * @return
	 */
	public Config loadConfig(Config config);
}
