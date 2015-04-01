/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.metricsmanager.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.interceptor.BaseInterceptor;

/**
 * MetricLogger times a method execution
 * 
 * @author edward.biton@wellpoint.com
 */
public class MetricConsoleLogger extends BaseInterceptor
{
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(MetricConsoleLogger.class);

	@AroundInvoke
	public Object interceptorAroundInvoke(InvocationContext invocationContext) throws Exception
	{
		Object result = null;
		String methodSignature = createMethodSignature(invocationContext, false, true, true);
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		long elapsedTime = 0;
		try
		{
			result = invocationContext.proceed();
			endTime = System.currentTimeMillis();
		}
		catch (Exception e)
		{
			endTime = System.currentTimeMillis();
		}
		elapsedTime = endTime - startTime;
		logger.debug(methodSignature + " taking " + elapsedTime + "ms");
		return result;
	}
}
