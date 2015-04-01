package com.wellpoint.mobility.aggregation.core.facade.facademanager;

import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBContextSOAPHandler;
import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBHeaderSOAPHandler;
import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBSecuritySOAPHandler;

/**
 * Facade manager
 * 
 * @author bharat.meda@wellpoint.com
 */
public interface FacadeManager
{
	/**
	 * Interface method for returning ESBSecuritySOAPHandler
	 * @return ESBSecuritySOAPHandler
	 */
	public ESBSecuritySOAPHandler getESBSecuritySOAPHandler(String method, String env);
	
	/**
	 * Interface method for returning ESBContextSOAPHandler
	 * @return ESBContextSOAPHandler
	 */
	public ESBContextSOAPHandler getESBContextSOAPHandler(String method, String env);
	
	/**
	 * Interface method for return serrvice end point
	 * @return end point url
	 */
	public String getEndPointURL(String method, String env);
	
	/**
	 * Interface method for returning ESBHeaderSOAPHandler
	 * @return ESBHeaderSOAPHandler
	 */
	public ESBHeaderSOAPHandler getESBHeaderSOAPHandler(String method, String env);
}