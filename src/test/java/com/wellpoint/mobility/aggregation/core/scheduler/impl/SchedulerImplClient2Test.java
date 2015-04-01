/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.scheduler.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wellpoint.mobility.aggregation.core.scheduler.Scheduler;
import com.wellpoint.mobility.aggregation.core.scheduler.Task;
import com.wellpoint.mobility.aggregation.core.scheduler.TaskResponse;
import com.wellpoint.mobility.aggregation.core.scheduler.tasks.LongRunningTask;
import com.wellpoint.mobility.aggregation.core.scheduler.tasks.MultiplyTask;
import com.wellpoint.mobility.aggregation.core.scheduler.tasks.SumTask;

/**
 * Test class for the default Scheduler implementation
 * 
 * @author bharat.meda@wellpoint.com
 */
public class SchedulerImplClient2Test
{

	private Scheduler instance;

	@Before
	public void init()
	{
		instance = SchedulerImpl.getInstance();
	}

	@Test
	public void testParallelExecution()
	{
		System.out.println("************************************");
		System.out.println("<< START of parallel execution >>");
		long start = System.currentTimeMillis();

		// create multiple tasks

		// sum task
		SumTask sumTask = new SumTask(10, 50);
		// multiply task
		MultiplyTask multiTask = new MultiplyTask(20, 5);
		// long running task
		LongRunningTask longTask = new LongRunningTask();

		// add the tasks to the task list
		List<Task> tasks = new ArrayList<Task>();

		for (int i = 0; i < 5; i++)
		{
			tasks.add(sumTask);
			tasks.add(multiTask);
			tasks.add(longTask);
		}

		// Task scheduler
		List<TaskResponse> responses = instance.invokeTasks(tasks);

		for (TaskResponse response : responses)
		{
			System.out.println("Response ::" + response.getResponse() + " for task :: " + response.getTask());
		}

		long end = System.currentTimeMillis();
		System.out.println("Total time taken for parallel execution : " + (end - start));

		System.out.println("<<END of parallel execution >>");
		System.out.println("************************************");
	}

}