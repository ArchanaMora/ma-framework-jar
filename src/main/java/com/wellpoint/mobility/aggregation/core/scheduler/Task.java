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

import java.util.concurrent.Callable;

/**
 * Abstract Task class with overridden call() method and abstract execute() method
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public abstract class Task implements Callable<TaskResponse>
{

	private TaskResponse response;

	@Override
	public TaskResponse call() throws Exception
	{
		response = new TaskResponse();
		response.setTask(this);
		long startTime = System.currentTimeMillis();
		response.setResponse(execute());
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		System.out.println(this.getClass().getName() + " - " + elapsedTime + " ms");
		return response;
	}

	/**
	 * For client implementation
	 * 
	 * @return
	 */
	public abstract Object execute();

	public TaskResponse getResponse()
	{
		return response;
	}

	public void setResponse(TaskResponse response)
	{
		this.response = response;
	}
}