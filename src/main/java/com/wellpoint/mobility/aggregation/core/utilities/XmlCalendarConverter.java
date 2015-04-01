/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 */
package com.wellpoint.mobility.aggregation.core.utilities;


import java.lang.reflect.Type;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
/**
 * Utility class to parse XmlCalanderConverter
 * @author aneesh.arjunan@wellpoint.com
 */
public class XmlCalendarConverter
{
	public static class Serializer implements JsonSerializer
	{
		public Serializer()
		{
			super();
		}

		/**
		 * serialize method
		 */
		public JsonElement serialize(Object t, Type type, JsonSerializationContext jsonSerializationContext)
		{
			XMLGregorianCalendar xgcal = (XMLGregorianCalendar) t;
			return new JsonPrimitive(xgcal.toXMLFormat());
		}
	}

	/**
	 * Deserializer method
	 */
	public static class Deserializer implements JsonDeserializer
	{

		public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
		{
			try
			{
				return DatatypeFactory.newInstance().newXMLGregorianCalendar(jsonElement.getAsString());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return null;
			}
		}
	}
}
