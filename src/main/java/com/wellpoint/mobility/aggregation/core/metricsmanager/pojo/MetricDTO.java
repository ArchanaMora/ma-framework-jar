/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.metricsmanager.pojo;

import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodInfoDTO;

/**
 * A wrapper object that contains metrics data
 * 
 * @author bharat.meda@wellpoint.com
 */
public class MetricDTO extends MethodInfoDTO
{
	private long executionTime;

	/**
	 * @return the executionTime
	 */
	public long getExecutionTime()
	{
		return executionTime;
	}

	/**
	 * @param executionTime
	 *            the executionTime to set
	 */
	public void setExecutionTime(long executionTime)
	{
		this.executionTime = executionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MetricDTO [executionTime=" + executionTime + ", getExecutionTime()=" + getExecutionTime() + ", getMethodSignature()=" + getMethodSignature()
				+ ", getCallSignature()=" + getCallSignature() + ", getPackageName()=" + getPackageName() + ", getClassName()=" + getClassName()
				+ ", getMethodName()=" + getMethodName() + ", getMethodParams()=" + getMethodParams() + ", getReturnType()=" + getReturnType()
				+ ", getReturnValue()=" + getReturnValue() + ", getExceptionType()=" + getExceptionType() + "]";
	}

}