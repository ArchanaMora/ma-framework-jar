package com.wellpoint.mobility.aggregation.core.facade.esbheader;

/**
 * ESBHeader pojo
 * @author bharat.meda@wellpoint.com
 *
 */
public class ESBHeader 
{
	private String serviceName;
	private String serviceMethod;
	private String env;
	private String properties;
	private String propertiesControl;
	private String routingVersion;
	private String routingFieldName;
	private String routingFieldValue;
	private String routingControl;
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
	/**
	 * @return the properties
	 */
	public String getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}
	/**
	 * @return the propertiesControl
	 */
	public String getPropertiesControl() {
		return propertiesControl;
	}
	/**
	 * @param propertiesControl the propertiesControl to set
	 */
	public void setPropertiesControl(String propertiesControl) {
		this.propertiesControl = propertiesControl;
	}
	/**
	 * @return the routingVersion
	 */
	public String getRoutingVersion() {
		return routingVersion;
	}
	/**
	 * @param routingVersion the routingVersion to set
	 */
	public void setRoutingVersion(String routingVersion) {
		this.routingVersion = routingVersion;
	}
	/**
	 * @return the routingFieldName
	 */
	public String getRoutingFieldName() {
		return routingFieldName;
	}
	/**
	 * @param routingFieldName the routingFieldName to set
	 */
	public void setRoutingFieldName(String routingFieldName) {
		this.routingFieldName = routingFieldName;
	}
	/**
	 * @return the routingFieldValue
	 */
	public String getRoutingFieldValue() {
		return routingFieldValue;
	}
	/**
	 * @param routingFieldValue the routingFieldValue to set
	 */
	public void setRoutingFieldValue(String routingFieldValue) {
		this.routingFieldValue = routingFieldValue;
	}
	/**
	 * @return the routingControl
	 */
	public String getRoutingControl() {
		return routingControl;
	}
	/**
	 * @param routingControl the routingControl to set
	 */
	public void setRoutingControl(String routingControl) {
		this.routingControl = routingControl;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ESBHeader [serviceName=" + serviceName + ", serviceMethod="
				+ serviceMethod + ", env=" + env + ", properties=" + properties
				+ ", propertiesControl=" + propertiesControl
				+ ", routingVersion=" + routingVersion + ", routingFieldName="
				+ routingFieldName + ", routingFieldValue=" + routingFieldValue
				+ ", routingControl=" + routingControl + "]";
	}
}