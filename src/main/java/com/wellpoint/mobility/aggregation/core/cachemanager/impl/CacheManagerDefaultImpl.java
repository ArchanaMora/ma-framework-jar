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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import net.sf.ehcache.config.CacheConfiguration;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.CacheManagerMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.CacheValueDTO;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.MethodCacheDTO;
import com.wellpoint.mobility.aggregation.core.scheduler.Scheduler;

/**
 * Default implementation of cache manager
 * 
 * @author edward.biton@wellpoint.com
 */
@Singleton(name = "CacheManager")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CacheManagerDefaultImpl implements CacheManager
{
	/**
	 * Default cache size entries
	 */
	private static final int DEFAULT_MAX_HEAP_ENTRIES = 100;
	/**
	 * State of the cache manager if enabled or disabled
	 */
	private boolean enabled = true;
	/**
	 * Application Cache Configuration
	 */
	private CacheConfiguration applicationCacheConfiguration;
	/**
	 * User Cache Configuration
	 */
	private CacheConfiguration userCacheConfiguration;

	/**
	 * Application Cache instance
	 */
	private ApplicationCacheStore applicationCache;
	/**
	 * A map of User Cache identified by the key
	 */
	private Map<String, UserCacheStore> userCacheMap = new HashMap<String, UserCacheStore>();
	/**
	 * Entity Manager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@EJB
	private Scheduler scheduler;

	@Resource(mappedName = "jms/TopicConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/CacheManagerTopic")
	private Topic topic;

	/**
	 * Default constructor
	 */
	public CacheManagerDefaultImpl()
	{
		applicationCacheConfiguration = new CacheConfiguration("APPLICATION", DEFAULT_MAX_HEAP_ENTRIES);
		userCacheConfiguration = new CacheConfiguration();
		userCacheConfiguration.setMaxEntriesLocalHeap(DEFAULT_MAX_HEAP_ENTRIES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getUserCache(java.lang.String)
	 */
	@Override
	public UserCacheStore getUserCacheStore(String id)
	{
		UserCacheStore userCache = userCacheMap.get(id);
		if (userCache == null)
		{
			return createUserCacheStore(id);
		}
		userCache.setEntityManager(this.entityManager);
		return userCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#createUserCache(java.lang.String)
	 */
	@Override
	public UserCacheStore createUserCacheStore(String id)
	{
		UserCacheStore userCache = new UserCacheStore(userCacheConfiguration, id);
		userCacheMap.put(id, userCache);
		userCache.setEntityManager(this.entityManager);
		return userCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getApplication()
	 */
	@Override
	public ApplicationCacheStore getApplicationCacheStore()
	{
		if (applicationCache == null)
		{
			applicationCache = new ApplicationCacheStore(applicationCacheConfiguration);
			applicationCache.setEntityManager(entityManager);
			applicationCache.setScheduler(scheduler);
		}
		return applicationCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#setApplicationCacheValue(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setApplicationCacheValue(String key, Object value)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		CacheValueDTO cacheValueDTO = new CacheValueDTO(key, value);
		applicationCacheStore.setCacheValue(cacheValueDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getApplicationCacheValue(java.lang.String)
	 */
	@Override
	public Object getApplicationCacheValue(String key)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		return applicationCacheStore.getCacheValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#deleteApplicationCacheValue(java.lang.String)
	 */
	@Override
	public void deleteApplicationCacheValue(String key)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		applicationCacheStore.removeCacheValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#setUserCacheValue(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public void setUserCacheValue(String userKey, String key, Object value)
	{
		UserCacheStore userCacheStore = this.getUserCacheStore(userKey);
		CacheValueDTO cacheValueDTO = new CacheValueDTO(key, value);
		userCacheStore.setCacheValue(cacheValueDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getUserCacheValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object getUserCacheValue(String userKey, String key)
	{
		UserCacheStore userCacheStore = this.getUserCacheStore(userKey);
		return userCacheStore.getCacheValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#deleteUserCacheValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteUserCacheValue(String userKey, String key)
	{
		UserCacheStore userCacheStore = this.getUserCacheStore(userKey);
		userCacheStore.removeCacheValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#setMethodCacheValue(com.wellpoint.mobility.
	 * aggregation.core.cachemanager.pojo.MethodCacheDTO)
	 */
	@Override
	public void setMethodCacheValue(MethodCacheDTO methodCacheDTO)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		applicationCacheStore.setMethodCacheValue(methodCacheDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getMethodCacheValue(java.lang.String)
	 */
	@Override
	public Object getMethodCacheValue(String callSignature)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		return applicationCacheStore.getMethodCacheValue(callSignature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#deleteMethodCacheValue(java.lang.String)
	 */
	@Override
	public void deleteMethodCacheValue(String callSignature)
	{
		ApplicationCacheStore applicationCacheStore = getApplicationCacheStore();
		applicationCacheStore.removeMethodCacheValue(callSignature);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#getUserCacheConfiguration()
	 */
	@Override
	public CacheConfiguration getUserCacheConfiguration()
	{
		return userCacheConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#clearApplicationCache(int)
	 */
	@Override
	public void clearApplicationCache(int storageType)
	{
		ApplicationCacheStore applicationCacheStore = this.getApplicationCacheStore();
		applicationCacheStore.clearCache(storageType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#clearMethodCache(int)
	 */
	@Override
	public void clearMethodCache(int storageType)
	{
		ApplicationCacheStore applicationCacheStore = this.getApplicationCacheStore();
		applicationCacheStore.clearMethodCache(storageType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager#clearUserCacheMemory(int,
	 * java.lang.String)
	 */
	@Override
	public void clearUserCache(int storageType, String userKey)
	{
		if (userKey == null)
		{
			// clear all in memory cache
			for (Entry<String, UserCacheStore> entry : this.userCacheMap.entrySet())
			{
				entry.getValue().clearCache(storageType);
			}
		}
		else
		{
			UserCacheStore userCacheStore = userCacheMap.get(userKey);
			if (userCacheStore != null)
			{
				userCacheStore.clearCache(storageType);
			}
		}
	}

	/**
	 * Post a message to the cache manager
	 * 
	 * @param cacheMessage
	 */
	public void postMessage(CacheManagerMessage cacheManagerMessage)
	{
		try
		{
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			ObjectMessage objectMessage = session.createObjectMessage();
			objectMessage.setObject(cacheManagerMessage);

			MessageProducer messageProducer = session.createProducer(topic);
			messageProducer.send(objectMessage);
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}
}
