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

import java.io.InputStream;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.cachemanager.MethodCacheStore;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.CacheValueDTO;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.MethodCacheDTO;
import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodParamDTO;
import com.wellpoint.mobility.aggregation.core.scheduler.Scheduler;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;
import com.wellpoint.mobility.aggregation.persistence.domain.ApplicationCache;
import com.wellpoint.mobility.aggregation.persistence.domain.MethodCache;
import com.wellpoint.mobility.aggregation.persistence.domain.MethodCacheMethodParam;

/**
 * Implements the CacheStore interface for application scope caching
 * 
 * @author edward.biton@wellpoint.com
 */
public class ApplicationCacheStore implements MethodCacheStore
{
	/**
	 * Cache name
	 */
	public static final String CACHE_NAME = "Application";

	/**
	 * Application cache object
	 */
	private Cache cache;

	/**
	 * method cache object
	 */
	private Cache methodCache;

	/**
	 * Entity Manager. This is injected by the CacheManager implementation
	 */
	private EntityManager entityManager;

	/**
	 * Scheduler
	 */
	private Scheduler scheduler;
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ApplicationCacheStore.class);
	/**
	 * Is debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	/**
	 * Constructor
	 * 
	 * @param cacheConfiguration
	 */
	public ApplicationCacheStore(CacheConfiguration cacheConfiguration)
	{
		// CacheManager cacheManager = CacheManager.newInstance("ehcache.xml");
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ehcache.xml");
		CacheManager cacheManager = CacheManager.newInstance(inputStream);
		cache = cacheManager.getCache("APPLICATION");
		methodCache = cacheManager.getCache("METHOD_CACHE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.Cache#getCacheName()
	 */
	@Override
	public String getCacheName()
	{
		return CACHE_NAME;
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
		Query query = entityManager.createQuery("DELETE FROM ApplicationCache ac WHERE ac.cacheKey = :cacheKey");
		query.setParameter("cacheKey", key);
		int deleted = query.executeUpdate();
		if (DEBUG_ENABLED)
		{
			if (deleted == 0)
			{
				logger.debug(key + " cache value is not found in the database");
			}
			else
			{
				logger.debug(key + " old cache value is deleted from the database");
			}
		}
		// inserting the new set cache value
		// use a single variable to set the same value for all the time fields
		// e.g. createdDate, updatedDate, lastAccessTime
		long currentTime = System.currentTimeMillis();

		// create application cache data
		ApplicationCache applicationCache = new ApplicationCache();
		applicationCache.setCacheKey(key);
		applicationCache.setCacheType(value.getClass().getName());
		applicationCache.setCacheValue(XML2ObjectUtility.toXml(value));
		applicationCache.setLastAccessTime(currentTime);
		applicationCache.setExpireDuration(cacheValue.getExpireDuration());
		applicationCache.setExpiresOn(cacheValue.getExpiresOn());
		applicationCache.setHitCount(1);
		applicationCache.setCreatedBy("SYSTEM");
		applicationCache.setCreatedDate(new Timestamp(currentTime));
		applicationCache.setUpdatedBy("SYSTEM");
		applicationCache.setUpdatedDate(new Timestamp(currentTime));
		entityManager.persist(applicationCache);
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
			value = element.getObjectValue();
			if (DEBUG_ENABLED)
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
				logger.debug("data loaded from the memory for [" + cacheKey + "=" + value + "]");
			}
			// TODO we should not return yet, instead updated the hitCount and lastAccessTime fields
			// on the database. But for now, we will skip this
			return value;
		}
		if (DEBUG_ENABLED)
		{
			logger.debug(cacheKey + " cache value is not found in the memory");
		}
		// if not in the memory, check from the database
		Query query = entityManager.createQuery("SELECT ap FROM ApplicationCache ap WHERE ap.cacheKey = :cacheKey", ApplicationCache.class);
		query.setParameter("cacheKey", cacheKey);
		try
		{
			ApplicationCache applicationCache = (ApplicationCache) query.getSingleResult();
			if (applicationCache != null)
			{
				if (DEBUG_ENABLED)
				{
					logger.debug(cacheKey + " cache value is found in the database");
				}
				// update the necessary fields for statistics
				applicationCache.setHitCount(applicationCache.getHitCount() + 1);
				applicationCache.setLastAccessTime(System.currentTimeMillis());

				if (applicationCache.getCacheValue() != null)
				{
					String xmlValue = new String(applicationCache.getCacheValue());
					value = XML2ObjectUtility.toObject(xmlValue);

					if (DEBUG_ENABLED)
					{
						logger.debug("Data loaded from the database for [" + cacheKey + " with the value of " + value + "]");
						// store this value in the memory again
						logger.debug("Storing " + cacheKey + " to memory for faster access");
					}
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
			if (DEBUG_ENABLED)
			{
				logger.debug(cacheKey + " cache value is not found in the database");
			}
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
			Query query = entityManager.createQuery("DELETE FROM ApplicationCache ac WHERE ac.cacheKey = :cacheKey");
			query.setParameter("cacheKey", cacheKey);
			int deleted = query.executeUpdate();
			if (DEBUG_ENABLED)
			{
				logger.debug("Deleted " + deleted + " ApplicationCache rows");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.cachemanager.MethodCacheStore#setValue(com.wellpoint.mobility.aggregation
	 * .core.cachemanager.pojo.MethodCacheDTO)
	 */
	@Override
	public void setMethodCacheValue(MethodCacheDTO methodCacheDTO)
	{
		String key = methodCacheDTO.getCallSignature();
		Object value = methodCacheDTO.getReturnValue();

		// always put the value in the cache, replace existing if the key already exists
		Element cacheValue = new Element(key, value);
		cacheValue.setTimeToLive((int) (methodCacheDTO.getExpireDuration() / 1000));
		// set additional properties here
		// remove the existing value from the cache
		methodCache.remove(key);
		// put the new value in the cache
		// this is to prevent an update instead of new entry
		methodCache.put(cacheValue);

		long startTime = System.currentTimeMillis();
		deleteMethodCache(key);
		System.out.println("(OPTMIZATION) DELETE METHOD CACHE=" + (System.currentTimeMillis() - startTime) + "ms");
		// save the method cache through async tasks
		if (DEBUG_ENABLED)
		{
			logger.debug("Converting object to XML");
		}
		startTime = System.currentTimeMillis();
		String xmlValue = XML2ObjectUtility.toXml(value);
		System.out.println("(OPTMIZATION) CONVERTING TO XML=" + (System.currentTimeMillis() - startTime) + "ms");
		if (xmlValue != null && xmlValue.length() > com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.MAX_RETURN_VALUE_SIZE)
		{
			logger.warn("The size of the data " + xmlValue.length() + " is bigger than the table column size. Data will not be cache to the database");
			return;
		}
		// inserting the new set cache value
		// use a single variable to set the same value for all the time fields
		// e.g. createdDate, updatedDate, lastAccessTime
		startTime = System.currentTimeMillis();
		try
		{
			long currentTime = System.currentTimeMillis();
			MethodCache methodCache = new MethodCache();
			methodCache.setCallSignature(methodCacheDTO.getCallSignature());
			methodCache.setPackageName(methodCacheDTO.getPackageName());
			methodCache.setClassName(methodCacheDTO.getClassName());
			methodCache.setMethodName(methodCacheDTO.getMethodName());
			methodCache.setReturnType(methodCacheDTO.getReturnType());
			methodCache.setReturnValue(xmlValue);
			methodCache.setLastAccessTime(currentTime);
			methodCache.setExpireDuration(methodCacheDTO.getExpireDuration());
			methodCache.setExpiresOn(methodCacheDTO.getExpiresOn());
			methodCache.setHitCount(1);
			methodCache.setCreatedBy("SYSTEM");
			methodCache.setCreatedDate(new Timestamp(currentTime));
			methodCache.setUpdatedBy("SYSTEM");
			methodCache.setUpdatedDate(new Timestamp(currentTime));
			startTime = System.currentTimeMillis();
			entityManager.persist(methodCache);

			// create the method parameters
			for (MethodParamDTO methodParamDTO : methodCacheDTO.getMethodParams())
			{
				MethodCacheMethodParam methodParam = new MethodCacheMethodParam();
				methodParam.setMethodCache(methodCache);
				methodParam.setParamPosition(methodParamDTO.getParamPosition());
				methodParam.setParamType(methodParamDTO.getParamType());
				methodParam.setParamValue(XML2ObjectUtility.toXml(methodParamDTO.getParamValue()));
				methodParam.setCreatedBy("SYSTEM");
				methodParam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				methodParam.setUpdatedBy("SYSTEM");
				methodParam.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(methodParam);
			}
			System.out.println("(OPTMIZATION) ACTUAL SAVING=" + (System.currentTimeMillis() - startTime) + "ms");
		}
		catch (Exception e)
		{
			logger.error("Error saving to cache table");
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.cachemanager.MethodCacheStore#removeMethodCacheValue(java.lang.String)
	 */
	@Override
	public void removeMethodCacheValue(String callSignature)
	{
		// delete in-memory cache
		methodCache.remove(callSignature);
		// delete database cache
		deleteMethodCache(callSignature);
	}

	/**
	 * Deletes an existing cache entry given the call signature
	 * 
	 * @param callSignature
	 *            the signature to be deleted
	 */
	protected void deleteMethodCache(String callSignature)
	{
		Query query = entityManager.createQuery("SELECT mc FROM MethodCache mc WHERE mc.callSignature = :callSignature", MethodCache.class);
		query.setParameter("callSignature", callSignature);
		try
		{
			MethodCache methodCache = (MethodCache) query.getSingleResult();
			if (methodCache != null)
			{
				query = entityManager.createQuery("DELETE FROM MethodCacheMethodParam mcmp WHERE mcmp.methodCacheId = :methodCacheId");
				query.setParameter("methodCacheId", methodCache.getId());
				int deleted = query.executeUpdate();
				if (DEBUG_ENABLED)
				{
					logger.debug("Deleted " + deleted + " MethodCacheMethodParam rows");
				}
				query = entityManager.createQuery("DELETE FROM MethodCache mc WHERE mc.id = :methodCacheId");
				query.setParameter("methodCacheId", methodCache.getId());
				deleted = query.executeUpdate();
				if (DEBUG_ENABLED)
				{
					logger.debug("Deleted " + deleted + " MethodCache rows");
				}
			}
		}
		catch (Exception e)
		{
			if (DEBUG_ENABLED)
			{
				logger.debug(callSignature + " cache value is not found in the database");
			}
		}
	}

	@Override
	public Object getMethodCacheValue(String callSignature)
	{
		Object value = null;
		// check if the value is in memory
		Element element = methodCache.get(callSignature);
		if (element != null)
		{
			value = element.getObjectValue();
			if (DEBUG_ENABLED)
			{
				logger.debug(callSignature + " is cache value is found in the memory");
				logger.debug("callSignature=" + callSignature);
				logger.debug("creationTime=" + element.getCreationTime());
				logger.debug("expirationTime=" + element.getExpirationTime());
				logger.debug("timeToLive=" + element.getTimeToLive());
				logger.debug("hitCount=" + element.getHitCount());
				logger.debug("serializable=" + element.isSerializable());
				logger.debug("serializedSize=" + element.getSerializedSize());
				logger.debug("version=" + element.getVersion());
				logger.debug("data loaded from the memory for [" + callSignature + "=" + value.toString() + "]");
			}
			// TODO we should not return yet, instead updated the hitCount and lastAccessTime fields
			// on the database. But for now, we will skip this
			return value;
		}
		if (DEBUG_ENABLED)
		{
			logger.debug(callSignature + " cache value is not found in the memory");
		}
		// if not in the memory, check from the database
		Query query = entityManager.createQuery("SELECT mc  FROM MethodCache mc WHERE mc.callSignature = :callSignature", MethodCache.class);
		query.setParameter("callSignature", callSignature);
		try
		{
			MethodCache methodCache = (MethodCache) query.getSingleResult();
			if (methodCache != null)
			{
				if (DEBUG_ENABLED)
				{
					logger.debug(callSignature + " cache value is found in the database");
				}
				// update the necessary fields for statistics
				methodCache.setHitCount(methodCache.getHitCount() + 1);
				methodCache.setLastAccessTime(System.currentTimeMillis());

				if (methodCache.getReturnValue() != null)
				{
					String xmlValue = new String(methodCache.getReturnValue());
					value = XML2ObjectUtility.toObject(xmlValue);
					if (DEBUG_ENABLED)
					{
						logger.debug("Data loaded from the database for [" + callSignature + " with the value of " + value + "]");
						// store this value in the memory again
						logger.debug("Storing " + callSignature + " to memory for faster access");
					}
					this.methodCache.put(new Element(callSignature, value));
				}
				// update the database
				try
				{
					entityManager.merge(methodCache);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			if (DEBUG_ENABLED)
			{
				logger.debug(callSignature + " cache value is not found in the database");
			}
		}
		return value;
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
			logger.info("Clearing application in-memory cache");
			cache.removeAll();
		}
		if ((storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.ALL_STORAGE_TYPE)
				|| (storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.DATABASE_STORAGE_TYPE))
		{
			try
			{
				Query query = entityManager.createQuery("DELETE FROM ApplicationCache");
				int deleted = query.executeUpdate();
				if (DEBUG_ENABLED)
				{
					logger.debug("Deleted " + deleted + " rows on ApplicationCache");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.MethodCacheStore#clearMethodCache(int)
	 */
	@Override
	public void clearMethodCache(int storageType)
	{
		if ((storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.ALL_STORAGE_TYPE)
				|| (storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.MEMORY_STORAGE_TYPE))
		{
			logger.info("Clearing method in-memory cache");
			methodCache.removeAll();
		}
		if ((storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.ALL_STORAGE_TYPE)
				|| (storageType == com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager.DATABASE_STORAGE_TYPE))
		{
			logger.info("Clearing method database cache");
			Query query;
			int deleted;
			try
			{
				query = entityManager.createQuery("DELETE FROM MethodCacheMethodParam");
				deleted = query.executeUpdate();
				if (DEBUG_ENABLED)
				{
					logger.debug("Deleted " + deleted + " rows on MethodCacheMethodParam");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				query = entityManager.createQuery("DELETE FROM MethodCache");
				deleted = query.executeUpdate();
				if (DEBUG_ENABLED)
				{
					logger.debug("Deleted " + deleted + " rows on MethodCache");
				}
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
	 * @return the methodCache
	 */
	public Cache getMethodCache()
	{
		return methodCache;
	}

	/**
	 * @param methodCache
	 *            the methodCache to set
	 */
	public void setMethodCache(Cache methodCache)
	{
		this.methodCache = methodCache;
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
	 * @return the scheduler
	 */
	public Scheduler getScheduler()
	{
		return scheduler;
	}

	/**
	 * @param scheduler
	 *            the scheduler to set
	 */
	public void setScheduler(Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}
}
