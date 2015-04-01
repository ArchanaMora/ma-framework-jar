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
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.scheduler.Scheduler;
import com.wellpoint.mobility.aggregation.core.scheduler.Task;
import com.wellpoint.mobility.aggregation.core.scheduler.TaskResponse;

/**
 * Default implementation for the Scheduler logic
 * 
 * 
 * @author  bharat.meda@wellpoint.com
 * 			edward.biton@wellpoint.com
 * 
 */
@Singleton(name = "Scheduler")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class SchedulerDefaultImpl implements Scheduler
{
	private ExecutorService executorService;
	/**
	 * logger
	 */
	private Logger logger = Logger.getLogger(SchedulerDefaultImpl.class);
	/**
	 * Constructor
	 */
	public SchedulerDefaultImpl()
	{
		executorService = Executors.newFixedThreadPool(100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.scheduler.Scheduler#invokeTask(com.wellpoint.mobility.aggregation.core
	 * .scheduler.Task)
	 */
	@Override
	public TaskResponse invokeTask(Task task)
	{
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		List<TaskResponse> responses = invokeTasks(tasks);
		return responses.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.scheduler.Scheduler#invokeTasks(java.util.List)
	 */
	@Override
	public List<TaskResponse> invokeTasks(List<Task> tasks)
	{
		// Callable tasks list to be built
		Collection<Callable<TaskResponse>> callableTasks = new ArrayList<Callable<TaskResponse>>();

		// response list to be sent to the client
		List<TaskResponse> responseList = new ArrayList<TaskResponse>();

		for (Task eachTask : tasks)
		{
			callableTasks.add(eachTask);
		}

		try
		{
			// all the given tasks are executed in parallel and results are captured
			List<Future<TaskResponse>> results = executorService.invokeAll(callableTasks);

			// loop over the list of future results
			for (Future<TaskResponse> result : results)
			{
				// build the response list from the completed result; one at a time
				TaskResponse schedulerResponse = result.get();
				responseList.add(schedulerResponse);
			}
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		catch (ExecutionException ee)
		{
			ee.printStackTrace();
		}
		return responseList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.scheduler.Scheduler#invokeTaskAsync(com.wellpoint.mobility.aggregation
	 * .core.scheduler.Task)
	 */
	@Override
	public void invokeTaskAsync(Task task)
	{
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		logger.debug("Invoking async task...");
		invokeTaskAsync(tasks);
	}

	@Override
	public void invokeTaskAsync(List<Task> tasks)
	{
		Collection<Callable<TaskResponse>> callableTasks = new ArrayList<Callable<TaskResponse>>();
		for (Task eachTask : tasks)
		{
			callableTasks.add(eachTask);
			executorService.submit(eachTask);
		}
	}
}