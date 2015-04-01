/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager.pojo;

/**
 * A cache value holder
 * 
 * @author edward.biton@wellpoint.com
 */
public class CacheValueDTO
{
	/**
	 * Cache key
	 */
	private String cacheKey;
	/**
	 * Cache value
	 */
	private Object cacheValue;
	/**
	 * The default duration is set to 1 day
	 */
	private static final long DEFAULT_DURATION = 1000 * 60 * 60 * 24;
	/**
	 * Number of hits (or number of access)
	 */
	private long hitCount;
	/**
	 * Expiration duration time in milliseconds
	 */
	private long expireDuration = DEFAULT_DURATION;
	/**
	 * Expire time in milliseconds
	 */
	private long expiresOn;

	/**
	 * Constructor
	 * 
	 * @param key
	 *            cache key
	 * @param value
	 *            cache value
	 */
	public CacheValueDTO(String cacheKey, Object cacheValue)
	{
		this.cacheKey = cacheKey;
		this.cacheValue = cacheValue;
		this.expireDuration = System.currentTimeMillis() + expireDuration;
	}

	/**
	 * @return the cacheKey
	 */
	public String getCacheKey()
	{
		return cacheKey;
	}

	/**
	 * @param cacheKey
	 *            the cacheKey to set
	 */
	public void setCacheKey(String cacheKey)
	{
		this.cacheKey = cacheKey;
	}

	/**
	 * @return the cacheValue
	 */
	public Object getCacheValue()
	{
		return cacheValue;
	}

	/**
	 * @param cacheValue
	 *            the cacheValue to set
	 */
	public void setCacheValue(Object cacheValue)
	{
		this.cacheValue = cacheValue;
	}

	/**
	 * @return the hitCount
	 */
	public long getHitCount()
	{
		return hitCount;
	}

	/**
	 * @param hitCount
	 *            the hitCount to set
	 */
	public void setHitCount(long hitCount)
	{
		this.hitCount = hitCount;
	}

	/**
	 * @return the expireDuration
	 */
	public long getExpireDuration()
	{
		return expireDuration;
	}

	/**
	 * @param expireDuration
	 *            the expireDuration to set
	 */
	public void setExpireDuration(long expireDuration)
	{
		this.expireDuration = expireDuration;
	}

	/**
	 * @return the expiresOn
	 */
	public long getExpiresOn()
	{
		return expiresOn;
	}

	/**
	 * @param expiresOn
	 *            the expiresOn to set
	 */
	public void setExpiresOn(long expiresOn)
	{
		this.expiresOn = expiresOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CacheValueDTO [cacheKey=" + cacheKey + ", cacheValue=" + cacheValue + ", hitCount=" + hitCount + ", expireDuration=" + expireDuration
				+ ", expiresOn=" + expiresOn + "]";
	}

}
