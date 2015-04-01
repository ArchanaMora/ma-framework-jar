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

import com.thoughtworks.xstream.XStream;

/**
 * A utility class to convert object to xml and vice versa
 * 
 * @author edward.biton@wellpoint.com
 */
public class XML2ObjectUtility
{
	/**
	 * Single instance object of xtream for converting object to xml to object and vice versa
	 */
	public static XStream xstream = new XStream();

	/**
	 * Private constructor for utility classes
	 */
	private XML2ObjectUtility()
	{

	}

	/**
	 * Converts an XML to an object
	 * 
	 * @param xml
	 *            xml to be converted
	 * @return
	 */
	public static Object toObject(String xml)
	{
		return xstream.fromXML(xml);
	}

	/**
	 * Converts an object to an xml
	 * 
	 * @param object
	 *            object to be converted
	 * @return
	 */
	public static String toXml(Object object)
	{
		return xstream.toXML(object);
	}
}
