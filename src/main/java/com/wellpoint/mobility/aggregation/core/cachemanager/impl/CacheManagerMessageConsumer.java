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

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.ClearApplicationCacheMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.ClearMethodCacheMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.ClearUserCacheMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.DisableCacheManagerMessage;
import com.wellpoint.mobility.aggregation.core.cachemanager.message.EnableCacheManagerMessage;

/**
 * A topic consumer that processes the cache messages
 * 
 * @author edward.biton@wellpoint.com
 */
@MessageDriven(mappedName = "jms/CacheManagerTopic")
public class CacheManagerMessageConsumer implements MessageListener
{
	/**
	 * topic to listen
	 */
	@Resource(mappedName = "jms/CacheManagerTopic")
	private Topic topic;
	/**
	 * Cache Manager
	 */
	@EJB(beanName = "CacheManager")
	private CacheManager cacheManager;
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(CacheManagerMessageConsumer.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message)
	{
		try
		{
			if (message instanceof ObjectMessage)
			{
				ObjectMessage objectMessage = (ObjectMessage) message;
				Object object = objectMessage.getObject();
				logger.debug(object);
				if (object instanceof EnableCacheManagerMessage)
				{
					cacheManager.setEnabled(true);
				}
				else if (object instanceof DisableCacheManagerMessage)
				{
					cacheManager.setEnabled(false);
				}
				else if (object instanceof ClearApplicationCacheMessage)
				{
					ClearApplicationCacheMessage clearApplicationCacheMessage = (ClearApplicationCacheMessage) object;
					cacheManager.clearApplicationCache(clearApplicationCacheMessage.getStorageType());
				}
				else if (object instanceof ClearMethodCacheMessage)
				{
					ClearMethodCacheMessage clearMethodCacheMessage = (ClearMethodCacheMessage) object;
					cacheManager.clearMethodCache(clearMethodCacheMessage.getStorageType());
				}
				else if (object instanceof ClearUserCacheMessage)
				{
					ClearUserCacheMessage clearUserCacheMessage = (ClearUserCacheMessage) object;
					cacheManager.clearUserCache(clearUserCacheMessage.getStorageType(), clearUserCacheMessage.getUserKey());
				}
			}
		}
		catch (JMSException e)
		{
			e.printStackTrace();
		}
	}

}
