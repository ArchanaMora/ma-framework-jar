/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionhandler.impl;

import java.sql.Timestamp;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.pojo.ExceptionDTO;
import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodParamDTO;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;
import com.wellpoint.mobility.aggregation.persistence.domain.ErrorCodeMapping;
import com.wellpoint.mobility.aggregation.persistence.domain.ExceptionLog;
import com.wellpoint.mobility.aggregation.persistence.domain.ExceptionLogMethodParam;

/**
 * Default implementation of Exception Manager
 * 
 * @author edward.biton@wellpoint.com
 */
@Stateless(name = "ExceptionManager")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ExceptionManagerDefaultImpl implements ExceptionManager
{
	/**
	 * Default locale to use
	 */
	public static final String DEFAULT_LOCALE = Locale.getDefault().toString();
	/**
	 * Entity Manager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(ExceptionManagerDefaultImpl.class);

	/**
	 * Default constructor
	 */
	public ExceptionManagerDefaultImpl()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#saveException(com.wellpoint.mobility
	 * .aggregation.core.exceptionhandler.pojo.ExceptionDTO)
	 */
	@Override
	public void saveException(ExceptionDTO exceptionDTO)
	{
		try
		{
			ExceptionLog exceptionLog = new ExceptionLog();
			exceptionLog.setPackageName(exceptionDTO.getPackageName());
			exceptionLog.setClassName(exceptionDTO.getClassName());
			exceptionLog.setMethodName(exceptionDTO.getMethodName());
			exceptionLog.setExceptionType(exceptionDTO.getExceptionType());
			exceptionLog.setExceptionMessage(exceptionDTO.getErrorMessage());
			exceptionLog.setExceptionStackTrace(exceptionDTO.getExceptionStackTrace());
			exceptionLog.setWebService(exceptionDTO.getWebService());
			exceptionLog.setWebServiceWsdlLocation(exceptionDTO.getWebServiceWsdlLocation());
			exceptionLog.setCreatedBy("SYSTEM");
			exceptionLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			exceptionLog.setUpdatedBy("SYSTEM");
			exceptionLog.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(exceptionLog);
			for (MethodParamDTO methodParamDTO : exceptionDTO.getMethodParams())
			{
				ExceptionLogMethodParam exceptionLogMethodParam = new ExceptionLogMethodParam();
				exceptionLogMethodParam.setExceptionLog(exceptionLog);
				exceptionLogMethodParam.setParamPosition(methodParamDTO.getParamPosition());
				exceptionLogMethodParam.setParamType(methodParamDTO.getParamType());
				exceptionLogMethodParam.setParamValue(XML2ObjectUtility.toXml(methodParamDTO.getParamValue()));
				exceptionLogMethodParam.setCreatedBy("SYSTEM");
				exceptionLogMethodParam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				exceptionLogMethodParam.setUpdatedBy("SYSTEM");
				exceptionLogMethodParam.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(exceptionLogMethodParam);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#saveErrorCodeMessage(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveErrorCodeMessage(String errorCode, String errorMessage)
	{
		saveErrorCodeMessage(DEFAULT_LOCALE, errorCode, errorMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#saveErrorCodeMessage(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void saveErrorCodeMessage(String locale, String errorCode, String errorMessage)
	{
		ErrorCodeMapping errorCodeMapping = new ErrorCodeMapping();
		errorCodeMapping.setLocale(locale);
		errorCodeMapping.setErrorCode(errorCode);
		errorCodeMapping.setErrorMessage(errorMessage);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		errorCodeMapping.setCreatedDate(timestamp);
		errorCodeMapping.setCreatedBy("SYSTEM");
		errorCodeMapping.setUpdatedDate(timestamp);
		errorCodeMapping.setUpdatedBy("SYSTEM");

		try
		{
			entityManager.persist(errorCodeMapping);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#lookupErrorMessage(java.lang.String)
	 */
	@Override
	public String lookupErrorMessage(String errorCode)
	{
		return lookupErrorMessage(DEFAULT_LOCALE, errorCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#lookupErrorMessage(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String lookupErrorMessage(String locale, String errorCode)
	{
		logger.debug("lookupErrorMessage() - ENTRY");
		// default the error message to errorCode
		String errorMessage = null;
		Query query = entityManager.createQuery("SELECT ecm FROM ErrorCodeMapping ecm WHERE ecm.locale = :locale AND ecm.errorCode = :errorCode",
				ErrorCodeMapping.class);
		query.setParameter("locale", locale);
		query.setParameter("errorCode", errorCode);
		try
		{
			ErrorCodeMapping errorCodeMapping = (ErrorCodeMapping) query.getSingleResult();
			if (errorCodeMapping != null)
			{
				errorMessage = errorCodeMapping.getErrorMessage();
			}
		}
		catch (Exception e)
		{
			logger.error(errorCode + " errorCode with " + locale + " locale is not in the database");
		}
		logger.debug("lookupErrorMessage() - EXIT => " + errorMessage);
		return errorMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager#lookupErrorNumber(java.lang.String)
	 */
	@Override
	public Long lookupErrorNumber(String errorCode)
	{
		logger.debug("lookupErrorNumber() - ENTRY");
		Long errorNumber = null;
		// default the error message to errorCode
		String errorMessage = null;
		Query query = entityManager.createQuery("SELECT ecm FROM ErrorCodeMapping ecm WHERE ecm.locale = :locale AND ecm.errorCode = :errorCode",
				ErrorCodeMapping.class);
		query.setParameter("locale", DEFAULT_LOCALE);
		query.setParameter("errorCode", errorCode);
		try
		{
			ErrorCodeMapping errorCodeMapping = (ErrorCodeMapping) query.getSingleResult();
			if (errorCodeMapping != null)
			{
				errorNumber = errorCodeMapping.getId() + BASE_ERROR_NUMBER;
			}
		}
		catch (Exception e)
		{
			logger.error(errorCode + " errorCode with " + DEFAULT_LOCALE + " locale is not in the database");
		}
		logger.debug("lookupErrorNumber() - EXIT => " + errorMessage);
		return errorNumber;
	}
}
