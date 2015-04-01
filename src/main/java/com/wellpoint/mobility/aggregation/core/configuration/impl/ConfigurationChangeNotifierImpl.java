package com.wellpoint.mobility.aggregation.core.configuration.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.configuration.Config;
import com.wellpoint.mobility.aggregation.core.configuration.ConfigurationChangeNotifier;


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
 * @author robert.swarr@wellpoint.com
 * 
 * Message-Driven Bean implementation class for: ConfigurationChangeNotifier
 * This MDB receives a changed configuration message from the ConfigurationManager EJB 
 * and publishes configuration changes to the ConfigurationChangeTopic.
 *
 */

@MessageDriven(name="ConfigurationChangeNotifier", messageListenerInterface=MessageListener.class,
		activationConfig =  {
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "ConfigurationChangeQueue")
		})
public class ConfigurationChangeNotifierImpl implements ConfigurationChangeNotifier 
{
	
	private static final boolean TRANSACTED_TOPIC = false;
	
	/**
	 * Log4j Logger
	 */
	private static final Logger LOG = Logger.getLogger(ConfigurationChangeNotifierImpl.class.getName());

	
	// inject Topic
	@Resource(mappedName="jms/ConfigurationChangeTopic")
	private Topic configurationChangeTopic;
	// inject Topic ConnectionFactory
	@Resource(mappedName="jms/TopicConnectionFactory")
	private TopicConnectionFactory topicConnectionFactory;
	private TopicSession topicSession;
	private TopicConnection topicConnection;

    /**
     * Default zero-argument constructor. 
     */
    public ConfigurationChangeNotifierImpl() {
        super();
    }
    
    @PostConstruct
    public void initialize() 
    {
    	try {
			topicConnection = topicConnectionFactory.createTopicConnection();
		} catch (JMSException e) {
			e.printStackTrace();
		} 
    }
    
    @PreDestroy
    public void cleanup() {
    	
    	try {
    		topicSession.close();
			topicConnection.close();
			topicSession = null;
			topicConnection = null;
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
	
	/**
	 * This method is required to implement the MessageListner interface.
	 * This MDB receives messages from the ConfigurationManager.
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        LOG.info("Entered ConfigurationChangeNotifier.onMessage() method");
        
        try {
			Config config = (Config) objectMessage.getObject();
			publishConfigurationChange(config);
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
        LOG.info("Exiting ConfigurationChangeNotifier.onMessage() method");
    }
    
	/**
	 * This method publishes configuration changes to the ConfigurationChangeTopic
	 */
	public void publishConfigurationChange(Config config) 
	{
		
		LOG.info("Entered ConfigurationChangeNotifier.publishConfigurationChange() method");
		try {
			topicSession = topicConnection.createTopicSession(TRANSACTED_TOPIC, Session.AUTO_ACKNOWLEDGE);
			TopicPublisher topicPublisher = topicSession.createPublisher(configurationChangeTopic);
			ObjectMessage objectMessage = topicSession.createObjectMessage();
			objectMessage.setObject(config);
			topicPublisher.publish(objectMessage, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_PRIORITY);
			topicSession.close();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
		LOG.info("Exiting ConfigurationChangeNotifier.publishConfigurationChange() method");
	}
	
	
    /**
     * Generated toString() method that overrides Object.toString()
     */
	@Override
	public String toString()
	{
		return "ConfigurationChangeNotifierImpl [connectionFactory=" + topicConnectionFactory + ", topicConnection=" + topicConnection + "]";
	}

}
