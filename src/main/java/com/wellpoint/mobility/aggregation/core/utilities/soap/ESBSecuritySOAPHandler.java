/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 */
package com.wellpoint.mobility.aggregation.core.utilities.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

/**
 * ESB Security Header Appender
 * 
 * This inserts the following security tag
 * 
 * <pre>
 *    <wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
 *        <wsse:UsernameToken wsu:Id="UsernameToken-2" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
 *           <wsse:Username>${username}</wsse:Username>
 *           <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">${password}</wsse:Password>
 *        </wsse:UsernameToken>
 *     </wsse:Security>
 * </pre>
 * 
 * This provides a configurable username and password
 * 
 * @author edward.biton@wellpoint.com
 */
public class ESBSecuritySOAPHandler implements SOAPHandler<SOAPMessageContext>
{
	/**
	 * username property
	 */
	private String username;
	/**
	 * password property
	 */
	private String password;
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ESBSecuritySOAPHandler.class);
	/**
	 * Is debug enable flag
	 */
	private static final boolean DEBUG_ENABLED = logger.isDebugEnabled();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext soapMessageContext)
	{
		if (DEBUG_ENABLED)
		{
			logger.debug("ESBSecuritySOAPHandler.handleMessage(soapMessageContext):ENTRY");
		}
		boolean isRequest = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (isRequest == false)
		{
			if (DEBUG_ENABLED)
			{
				logger.debug("ESBSecuritySOAPHandler.handleMessage(soapMessageContext):EXIT => false");
			}
			return false;
		}

		try
		{
//			long startTime = System.currentTimeMillis();
			if (DEBUG_ENABLED)
			{
				SOAPMessage soapMessage = soapMessageContext.getMessage();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBSecuritySOAPHandler.handleMessage() - CURRENT MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
			}
			SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnvelope.getHeader();
			if (soapHeader == null)
			{
				soapHeader = soapEnvelope.addHeader();
			}

			SOAPElement securityElement = soapHeader.addChildElement("Security", "wsse",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
			SOAPElement usernameTokenElement = securityElement.addChildElement("UsernameToken", "wsse");
			// This is commented since it is causing an error in the unmarshalling of xml
			// usernameTokenElement.addAttribute(new QName("xmlns:wsu"),
			// "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
			SOAPElement usernameElement = usernameTokenElement.addChildElement("Username", "wsse");
			usernameElement.addTextNode(getUsername());
			SOAPElement passwordElement = usernameTokenElement.addChildElement("Password", "wsse");
			passwordElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
			passwordElement.addTextNode(getPassword());

			if (DEBUG_ENABLED)
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				SOAPMessage soapMessage = soapMessageContext.getMessage();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBSecuritySOAPHandler.handleMessage() - UPDATED MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
				logger.debug("ESBSecuritySOAPHandler.handleMessage(soapMessageContext):EXIT => true");
			}
//			logger.debug("(OPTMIZATION) ESBSecuritySOAPHandler=" + (System.currentTimeMillis() - startTime) + "ms");
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}

		logger.debug("ESBSecuritySOAPHandler.handleMessage(soapMessageContext):EXIT => false");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext soapMessageContext)
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext messageContext)
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders()
	{
		return null;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ESBSOAPHandler [username=" + username + ", password=" + password + "]";
	}
}
