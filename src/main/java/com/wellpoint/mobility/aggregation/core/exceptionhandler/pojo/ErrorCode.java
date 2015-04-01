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

/**
 * An ErrorCode DTO object than encapsulates the error code details
 * 
 * <pre>
 * <code>
 * ErrorCode errorCode = new ErrorCode(SampleClass.class, "sampleMethod", "appIdRequired");
 * throw ApplicationException(errorCode);
 * </code>
 * </pre>
 * 
 * @author edward.biton@wellpoint.com
 */
public class ErrorCode
{
	/**
	 * package causing the error
	 */
	private String packageName;
	/**
	 * class name causing the error
	 */
	private String className;
	/**
	 * method name causing the error
	 */
	private String methodName;
	/**
	 * error code
	 */
	private String errorCode;

	/**
	 * Constructor that accepts a class, a method name and error code.
	 * 
	 * @param clazz
	 *            class that is causing the error
	 * @param methodName
	 *            method name causing the error
	 * @param errorCode
	 *            errorCode
	 */
	public ErrorCode(@SuppressWarnings("rawtypes") Class clazz, String methodName, String errorCode)
	{
		this.setPackageName(clazz.getPackage().getName());
		this.setClassName(clazz.getSimpleName());
		this.setMethodName(methodName);
		this.setErrorCode(errorCode);
	}

	/**
	 * This constructor is designed for creating an ErrorCode object without specifying the actual errorCode. This
	 * constructor is created for reusability and should be use with caution. A business logic can have multiple
	 * validation within the method, and creating a single ErrorCode object for the entire validation process reduces
	 * the call to new operator, but the developer needs to set the errorCode before passing this object to the
	 * ApplicationException
	 * 
	 * <pre>
	 * <code>
	 * ErrorCode errorCode = new ErrorCode(Composite.class, "compositeMethod");
	 * if (params == null) 
	 * {
	 *    errorCode.setErrorCode("paramsRequired");
	 *    throw ApplicationException(errorCode);
	 * }
	 * if (params.getId() == null) 
	 * {
	 *    errorCode.setErrorCode("idRequired");
	 *    throw ApplicationException(errorCode);
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param clazz
	 *            class that is causing the error
	 * @param methodName
	 *            method name causing the error
	 */
	public ErrorCode(@SuppressWarnings("rawtypes") Class clazz, String methodName)
	{
		this.setPackageName(clazz.getPackage().getName());
		this.setClassName(clazz.getSimpleName());
		this.setMethodName(methodName);
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName()
	{
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
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
	 * returns the fully qualified name of the error code
	 */
	public String getFullErrorCode()
	{
		return packageName + "." + className + "." + methodName + "." + errorCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ErrorCode [packageName=" + packageName + ", className=" + className + ", methodName=" + methodName + ", errorCode=" + errorCode + "]";
	}

}
