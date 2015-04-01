/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */

package com.wellpoint.mobility.aggregation.core.scheduler.services;

/**
 * Class representing adhoc service
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public class MultiplyService
{

	public long multiply(long a, long b)
	{
		System.out.println("<< Multiply service START >>");
		long start = System.currentTimeMillis();

		try
		{
			// this service takes 5 seconds for the processing logic
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		long multiplyValue = a * b;

		long end = System.currentTimeMillis();
		System.out.println("Total time taken for multiply service:: " + (end - start));

		System.out.println("<< Multiply service END >>");
		return multiplyValue;
	}
}