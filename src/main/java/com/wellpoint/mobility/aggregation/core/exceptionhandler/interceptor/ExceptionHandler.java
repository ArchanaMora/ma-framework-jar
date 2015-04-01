/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.exceptionhandler.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wellpoint.mobility.aggregation.core.composite.response.ApplicationResponse;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.ExceptionManager;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.exceptions.ApplicationException;
import com.wellpoint.mobility.aggregation.core.exceptionhandler.pojo.ExceptionDTO;
import com.wellpoint.mobility.aggregation.core.failurenotification.FailureNotificationInformationPojo;
import com.wellpoint.mobility.aggregation.core.failurenotification.FailureNotificationProducer;
import com.wellpoint.mobility.aggregation.core.interceptor.BaseInterceptor;

/**
 * Exception Handler intercepts method calls for capturing exceptions and logging it to the database This also looks up
 * the database and loads the message to be returned to the service client
 * 
 * @author edward.biton@wellpoint.com
 */
public class ExceptionHandler extends BaseInterceptor
{
	/**
	 * exception logging to the database is enabled or disabled
	 */
	private boolean exceptionLoggingEnabled = true;
	/**
	 * if exceptionStackTrace will be sent to the client
	 */
	private boolean exceptionStackTraceEnabled = false;
	/**
	 * CacheManager instance
	 */
	@EJB(beanName = "ExceptionManager")
	private ExceptionManager exceptionManager;

	@EJB
	private FailureNotificationProducer failureNotificationProducer;

	/**
	 * logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
//	private final static boolean DEBUG_LOGGER_ENABLED = logger.isDebugEnabled();
	private final static boolean INFO_LOGGER_ENABLED = logger.isInfoEnabled();

	@AroundInvoke
	public Object interceptorAroundInvoke(InvocationContext invocationContext) throws Exception
	{
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("interceptorAroundInvoke() - ENTRY");
		}
		
		Object returnValue = null;
		final long startTimeMS = System.currentTimeMillis();
		long endTimeMS = -1;
		
		try
		{
			returnValue = invocationContext.proceed();
			endTimeMS = System.currentTimeMillis();
		}
		catch (ApplicationException applicationException)
		{
			endTimeMS = System.currentTimeMillis();
			
			// No need to check for failure notification as none of the facade classes throw 'ApplicationException'.

			// check if the return type is an instance of ApplicationResponse
			// If it is an instance ApplicationResponse, it means it is a Composite class, we need to create an instance
			// of ApplicationResponse object and set the error to true so that it returns a proper json response to the
			// client We need to lookup the error code from the database and fill up the ApplicationResponse object and
			// return it.
			Object returnObject = invocationContext.getMethod().getReturnType().newInstance();
			if (returnObject instanceof ApplicationResponse)
			{
				ApplicationResponse applicationResponse = (ApplicationResponse) returnObject;
				String mappedErrorMessage = exceptionManager.lookupErrorMessage(applicationException.getErrorCode());
				Long errorNumber = exceptionManager.lookupErrorNumber(applicationException.getErrorCode());
				if ((errorNumber == null) || (mappedErrorMessage == null))
				{
					// try to get the error message from the root cause
					if (applicationException.getCause() != null)
					{
						mappedErrorMessage = applicationException.getCause().getMessage();
					}
					// there is no error code mapping in the database, create one
					exceptionManager.saveErrorCodeMessage(applicationException.getErrorCode(), mappedErrorMessage);
					// pull the error code number again
					errorNumber = exceptionManager.lookupErrorNumber(applicationException.getErrorCode());
				}
				applicationResponse.setError(true);
				applicationResponse.setErrorCode("" + errorNumber);
				applicationResponse.setErrorMessage(mappedErrorMessage);
				if ((applicationResponse.getErrorMessage() == null || (applicationResponse.getErrorMessage().equals(""))))
				{
					applicationResponse.setErrorMessage("A general error occurred");
				}
				
				logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught an ApplicationException=" + applicationException + 
					". Runtime=" + (endTimeMS-startTimeMS) + "ms. Returning an ApplicationResponse=" + applicationResponse, applicationException);
				
				return applicationResponse;
			}
			else
			{
				// if the return type is not an instance of ApplicationResponse then just re-throw the exception
				// until we reach the Composite Class (which returns an ApplicationResponse or its subtypes)
				logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught an ApplicationException. Runtime=" +
						(endTimeMS-startTimeMS) + "ms. Returning said ApplicationException=" + applicationException, applicationException);
					

				throw applicationException;
			}
		}
		catch (EJBException ejbException)
		{
			endTimeMS = System.currentTimeMillis();

			// check if the return type is an instance of ApplicationResponse
			// If it is an instance ApplicationResponse, it means it is a Composite class, we need to create an instance
			// of ApplicationResponse object and set the error to true so that it returns a proper json response to the
			// client We need to lookup the error code from the database and fill up the ApplicationResponse object
			// and return it.
			Object returnObject = invocationContext.getMethod().getReturnType().newInstance();
			ApplicationResponse applicationResponse = null;
			if (returnObject instanceof ApplicationResponse)
			{
				ApplicationException applicationException = findApplicationException(ejbException);
				if (applicationException != null)
				{
					applicationResponse = (ApplicationResponse) returnObject;
					String mappedErrorMessage = exceptionManager.lookupErrorMessage(applicationException.getErrorCode());
					Long errorNumber = exceptionManager.lookupErrorNumber(applicationException.getErrorCode());
					if (errorNumber == null)
					{
						// try to get the error message from the root cause
						if (applicationException.getCause() != null)
						{
							mappedErrorMessage = applicationException.getCause().getMessage();
						}
						// there is no error code mapping in the database, create one
						exceptionManager.saveErrorCodeMessage(applicationException.getErrorCode(), mappedErrorMessage);
						// pull the error code number again
						errorNumber = exceptionManager.lookupErrorNumber(applicationException.getErrorCode());
					}
					applicationResponse.setError(true);
					applicationResponse.setErrorCode("" + errorNumber);
					applicationResponse.setErrorMessage(mappedErrorMessage);
					if ((applicationResponse.getErrorMessage() == null || (applicationResponse.getErrorMessage().equals(""))))
					{
						applicationResponse.setErrorMessage("A general error occurred");
					}
					
					logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught an ejbException=" + ejbException + 
							". Runtime=" + (endTimeMS-startTimeMS) + "ms. Returning an ApplicationResponse=" + applicationResponse, ejbException);
						

//					return applicationResponse;
				}
				
				
				logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught and rethrowing(1) an ejbException=" + ejbException + 
						". Runtime=" + (endTimeMS-startTimeMS) + "ms.", ejbException);
					
				if (applicationResponse != null)
				{
					processFailureNotification(invocationContext, ejbException, applicationResponse.toString(), "N/A", endTimeMS-startTimeMS);
				
					return applicationResponse;
				}
				else
				{
					processFailureNotification(invocationContext, ejbException, "N/A", "N/A", endTimeMS-startTimeMS);
					// application exception not found in the cause, just re-throw the exception
					throw ejbException;
				}
				
			}
			else
			{
				logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught and rethrowing(2) an ejbException=" + ejbException + 
						". Runtime=" + (endTimeMS-startTimeMS) + "ms.", ejbException);
					
				processFailureNotification(invocationContext, ejbException, "N/A", "N/A", endTimeMS-startTimeMS);

				// if the return type is not an instance of ApplicationResponse then just re-throw the exception
				// until we reach the Composite Class (which returns an ApplicationResponse or its subtypes)
				throw ejbException;
			}
		}
		catch (Exception exception)
		{
			endTimeMS = System.currentTimeMillis();

			// if it is not an ApplicationException, then wrap it as an ApplicationException. Typically, the developer
			// should throw an ApplicationException to specify their own error code but if they did not handle it, we
			// need to wrap it into an ApplicationException specifying the package name, class name and method name
			// where the exception occurred. so the error code lookup will look at the right package, class, method in
			// the error code list

			// first save the exception
			String exceptionStackTrace = this.getExceptionStackTraceString(exception);
//			logger.error("ExceptionHandler.interceptorAroundInvoke(): Caught an Exception.", exception);
			ExceptionDTO exceptionDTO = (ExceptionDTO) this.createMethodInfo(new ExceptionDTO(), invocationContext);
			exceptionDTO.setExceptionStackTrace(exceptionStackTrace);
			exceptionDTO.setExceptionType(exception.getClass().getName());
			exceptionDTO.setErrorMessage(exception.getMessage());
			if (this.isExceptionLoggingEnabled())
			{
				exceptionManager.saveException(exceptionDTO);
			}

			String errorCode = exceptionDTO.getPackageName() + "." + exceptionDTO.getClassName() + "." + exceptionDTO.getMethodName();
			if (exception instanceof SOAPFaultException)
			{
				// This is a web service exception
				SOAPFaultException soapFaultException = (SOAPFaultException) exception;
				if (soapFaultException.getFault() != null && soapFaultException.getFault().getFaultCode() != null)
				{
					errorCode = errorCode + "." + soapFaultException.getFault().getFaultCode();
				}
			}


			// Hook into the Failure Notification logic
			processFailureNotification(invocationContext, exception, errorCode, exceptionDTO.toString(), endTimeMS-startTimeMS);
			
			// final check to see if it is returning ApplicationException
			final Class<?> returnType = invocationContext.getMethod().getReturnType();
			// Check if the return type is 'void'. If so, it's not possible to execute returnType.newInstance() 
			if (Void.TYPE.getClass() != returnType.getClass())
			{
				final Object returnObject = returnType.newInstance();
				if (returnObject instanceof ApplicationResponse)
				{
					ApplicationResponse applicationResponse = (ApplicationResponse) returnObject;
					String mappedErrorMessage = exceptionManager.lookupErrorMessage(errorCode);
					if (mappedErrorMessage == null)
					{
						mappedErrorMessage = exception.getMessage();
					}
					Long errorNumber = exceptionManager.lookupErrorNumber(errorCode);
					if (errorNumber == null)
					{
						exceptionManager.saveErrorCodeMessage(errorCode, mappedErrorMessage);
						// pull the error code number again
						errorNumber = exceptionManager.lookupErrorNumber(errorCode);
					}
					applicationResponse.setError(true);
					applicationResponse.setErrorCode("" + errorNumber);
					applicationResponse.setErrorMessage(mappedErrorMessage);
					if ((applicationResponse.getErrorMessage() == null || (applicationResponse.getErrorMessage().equals(""))))
					{
						applicationResponse.setErrorMessage("A general error occurred");
					}

					logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught an Exception=" + exception + 
						". Runtime=" + (endTimeMS-startTimeMS) + "ms. Returning an ApplicationResponse=" + applicationResponse +
						". exceptionDTO=" + exceptionDTO + ". ErrorCode=" + errorCode, exception);
						
					return applicationResponse;
				}
			}
			
			// wrap the exception
			ApplicationException applicationException = new ApplicationException(errorCode, exception);
			
			logger.warn("ExceptionHandler.interceptorAroundInvoke(): Caught an Exception=" + exception + 
					". Runtime=" + (endTimeMS-startTimeMS) + "ms. Returning an ApplicationException=" + applicationException +
					". exceptionDTO=" + exceptionDTO + ". ErrorCode=" + errorCode, exception);

			throw applicationException;
			
		} // of catch(Exception)

		if (INFO_LOGGER_ENABLED)
		{
			logger.info("ExceptionHandler.interceptorAroundInvoke(): EXIT. Runtime=" + (endTimeMS - startTimeMS) + "ms");
		}

		return returnValue;
	}

	/**
	 * Finds an ApplicationException from the exception cause
	 * 
	 * @param e exception
	 * @return ApplicationException if found, else null
	 */
	protected ApplicationException findApplicationException(Exception e)
	{
		Throwable throwable = e;
		while (throwable != null)
		{
			if (throwable instanceof ApplicationException)
			{
				return (ApplicationException) throwable;
			}
			logger.error("ExceptionHandler.findApplicationException(): ExceptionMessage:" + throwable.getMessage() + ". Class:" + throwable.getClass().getName());
			throwable = throwable.getCause();
		}
		return null;
	}

	/**
	 * return the exception stack trace given an exception
	 * 
	 * @param exception
	 *            exception object to process
	 * @return
	 */
	protected String getExceptionStackTraceString(Exception exception)
	{
		String exceptionStackTrace = "";
		ByteArrayOutputStream byteArrayOutputStream;
		try
		{
			byteArrayOutputStream = new ByteArrayOutputStream();
			PrintStream printStream = new PrintStream(byteArrayOutputStream);
			exception.printStackTrace(printStream);
			exceptionStackTrace = new String(byteArrayOutputStream.toByteArray());
			printStream.close();
			byteArrayOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return exceptionStackTrace;
	}

	/**
	 * @return the exceptionLoggingEnabled
	 */
	public boolean isExceptionLoggingEnabled()
	{
		return exceptionLoggingEnabled;
	}

	/**
	 * @param exceptionLoggignEnabled
	 *            the exceptionLoggingEnabled to set
	 */
	public void setExceptionLoggingEnabled(boolean exceptionLoggingEnabled)
	{
		this.exceptionLoggingEnabled = exceptionLoggingEnabled;
	}

	/**
	 * @return the exceptionStackTraceEnabled
	 */
	public boolean isExceptionStackTraceEnabled()
	{
		return exceptionStackTraceEnabled;
	}

	/**
	 * @param exceptionStackTraceEnabled
	 *            the exceptionStackTraceEnabled to set
	 */
	public void setExceptionStackTraceEnabled(boolean exceptionStackTraceEnabled)
	{
		this.exceptionStackTraceEnabled = exceptionStackTraceEnabled;
	}

	protected void getCausalChain(final List<Throwable> throwableList, final Throwable throwable)
	{
		throwableList.add(throwable);
		if (throwable.getCause() != null)
		{
			getCausalChain(throwableList, throwable.getCause());
		}
	}

	protected String throwableListAsString(final List<Throwable> throwableList)
	{
		final StringBuilder sb = new StringBuilder(120 * throwableList.size());
		boolean first = true;
		for (final Throwable throwable : throwableList)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(" --> ");

			}

			sb.append(throwable.getClass().getSimpleName()).append(":'").append(throwable.getMessage()).append("'");

		}

		return sb.toString();
	}

	
	protected void processFailureNotification(final InvocationContext invocationContext, final Exception exception, final String errorCode, 
			final String exceptionDTOInfo, final long runtime) {
		
		final Method method = invocationContext.getMethod();
		final String fullPackageClassName = method.getDeclaringClass().getName();
		final String fullPackageName = fullPackageClassName.substring(0, fullPackageClassName.lastIndexOf('.'));
//		System.out.println("ExceptionHandler.interceptorAroundInvoke(): fullPackageClassName=" + fullPackageClassName);
//		System.out.println("ExceptionHandler.interceptorAroundInvoke(): exceptionDTO.getPackageName()=" + exceptionDTO.getPackageName());
//		System.out.println("ExceptionHandler.interceptorAroundInvoke(): fullPackageName 4=" + fullPackageName);
		
		if ("com.wellpoint.mobility.aggregation.services.facade.web".equals(fullPackageName))
		{
			LinkedList<Throwable> throwableList = new LinkedList<Throwable>();
			getCausalChain(throwableList, exception);
			final String throwableListAsString = throwableListAsString(throwableList);
//			logger.error("Causal chain: " + throwableListAsString);
			final long contextId = System.currentTimeMillis();
			final String className = fullPackageClassName.substring(fullPackageClassName.lastIndexOf('.') + 1);
			final String context = "ErrorCode=" + errorCode + ". Runtime=" + runtime + "ms. exceptionDTOInfo=" + exceptionDTOInfo + ". Stack information: " + throwableListAsString;
			FailureNotificationInformationPojo fnip = new FailureNotificationInformationPojo(className, method.getName(), context, contextId, exception.getMessage());
			
			// Send the information to the log file along with the context id for easy searching.
			logger.error("ExceptionHandler.processFailureNotification(): ContextId=" + contextId + ". Context=" + context);
			
//			System.out.println("ExceptionHandler.interceptorAroundInvoke(): Submitting fni=" + fni);
			failureNotificationProducer.submitFailure(fnip);
			
		} // of if from facade class
		
	} // of processFailureNotification
		

	
} // of class
