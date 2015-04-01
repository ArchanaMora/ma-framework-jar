package com.wellpoint.mobility.aggregation.core.configuration.impl;

import org.apache.commons.lang.NotImplementedException;

import com.wellpoint.mobility.aggregation.core.configuration.Config;

/**
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * Base class for a configuration object: the data from this object is used to construct
 * a Configuration entity.
 * 
 * @author robert.swarr@wellpoint.com
 *
 */
public abstract class ConfigImpl implements Config 
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7064111821611102697L;
	private final String className = this.getClass().getSimpleName();
	
	/**
	 * Default, no-argument constructor.
	 */
	public ConfigImpl() 
	{
		super();
	}
	
	/**
	 * Accessor method for The component class name.
	 */
	public String getConfigName() {
		return className;
	}
	
	
	/* (non-Javadoc)
	 * @see com.wellpoint.mobility.aggregation.core.configuration.Config#getDefaultConfig()
	 */
	public Config getDefaultConfig() {
		throw new NotImplementedException("The class with config name=" + getConfigName() + " hasn't implemented this method!");
	} // of getDefaultConfig
	
}
