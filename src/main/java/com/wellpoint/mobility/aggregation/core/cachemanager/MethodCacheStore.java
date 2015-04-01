/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager;

import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.MethodCacheDTO;

/**
 * Method cache storage interface which extends the CacheStore. This provides additional method to store and read method
 * calls cache values
 * 
 * @author edward.biton@wellpoint.com
 */
public interface MethodCacheStore extends CacheStore
{

	/**
	 * Stores the data in the cache using the key
	 * 
	 * @param methodCacheDTO
	 *            information about the method call which contains the method signature and the method return value
	 */
	public void setMethodCacheValue(MethodCacheDTO methodCacheDTO);

	/**
	 * Loads the cache value given a call signature
	 * 
	 * @param callSignature
	 *            information about the method call which contains the method signature and the method return value
	 */
	public Object getMethodCacheValue(String callSignature);

	/**
	 * Removes a method cache value
	 * 
	 * @param callSignature
	 *            method signature
	 */
	public void removeMethodCacheValue(String callSignature);

	/**
	 * Clears method in memory cache
	 */
	public void clearMethodCache(int storageType);
}
