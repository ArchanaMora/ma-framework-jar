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

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheStore;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.CacheValueDTO;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;
import com.wellpoint.mobility.aggregation.persistence.domain.ApplicationCache;
import com.wellpoint.mobility.aggregation.persistence.domain.UserCache;

/**
 * Implements the CacheStore interface for user scope caching
 * 
 * @author edward.biton@wellpoint.com
 */
public class UserCacheStore implements CacheStore
{
	/**
	 * The unique user id that this cache is for
	 */
	private String userId = null;
	/**
	 * User cache object
	 */
	private Cache cache;
	/**
	 * Entity Manager
	 */
	private EntityManager entityManager;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(UserCacheStore.class);

	/**
	 * Constructor. Creates a user cache given the unique id
	 * 
	 * @param cacheConfiguration
	 *            cache configuration
	 * @param userId
	 *            user cache unique id
	 */
	public UserCacheStore(CacheConfiguration cacheConfiguration, String userId)
	{
		this.cache = new Cache(cacheConfiguration);
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.Cache#getCacheName()
	 */
	@Override
	public String getCacheName()
	{
		return userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.cachemanager.CacheStore#setCacheValue(com.wellpoint.mobility.aggregation
	 * .core.cachemanager.pojo.CacheValueDTO)
	 */
	@Override
	public void setCacheValue(CacheValueDTO cacheValue)
	{
		String key = cacheValue.getCacheKey();
		Object value = cacheValue.getCacheValue();

		// always put the value in the cache, replace existing if the key already exists
		Element ehCacheValue = new Element(key, value);
		// set additional properties here
		// remove the existing value from the cache
		cache.remove(key);
		// put the new value in the cache
		// this is to prevent an update instead of new entry
		cache.put(ehCacheValue);

		// delete existing entry in the database
		Query query = entityManager.createQuery("DELETE FROM UserCache uc WHERE uc.userKey = :userKey AND uc.cacheKey = :cacheKey");
		query.setParameter("userKey", userId);
		query.setParameter("cacheKey", key);
		int deleted = query.executeUpdate();
		if (deleted == 0)
		{
			logger.debug(key + " cache value is not found in the database");
		}
		else
		{
			logger.debug(key + " old cache value is deleted from the database");
		}

		// inserting the new set cache value

		// use a single variable to set the same value for all the time fields
		// e.g. createdDate, updatedDate, lastAccessTime
		long currentTime = System.currentTimeMillis();

		// create application cache data
		UserCache userCache = new UserCache();
		userCache.setUserKey(this.userId);
		userCache.setCacheKey(key);
		userCache.setCacheType(value.getClass().getName());
		userCache.setCacheValue(XML2ObjectUtility.toXml(value));
		userCache.setLastAccessTime(currentTime);
		userCache.setExpireDuration(cacheValue.getExpireDuration());
		userCache.setExpiresOn(cacheValue.getExpiresOn());
		userCache.setHitCount(1);
		userCache.setCreatedBy("SYSTEM");
		userCache.setCreatedDate(new Timestamp(currentTime));
		userCache.setUpdatedBy("SYSTEM");
		userCache.setUpdatedDate(new Timestamp(currentTime));
		entityManager.persist(userCache);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheStore#getCacheValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object getCacheValue(String cacheKey)
	{
		Object value = null;
		// check if the value is in memory
		Element element = cache.get(cacheKey);
		if (element != null)
		{
			logger.debug(cacheKey + " cache value is found in the memory");
			logger.debug("key=" + cacheKey);
			logger.debug("creationTime=" + element.getCreationTime());
			logger.debug("expirationTime=" + element.getExpirationTime());
			logger.debug("timeToLive=" + element.getTimeToLive());
			logger.debug("hitCount=" + element.getHitCount());
			logger.debug("serializable=" + element.isSerializable());
			logger.debug("serializedSize=" + element.getSerializedSize());
			logger.debug("version=" + element.getVersion());
			value = element.getObjectValue();
			logger.debug("data loaded from the memory for [" + cacheKey + "=" + value.toString() + "]");
			// TODO we should not return yet, instead updated the hitCount and lastAccessTime fields
			// on the database. But for now, we will skip this
			return value;
		}
		logger.debug(cacheKey + " cache value is not found in the memory");

		// if not in the memory, check from the database
		Query query = entityManager.createQuery("SELECT ap FROM ApplicationCache ap WHERE ap.cacheKey = :cacheKey", ApplicationCache.class);
		query.setParameter("cacheKey", cacheKey);
		try
		{
			ApplicationCache applicationCache = (ApplicationCache) query.getSingleResult();
			if (applicationCache != null)
			{
				logger.debug(cacheKey + " cache value is found in the database");
				// update the necessary fields for statistics
				applicationCache.setHitCount(applicationCache.getHitCount() + 1);
				applicationCache.setLastAccessTime(System.currentTimeMillis());

				if (applicationCache.getCacheValue() != null)
				{
					String xmlValue = new String(applicationCache.getCacheValue());
					value = XML2ObjectUtility.toObject(xmlValue);
					logger.debug("Data loaded from the database for [" + cacheKey + " with the value of " + value + "]");

					// store this value in the memory again
					logger.debug("Storing " + cacheKey + " to memory for faster access");
					this.cache.put(new Element(cacheKey, value));
				}
				// update the database
				try
				{
					entityManager.merge(applicationCache);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			logger.debug(cacheKey + " cache value is not found in the database");
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheStore#removeCacheValue(java.lang.String)
	 */
	@Override
	public void removeCacheValue(String cacheKey)
	{
		// remove the value from the in-memory cache
		cache.remove(cacheKey);
		// remove the value from the database
		try
		{
			Query query = entityManager.createQuery("DELETE FROM UserCache uc WHERE uc.userKey = :userKey AND uc.cacheKey = :cacheKey");
			query.setParameter("userKey", userId);
			query.setParameter("cacheKey", cacheKey);
			int deleted = query.executeUpdate();
			logger.debug("Deleted " + deleted + " UserCache rows");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheStore#clearCache(int)
	 */
	@Override
	public void clearCache(int storageType)
	{
		if ((storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.ALL_STORAGE_TYPE)
				|| (storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.MEMORY_STORAGE_TYPE))
		{
			logger.info("clearning user in-memory cache for " + this.userId);
			cache.removeAll();
		}
		if ((storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.ALL_STORAGE_TYPE)
				|| (storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.DATABASE_STORAGE_TYPE))
		{
			try
			{
				Query query = entityManager.createQuery("DELETE FROM UserCache uc WHERE uc.userKey = :userKey");
				query.setParameter("userKey", this.userId);
				int deleted = query.executeUpdate();
				logger.debug("Deleted " + deleted + " rows on UserCache");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the cache
	 */
	public Cache getCache()
	{
		return cache;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	public void setCache(Cache cache)
	{
		this.cache = cache;
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	/**
	 * @param entityManager
	 *            the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger()
	{
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

}
