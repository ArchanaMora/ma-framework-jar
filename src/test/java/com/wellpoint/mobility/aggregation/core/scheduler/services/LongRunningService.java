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

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a long running service
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public class LongRunningService
{

	public List<String> getNames()
	{

		System.out.println("<< Long running service START >>");
		long start = System.currentTimeMillis();

		List<String> names = new ArrayList<String>();

		try
		{
			// this service takes 10 seconds for the processing logic
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		for (int i = 0; i < 10; i++)
		{
			names.add("Django " + i);
		}

		long end = System.currentTimeMillis();
		System.out.println("Total time taken for Long running service:: " + (end - start));

		System.out.println("<< Long running service END >>");

		return names;
	}
}