package com.wellpoint.mobility.aggregation.core.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.facade.endpoints.ESBEndPoint;
import com.wellpoint.mobility.aggregation.core.facade.esbcontext.ESBContextHeader;
import com.wellpoint.mobility.aggregation.core.facade.esbheader.ESBHeader;
import com.wellpoint.mobility.aggregation.core.facade.esbsecurity.ESBSecurityHeader;
import com.wellpoint.mobility.aggregation.core.facade.facademanager.FacadeManager;
import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBContextSOAPHandler;
import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBHeaderSOAPHandler;
import com.wellpoint.mobility.aggregation.core.utilities.soap.ESBSecuritySOAPHandler;

/**
 * Base Facade class for service end points, context headers and security headers
 * @author bharat.meda@wellpoint.com
 *
 */
public class BaseFacade implements FacadeManager 
{	
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(BaseFacade.class);
	protected boolean DEBUG_LOGGER_ENABLED = logger.isDebugEnabled();
	protected boolean INFO_LOGGER_ENABLED = logger.isInfoEnabled();
	
	private String service;
	
	/**
	 * A map of keys
	 * 
	 * Created based on the service, method and environment values passed to the constructor
	 * This map will be used for keeping the option of having different values different environments
	 */
	private static Map<String, String> keyMap = new HashMap<String, String>();
	
	/**
	 * A map of ESBSecuritySOAPHandler
	 */
	private static Map<String, ESBSecuritySOAPHandler> esbSecuritySOAPHandlerMap = new HashMap<String, ESBSecuritySOAPHandler>();
	
	/**
	 * A map of ESBContextSOAPHandler
	 */
	private static Map<String, ESBContextSOAPHandler> esbContextSOAPHandlerMap = new HashMap<String, ESBContextSOAPHandler>();
	
	/**
	 * A map of ESBHeaderSOAPHandler
	 */
	private static Map<String, ESBHeaderSOAPHandler> esbHeaderSOAPHandlerMap = new HashMap<String, ESBHeaderSOAPHandler>();
	
	/**
	 * A map of end point URLs
	 */
	private static Map<String, String> endPointURLMap = new HashMap<String, String>();
	
	/**
	 * A map of envs
	 */
	private static Map<String, String> envsMap = new HashMap<String, String>();
	
	@EJB(beanName = "BaseFacadeStore")
	BaseFacadeStore baseFacadeStore;
	
	/**
	 * Default constructor
	 */
	public BaseFacade()
	{		
	}
	
	/**
	 * Constructor with service
	 * @param service
	 */
	public BaseFacade(String service)
	{
		this.service = service;
	}
	
	public String getEnvironmentForService()
	{
		String env = envsMap.get(this.service);
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("Environment obtained::" + env);
		}
		
		if (env == null)
		{
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("Environment is null. Obtaining from facade store...");
			}
			
			env = baseFacadeStore.getEnvironment(this.service);
			envsMap.put(this.service, env);
		}
		return env;
	}

	@Override
	public ESBSecuritySOAPHandler getESBSecuritySOAPHandler(String method, String env)
	{
		String mapKey = keyMap.get(this.service + method + env);
		
		// possible to pass different env for for different operations
		if (mapKey == null)
		{
			mapKey = this.service + method + env;
			keyMap.put(mapKey, mapKey);
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("ESBSecuritySOAPHandler for::" + mapKey);
		}
		
		ESBSecuritySOAPHandler esbSecuritySOAPHandler = esbSecuritySOAPHandlerMap.get(mapKey);
		// if value is not available in the map, obtain the same
		if (esbSecuritySOAPHandler == null)
		{	
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("ESBSecuritySOAPHandler is null. Creating new from the facade store");
			}
			
			esbSecuritySOAPHandler = new ESBSecuritySOAPHandler();
			
			// get the data from the store
			ESBSecurityHeader esbSecurityHeader = baseFacadeStore.getESBSecurityData(this.service, method, env);
			esbSecuritySOAPHandler.setPassword(esbSecurityHeader.getPassword());
			esbSecuritySOAPHandler.setUsername(esbSecurityHeader.getUserName());
			
			// add to the map
			esbSecuritySOAPHandlerMap.put(mapKey, esbSecuritySOAPHandler);
		}
		return esbSecuritySOAPHandler;
	}

	@Override
	public ESBContextSOAPHandler getESBContextSOAPHandler(String method, String env) 
	{
		String mapKey = keyMap.get(this.service + method + env);
		
		// possible to pass different env for for different operations
		if (mapKey == null)
		{
			mapKey = this.service + method + env;
			keyMap.put(mapKey, mapKey);
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("ESBContextSOAPHandler for::" + mapKey);
		}
		
		// get the context object from the map if already exists
		ESBContextSOAPHandler esbContextSOAPHandler = esbContextSOAPHandlerMap.get(mapKey);
		
		if (esbContextSOAPHandler == null)
		{
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("ESBContextSOAPHandler is null. Creating new from the facade store");
			}
			
			// if does not exist, create the context object
			esbContextSOAPHandler = new ESBContextSOAPHandler();
			
			// get the data from the store and set to the context object
			ESBContextHeader esbContextHeader = baseFacadeStore.getESBContextData(this.service, method, env);
			esbContextSOAPHandler.setClientRequestId(esbContextHeader.getClientRequestId());
			esbContextSOAPHandler.setMessageType(esbContextHeader.getMessageType());
			esbContextSOAPHandler.setOperationName(esbContextHeader.getOperationName());
			esbContextSOAPHandler.setOperationVersion(esbContextHeader.getOperationVersion());
			esbContextSOAPHandler.setSender(esbContextHeader.getSenderApp());
			esbContextSOAPHandler.setServiceName(esbContextHeader.getServiceContext());
			esbContextSOAPHandler.setServiceVersion(esbContextHeader.getServiceVersion());
			esbContextSOAPHandler.setTransactionId(esbContextHeader.getTransactionId());
			
			// add to the context map
			esbContextSOAPHandlerMap.put(mapKey, esbContextSOAPHandler);
		}
		
		return esbContextSOAPHandler;
	}
	
	@Override
	public ESBHeaderSOAPHandler getESBHeaderSOAPHandler(String method, String env) 
	{
		String mapKey = keyMap.get(this.service + method + env);
		
		// possible to pass different env for for different operations
		if (mapKey == null)
		{
			mapKey = this.service + method + env;
			keyMap.put(mapKey, mapKey);
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("ESBHeaderSOAPHandler for::" + mapKey);
		}
		
		// get the context object from the map if already exists
		ESBHeaderSOAPHandler esbHeaderSOAPHandler = esbHeaderSOAPHandlerMap.get(mapKey);
		
		if (esbHeaderSOAPHandler == null)
		{
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("ESBHeaderSOAPHandler is null. Creating new from the facade store");
			}
			
			// if does not exist, create the context object
			esbHeaderSOAPHandler = new ESBHeaderSOAPHandler();
			
			// get the data from the store and set to the context object
			ESBHeader esbHeader = baseFacadeStore.getESBHeader(this.service, method, env);
			esbHeaderSOAPHandler.setRoutingControl(esbHeader.getRoutingControl());
			esbHeaderSOAPHandler.setRoutingFieldName(esbHeader.getRoutingFieldName());
			esbHeaderSOAPHandler.setRoutingFieldValue(esbHeader.getRoutingFieldValue());
			esbHeaderSOAPHandler.setRoutingVersion(esbHeader.getRoutingVersion());
			esbHeaderSOAPHandler.setPropertiesControl(esbHeader.getPropertiesControl());
			
			String properties = esbHeader.getProperties();
			
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("ESBHeaderSOAPHandler esbHeader.getPropertiesControl() :: " + esbHeader.getPropertiesControl());
				logger.debug("ESBHeaderSOAPHandler esbHeader properties :: " + properties);
			}
			
			// split the properties with "#"
			String [] propertiesArray = properties.split("#");
			
			if (propertiesArray != null)
			{
				ArrayList<HashMap <String, String>> propertyList = new ArrayList<HashMap <String, String>>();
				for (String eachProperty : propertiesArray) 
				{
					if (DEBUG_LOGGER_ENABLED)
					{
						logger.debug("ESBHeaderSOAPHandler eachProperty :: " + eachProperty);
					}
					
					// create a map for each of the comma separated key-value pairs
					HashMap <String, String> eachPropertyMap = new HashMap<String, String>();
					String [] eachPropertySet = eachProperty.split(",");
					
					if (DEBUG_LOGGER_ENABLED)
					{
						logger.debug("ESBHeaderSOAPHandler property set " + eachPropertySet.toString());
						logger.debug("ESBHeaderSOAPHandler eachPropertySet[0] :: " + eachPropertySet[0]);
						logger.debug("ESBHeaderSOAPHandler eachPropertySet[1] :: " + eachPropertySet[1]);
					}
					
					// put the key and value to the map
					eachPropertyMap.put(eachPropertySet[0], eachPropertySet[1]);
					// add the map to the ArrayList
					propertyList.add(eachPropertyMap);
				}
				esbHeaderSOAPHandler.setPropertyList(propertyList);
			}
			
			// add to the context map
			esbHeaderSOAPHandlerMap.put(mapKey, esbHeaderSOAPHandler);
		}
		
		return esbHeaderSOAPHandler;
	}


	@Override
	public String getEndPointURL(String method, String env) 
	{
		String mapKey = keyMap.get(this.service + method + env);
		
		// possible to pass different env for for different operations
		if (mapKey == null)
		{
			mapKey = this.service + method + env;
			keyMap.put(mapKey, mapKey);
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("endPointURL for ::" + mapKey);
		}
		
		String endPointURL = endPointURLMap.get(mapKey);
		
		if (endPointURL == null)
		{	
			logger.debug("ESBEndPoint is null. Creating new from the facade store");
			
			// get the data from the store and set to the end point url
			ESBEndPoint esbEndPoint = baseFacadeStore.getServiceEndPoint(this.service, method, env);
			if (esbEndPoint == null)
			{
				logger.error("BaseFacade.getEndPointURL(): esbEndPoint is null for method=" + method + ", env=" + env);
			}
			else if (esbEndPoint.getServiceUrl() == null)
			{
				logger.error("BaseFacade.getEndPointURL(): esbEndPoint.getServiceUrl() is null  for method=" + method + ", env=" + env);
			}
			
			endPointURL = esbEndPoint.getServiceUrl();
			
			// add the end point url to the map
			endPointURLMap.put(mapKey, endPointURL);
		}
		
		return endPointURL;
	}
}