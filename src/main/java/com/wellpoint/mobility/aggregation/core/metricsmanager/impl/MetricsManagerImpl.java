/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.metricsmanager.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.interceptor.dto.MethodParamDTO;
import com.wellpoint.mobility.aggregation.core.metricsmanager.MetricsManager;
import com.wellpoint.mobility.aggregation.core.metricsmanager.pojo.MetricDTO;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;
import com.wellpoint.mobility.aggregation.persistence.domain.Metric;
import com.wellpoint.mobility.aggregation.persistence.domain.MetricMethodParam;

/**
 * Default implementation of Metrics manager
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
@Stateless(name = "MetricsManager")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MetricsManagerImpl implements MetricsManager
{
	/**
	 * Entity Manager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(MetricsManagerImpl.class);
	/**
	 * Is debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	@Override
	public void storeMetricsInfo(MetricDTO metricDTO)
	{
		try
		{
			if (DEBUG_ENABLED)
			{
				logger.debug("Converting object to XML=" + metricDTO);
			}
			long startTime = System.currentTimeMillis();
//			String xmlValue = XML2ObjectUtility.toXml(metricDTO.getReturnValue());
			String xmlValue = null;
//			logger.debug("(OPTMIZATION) MetricsManager ConvertXML=" + (System.currentTimeMillis() - startTime) + "ms");
			
			long time = System.currentTimeMillis() - startTime;
			if (DEBUG_ENABLED)
			{
				logger.debug("Converting object to XML: " + time + "ms");
			}
			if (xmlValue != null && xmlValue.length() > MetricsManager.MAX_RETURN_VALUE_SIZE)
			{
				logger.warn("The size of the return value " + xmlValue.length() + " is bigger than the table column size. The data will be trimmed");
				xmlValue = xmlValue.substring(0, MetricsManager.MAX_RETURN_VALUE_SIZE);
			}

			Metric metric = new Metric();
			metric.setPackageName(metricDTO.getPackageName());
			metric.setClassName(metricDTO.getClassName());
			metric.setMethodName(metricDTO.getMethodName());
			metric.setReturnType(metricDTO.getReturnType());
			metric.setReturnValue(xmlValue);
			metric.setExceptionType(metricDTO.getExceptionType());
			metric.setExecutionTime((int) metricDTO.getExecutionTime());
			metric.setCreatedBy("SYSTEM");
			metric.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			metric.setUpdatedBy("SYSTEM");
			metric.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

			startTime = System.currentTimeMillis();
			entityManager.persist(metric);
			for (MethodParamDTO methodParamDTO : metricDTO.getMethodParams())
			{
				MetricMethodParam metricsMethodParam = new MetricMethodParam();
				metricsMethodParam.setMetric(metric);
				metricsMethodParam.setParamPosition(methodParamDTO.getParamPosition());
				metricsMethodParam.setParamType(methodParamDTO.getParamType());
				metricsMethodParam.setParamValue(XML2ObjectUtility.toXml(methodParamDTO.getParamValue()));
				metricsMethodParam.setCreatedBy("SYSTEM");
				metricsMethodParam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				metricsMethodParam.setUpdatedBy("SYSTEM");
				metricsMethodParam.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(metricsMethodParam);
			}
			if (DEBUG_ENABLED)
			{
				time = System.currentTimeMillis() - startTime;
				logger.debug("Saving metrics to database: " + time + "ms");
			}
//			logger.debug("(OPTMIZATION) MetricsManager ACTUAL SAVING=" + (System.currentTimeMillis() - startTime) + "ms");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<MetricDTO> getMetricsData(String filterType, String filterParam)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetricDTO> getMetricsDataBetweenTimestamps(String startTime, String endTime)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setThresholdValue(int threshOldValue)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getThresholdValue()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}