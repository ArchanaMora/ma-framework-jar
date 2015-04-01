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

import net.sf.ehcache.config.CacheConfiguration;

import com.wellpoint.mobility.aggregation.core.cachemanager.impl.ApplicationCacheStore;
import com.wellpoint.mobility.aggregation.core.cachemanager.impl.UserCacheStore;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.CacheManagerMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.MethodCacheDTO;

/**
 * Cache manager interface API
 * 
 * @author edward.biton@wellpoint.com
 */
public interface CacheManager
{
	/**
	 * All storage types (memory and database)
	 */
	public static final int ALL_STORAGE_TYPE = 1;
	/**
	 * Memory storage type
	 */
	public static final int MEMORY_STORAGE_TYPE = 2;
	/**
	 * Database storage type
	 */
	public static final int DATABASE_STORAGE_TYPE = 3;
	/**
	 * Default storage type
	 */
	public static final int DEFAULT_STORAGE_TYPE = 1;

	/**
	 * This is the size of the row for the returnValue object
	 */
	public static final int MAX_RETURN_VALUE_SIZE = 4000000;
	/**
	 * Enable or disable the cache manager
	 * 
	 * @param enable
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Returns true if the cache manager is enabled otherwise false
	 * 
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Returns the user cache given the unique id. This is a cache storage for user specific data. If the user cache is
	 * not found, it returns null
	 * 
	 * @param id
	 *            a unique id identifying the user
	 * @return
	 */
	public UserCacheStore getUserCacheStore(String id);

	/**
	 * Creates a user cache given a unique id. If the user cache already exists, it will return the existing user cache.
	 * 
	 * @param id
	 *            a unique id identifying the user
	 * @return
	 */
	public UserCacheStore createUserCacheStore(String id);

	/**
	 * Returns an application cache. This is a common cache for common data within the application. If the application
	 * cache does not exists, it creates one
	 * 
	 * @return
	 */
	public ApplicationCacheStore getApplicationCacheStore();

	/**
	 * Sets an application cache value
	 * 
	 * @param key
	 *            application cache key
	 * @param value
	 *            cache value
	 */
	public void setApplicationCacheValue(String key, Object value);

	/**
	 * gets an application cache value given the key
	 * 
	 * @param key
	 *            application cache key
	 * @return the cache value if it exists
	 */
	public Object getApplicationCacheValue(String key);

	/**
	 * delete an application cache value given a key
	 * 
	 * @param key
	 *            application cache key
	 */
	public void deleteApplicationCacheValue(String key);

	/**
	 * Set a user cache key value
	 * 
	 * @param userKey
	 *            user unique key
	 * @param key
	 *            user cache key
	 * @param value
	 *            cache value
	 */
	public void setUserCacheValue(String userKey, String key, Object value);

	/**
	 * Gets a user cache value given a key
	 * 
	 * @param userKey
	 *            user unique key
	 * @param key
	 *            user cache key
	 * @return the cache value if it exists
	 */
	public Object getUserCacheValue(String userKey, String key);

	/**
	 * Delete the user cache value
	 * 
	 * @param userKey
	 *            user unique key
	 * @param key
	 *            user cache key to delete
	 */
	public void deleteUserCacheValue(String userKey, String key);

	/**
	 * set a method cache value
	 * 
	 * @param methodCache
	 *            method information details
	 */
	public void setMethodCacheValue(MethodCacheDTO methodCache);

	/**
	 * returns a method cache value given the method call signature
	 * 
	 * @param callSignature
	 * @return cache value
	 */
	public Object getMethodCacheValue(String callSignature);

	/**
	 * delete a method cache value
	 * 
	 * @param callSignature
	 *            call signature
	 */
	public void deleteMethodCacheValue(String callSignature);

	/**
	 * @return get method returning the UserCache configuration
	 */
	public CacheConfiguration getUserCacheConfiguration();

	/**
	 * Clears the application cache in-memory
	 */
	public void clearApplicationCache(int storageType);

	/**
	 * Clears the method cache in-memory
	 */
	public void clearMethodCache(int storageType);

	/**
	 * Clears the user cache in-memory given the user key
	 * 
	 * @param userKey
	 *            user cache to clear. if null clears all user cache
	 */
	public void clearUserCache(int storageType, String userKey);

	/**
	 * Post a message to the cache manager
	 * 
	 * @param cacheManagerMessage
	 */
	public void postMessage(CacheManagerMessage cacheManagerMessage);
}
