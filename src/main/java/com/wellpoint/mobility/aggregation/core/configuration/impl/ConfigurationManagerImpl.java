package com.wellpoint.mobility.aggregation.core.configuration.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.configuration.Config;
import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationManager;
import com.wellpoint.mobility.aggregation.core.utilities.XML2ObjectUtility;
import com.wellpoint.mobility.aggregation.persistence.domain.Configuration;

/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * Concrete class that implements the ConfigurationManager interface.
 * @author Bob Swarr
 *
 */

@Stateless(name = "ConfigurationManager")
public class ConfigurationManagerImpl implements ConfigurationManager {
	
	/**
	 * Log4j Logger
	 */
	private static final Logger logger = Logger.getLogger(ConfigurationManagerImpl.class.getName());
//	private static final boolean DEBUG_LOGGER_ENABLED = logger.isDebugEnabled();
	private static final boolean INFO_LOGGER_ENABLED = logger.isInfoEnabled();
	
    private static final String SYSTEM_USER = "SYSTEM" ;
    // timer interval = 15 minutes
    private static final boolean SESSION_TRANSACTED = true;
    private static final String SCHEDULE_SECOND = "0";
    private static final String SCHEDULE_MINUTE = "*/5";
    private static final String SCHEDULE_HOUR = "*";
    
    @Resource(mappedName="jms/QueueConnectionFactory")
    private javax.jms.QueueConnectionFactory connectionFactory;
    @Resource(mappedName="jms/ConfigurationChangeQueue")
    private javax.jms.Queue destinationQueue;
    private QueueConnection queueConnection;
    
    // Map of currently loaded configurations
    private static Map<String,Configuration> configMap = Collections.synchronizedMap(new HashMap<String, Configuration>());

    /**
     * Default, zero argument constructor
     */
    public ConfigurationManagerImpl() 
    {
    	super();
    }
    
    /**
     * A method annotated with @PostConstruct that creates a Queue Connection
     */
    @PostConstruct
    public void intialize() 
    {
    	try {
    		queueConnection = connectionFactory.createQueueConnection();
    		
    	} catch (JMSException jmse) {
    		jmse.printStackTrace();
    	}
    }
    
    /**
     * A method annotated with @PreDestroy that closes and nulls the queue connection
     */
    @PreDestroy
    public void cleanup() 
    {
    	//configTimer.cancel();
    	try {
    		queueConnection.close();
    		queueConnection = null;
    	} catch (JMSException jmse) {
    		jmse.printStackTrace();
    	}
    }
    
    
	/**
	 * Entity Manager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
    
	// String for query used to retrieve configuration data
	private static final String CONFIG_QUERY = 
			"SELECT c FROM Configuration c"
			+ " WHERE c.configName =  :configName";
	
    
	// String for query used to check for the presence of configuration data
	private static final String CONFIG_QUERY_COUNT = 
			"SELECT count(*) FROM Configuration c"
			+ " WHERE c.configName =  :configName";
	
	/**
	 * Implementation of saveConfiguration method.  This method
	 * saves the data in the Config Object in the database.  
	 * This method performs a "smart" save.  It first queries
	 * the database to see if a configuration exists.  If it 
	 * exists it performs a merge, otherwise it performs a persist.
	 * 
	 * @return boolean
	 */
	public boolean saveConfiguration(Config config, String configType) {
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Entering saveConfiguration() method.");
		}
		boolean success = true;
		Configuration oldConfiguration = null;
		try {
			oldConfiguration = (Configuration) entityManager.createQuery(CONFIG_QUERY)
					.setParameter("configName", config.getConfigName())
					.getSingleResult();
		} catch (NoResultException nre) {
			return persistConfiguration(config, configType);
		}
		
		if (oldConfiguration != null) {
			return mergeConfiguration(oldConfiguration, config, configType);
		}
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Exiting saveConfiguration() method.");
		}
		return success;	
	}
	
	/**
	 * Inserts a new configuration into the database
	 * @param config
	 * @param configType
	 * @return boolean
	 */
	private boolean persistConfiguration(Config config, String configType) 
	{
		boolean success = true;
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Entered persistConfiguration() method");
		}
		
		// use XStream to convert the config Object to an XML String
		String xmlConfigString = XML2ObjectUtility.toXml(config);
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("XML:" + xmlConfigString);
		}
		
		Configuration configEntity = new Configuration();
		configEntity.setConfigName(config.getConfigName());
		configEntity.setConfigType(configType);
		configEntity.setConfigValue(xmlConfigString);
		configEntity.setCreatedBy(SYSTEM_USER);
		configEntity.setUpdatedBy(SYSTEM_USER);
			
		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		// 2) get a java.util.Date from the calendar instance.
		//    this date will represent the current instant, or "now".
		Date now = calendar.getTime();
		// 3) a java current time (now) instance
		Timestamp ts = new java.sql.Timestamp(now.getTime());
		configEntity.setCreatedDate(ts);
		configEntity.setUpdatedDate(ts);
			
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Calling entity manager to persist Configuration entity");
		}
		// persist the Configuration entity
		try {
			entityManager.persist(configEntity);
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Exiting persistConfiguration() method");
		}
		return success;
	}
	/**
	 * Helper method that 
	 * @param oldConfiguration
	 * @param newConfig
	 * @param configType
	 * @return boolean
	 */
	private boolean mergeConfiguration(Configuration oldConfiguration, Config newConfig, String configType)
	{
		boolean success = true;
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Entering mergeConfiguration() method");
		}
		
		// use XStream to convert the config Object to an XML String
		String xmlConfigString = XML2ObjectUtility.toXml(newConfig);
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.debug("XML:" + xmlConfigString);
		}
		
		Configuration configEntity = new Configuration();
		configEntity.setId(oldConfiguration.getId());
		configEntity.setConfigName(newConfig.getConfigName());
		configEntity.setConfigType(configType);
		configEntity.setConfigValue(xmlConfigString);
		configEntity.setCreatedBy(SYSTEM_USER);
		configEntity.setUpdatedBy(SYSTEM_USER);
		
		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		// 2) get a java.util.Date from the calendar instance.
		//    this date will represent the current instant, or "now".
		Date now = calendar.getTime();
		// 3) a java current time (now) instance
		Timestamp ts = new java.sql.Timestamp(now.getTime());
		configEntity.setCreatedDate(ts);
		configEntity.setUpdatedDate(ts);
		
		try {
			entityManager.merge(configEntity);
			
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Exiting mergeConfiguration() method");
		}
		
		return success;
	}
	
	
	/**
	 * Implementation of the loadConfiguration method. This method retrieves the 
	 * data for the configuration identified by the component name, populates 
	 * a ConfigurationDTO, and returns this DTO to the caller.
	 * 
	 * @return a Config object or null if not found.
	 * 
	 */
	public Config loadConfiguration(Config configuration)  {
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Entered loadConfiguration() method");
		}
		
		// retrieve the configuration from the database by the component name
		final Long configCount = (Long) entityManager.createQuery(CONFIG_QUERY_COUNT)
				.setParameter("configName", configuration.getConfigName())
				.getSingleResult();

		if (configCount == 0)
		{
			logger.warn("ConfigurationManagerImpl.loadConfiguration():Configuration not found for=" + configuration + ". Returning null.");
			return null;
		}
		
		Configuration configEntity = null;
		try
		{
			configEntity = (Configuration) entityManager.createQuery(CONFIG_QUERY)
					.setParameter("configName", configuration.getConfigName())
					.getSingleResult();
		}
		catch (NoResultException e)
		{
			final String msg = "ConfigurationManagerImpl.loadConfiguration(): no config info found for:" + configuration;
			logger.error(msg);
			return null;
		}
		
		// translate XML String to a Config object
		String xmlStr = configEntity.getConfigValue();
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("XML: " + xmlStr);
		}
		
		Config configObject = (Config) XML2ObjectUtility.toObject(xmlStr);
		// update the collection of configurations with 
		// this loaded configuration
		ConfigurationManagerImpl.configMap.put(configEntity.getConfigName(), configEntity);
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Exiting loadConfiguration() method");
		}
		
		return configObject;
	}
	
	/**
	 * This class uses the @Schedule annotation to create a timer that times out 
	 * based on a chron-like expression.
	 *  
	 */
	@Schedule(second = SCHEDULE_SECOND, minute = SCHEDULE_MINUTE, hour = SCHEDULE_HOUR)
	public void monitorConfiguration() 
	{
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("Entered monitorConfiguration() method");
		}
		
		for (Map.Entry<String, Configuration> configEntry: configMap.entrySet() )
		{
			String key = configEntry.getKey();
			Configuration currentConfig = configEntry.getValue();
			if (INFO_LOGGER_ENABLED)
			{
				logger.info("In monitorConfiguration(), key = " + key + ", value = " + currentConfig);
			}
			
			Configuration dbConfig = entityManager.find(Configuration.class, currentConfig.getId());
			// determine if the configuration has changed
			if (dbConfig != null && currentConfig.getUpdatedDate().before(dbConfig.getUpdatedDate())) 
			{
				// send a message to the ConfigurationChangeNotifier that the configuration has changed
				try {
					QueueSession queueSession = queueConnection.createQueueSession(SESSION_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
					MessageProducer messageProducer = queueSession.createProducer(destinationQueue);
		    		ObjectMessage objectMessage = queueSession.createObjectMessage();
		    		// translate XML String to a Config object
		    		String xmlStr = dbConfig.getConfigValue();
		    		if (INFO_LOGGER_ENABLED)
					{
						logger.info("XML: " + xmlStr);
					}
		    		
					Config configObject = (Config) XML2ObjectUtility.toObject(xmlStr);
		    		objectMessage.setObject(configObject);
		    		messageProducer.send(objectMessage);
		            queueSession.close();
				} catch (JMSException jmse) {
					jmse.printStackTrace();
				}
				
			} // end of if statement
			
		} // end of for loop
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("exiting monitorConfiguration() method");
		} 
		
	} // end of monitorConfiguration method
	
	
	@Override
	public Config loadConfiguration(final Config configuration, final boolean useDefault) {
		Config config = loadConfiguration(configuration);
		if (config == null && useDefault)
		{
			config = configuration.getDefaultConfig();
			final String configName = config.getClass().getName();
			if (INFO_LOGGER_ENABLED)
			{
				logger.info("ConfigurationManagerImpl.loadConfiguration(): Config object not found. Using and storing the default config object. configName=" + configName + ", configValue=" + config);
			}
			saveConfiguration(config, configName);
		}

		return config;
		
	} // of loadConfiguration

} // of class
