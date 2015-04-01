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
 * Class representing ad hoc service
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public class SumService
{

	public long sum(long a, long b)
	{
		System.out.println("<< Sum service START >>");
		long start = System.currentTimeMillis();

		try
		{
			// this service takes 3 seconds for the processing logic
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		long sumValue = a + b;

		long end = System.currentTimeMillis();
		System.out.println("Total time taken for sum service:: " + (end - start));

		System.out.println("<< Sum service END >>");

		return sumValue;
	}
}