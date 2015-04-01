/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.composite;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.composite.headers.RequestHeader;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.exceptions.ApplicationException;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.pojo.ErrorCode;

/**
 * This serves as the base class for all composite service in aggregation layer. This will provide the basic validation
 * on the application header
 * 
 * @author edward.biton@wellpoint.com
 */
public class BaseComposite
{
	/**
	 * List of valid applications that are allowed
	 */
	protected List<String> validAppIds = new ArrayList<String>();
	/**
	 * Logger
	 */
	protected Logger logger = Logger.getLogger(BaseComposite.class);

	/**
	 * Default constructor
	 */
	public BaseComposite()
	{
		// TODO reading valid application from the database
		validAppIds.add("ABCBS");
		validAppIds.add("ABC");
		validAppIds.add("EBCBS");
		validAppIds.add("BCBSGA");
	}

	/**
	 * Validates the application header. Throws an ApplicationException if header is not valid
	 * 
	 * @param requestHeader
	 */
	protected void validateApplicationHeader(RequestHeader requestHeader) throws ApplicationException
	{
		logger.debug("BaseComposite.validateApplicationHeader() - ENTRY");
		logger.debug("BaseComposite.validateApplicationHeader() - " + requestHeader);
		// construct a reusable errorCode object within the method
		ErrorCode errorCode = new ErrorCode(BaseComposite.class, "validateApplicationHeader");
		if (requestHeader == null)
		{
			logger.error("BaseComposite.validateApplicationHeader() - EXIT => request header is required");
			errorCode.setErrorCode("requestHeaderRequired");
			throw new ApplicationException(errorCode);
		}
		String appId = requestHeader.getAppId();
		if (appId == null)
		{
			logger.error("BaseComposite.validateApplicationHeader() - EXIT => application id is required");
			errorCode.setErrorCode("appIdRequired");
			throw new ApplicationException(errorCode);
		}
		appId = appId.toUpperCase();
		if (validAppIds.contains(appId) == false)
		{
			logger.error("BaseComposite.validateApplicationHeader() - EXIT => application id is not allowed");
			errorCode.setErrorCode("appIdDenied");
			throw new ApplicationException(errorCode);
		}

		String appVersion = requestHeader.getAppVersion();
		if (appVersion == null)
		{
			logger.error("BaseComposite.validateApplicationHeader() - EXIT => application version is required");
			errorCode.setErrorCode("appVersionRequired");
			throw new ApplicationException(errorCode);
		}
		logger.debug("BaseComposite.validateApplicationHeader() - EXIT");
	}
}
