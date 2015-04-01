/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.metricsmanager;

import java.util.List;

import com.wellpoint.mobility.aggregation.core.metricsmanager.pojo.MetricDTO;

/**
 * Metrics manager Interface API
 * 
 * @author bharat.meda@wellpoint.com
 * 
 */
public interface MetricsManager
{
	public static final int MAX_RETURN_VALUE_SIZE = 4000000;
	/**
	 * Stores the metrics value
	 * 
	 * @param metricDTO
	 * 
	 */
	public void storeMetricsInfo(MetricDTO metricDTO);

	/**
	 * Returns the list of MetricsValue objects for the given filer type and filter params
	 * 
	 * @param filterType
	 * @param filterParam
	 * @return List
	 */
	public List<MetricDTO> getMetricsData(String filterType, String filterParam);

	/**
	 * Returns the list of MetricsValue objects between the given timestamps
	 * 
	 * @param startTime
	 * @param endTime
	 * @return List
	 */
	public List<MetricDTO> getMetricsDataBetweenTimestamps(String startTime, String endTime);

	/**
	 * To set the threshold value in the configuration. Metrics will be logged only if execution time of the method
	 * exceeds this value
	 * 
	 * @param threshOldValue
	 *            configuration value in seconds
	 */
	public void setThresholdValue(int threshOldValue);

	/**
	 * Gets the threshold value from configuration entries
	 * 
	 * @return Threshold value obtained from configuration
	 */
	public int getThresholdValue();
}