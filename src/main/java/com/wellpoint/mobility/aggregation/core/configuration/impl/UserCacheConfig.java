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

public class UserCacheConfig extends ConfigImpl {
	public static final long DEFAULT_MAX_HEAP_SIZE = 1000;
	
	public long maxEntriesLocalHeap;
	
	@Override
	public String toString() {
		return "UserCacheConfig [DEFAULT_MAX_HEAP_SIZE=" + DEFAULT_MAX_HEAP_SIZE + ", maxEntriesLocalHeap=" + maxEntriesLocalHeap + "]";
	}
} // of class
