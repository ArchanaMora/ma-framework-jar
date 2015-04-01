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

import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.CacheValueDTO;

/**
 * Cache storage interface.
 * 
 * @author edward.biton@wellpoint.com
 */
public interface CacheStore
{
	/**
	 * Returns the name of this cache
	 * 
	 * @return
	 */
	public String getCacheName();

	/**
	 * Returns the cached value given the key if it exists. Returns null if data has not be cached
	 * 
	 * @param cacheKey
	 *            cache key
	 * @return
	 */
	public Object getCacheValue(String cacheKey);

	/**
	 * Stores a cache value
	 * 
	 * @param cacheValue
	 *            cache value object that contains the key and value
	 */
	public void setCacheValue(CacheValueDTO cacheValue);

	/**
	 * Deletes a cache value from the store given a key
	 * 
	 * @param cacheKey
	 *            cache key
	 */
	public void removeCacheValue(String cacheKey);

	/**
	 * Clears in memory cache
	 */
	public void clearCache(int storageType);
}
