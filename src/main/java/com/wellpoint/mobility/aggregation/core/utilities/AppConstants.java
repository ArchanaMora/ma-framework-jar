/*
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
*/
package com.wellpoint.mobility.aggregation.core.utilities;

/**
 * A class to hold application wide constants.
 * 
 * @author frank.garber@wellpoint.com
 *
 */
public class AppConstants
{

	private AppConstants() {}
	
	// Environment values. Must match what's in the Properties DB with key "com.wellpoint.mobility.aggregation.core.environment"
	public static final String ENVIRONMENT_LOCAL = "local";
	public static final String ENVIRONMENT_DEV   = "dev";
	public static final String ENVIRONMENT_UAT   = "uat";
	public static final String ENVIRONMENT_SIT   = "sit";
	public static final String ENVIRONMENT_PERF  = "perf";
	public static final String ENVIRONMENT_PROD  = "prod";
	
	
} // of class
