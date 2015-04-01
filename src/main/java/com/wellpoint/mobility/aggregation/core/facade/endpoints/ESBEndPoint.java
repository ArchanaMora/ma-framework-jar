package com.wellpoint.mobility.aggregation.core.facade.endpoints;

public class ESBEndPoint 
{
	private String serviceName;
	private String serviceMethod;
	private String serviceUrl;
	private String env;
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceMethod
	 */
	public String getServiceMethod() {
		return serviceMethod;
	}
	/**
	 * @param serviceMethod the serviceMethod to set
	 */
	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}
	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}
	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	/**
	 * @return the env
	 */
	public String getEnv() {
		return env;
	}
	/**
	 * @param env the env to set
	 */
	public void setEnv(String env) {
		this.env = env;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ESBEndPoint [serviceName=" + serviceName + ", serviceMethod="
				+ serviceMethod + ", serviceUrl=" + serviceUrl + ", env=" + env
				+ "]";
	}
}