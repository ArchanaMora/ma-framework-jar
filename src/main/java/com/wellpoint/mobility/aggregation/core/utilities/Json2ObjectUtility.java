/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.utilities;

import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A utility class to convert object to json and vice versa
 * 
 * @author edward.biton@wellpoint.com
 */
public class Json2ObjectUtility
{
	/**
	 * Single instance of the Gson object to convert object to json and vice versa
	 */
	public static Gson gson = null;
	
	static
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		
        gsonBuilder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Serializer());
        gsonBuilder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Deserializer());
        gson = gsonBuilder.create();
	}

	/**
	 * Private constructor for utility classes
	 */
	private Json2ObjectUtility()
	{
	}

	/**
	 * Converts a json to an object
	 * 
	 * @param json
	 *            json to be converted
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object toObject(String json, @SuppressWarnings("rawtypes") Class clazz)
	{
		if(json == null)
		{
			return null ;
		}
		return gson.fromJson(json, clazz);
	}

	/**
	 * Converts an object to an json string
	 * 
	 * @param object
	 *            object to be converted
	 * @return
	 */
	public static String toJson(Object object)
	{
		if(object == null)
		{
			return null;
		}
		return gson.toJson(object);
	}
}
