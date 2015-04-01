/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */

package com.wellpoint.mobility.aggregation.core.scheduler.tasks;

import com.wellpoint.mobility.aggregation.core.scheduler.services.MultiplyService;
import com.wellpoint.mobility.aggregation.core.scheduler.Task;

/**
 * Class representing ad hoc task
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public class MultiplyTask extends Task
{

	private long a;
	private long b;

	public MultiplyTask(long a, long b)
	{
		this.a = a;
		this.b = b;
	}

	@Override
	public Object execute()
	{

		// invoke the multiply service
		MultiplyService mulService = new MultiplyService();
		return mulService.multiply(a, b);
	}

	@Override
	public String toString()
	{
		return this.getClass().getName();
	}
}