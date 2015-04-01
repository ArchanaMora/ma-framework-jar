/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.logmanager.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.interceptor.BaseInterceptor;

/**
 * MethodLogger logs a method call before and after the method ends. This provides automatic logging for method entry
 * and exit
 * 
 * @author edward.biton@wellpoint.com
 */
public class MethodLogger extends BaseInterceptor
{

	@AroundInvoke
	public Object interceptorAroundInvoke(InvocationContext invocationContext) throws Exception
	{
		Object result = null;
		String methodSignature = createMethodSignature(invocationContext, false, true, true);
		Logger logger = Logger.getLogger(invocationContext.getTarget().getClass());
		try
		{
			logger.debug(methodSignature + " - ENTRY");
			result = invocationContext.proceed();
			logger.debug(methodSignature + " - EXIT Result:" + result);
		}
		catch (Exception e)
		{
			logger.debug(methodSignature + " - EXIT with exception:" + e.getMessage());
			throw e;
		}
		return result;
	}

}
