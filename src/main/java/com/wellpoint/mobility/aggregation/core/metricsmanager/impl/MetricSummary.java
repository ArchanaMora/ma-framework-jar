/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.metricsmanager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wellpoint.mobility.aggregation.persistence.domain.Metric;
import com.wellpoint.mobility.aggregation.persistence.domain.MetricsDailyrollup;

/**
 * A metric summary class for summarizing metric data
 * 
 * @author edward.biton@wellpoint.com
 */
public class MetricSummary
{
	/**
	 * List of metrics to be summarized
	 */
	private List<Metric> metrics = new ArrayList<Metric>();
	/**
	 * Application name to be summarized
	 */
	private String applicationName;
	/**
	 * Package name to be summarized
	 */
	private String packageName;
	/**
	 * Class name to be summarized
	 */
	private String className;
	/**
	 * Method name to be summarized
	 */
	private String methodName;
	/**
	 * Date to be summarized
	 */
	private Date date;
	/**
	 * Total execution time
	 */
	private long totalTime = 0l;
	/**
	 * Total exceptions
	 */
	private long totalExceptions = 0l;

	/**
	 * Constructor
	 * 
	 * @param applicationName
	 *            application name to be summarized
	 * @param packageName
	 *            package name to be summarized
	 * @param className
	 *            class name to be summarized
	 * @param methodName
	 *            method name to be summarized
	 * @param date
	 *            date to be summarized
	 */
	public MetricSummary(String applicationName, String packageName, String className, String methodName, Date date)
	{
		this.applicationName = applicationName;
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		this.date = date;
	}

	/**
	 * Adds a metric information
	 * 
	 * @param metric
	 *            object
	 */
	public void add(Metric metric)
	{
		totalTime = metric.getExecutionTime();
		metrics.add(metric);
	}

	/**
	 * Returns a MetricsDailyrollup object
	 * 
	 * @return MetricsDailyrollup
	 */
	public MetricsDailyrollup getMetricsDailyRollup()
	{
		MetricsDailyrollup metricsDailyrollup = new MetricsDailyrollup();
		metricsDailyrollup.setApplicationName(getApplicationName());
		String componentName = getPackageName();
		componentName = componentName.substring(componentName.lastIndexOf(".") + 1);
		metricsDailyrollup.setComponentName(componentName);
		metricsDailyrollup.setClassName(getClassName());
		metricsDailyrollup.setMethodSignature(getMethodName());
		metricsDailyrollup.setNumExceptions((int) getTotalExceptions());
		int averageTime = (int) ((double) totalTime / (double) metrics.size());
//		System.out.println("average time is " + averageTime);
		metricsDailyrollup.setAvgTimeSpentInMillis(averageTime);
		metricsDailyrollup.setNumInvocations(metrics.size());
		metricsDailyrollup.setNumSuccesses(metrics.size());
		metricsDailyrollup.setTsDate(date);
		return metricsDailyrollup;
	}

	/**
	 * @return the metrics
	 */
	public List<Metric> getMetrics()
	{
		return metrics;
	}

	/**
	 * @param metrics
	 *            the metrics to set
	 */
	public void setMetrics(List<Metric> metrics)
	{
		this.metrics = metrics;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName()
	{
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName)
	{
		this.applicationName = applicationName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName()
	{
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * @return the totalTime
	 */
	public long getTotalTime()
	{
		return totalTime;
	}

	/**
	 * @param totalTime
	 *            the totalTime to set
	 */
	public void setTotalTime(long totalTime)
	{
		this.totalTime = totalTime;
	}

	/**
	 * @return the totalExceptions
	 */
	public long getTotalExceptions()
	{
		return totalExceptions;
	}

	/**
	 * @param totalExceptions
	 *            the totalExceptions to set
	 */
	public void setTotalExceptions(long totalExceptions)
	{
		this.totalExceptions = totalExceptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MetricSummary [metrics=" + metrics + ", applicationName=" + applicationName + ", packageName=" + packageName + ", className=" + className
				+ ", methodName=" + methodName + ", date=" + date + ", totalTime=" + totalTime + ", totalExceptions=" + totalExceptions + "]";
	}

}
