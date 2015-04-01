package com.wellpoint.mobility.aggregation.core.configuration;
/**
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * The interface for a configuration object: implementations of a configuration object
 * use this interface.
 * 
 * @see com.wellpoint.mobility.aggregation.core.configuration.impl
 * 
 * @author robert.swarr@wellpoint.com
 *
 */
public interface Config extends java.io.Serializable
{
	public String getConfigName();
	
	/**
	 * @return a fully configured default object representing the configuration
	 */
	public Config getDefaultConfig();
	
}
