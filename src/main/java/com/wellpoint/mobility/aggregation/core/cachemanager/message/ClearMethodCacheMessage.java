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
 * A message to clear the method cache
 * 
 * @author edward.biton@wellpoint.com
 */
public class ClearMethodCacheMessage extends CacheManagerMessage
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5386059744318216554L;
	/**
	 * storage type to clear
	 */
	protected int storageType = CacheManager.DEFAULT_STORAGE_TYPE;

	/**
	 * Default constructor sets the storage type to clear to ALL_STORAGE_TYPE
	 */
	public ClearMethodCacheMessage()
	{
	}

	/**
	 * Constructor that accepts the type of storage to clear
	 */
	public ClearMethodCacheMessage(int storageType)
	{
		this.storageType = storageType;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ClearMethodCacheMessage [storageType=" + storageType + "]";
	}

}
