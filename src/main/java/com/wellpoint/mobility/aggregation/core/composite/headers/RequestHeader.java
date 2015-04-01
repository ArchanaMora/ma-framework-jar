/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.composite.headers;

/**
 * An application header required for sending request to the aggregation layer
 * 
 * @author edward.biton@wellpoint.com
 */
public class RequestHeader
{
	// Core Request Information
	/**
	 * application id -
	 */
	private String appId;
	/**
	 * application version - validation (not empty, numeric value)
	 */
	private String appVersion;
	/**
	 * channel - native or spa
	 */
	private String channel;
	/**
	 * device - device type sending the request, e.g. iphone, android etc.
	 */
	private String device;
	/**
	 * device id - unique id of the device
	 */
	private String deviceId;
	/**
	 * mock response - if true, the service is to mock the response.
	 */
	private boolean mockResponse;
	/**
	 * userToken - assigned when user is registered. must always be present.
	 */
	private String userToken;

	// Member Coverage Information
	/**
	 * Health Care Identifier
	 */
	private String hcid;
	/**
	 * Member's Group Identifier.
	 */
	private String groupId;
	/**
	 * The coverage status - active, inactive, etc.
	 */
	private long coverageStatus;
	/**
	 * Area of business where member is handled
	 */
	private long serviceArea;
	/**
	 * Member's core membership system
	 */
	private long sourceSystem;
	/*
	 * The type of client - individual, group, etc.
	 */
	private long clientType;
	/**
	 * Line of business
	 */
	private long lob;
	/**
	 * exchange indicator
	 */
	private long exIndicator;
	/**
	 * exchange state
	 */
	private long exState;
	/**
	 * Exchange code
	 */
	private long exCode;
	/**
	 * Underwriting state
	 */
	private String uwState;

	/**
	 * @return the appId
	 */
	public String getAppId()
	{
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion()
	{
		return appVersion;
	}

	/**
	 * @param appVersion
	 *            the appVersion to set
	 */
	public void setAppVersion(String appVersion)
	{
		this.appVersion = appVersion;
	}

	/**
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	/**
	 * @return the device
	 */
	public String getDevice()
	{
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device)
	{
		this.device = device;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId()
	{
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	/**
	 * @return the mockResponse
	 */
	public boolean isMockResponse()
	{
		return mockResponse;
	}

	/**
	 * @param mockResponse
	 *            the mockResponse to set
	 */
	public void setMockResponse(boolean mockResponse)
	{
		this.mockResponse = mockResponse;
	}

	/**
	 * @return the userToken
	 */
	public String getUserToken()
	{
		return userToken;
	}

	/**
	 * @param userToken the userToken to set
	 */
	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	public String getHcid()
	{
		return hcid;
	}

	public void setHcid(String hcid)
	{
		this.hcid = hcid;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public long getCoverageStatus()
	{
		return coverageStatus;
	}

	public void setCoverageStatus(long coverageStatus)
	{
		this.coverageStatus = coverageStatus;
	}

	public long getServiceArea()
	{
		return serviceArea;
	}

	public void setServiceArea(long serviceArea)
	{
		this.serviceArea = serviceArea;
	}

	public long getSourceSystem()
	{
		return sourceSystem;
	}

	public void setSourceSystem(long sourceSystem)
	{
		this.sourceSystem = sourceSystem;
	}

	public long getClientType()
	{
		return clientType;
	}

	public void setClientType(long clientType)
	{
		this.clientType = clientType;
	}

	public long getLob()
	{
		return lob;
	}

	public void setLob(long lob)
	{
		this.lob = lob;
	}

	public long getExIndicator() {
		return exIndicator;
	}
	public void setExIndicator(long exIndicator) {
		this.exIndicator = exIndicator;
	}

	public long getExState() {
		return exState;
	}
	public void setExState(long exState) {
		this.exState = exState;
	}

	public long getExCode() {
		return exCode;
	}
	public void setExCode(long exCode) {
		this.exCode = exCode;
	}

	public String getUwState() {
		return uwState;
	}
	public void setUwState(String uwState) {
		this.uwState = uwState;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "RequestHeader [appId=" + appId + ", appVersion=" + appVersion + ", channel=" + channel + ", device=" + device + ", deviceId=" + deviceId
				+ ", mockResponse=" + mockResponse + ", userToken=" + userToken + "]";
	}

}
