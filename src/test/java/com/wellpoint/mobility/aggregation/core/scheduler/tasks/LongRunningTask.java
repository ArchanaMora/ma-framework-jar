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

import com.wellpoint.mobility.aggregation.core.scheduler.Task;
import com.wellpoint.mobility.aggregation.core.scheduler.services.LongRunningService;

/**
 * Class representing long running task
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public class LongRunningTask extends Task
{

	@Override
	public Object execute()
	{

		// invoke the long running service
		LongRunningService longService = new LongRunningService();
		return longService.getNames();
	}

	@Override
	public String toString()
	{
		return this.getClass().getName();
	}
}