package com.wellpoint.mobility.aggregation.core.facade.esbcontext;

/**
 * ESBContextHeader pojo
 * @author bharat.meda@wellpoint.com
 *
 */
public class ESBContextHeader 
{
	private String serviceName;
	private String serviceMethod;
	private String serviceContext;
	private String serviceVersion;
	private String operationName;
	private String operationVersion;
	private String senderApp;
	private String clientRequestId;
	private String transactionId;
	private String messageType;
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
	 * @return the serviceContext
	 */
	public String getServiceContext() {
		return serviceContext;
	}
	/**
	 * @param serviceContext the serviceContext to set
	 */
	public void setServiceContext(String serviceContext) {
		this.serviceContext = serviceContext;
	}
	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}
	/**
	 * @param serviceVersion the serviceVersion to set
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}
	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	/**
	 * @return the operationVersion
	 */
	public String getOperationVersion() {
		return operationVersion;
	}
	/**
	 * @param operationVersion the operationVersion to set
	 */
	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}
	/**
	 * @return the senderApp
	 */
	public String getSenderApp() {
		return senderApp;
	}
	/**
	 * @param senderApp the senderApp to set
	 */
	public void setSenderApp(String senderApp) {
		this.senderApp = senderApp;
	}
	/**
	 * @return the clientRequestId
	 */
	public String getClientRequestId() {
		return clientRequestId;
	}
	/**
	 * @param clientRequestId the clientRequestId to set
	 */
	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
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
		return "ESBContextHeader [serviceName=" + serviceName
				+ ", serviceMethod=" + serviceMethod + ", serviceContext="
				+ serviceContext + ", serviceVersion=" + serviceVersion
				+ ", operationName=" + operationName + ", operationVersion="
				+ operationVersion + ", senderApp=" + senderApp
				+ ", clientRequestId=" + clientRequestId + ", transactionId="
				+ transactionId + ", messageType=" + messageType + ", env="
				+ env + "]";
	}
}