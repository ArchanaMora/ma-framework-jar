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

import java.text.NumberFormat;

/**
 * Utility class for formatting phone numbers.
 * 
 * @author edward.biton@wellpoint.com
 */
public class FormatUtility
{
	/**
	 * Private constructor for utility classes
	 */
	private FormatUtility()
	{
	}
	/**
	 * Formats a phone number from 1234567890 to 123.456.7890 if length is 10
	 * 
	 * @param phone
	 * @return
	 */
	public static String formatPhone(String phone)
	{
		if (phone == null)
		{
			return phone;
		}
		if (phone.length() == 10)
		{
			phone = phone.substring(0, 3) + "." + phone.substring(3, 6) + "." + phone.substring(6, 10);
		}
		return phone;
	}

	public static String formatPercent(String percent)
	{
		if (percent == null)
		{
			return percent;
		}
		Double doubleValue = Double.parseDouble(percent);
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		String formattedValue = numberFormat.format(doubleValue);
		return formattedValue;

	}
}
