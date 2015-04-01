/**
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
*/
package com.wellpoint.mobility.aggregation.core.failurenotification;

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
 * Takes a failure information object and places it on a queue for processing.
 *
 */
@Stateless
public class FailureNotificationProducer {

	@Resource(mappedName = "jms/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/FailureNotificationQueue")
	private Queue queue;
	

	private static final Logger logger = Logger.getLogger(FailureNotificationProducer.class.getName());
//	private static final boolean DEBUG_LOGGING_ENABLED = logger.isDebugEnabled();
	private static final boolean INFO_LOGGING_ENABLED = logger.isInfoEnabled();

	/**
	 * Creates and sends a message regarding the failure. 
	 * @param fnip
	 */
	public void submitFailure(final FailureNotificationInformationPojo fnip) {

		if (INFO_LOGGING_ENABLED)
		{
			logger.info("FailureNotificationProducer.submitFailure(): Sending message for service: " + fnip);
		}
		
		Connection connection = null;
		try {
			if (connectionFactory != null) {
				connection = connectionFactory.createConnection();
			} else {
				logger.error("FailureNotificationProducer.submitFailure(): connectionFactory = null!!!");
				return;
			}
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final ObjectMessage message = session.createObjectMessage();
			message.setObject(fnip);
			final MessageProducer messageProducer = session.createProducer(queue);
			messageProducer.send(message);
		} catch (JMSException e) {
			logger.error("FailureNotificationProducer.submitFailure(): Error during the send of a failure message", e);
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
				logger.error("FailureNotificationProducer.submitFailure(): Error during the connection.close()", e);
			}
		}

	} // of sendTestMessage

} // of class
