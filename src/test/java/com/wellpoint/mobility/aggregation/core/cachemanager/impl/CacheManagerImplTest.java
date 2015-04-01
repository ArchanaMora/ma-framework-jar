/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.CacheValueDTO;

/**
 * Test class for the default CacheManager implementation
 * 
 * @author edward.biton@wellpoint.com
 */
public class CacheManagerImplTest
{
	/**
	 * cache manager instance
	 */
	private CacheManager cacheManager;

	@Before
	public void init()
	{
		// for testing purposes, instantiate the cache manager implementation
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		cacheManager = new CacheManagerDefaultImpl();
		((CacheManagerDefaultImpl) cacheManager).setEntityManager(entityManager);
	}

	@Test
	public void testGetUserCache()
	{
		UserCacheStore userCache = cacheManager.getUserCacheStore("123");
		Assert.assertNull(userCache);
	}

	@Test
	public void testCreateUserCacheStore()
	{
		UserCacheStore userCacheStore = cacheManager.getUserCacheStore("123");
		Assert.assertNull(userCacheStore);

		cacheManager.createUserCacheStore("123");
		userCacheStore = cacheManager.getUserCacheStore("123");
		Assert.assertNotNull(userCacheStore);
	}

	@Test
	public void testGetApplicationCacheStore()
	{
		//TODO change the implementation to avoid nullpointer
		ApplicationCacheStore applicationCacheStore = null;//cacheManager.getApplicationCacheStore();
		// application cache will not be null.
		// there should always be an application cache
		Assert.assertNotNull(applicationCacheStore);

		// since the cache is empty, this should be null
		String value = (String) applicationCacheStore.getCacheValue("KEY");
		Assert.assertNull(value, null);

		// store some values and read it again
		CacheValueDTO cacheValue = new CacheValueDTO("KEY", "VALUE");
		applicationCacheStore.setCacheValue(cacheValue);
		value = (String) applicationCacheStore.getCacheValue("KEY");
		Assert.assertEquals(value, "VALUE");
	}
}
