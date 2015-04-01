/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionmanager.property;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 
 * 
 * 
 *
 */
public class PropertyReader
{
	public static Map<String, String> propertMap = null;

	public static String readProperty(String fileName, String errorCode)
	{

		Properties prop = new Properties();

		InputStream input = null;

		try
		{
			// propertMap=new HashMap<String, String>();

			input = new FileInputStream(getPropertiesDirectory() + fileName);
			prop.load(input);
			return prop.getProperty(errorCode);
			// propertMap.put(errorCode, prop.getProperty(errorCode));

		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return "";

	}

	/**
	 * Method to get the root directory of middleware config files.
	 * 
	 * @return
	 */
	private static String getPropertiesDirectory()
	{
		String dirPath = null;
		/* if (SecurityFilter.REGION_PARAM == null) { */

		if (System.getProperty("middleware.home") != null)
		{
			dirPath = System.getProperty("middleware.home") + "/middleware/middleware-bootconfig/";
		}
		else
		{
			dirPath = "C:\\KonyOne\\KonyOne-Server\\install\\middleware\\middleware-bootconfig";
		}
		return dirPath;
	}
}