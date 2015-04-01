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
package com.wellpoint.mobility.aggregation.core.configuration.impl;

import com.wellpoint.mobility.aggregation.core.configuration.Config;

/**
 * A class that defines the configuration of the failure notification email.
 * 
 * @author  frank.garber@wellpoint.com
 *
 */
public class PerformanceNotificationEmailConfig extends ConfigImpl {  

	private static final long serialVersionUID = 1L;

	final public static String SEARCH_KEY_OCCURRED_DATE     = "|OCCURRED_DATE|";
	final public static String SEARCH_KEY_SERVICE_NAME      = "|SERVICE_NAME|";
	final public static String SEARCH_KEY_METHOD_NAME       = "|METHOD_NAME|";
	final public static String SEARCH_KEY_ADMINTOOL_URL     = "|ADMINTOOL_URL|";
	final public static String SEARCH_KEY_ELAPSED_TIME      = "|ELAPSED_TIME|";
	final public static String SEARCH_KEY_THRESHHOLD_TIME   = "|THRESHHOLD_TIME|";



	/**
	 * This default value exemplifies the use of the replaceable variables.
	 */
	public static final String defaultBody = "This is the body. Occurred on " + SEARCH_KEY_OCCURRED_DATE + 
		". Service name: " + SEARCH_KEY_SERVICE_NAME +
		". Method name: " + SEARCH_KEY_METHOD_NAME +
		". Time threshold= " + SEARCH_KEY_THRESHHOLD_TIME+
		". Elapsed time is seconds = " + SEARCH_KEY_ELAPSED_TIME+
		". From, The Service Team! -- " + 
		". Click here " + SEARCH_KEY_ADMINTOOL_URL + " to go to the Admin Tool" +
		".";
	
	public static final String defaultSubject = "Aggregation Layer Performance Notification: Service " + SEARCH_KEY_SERVICE_NAME + ", Method " + SEARCH_KEY_METHOD_NAME + " failed " ;
	
	@Override
	public Config getDefaultConfig()
	{
		final PerformanceNotificationEmailConfig config = new PerformanceNotificationEmailConfig();
		config.setBody(defaultBody);
		config.setCc("");
		config.setFrom("default.from.address@wellpoint.com");
		config.setSubject(defaultSubject);
		config.setTo("default.to.address@wellpoint.com");
		
		return config;
	} // getDefaultConfig
	
	
	@Override
	public String toString()
	{
		return "PerformanceNotificationEmailConfig [to=" + to + ", cc=" + cc + ", from=" + from + ", body=" + body + ", subject=" + subject + "]";
	}

	protected String to, cc, from, body, subject;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCc()
	{
		return cc;
	}

	public void setCc(String cc)
	{
		this.cc = cc;
	}
	
} // of class EmailNotificationConfig



