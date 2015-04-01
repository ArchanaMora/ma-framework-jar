package com.wellpoint.mobility.aggregation.core.configuration;


/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * Concrete class that implements the ConfigurationManager interface.
 * @author robert.swarr@wellpoint.com
 * 
 * Interface for Message-Driven Bean that publishes configuration change events
 *
 */
public interface ConfigurationChangeNotifier
{
	/**
	 * Publishes a changed Config object to a topic.
	 * @param config
	 */
	public void publishConfigurationChange(Config config);

}
