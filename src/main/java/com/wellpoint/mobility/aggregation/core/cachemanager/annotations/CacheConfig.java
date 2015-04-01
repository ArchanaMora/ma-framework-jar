/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * A cache config annotation
 * 
 * @author edward.biton@wellpoint.com
 */
@Documented
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface CacheConfig
{
	/**
	 * Default cache duration
	 */
	public static final long CACHE_DURATION = 1000 * 60 * 60 * 24;

	/**
	 * Configurable cache duration in milliseconds
	 * 
	 * @return
	 */
	public long duration() default CACHE_DURATION;

	/**
	 * determines if the value is encrypted or not.
	 * 
	 * @return
	 */
	public boolean encrypt() default false;
}