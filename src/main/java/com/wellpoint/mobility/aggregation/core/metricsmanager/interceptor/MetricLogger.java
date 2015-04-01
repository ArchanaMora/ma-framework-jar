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

import java.lang.reflect.Method;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.interceptor.BaseInterceptor;
import com.wellpoint.mobility.aggregation.core.metricsmanager.MetricsManager;
import com.wellpoint.mobility.aggregation.core.metricsmanager.pojo.MetricDTO;
import com.wellpoint.mobility.aggregation.core.misc.OnDemainActionManager;
import com.wellpoint.mobility.aggregation.core.performancenotification.PerformanceNotificationInformationPojo;
import com.wellpoint.mobility.aggregation.core.performancenotification.PerformanceNotificationProducer;
import com.wellpoint.mobility.aggregation.persistence.domain.OnDemandAction;

/**
 * Metrics interceptor intercepts method calls for capturing metrics information
 * 
 * @author bharat.meda@wellpoint.com edward.biton@wellpoint.com - modified to include enable/disable option
 */
public class MetricLogger extends BaseInterceptor
{
	private boolean enabled = true;
	@EJB(beanName = "MetricsManager")
	private MetricsManager metricsManager;
	
	
	@EJB PerformanceNotificationProducer performanceNotificationProducer;
	
	
	/**
	 * logger
	 */
	private static final Logger logger = Logger.getLogger(MetricLogger.class);
	/**
	 * Is debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	@AroundInvoke
	public Object interceptorAroundInvoke(InvocationContext invocationContext) throws Exception
	{
		if (enabled == false)
		{
			return invocationContext.proceed();
		}
		
		if (DEBUG_ENABLED)
		{
			logger.debug("interceptorAroundInvoke() - START");
		}
		MetricDTO metric = new MetricDTO();
		metric = (MetricDTO) this.createMethodInfo(metric, invocationContext);

		Object result = null;
		final long startTime = System.currentTimeMillis();

		result = invocationContext.proceed();

		final long endTime = System.currentTimeMillis();
		
		Method method = invocationContext.getMethod();
		if (DEBUG_ENABLED)
		{
			logger.debug("method " + method.toString() + " time: " + endTime + "ms");
		}

		final long elapsedTime = endTime - startTime;
		metric.setExecutionTime(elapsedTime);
		// FIX edward.biton@wellpoint.com to include return value in the metrics table
		metric.setReturnValue(result);
		// call the persistence layer to save the metrics information
		metricsManager.storeMetricsInfo(metric);

		
		// If the call completed w/o throwing an exception, check for performance breaches
		final String fullPackageClassName = method.getDeclaringClass().getName();
		final String fullPackageName = fullPackageClassName.substring(0, fullPackageClassName.lastIndexOf('.'));
		final String fullClassName = fullPackageClassName.substring(fullPackageClassName.lastIndexOf('.') + 1);

		final PerformanceNotificationInformationPojo performanceNotificationInformationPojo = 
			new PerformanceNotificationInformationPojo(fullPackageName, fullClassName, method.getName(), elapsedTime);
		
		performanceNotificationProducer.submitPerformanceInfo(performanceNotificationInformationPojo);


		if (DEBUG_ENABLED)
		{
			logger.debug("interceptorAroundInvoke() - END");
		}
		
		return result;
	
	} // of interceptorAroundInvoke
	
} //  of class