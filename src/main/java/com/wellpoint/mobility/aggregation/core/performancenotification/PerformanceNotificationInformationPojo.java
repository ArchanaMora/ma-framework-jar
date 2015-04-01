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
package com.wellpoint.mobility.aggregation.core.performancenotification;

import java.io.Serializable;
import java.util.Date;

/**
 * This object will be created and populated by the services encountering
 * a failure. It will be passed to the onMessage() method of the MDB. The 
 * constructor sets the failureDate to 'now', it can be overriden by calling
 * the setFailureDate(Date) method.
 *
 */
public class PerformanceNotificationInformationPojo implements Serializable
{

	private static final long serialVersionUID = 1L;
	final protected String className;
	final protected String packageName;
	final protected String methodName;
	final protected long occuranceDateMS;
	final protected long elapsedRuntime;
	
	/**
	 * @param className the name of the service that had the failure
	 */
	/**
	 * @param packageName
	 * @param className
	 * @param methodName
	 * @param elapsedRuntime
	 */
	public PerformanceNotificationInformationPojo(final String packageName, final String className, final String methodName, final long elapsedRuntime) {
		occuranceDateMS = new Date().getTime();
		this.packageName = packageName;
		this.className = className;
		this.methodName = methodName;
		this.elapsedRuntime = elapsedRuntime;
	} // of ctor
	

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}


	public String getPackageName() {
		return packageName;
	}


	public long getOccuranceDateMS() {
		return occuranceDateMS;
	}


	public long getElapsedRuntime() {
		return elapsedRuntime;
	}


	@Override
	public String toString() {
		return "PerformanceNotificationInformationPojo [className=" + className + ", packageName=" + packageName + ", methodName=" + methodName
				+ ", occuranceDateMS=" + occuranceDateMS + ", elapsedRuntime=" + elapsedRuntime + "]";
	}
	

} // of class
