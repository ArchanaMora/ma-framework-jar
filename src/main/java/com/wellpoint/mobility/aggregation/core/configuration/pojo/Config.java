/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.configuration.pojo;

/**
 * A base class configuration file
 * 
 * @author edward.biton@wellpoint.com
 */
public class Config
{
	/**
	 * name of the config
	 */
	private String name;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public Config(String name)
	{
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
