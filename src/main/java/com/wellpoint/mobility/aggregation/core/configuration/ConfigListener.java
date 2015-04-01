/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.configuration;

import com.wellpoint.mobility.aggregation.core.configuration.Config;

/**
 * This interface provides the method callbacks when a configuration is change.
 * 
 * @author edward.biton@wellpoint.com
 * 
 */
public interface ConfigListener
{
	/**
	 * A configuration has changed
	 * 
	 * @param config
	 */
	public void onConfigChange(Config config);
}
