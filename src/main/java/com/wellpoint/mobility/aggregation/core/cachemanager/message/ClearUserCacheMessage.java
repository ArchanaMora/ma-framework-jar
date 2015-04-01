/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager.message;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager;

/**
 * A message to clear the user cache
 * 
 * @author edward.biton@wellpoint.com
 */
public class ClearUserCacheMessage extends CacheManagerMessage
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7311412007601858774L;
	/**
	 * storage type to clear
	 */
	protected int storageType = CacheManager.DEFAULT_STORAGE_TYPE;
	/**
	 * User cache to clear
	 */
	protected String userKey = null;

	/**
	 * Default constructor sets the storage type to clear to ALL_STORAGE_TYPE
	 */
	public ClearUserCacheMessage(String userKey)
	{
		this.userKey = userKey;
	}

	/**
	 * Constructor that accepts the type of storage to clear
	 */
	public ClearUserCacheMessage(int storageType, String userKey)
	{
		this.storageType = storageType;
		this.userKey = userKey;
	}

	/**
	 * @return the storageType
	 */
	public int getStorageType()
	{
		return storageType;
	}

	/**
	 * @param storageType
	 *            the storageType to set
	 */
	public void setStorageType(int storageType)
	{
		this.storageType = storageType;
	}

	/**
	 * @return the userKey
	 */
	public String getUserKey()
	{
		return userKey;
	}

	/**
	 * @param userKey
	 *            the userKey to set
	 */
	public void setUserKey(String userKey)
	{
		this.userKey = userKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ClearUserCacheMessage [storageType=" + storageType + ", userKey=" + userKey + "]";
	}

}
