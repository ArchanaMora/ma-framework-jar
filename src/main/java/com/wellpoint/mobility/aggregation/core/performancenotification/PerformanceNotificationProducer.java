/**
* Copyright (c) 2015 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
*/
package com.wellpoint.mobility.aggregation.core.performancenotification;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;


/**
 * @author frank.garber@wellpoint.com
 * 
 * Takes a performance information object and places it on a queue for processing.
 *
 */
@Stateless
public class PerformanceNotificationProducer {

	@Resource(mappedName = "jms/PerformanceNotificationQueueConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/PerformanceNotificationQueue")
	private Queue queue;
	

	private static final Logger logger = Logger.getLogger(PerformanceNotificationProducer.class.getName());
//	private static final boolean DEBUG_LOGGING_ENABLED = logger.isDebugEnabled();
	private static final boolean INFO_LOGGING_ENABLED = logger.isInfoEnabled();

	/**
	 * Creates and sends a message regarding the failure. 
	 * @param fnip
	 */
	public void submitPerformanceInfo(final PerformanceNotificationInformationPojo fnip) {

//		if (INFO_LOGGING_ENABLED)
		{
			logger.info("PerformanceNotificationProducer.submitPerformanceInfo(): Sending message for service: " + fnip);
		}
		
		//if (1 == 1) return;
		
		Connection connection = null;
		try {
			if (connectionFactory != null) {
				connection = connectionFactory.createConnection();
			} else {
				logger.error("PerformanceNotificationProducer.submitPerformanceInfo(): connectionFactory = null!!!");
				return;
			}
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final ObjectMessage message = session.createObjectMessage();
			message.setObject(fnip);
			final MessageProducer messageProducer = session.createProducer(queue);
			messageProducer.send(message);
		} catch (JMSException e) {
			logger.error("PerformanceNotificationProducer.submitPerformanceInfo(): Error during the send of a performance message", e);
		} finally {
			try
			{
				if (connection != null)
				{
					connection.close();
				}
			}
			catch (Exception e)
			{
				logger.error("PerformanceNotificationProducer.submitPerformanceInfo(): Error during the connection.close()", e);
			}
		}

	} // of submitPerformanceInfo

} // of class
