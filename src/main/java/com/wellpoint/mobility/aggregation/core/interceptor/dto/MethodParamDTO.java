/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.interceptor.dto;

/**
 * A method parameter information holder that contains the parameter position, type and value of the parameter
 * 
 * @author edward.biton@wellpoint.com
 */
public class MethodParamDTO
{
	/**
	 * Parameter position. starts with 1
	 */
	private int paramPosition;
	/**
	 * Parameter type
	 */
	private String paramType;
	/**
	 * Parameter value
	 */
	private Object paramValue;

	/**
	 * @return the paramPosition
	 */
	public int getParamPosition()
	{
		return paramPosition;
	}

	/**
	 * @param paramPosition
	 *            the paramPosition to set
	 */
	public void setParamPosition(int paramPosition)
	{
		this.paramPosition = paramPosition;
	}

	/**
	 * @return the paramType
	 */
	public String getParamType()
	{
		return paramType;
	}

	/**
	 * @param paramType
	 *            the paramType to set
	 */
	public void setParamType(String paramType)
	{
		this.paramType = paramType;
	}

	/**
	 * @return the paramValue
	 */
	public Object getParamValue()
	{
		return paramValue;
	}

	/**
	 * @param paramValue
	 *            the paramValue to set
	 */
	public void setParamValue(Object paramValue)
	{
		this.paramValue = paramValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MethodParamDTO [paramPosition=" + paramPosition + ", paramType=" + paramType + ", paramValue=" + paramValue + "]";
	}

}
