/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.composite.response;

/**
 * This is the base class for all response of the composite. All return value should extend this class
 * 
 * @author edward.biton@wellpoint.com
 */
public class ApplicationResponse
{
	/**
	 * If the service call has errors
	 */
	private boolean error = false;
	/**
	 * Error code if the service call has errors
	 */
	private String errorCode = null;
	/**
	 * Error message if the service call has errors
	 */
	private String errorMessage = null;
	/**
	 * Stack trace if the service call has errors
	 */
	private String stackTrace = null;

	/**
	 * Constructor
	 */
	public ApplicationResponse()
	{
	}

	/**
	 * @return the error
	 */
	public boolean isError()
	{
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(boolean error)
	{
		this.error = error;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the stackTrace
	 */
	public String getStackTrace()
	{
		return stackTrace;
	}

	/**
	 * @param stackTrace
	 *            the stackTrace to set
	 */
	public void setStackTrace(String stackTrace)
	{
		this.stackTrace = stackTrace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ApplicationResponse [error=" + error + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", stackTrace=" + stackTrace + "]";
	}

}
