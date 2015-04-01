/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionhandler.pojo;

import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodInfoDTO;

/**
 * An extension of MethodInfo that contains additional exception information properties
 * 
 * @author edward.biton@wellpoint.com
 */
public class ExceptionDTO extends MethodInfoDTO
{
	/**
	 * error message
	 */
	private String errorMessage;
	/**
	 * Exception stack trace
	 */
	private String exceptionStackTrace;
	/**
	 * Wsdl location if it is a web service exception
	 */
	private String webServiceWsdlLocation;
	/**
	 * Web service name if it is a webservice exception
	 */
	private String webService;

	/**
	 * Default constructor
	 */
	public ExceptionDTO()
	{
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the exceptionStackTrace
	 */
	public String getExceptionStackTrace()
	{
		return exceptionStackTrace;
	}

	/**
	 * @param exceptionStackTrace
	 *            the exceptionStackTrace to set
	 */
	public void setExceptionStackTrace(String exceptionStackTrace)
	{
		this.exceptionStackTrace = exceptionStackTrace;
	}

	/**
	 * @return the webServiceWsdlLocation
	 */
	public String getWebServiceWsdlLocation()
	{
		return webServiceWsdlLocation;
	}

	/**
	 * @param webServiceWsdlLocation
	 *            the webServiceWsdlLocation to set
	 */
	public void setWebServiceWsdlLocation(String webServiceWsdlLocation)
	{
		this.webServiceWsdlLocation = webServiceWsdlLocation;
	}

	/**
	 * @return the webService
	 */
	public String getWebService()
	{
		return webService;
	}

	/**
	 * @param webService
	 *            the webService to set
	 */
	public void setWebService(String webService)
	{
		this.webService = webService;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString() + ". ExceptionDTO [errorMessage=" + errorMessage + ", webServiceWsdlLocation="
				+ webServiceWsdlLocation + ", webService=" + webService +  ", exceptionStackTrace=" + exceptionStackTrace + "]";
		
//		return "ExceptionDTO [errorMessage=" + errorMessage + ", webServiceWsdlLocation="
//				+ webServiceWsdlLocation + ", webService=" + webService + ", getMethodSignature()=" + getMethodSignature() + ", getCallSignature()="
//				+ getCallSignature() + ", getPackageName()=" + getPackageName() + ", getClassName()=" + getClassName() + ", getMethodName()=" + getMethodName()
//				+ ", getMethodParams()=" + getMethodParams() + ", getReturnType()=" + getReturnType() + ", getReturnValue()=" + getReturnValue()
//				+ ", getExceptionType()=" + getExceptionType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
//				+ hashCode() + ", exceptionStackTrace=" + exceptionStackTrace + "]";
	}
}
