/**
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
* Author: frank.garber@wellpoint.com
* 
*/
package com.wellpoint.mobility.aggregation.core.failurenotification;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * This object will be created and populated by the services encountering
 * a failure. It will be passed to the onMessage() method of the MDB. The 
 * constructor sets the failureDate to 'now', it can be overriden by calling
 * the setFailureDate(Date) method.
 *
 */
public class FailureNotificationInformationPojo implements Serializable
{

	private static final long serialVersionUID = 1L;
	final protected String className;
	final protected String methodName;
	final protected String context; 
	protected String exceptionMessage;
	protected long failureDateMS;
	protected long contextId;
	
	/**
	 * @param className the name of the service that had the failure
	 */
	public FailureNotificationInformationPojo(final String className, final String methodName) {
		this(className, methodName, "No context supplied.", 0l, null);
	} // of ctor
	
	/**
	 * @param className the name of the service that had the failure
	 * @param context optional context information
	 */
	public FailureNotificationInformationPojo(final String className, final String methodName, final String context, final long contextId, final String exceptionMessage) {
		if (StringUtils.isEmpty(className))
		{
			throw new IllegalArgumentException("serviceName is a required argument!");
		}
		failureDateMS = new Date().getTime();
		this.className = className;
		this.methodName = methodName;
		this.contextId = contextId;
		this.exceptionMessage = exceptionMessage;
		this.context = StringUtils.isEmpty(context) ? "" : context;
	} // of ctor
	
	

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public String getContext()
	{
		return context;
	}

	public long getFailureDateMS()
	{
		return failureDateMS;
	}

	public void setFailureDateMS(Date failureDate)
	{
		this.failureDateMS = failureDate.getTime();
	}

	@Override
	public String toString() {
		return "FailureNotificationInformationPojo [className=" + className + ", methodName=" + methodName + ", context=" + context + ", exceptionMessage="
				+ exceptionMessage + ", failureDateMS=" + failureDateMS + ", contextId=" + contextId + "]";
	}

	public long getContextId() {
		return contextId;
	}

	public void setContextId(long contextId) {
		this.contextId = contextId;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

} // of class
