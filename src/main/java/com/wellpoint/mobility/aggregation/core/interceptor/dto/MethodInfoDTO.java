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

import java.util.List;

/**
 * A method information object that contains the details about a method call including the actual parameters and return
 * value
 * 
 * @author edward.biton@wellpoint.com
 */
public class MethodInfoDTO
{
	/**
	 * The method signature the identifies a unique method within the class
	 */
	private String methodSignature;
	/**
	 * A signature that identifies a unique method call using the actual parameters
	 */
	private String callSignature;
	/**
	 * Package name of the class where this method belongs
	 */
	private String packageName;
	/**
	 * Class name where this method belongs
	 */
	private String className;
	/**
	 * Method name
	 */
	private String methodName;
	/**
	 * The list of parameters of the method
	 */
	private List<MethodParamDTO> methodParams;
	/**
	 * Return type of the method
	 */
	private String returnType;
	/**
	 * Return value of the method call
	 */
	private Object returnValue;
	/**
	 * Exception type if there was an exception that was thrown. Null if the method did not throw an exception
	 */
	private String exceptionType;

	/**
	 * @return the methodSignature
	 */
	public String getMethodSignature()
	{
		return methodSignature;
	}

	/**
	 * @param methodSignature
	 *            the methodSignature to set
	 */
	public void setMethodSignature(String methodSignature)
	{
		this.methodSignature = methodSignature;
	}

	/**
	 * @return the callSignature
	 */
	public String getCallSignature()
	{
		return callSignature;
	}

	/**
	 * @param callSignature
	 *            the callSignature to set
	 */
	public void setCallSignature(String callSignature)
	{
		this.callSignature = callSignature;
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
	 * @return the methodParams
	 */
	public List<MethodParamDTO> getMethodParams()
	{
		return methodParams;
	}

	/**
	 * @param methodParams
	 *            the methodParams to set
	 */
	public void setMethodParams(List<MethodParamDTO> methodParams)
	{
		this.methodParams = methodParams;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType()
	{
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType)
	{
		this.returnType = returnType;
	}

	/**
	 * @return the returnValue
	 */
	public Object getReturnValue()
	{
		return returnValue;
	}

	/**
	 * @param returnValue
	 *            the returnValue to set
	 */
	public void setReturnValue(Object returnValue)
	{
		this.returnValue = returnValue;
	}

	/**
	 * @return the exceptionType
	 */
	public String getExceptionType()
	{
		return exceptionType;
	}

	/**
	 * @param exceptionType
	 *            the exceptionType to set
	 */
	public void setExceptionType(String exceptionType)
	{
		this.exceptionType = exceptionType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MethodInfoDTO [methodSignature=" + methodSignature + ", callSignature=" + callSignature + ", packageName=" + packageName + ", className="
				+ className + ", methodName=" + methodName + ", methodParams=" + methodParams + ", returnType=" + returnType + ", returnValue=" + returnValue
				+ ", exceptionType=" + exceptionType + "]";
	}

}
