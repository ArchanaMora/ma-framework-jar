package com.wellpoint.mobility.aggregation.core.configuration.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.configuration.Config;
import com.wellpoint.mobility.aggregation.core.configuration.ConfigListener;
import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationChangeListener;
//import com.wellpoint.mobility.aggregation.persistence.domain.Configuration;

/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * This class is a Stateless Session bean that calls back registered listeners when a Configuration Change occurs. 
 * Services interested in configuration changes need to call the addConfigurationChangeListener method.
 * This EJB subscribes to a configuration change topic. 
 * It also contains a private inner class that implements the MessageListener interface.
 * When it receives a configuration change message it calls the notifyListeners method, which performs a callback to all registered Listeners.
 *
 * 
 * @author robert.swarr@wellpoint.com
 *
 */

@Stateless(name="ConfigurationChangeListener")
public class ConfigurationChangeListenerImpl implements ConfigurationChangeListener
{

	// inject Topic
	@Resource(mappedName="jms/ConfigurationChangeTopic")
	private Topic configurationChangeTopic;
	// inject Topic ConnectionFactory
	@Resource(mappedName="jms/DurableTopicConnectionFactory")
	private TopicConnectionFactory topicConnectionFactory;
	
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private TopicSubscriber topicSubscriber;
	
	private static final boolean SESSION_TRANSACTED = false;
	private static final String CLIENT_ID = "CONFIG_CHANGE_LISTENER";
	
	private String clientId;
	// the message listener
	ChangeTopicListener changeTopicListener = null;
	
	/**
	 * Log4j Logger
	 */
	private static final Logger LOG = Logger.getLogger(ConfigurationChangeListenerImpl.class.getName());

	// Map of currently loaded configurations
    private static Map<String,ConfigListener> listenerMap = Collections.synchronizedMap(new HashMap<String, ConfigListener>());


	
	/**
	 * Default, zero-argument Constructor.
	 */
	public ConfigurationChangeListenerImpl() {
		super();
	}
	
	
	  /**
     * A method annotated with @PostConstruct that creates a Topic Connection
     */

    @PostConstruct
    public void intialize() 
    {
    	try {
    		changeTopicListener = new ChangeTopicListener();
    		topicConnection = topicConnectionFactory.createTopicConnection();
    		clientId = topicConnection.getClientID();
    		LOG.info("In initialize() method, clientId = " + clientId);
    		topicSession = topicConnection.createTopicSession(SESSION_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
    		topicSubscriber = topicSession.createDurableSubscriber(configurationChangeTopic, CLIENT_ID);
    		topicSubscriber.setMessageListener(changeTopicListener);
    		topicConnection.start();
    	} catch (JMSException jmse) {
    		jmse.printStackTrace();
    	}
    }
    
    /**
     * A method annotated with @PreDestroy that closes and nulls the topic connection
     */
    @PreDestroy
    public void cleanup() 
    {
    	try {
    		topicConnection.close();
    		topicConnection = null;
    	} catch (JMSException jmse) {
    		jmse.printStackTrace();
    	}
   }
	
	/**
	 * Adds the ConfigListener to a List of Listeners
	 * @param configListener
	 * @return boolean
	 */
	public boolean addConfigurationChangeListener(Config config, ConfigListener configListener)
	{
		boolean success = true;
		ConfigurationChangeListenerImpl.listenerMap.put(config.getConfigName(), configListener);
		//configListenerList.add(configListener);
		return success;
	}
	
	/**
	 * This method performs a callback to a list of Listeners added by calls to the 
	 * addConfigurationChangeListener method.
	 * @param Config
	 */
	private void notifyListeners(Config config)
	{
		LOG.info("Entered ConfigurationChangeListener.notifyListeners() method");
		
		for (Map.Entry<String,ConfigListener> entry:  listenerMap.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(config.getConfigName())) {
				ConfigListener listener = entry.getValue();
				listener.onConfigChange(config);
			}
		}
		
		LOG.info("Entered ConfigurationChangeListener.notifyListeners() method");

	}
	
	/**
	 * A private inner class that is the receiver of messages on the configurationChangeTopic
	 * @author robert.swarr@wellpoint.com
	 *
	 */
	private class ChangeTopicListener implements MessageListener {
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        LOG.info("Entered ChangeTopicListener.onMessage() method");
        try {
        	if (message != null) {
        		objectMessage = (ObjectMessage) message;
        		Config config = (Config) objectMessage.getObject();
        		notifyListeners(config);
        	} else {
        		LOG.info(" In ChangeTopicListener.pollConfigurationTopic() method message is NULL!");
        	}
		} catch (JMSException jmse) {
			
		}
        LOG.info("Exiting ChangeTopicListener.onMessage() method");
        
    } // end of onMessage() method
	} // end of ChangeTopicListener class
}
