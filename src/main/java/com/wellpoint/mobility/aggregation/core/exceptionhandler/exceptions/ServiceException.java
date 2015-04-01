/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionhandler.exceptions;

import javax.xml.ws.soap.SOAPFaultException;

import com.wellpoint.mobility.aggregation.core.exceptionmanager.property.PropertyReader;

/**
 * If a web service is causing the exception, this should be use to throw the exception upwards
 * 
 * @author edward.biton@wellpoint.com
 */
public class ServiceException extends ApplicationException
{

	/** serial Version UID. */
	private static final long serialVersionUID = 1L;
	private String errorMessageFile = "";

	/**
	 * Contractor.
	 */
	public ServiceException(SOAPFaultException e)
	{
		super(e);
		this.setErrorCode(e.getFault().getFaultCode());
	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ServiceException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param message
	 *            the message
	 */
	public ServiceException(final String message)
	{
		super(message);

	}

	/**
	 * Instantiates a new ApplicationException.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ServiceException(final Throwable cause)
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

//	public ErrorMessage getSoapErrorDetails(SOAPFaultException e, String operationName)
//	{
//		ErrorMessage errorMessage = new ErrorMessage();
//		Boolean flag = false;
//		SOAPFault soaPFault = e.getFault();
//		Detail detail = (Detail) soaPFault.getDetail();
//		String errorCode = "";
//		String message = "";
//		Node exceptionList = (Node) detail.getFirstChild();
//		Node exceptionListChild = (Node) exceptionList.getFirstChild();
//		for (int j = 0; j < exceptionList.getChildNodes().getLength(); j++)
//		{
//
//			if ("Exception".equals(exceptionListChild.getLocalName()))
//			{
//				Node exceptionChild = (Node) exceptionListChild.getFirstChild();
//				for (int i = 0; i < exceptionListChild.getChildNodes().getLength(); i++)
//				{
//
//					if ("code".equals(exceptionChild.getLocalName()))
//					{
//						errorCode = exceptionChild.getValue();
//
//					}
//					if ("message".equals(exceptionChild.getLocalName()))
//					{
//						message = exceptionChild.getValue();
//
//					}
//					exceptionChild = (Node) exceptionChild.getNextSibling();
//				}
//
//			}
//			if (flag)
//			{
//				break;
//			}
//			exceptionListChild = (Node) exceptionListChild.getNextSibling();
//		}
//		// System.out.println("error code =" + errorCode);
//		errorMessage.setErrorCode(errorCode);
//		errorMessage.setErrorMessage(message + " " + operationName);
//		return errorMessage;
//	}
}
