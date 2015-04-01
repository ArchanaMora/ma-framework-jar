/*
* Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
*
* This program contains proprietary and confidential information and trade
* secrets of Wellpoint. This program may not be duplicated, disclosed or
* provided to any third parties without the prior written consent of
* Wellpoint. Disassembling or decompiling of the software and/or reverse
* engineering of the object code are prohibited.
* 
*/
package com.wellpoint.mobility.aggregation.core.utilities;

import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.propertiesmanager.PropertiesManagerEjb;

/**
* This class connects to the SMTP server and sends an email message.
* 
* Author: frank.garber@wellpoint.com
* 
 */
@Stateless
public class EmailClient
{

	@EJB
	PropertiesManagerEjb propertiesManager;
	
	private static final Logger logger = Logger.getLogger(EmailClient.class.getName());
//	private static final boolean DEBUG_LOGGER_ENABLED = logger.isDebugEnabled();
	private static final boolean INFO_LOGGER_ENABLED = logger.isInfoEnabled();

	static Properties properties = new Properties();
	static {
	  // These can be moved to an application property files once the values are known/working
	  properties.put("mail.smtp.auth", "false");
	  properties.put("mail.smtp.starttls.enable", "false");
	  properties.put("mail.smtp.host", "30.128.96.197");	// Works from DEV server on 9/29
	  properties.put("mail.smtp.port", "25");
	  properties.put("mail.smtp.debug", "true");
	  properties.put("mail.protocol.ssl.trust", "true");
	}

	private String environment;
	
	@PostConstruct
	void postContructMethod() {
		final String envPropertyValue = "com.wellpoint.mobility.aggregation.core.environment";
		try
		{
			environment = propertiesManager.getPropertiesByKey(envPropertyValue).getValue();
		}
		catch (Exception e)
		{
			logger.error("EmailClient.postContructMethod(): Unable to load property for key=" + envPropertyValue +
				". Exception message=" + e.getMessage() + ". Setting 'environment' to a value so that all messages will be sent.");
			environment = "xxx";
		}
		
		if (INFO_LOGGER_ENABLED)
		{
			logger.info("EmailClient.enclosing_method(): environment=" + environment);
		}
	}
	
	
	/**
	 * Send a single email message with the provided parameters
	 * 
	 * @param to an array of one or more well formed email addresses
	 * @param cc array of one or more well formed email addresses
	 * @param from a well formed email address
	 * @param subject the subject of the email message
	 * @param body the body of the message
	 */
	public void sendMessage(Address to[], Address cc[], String from, String subject, String body) {
		
		if (AppConstants.ENVIRONMENT_LOCAL.equals(environment))
		{
			logger.info("EmailClient.sendMessage(): email is not sent from the local env. To=" + Utils.toString(Arrays.asList(to)) +
				", cc=" + Utils.toString(Arrays.asList(cc)) +
				", from=" + from + ", subject=" + subject + ", body=" + body);
		}
		else
		{
			final Session session = Session.getInstance(properties, null); 
			final MimeMessage message = new MimeMessage(session);
			Transport transport = null;
			try {
			    message.setFrom(new InternetAddress(from));
			    message.addRecipients(Message.RecipientType.TO, to);
			    if (cc != null && cc.length > 0)
				{
				    message.setRecipients(Message.RecipientType.TO, cc);
				}
			    if (!AppConstants.ENVIRONMENT_PROD.equals(environment))
				{	
					subject += " (evn:" + environment + ")";
				}
			    
			    message.setSubject(subject);
			    message.setContent(body, "text/html");
			    transport = session.getTransport("smtp");
			    transport.connect(properties.getProperty("host"), null, null);
			    message.saveChanges();
			    transport.sendMessage(message, message.getAllRecipients());
			} catch (Exception ex) {
			    logger.error("Unable to send email.", ex);
			} finally {
				if (transport != null)
				{
					try
					{
						transport.close();
					}
					catch (Exception e) {}
				}
			}
		}

	} // of sendMessage
	
	
	
	/**
	 * Send a single email message with the provided parameters
	 * 
	 * @param to a CSV list of well formed email addresses
	 * @param cc a CSV list of well formed email addresses
	 * @param from a well formed email address
	 * @param subject the subject of the email message
	 * @param body the body of the message
	 * @throws AddressException if any of the 'to' or 'cc' addresses don't pass the InternetAddress.parse()
	 */
	public void sendMessage(String to, String cc, String from, String subject, String body) throws AddressException {
		
		sendMessage(InternetAddress.parse(to, false), InternetAddress.parse(cc, false), from, subject, body);
		
	} // of sendMessage	
	
} // of class