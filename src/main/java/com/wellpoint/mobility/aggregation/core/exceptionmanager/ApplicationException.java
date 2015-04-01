/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionmanager;

import com.wellpoint.mobility.aggregation.core.exceptionmanager.property.PropertyReader;

public class ApplicationException extends Exception
{

	/** serial Version UID. */
	private static final long serialVersionUID = 1L;
	private String errorMessageFile = "";

	/**
	 * Contractor.
	 */
	public ApplicationException()
	{
		super();
	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ApplicationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param message
	 *            the message
	 */
	public ApplicationException(final String message)
	{
		super(message);

	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ApplicationException(final Throwable cause)
	{
		super(cause);
	}

	public String getErrorMessage(String errorCode, String locale)
	{

		errorMessageFile = errorMessageFile + locale + ".properties";
		String errorMessage = PropertyReader.readProperty(errorMessageFile, errorCode);
		return errorMessage;
		// super();
	}

}
