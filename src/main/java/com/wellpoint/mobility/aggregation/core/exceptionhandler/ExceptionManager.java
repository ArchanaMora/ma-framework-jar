/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionhandler;

import com.wellpoint.mobility.aggregation.core.exceptionhandler.pojo.ExceptionDTO;

/**
 * Exception manager interface API
 * 
 * @author edward.biton@wellpoint.com
 */
public interface ExceptionManager
{
	public static final long BASE_ERROR_NUMBER = 90000000;
	/**
	 * Saves the exception to the database
	 * 
	 * @param exceptionDTO
	 *            to save
	 */
	public void saveException(ExceptionDTO exceptionDTO);

	/**
	 * Lookup an errorMessage given an errorCode
	 * 
	 * @param errorCode
	 * @return
	 */
	public String lookupErrorMessage(String errorCode);
	/**
	 * Lookup an error code given a locale
	 * 
	 * @param locale
	 *            locale
	 * @param errorCode
	 *            error code to lookup
	 * @return
	 */
	public String lookupErrorMessage(String locale, String errorCode);
	/**
	 * Lookup an error number given an errorCode
	 * 
	 * @param error number
	 * @return
	 */
	public Long lookupErrorNumber(String errorCode);

	/**
	 * Saves an error code with a given error message using default locale
	 * 
	 * @param errorCode
	 *            full error code
	 * @param errorMessage
	 *            error message
	 */
	public void saveErrorCodeMessage(String errorCode, String errorMessage);
	/**
	 * Saves an error code with a given error message to the database
	 * 
	 * @param locale
	 *            message local
	 * @param errorCode
	 *            full error code
	 * @param errorMessage
	 *            error message
	 */
	public void saveErrorCodeMessage(String locale, String errorCode, String errorMessage);
}
