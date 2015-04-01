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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.persistence.domain.Metric;
import com.wellpoint.mobility.aggregation.persistence.domain.MetricsDailyrollup;

/**
 * This is a data service for the metrics table.
 * 
 * @author edward.biton@wellpoint.com
 */
@Stateless
public class MetricDataService
{
	/**
	 * Entity Manager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	/**
	 * logger
	 */
	private Logger logger = Logger.getLogger(MetricDataService.class);

	public static void main(String args[])
	{
		// EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
		// EntityManager entityManager = entityManagerFactory.createEntityManager();
		// MetricDataService metricDataService = new MetricDataService();
		// metricDataService.entityManager = entityManager;
		//
		// Date date = DateUtilities.toDate("10/14/2014");
		// metricDataService.rollupMetrics(date);
//		MetricDataService metricDataService = new MetricDataService();
//		metricDataService.doRollUp();
	}

	/**
	 * Search the metrics data given a search criteria
	 * 
	 * @param metricSearchCriteria
	 * @return list of Metric object
	 */
	@SuppressWarnings("unchecked")
	public List<Metric> searchMetrics(MetricSearchCriteria metricSearchCriteria)
	{
		logger.debug("ENTRY");
		List<Metric> metrics = new ArrayList<Metric>();

		logger.debug("MetricSearchCriteria=" + metricSearchCriteria);
		StringBuilder jpql = new StringBuilder("SELECT m FROM Metric m");
		StringBuilder condition = new StringBuilder("");

		if (metricSearchCriteria.getPackageName() != null)
		{
			if (condition.toString().equals("") == false)
			{
				condition.append(" AND ");
			}
			condition.append(" m.packageName = :packageName");
		}
		if (metricSearchCriteria.getClassName() != null)
		{
			if (condition.toString().equals("") == false)
			{
				condition.append(" AND ");
			}
			condition.append(" m.className = :className");
		}
		if (metricSearchCriteria.getMethodName() != null)
		{
			if (condition.toString().equals("") == false)
			{
				condition.append(" AND ");
			}
			condition.append(" m.methodName = :methodName");
		}
		if (metricSearchCriteria.getStartDate() != null)
		{
			if (condition.toString().equals("") == false)
			{
				condition.append(" AND ");
			}
			condition.append(" m.createdDate >= :startDate");
		}
		if (metricSearchCriteria.getEndDate() != null)
		{
			if (condition.toString().equals("") == false)
			{
				condition.append(" AND ");
			}
			condition.append(" m.createdDate <= :endDate");
		}
		if (condition.toString().length() > 0)
		{
			jpql.append(" WHERE ");
			jpql.append(condition.toString());
		}
		logger.debug("JPQL = " + jpql.toString());
		Query query = entityManager.createQuery(jpql.toString(), Metric.class);
		if (metricSearchCriteria.getPackageName() != null)
		{
			query.setParameter("packageName", metricSearchCriteria.getPackageName());
		}
		if (metricSearchCriteria.getClassName() != null)
		{
			query.setParameter("clasName", metricSearchCriteria.getClassName());
		}
		if (metricSearchCriteria.getMethodName() != null)
		{
			query.setParameter("methodName", metricSearchCriteria.getMethodName());
		}
		if (metricSearchCriteria.getStartDate() != null)
		{
			query.setParameter("startDate", metricSearchCriteria.getStartDate());
		}
		if (metricSearchCriteria.getEndDate() != null)
		{
			query.setParameter("endDate", metricSearchCriteria.getEndDate());
		}
		metrics = query.getResultList();
		logger.debug("EXIT");
		return metrics;
	}

	/**
	 * Returns the unique set of packages
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPackages()
	{
		List<String> packages = new ArrayList<String>();
		Query query = entityManager.createQuery("SELECT DISTINCT(m.packageName) FROM Metric m", String.class);
		packages = (List<String>) query.getResultList();
		return packages;
	}

	/**
	 * Returns the unique set of classes
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getClasses()
	{
		List<String> packages = new ArrayList<String>();
		Query query = entityManager.createQuery("SELECT DISTINCT(m.className) FROM Metric m", String.class);
		packages = (List<String>) query.getResultList();
		return packages;
	}

	/**
	 * Returns the unique set of classes
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getClasses(String packageName)
	{
		List<String> packages = new ArrayList<String>();
		Query query = entityManager.createQuery("SELECT DISTINCT(m.className) FROM Metric m WHERE m.packageName = :packageName", String.class);
		query.setParameter("packageName", packageName);
		packages = (List<String>) query.getResultList();
		return packages;
	}

	/**
	 * Returns the unique set of methods
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMethods()
	{
		List<String> packages = new ArrayList<String>();
		Query query = entityManager.createQuery("SELECT DISTINCT(m.methodName) FROM Metric m", String.class);
		packages = (List<String>) query.getResultList();
		return packages;
	}

	/**
	 * Returns the unique set of methods
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMethods(String packageName, String className)
	{
		List<String> packages = new ArrayList<String>();
		Query query = entityManager.createQuery("SELECT DISTINCT(m.methodName) FROM Metric m WHERE m.packageName = :packageName AND m.className = :className",
				String.class);
		query.setParameter("packageName", packageName);
		query.setParameter("className", className);
		packages = (List<String>) query.getResultList();
		return packages;
	}

	/**
	 * Deletes the summary data from the MetricsDailyrollup table if it already exists for the given date
	 * 
	 * @param date
	 *            date of the summary to be deleted
	 */
	public void clearSummary(Date date)
	{
		logger.debug("ENTRY");
		logger.info("Deleting summary data on MetricsDailyrollup table on " + date);
		try
		{
			Date startDate = date;
			long endTime = date.getTime() + (1000 * 60 * 60 * 24) - 1;
			Date endDate = new Date(endTime);
			Query query = entityManager.createQuery("DELETE FROM MetricsDailyrollup mdr WHERE mdr.tsDate BETWEEN :startDate AND :endDate");
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			int rowsDeleted = query.executeUpdate();
			logger.info("Deleted " + rowsDeleted + " from the MetricsDailyrollup table");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info("Error occurred dleting summary data on MetricsDailyrollup table on " + date);
		}
		logger.info("Deleting summary data on MetricsDailyrollup table on " + date + " completed");
		logger.debug("EXIT");
	}


	/**
	 * This summarized the metrics data into the metrics_dailyrollup given a date
	 * 
	 * @param date
	 */
	public void rollupDailyMetrics(Date date)
	{
		logger.debug("ENTRY");
		if (date == null)
		{
			logger.error("Date passed was null");
			return;
		}
		clearSummary(date);

		MetricSearchCriteria metricSearchCriteria = new MetricSearchCriteria();
		metricSearchCriteria.setStartDate(date);
		Date startDate = date;
		long endTime = date.getTime() + (1000 * 60 * 60 * 24) - 1;
		Date endDate = new Date(endTime);
		metricSearchCriteria.setEndDate(endDate);

		Map<String, MetricSummary> metricSummaryMap = new HashMap<String, MetricSummary>();
		List<Metric> metrics = searchMetrics(metricSearchCriteria);

		for (Metric metric : metrics)
		{
			String key = metric.getPackageName() + "." + metric.getClassName() + "." + metric.getMethodName();
			MetricSummary metricSummary = metricSummaryMap.get(key);
			if (metricSummary == null)
			{
				metricSummary = new MetricSummary("MobilityAggregation", metric.getPackageName(), metric.getClassName(), metric.getMethodName(), date);
				metricSummaryMap.put(key, metricSummary);
			}
			metricSummary.add(metric);
		}
		for (Entry<String, MetricSummary> entry : metricSummaryMap.entrySet())
		{
			MetricSummary metricSummary = entry.getValue();
			countExceptions(metricSummary, startDate, endDate);
			MetricsDailyrollup metricsDailyRollup = metricSummary.getMetricsDailyRollup();
			entityManager.persist(metricsDailyRollup);
		}
		logger.debug("EXIT");
	}

	/**
	 * An internal method for counting the exceptions of a given summary
	 * 
	 * @param metricSummary
	 *            metric summary
	 * @param startDate
	 *            start date
	 * @param endDate
	 *            end date
	 */
	protected void countExceptions(MetricSummary metricSummary, Date startDate, Date endDate)
	{
		logger.debug("ENTRY");
		Query query = entityManager
				.createQuery(
						"SELECT COUNT(*) FROM ExceptionLog el WHERE el.packageName = :packageName AND el.className = :className AND el.methodName = :methodName AND el.createdDate BETWEEN :startDate AND :endDate",
						Long.class);
		query.setParameter("packageName", metricSummary.getPackageName());
		query.setParameter("className", metricSummary.getClassName());
		query.setParameter("methodName", metricSummary.getMethodName());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		try
		{
			Long count = (Long) query.getSingleResult();
			if (count != null)
			{
				metricSummary.setTotalExceptions(count);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		logger.debug("EXIT");
	}
}
