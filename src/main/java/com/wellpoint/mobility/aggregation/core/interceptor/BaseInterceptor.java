/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.interceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodInfoDTO;
import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodParamDTO;
import com.wellpoint.mobility.aggregation.core.utilities.HashUtility;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;

/**
 * Base Interceptor class that provides utility methods for creating method signature
 * 
 * @author edward.biton@wellpoint.com
 */
public class BaseInterceptor
{
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(BaseInterceptor.class);
	/**
	 * Debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	/**
	 * Creates a method signature
	 * 
	 * @param invocationContext
	 *            method invocation information
	 * @param fullClassName
	 *            if true uses the full class name including the package
	 * @param includeParameterType
	 *            if true includes the parameter
	 * @param includeParameterValues
	 *            if true includes the parameter
	 * @return
	 */
	protected String createMethodSignature(InvocationContext invocationContext, boolean fullClassName, boolean includeParameterTypes,
			boolean includeParameterValues)
	{
		if (DEBUG_ENABLED)
		{
			logger.debug("ENTRY: BaseInterceptor.createMethodSignature()");
		}
		StringBuilder methodSignature = new StringBuilder();
		final String className;
		if (fullClassName == false)
		{
			className = invocationContext.getTarget().getClass().getSimpleName();
		}
		else
		{
			className = invocationContext.getTarget().getClass().getName();
		}
		
		methodSignature.append(className);
		Method method = invocationContext.getMethod();
		methodSignature.append(".");
		methodSignature.append(method.getName());
		methodSignature.append("(");
		Object[] parameters = invocationContext.getParameters();
		if (parameters != null && (includeParameterTypes == true || includeParameterValues == true))
		{
			for (int i = 0; i < parameters.length; i++)
			{
				Object parameter = parameters[i];
				if (includeParameterTypes == true)
				{
					if (parameter == null)
					{
						methodSignature.append("unknown");
					}
					else
					{
						methodSignature.append(parameter.getClass().getSimpleName());
					}
				}
				// add a space if both types and parameters are included
				if ((includeParameterTypes == true) && (includeParameterValues == true))
				{
					methodSignature.append(" ");
				}
				if (includeParameterValues == true)
				{
					// methodSignature.append(parameter.hashCode());
					methodSignature.append(XML2ObjectUtility.toXml(parameter));
				}
				if ((i + 1) < parameters.length)
				{
					methodSignature.append(",");
				}
			}
		}
		methodSignature.append(")");
		String methodSignatureString = methodSignature.toString();
		String methodSignatureHash = HashUtility.hashValue(methodSignatureString);
		if (DEBUG_ENABLED)
		{
			logger.debug("BaseInterceptor.createMethodSignature() => MethodSignature=" + methodSignatureString);
			logger.debug("EXIT : BaseInterceptor.createMethodSignature() => MethodSignatureHash=" + methodSignatureHash);
		}
		return methodSignatureHash;
	}

	/**
	 * Overloaded method that uses the default parameters for includeParameterTypes and includeParameterValues as false
	 * 
	 * @param invocationContext
	 *            method invocation information
	 * @return
	 */
	protected String createMethodSignature(InvocationContext invocationContext)
	{
		return createMethodSignature(invocationContext, false, false, false);
	}

	/**
	 * create method cache entity object
	 * 
	 * @param invocationContext
	 * @return
	 */
	public MethodInfoDTO createMethodInfo(MethodInfoDTO methodInfo, InvocationContext invocationContext)
	{
		if (DEBUG_ENABLED)
		{
			logger.debug("ENTRY: BaseInterceptor.createMethodInfo()");
		}
		methodInfo.setMethodSignature(createMethodSignature(invocationContext, true, true, false));
		methodInfo.setCallSignature(createMethodSignature(invocationContext, true, false, true));
		methodInfo.setPackageName(invocationContext.getTarget().getClass().getPackage().getName());
		methodInfo.setClassName(invocationContext.getTarget().getClass().getSimpleName());
		methodInfo.setMethodName(invocationContext.getMethod().getName());
		methodInfo.setReturnType(invocationContext.getMethod().getReturnType().getName());
		// to be set actual implementation
		methodInfo.setReturnValue(null);

		List<MethodParamDTO> methodParams = new ArrayList<MethodParamDTO>();
		Object[] parameters = invocationContext.getParameters();
		for (int i = 0; parameters != null && i < parameters.length; i++)
		{
			Object parameter = parameters[i];
			MethodParamDTO methodParam = new MethodParamDTO();
			methodParam.setParamPosition(i + 1);
			if (parameter != null)
			{
				methodParam.setParamType(parameter.getClass().getName());
			}
			else
			{
				methodParam.setParamType("");
			}
			methodParam.setParamValue(parameter);
			methodParams.add(methodParam);
		}
		methodInfo.setMethodParams(methodParams);
		if (DEBUG_ENABLED)
		{
			logger.debug("EXIT: BaseInterceptor.createMethodInfo() => " + methodInfo.toString());
		}
		return methodInfo;
	}
}
