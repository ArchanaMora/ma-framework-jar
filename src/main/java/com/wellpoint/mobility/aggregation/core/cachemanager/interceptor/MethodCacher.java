/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.cachemanager.interceptor;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.cachemanager.CacheManager;
import com.wellpoint.mobility.aggregation.core.cachemanager.annotations.CacheConfig;
import com.wellpoint.mobility.aggregation.core.cachemanager.impl.ApplicationCacheStore;
import com.wellpoint.mobility.aggregation.core.cachemanager.pojo.MethodCacheDTO;
import com.wellpoint.mobility.aggregation.core.interceptor.BaseInterceptor;

/**
 * MethodCacher caches a method using the method parameter values as the unique key. If a method has been called with
 * the same set of parameters, the actual method will not be executed instead the return value is retrieved from the
 * cache
 * 
 * @author edward.biton@wellpoint.com
 */
public class MethodCacher extends BaseInterceptor
{
	/**
	 * CacheManager instance
	 */
	@EJB(beanName = "CacheManager")
	private CacheManager cacheManager;
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(MethodCacher.class);
	/**
	 * Is debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	@AroundInvoke
	public Object interceptorAroundInvoke(InvocationContext invocationContext) throws Exception
	{
		long startTime = System.currentTimeMillis();
		if (cacheManager.isEnabled() == false)
		{
//			logger.debug("(OPTMIZATION) MethodCacher=" + (System.currentTimeMillis() - startTime) + "ms");
			return invocationContext.proceed();
		}
		String callSignature = createMethodSignature(invocationContext, true, false, true);
//		logger.debug("(OPTMIZATION) CREATE SIGNATURE=" + (System.currentTimeMillis() - startTime) + "ms");
		if (DEBUG_ENABLED)
		{
			logger.debug("callSignature=" + callSignature);
		}
		ApplicationCacheStore applicationCache = cacheManager.getApplicationCacheStore();
		Object result = applicationCache.getMethodCacheValue(callSignature);

		// cache not present
		if (result != null)
		{
			// do not call the method, just return the value directly
			if (DEBUG_ENABLED)
			{
				logger.debug(callSignature + " already called before, returning cached value" + result);
			}
//			logger.debug("(OPTMIZATION) MethodCacher=" + (System.currentTimeMillis() - startTime) + "ms");
			return result;
		}
		if (DEBUG_ENABLED)
		{
			logger.debug("Executing unique method call " + callSignature);
		}
		try
		{
			result = invocationContext.proceed();
			CacheConfig cacheConfig = invocationContext.getMethod().getAnnotation(CacheConfig.class);
			if (DEBUG_ENABLED)
			{
				logger.debug("Caching " + callSignature + " with value " + result);
			}
			MethodCacheDTO methodCacheDTO = new MethodCacheDTO();
//			long startTime2 = System.currentTimeMillis();
			methodCacheDTO = (MethodCacheDTO) createMethodInfo(methodCacheDTO, invocationContext);
//			logger.debug("(OPTMIZATION) CREATE METHOD INFO=" + (System.currentTimeMillis() - startTime2) + "ms");
			// use the original method signature (some parameters might be outbound so it will not match the original
			// request
			methodCacheDTO.setCallSignature(callSignature);
			// TODO check the annotation if the value should be encrypted or not
			methodCacheDTO.setReturnValue(result);
			methodCacheDTO.setHitCount(1);
			if (cacheConfig != null)
			{
				methodCacheDTO.setExpireDuration(cacheConfig.duration());
			}
			methodCacheDTO.setExpiresOn(System.currentTimeMillis() + methodCacheDTO.getExpireDuration());
//			long startTime3 = System.currentTimeMillis();
			applicationCache.setMethodCacheValue(methodCacheDTO);
//			logger.debug("(OPTMIZATION) MethodCacher, setMethodCacheValue=" + (System.currentTimeMillis() - startTime3) + "ms");
//			logger.debug("(OPTMIZATION) MethodCacher=" + (System.currentTimeMillis() - startTime) + "ms");
		}
		catch (Exception e)
		{
			throw e;
		}
		return result;
	}

}
