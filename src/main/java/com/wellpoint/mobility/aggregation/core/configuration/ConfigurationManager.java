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
 * Interface for the ConfigurationManager.  The implementation of the configuration
 * manager uses this interface.
 * 
 * @see com.wellpoint.mobility.aggregation.core.configuration.impl
 * 
 * @author robert.swarr@wellpoint.com
 *
 */
public interface ConfigurationManager {
	/**
	 * Stores the configuration values in the data transfer object in the database.
	 * Returns true if the save is successful. 
	 * @param config
	 * @param configType
	 * @return boolean
	 */
	public boolean saveConfiguration(Config config, String configType);
	
	/**
	 * @Deprecated -- Please use the more powerful loadConfiguration(Config, boolean)<p>
	 * 
	 * Retrieves the configuration from the database for the component
	 * and loads the data in the ConfigurationDTO.
	 * @param configComponent
	 * @return Config
	 */
	@Deprecated
	public Config loadConfiguration(Config configuration);
	
	
	/**
	 * Retrieves the configuration from the database for the component
	 * and loads the data in the ConfigurationDTO.
	 * @param configComponent
	 * @param useDefault if true and the configuration can't be loaded, the configuration.getDefaultConfig()
	 *    method will be called AND saved to DB.
	 * @return Config a object or null if can't be loaded and useDefault == false
	 */
	public Config loadConfiguration(final Config configuration, final boolean useDefault);

}
