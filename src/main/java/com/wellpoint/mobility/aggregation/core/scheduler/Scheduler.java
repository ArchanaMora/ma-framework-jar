/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.scheduler;

import java.util.List;

/**
 * Scheduler interface API
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public interface Scheduler
{

	/**
	 * Executes a single task
	 * 
	 * @param tasks
	 * @return
	 */
	public TaskResponse invokeTask(Task task);

	/**
	 * Takes the list of tasks and execute them in parallel; all the responses are added to a list and sent back
	 * 
	 * @param tasks
	 * @return
	 */
	public List<TaskResponse> invokeTasks(List<Task> tasks);

	/**
	 * Takes the list of tasks and execute them in parallel; each response is sent back in the callback as and when
	 * available
	 * 
	 * @param tasks
	 */
	public void invokeTaskAsync(Task task);
	/**
	 * Takes the list of tasks and execute them in parallel; each response is sent back in the callback as and when
	 * available
	 * 
	 * @param tasks
	 */
	public void invokeTaskAsync(List<Task> tasks);
}