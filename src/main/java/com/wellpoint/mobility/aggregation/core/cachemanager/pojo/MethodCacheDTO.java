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

import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodInfoDTO;

/**
 * An extension of MethodInfo that contains additional cache information properties
 * 
 * @author edward.biton@wellpoint.com
 */
public class MethodCacheDTO extends MethodInfoDTO
{
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
	 * Default constructor
	 */
	public MethodCacheDTO()
	{
		this.expiresOn = System.currentTimeMillis() + expireDuration;
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
		return "MethodCacheDTO [hitCount=" + hitCount + ", expireDuration=" + expireDuration + ", expiresOn=" + expiresOn + ", getMethodSignature()="
				+ getMethodSignature() + ", getCallSignature()=" + getCallSignature() + ", getPackageName()=" + getPackageName() + ", getClassName()="
				+ getClassName() + ", getMethodName()=" + getMethodName() + ", getMethodParams()=" + getMethodParams() + ", getReturnType()=" + getReturnType()
				+ ", getReturnValue()=" + getReturnValue() + ", getExceptionType()=" + getExceptionType() + "]";
	}
}
