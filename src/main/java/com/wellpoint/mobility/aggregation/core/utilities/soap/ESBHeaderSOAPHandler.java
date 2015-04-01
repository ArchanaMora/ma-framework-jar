package com.wellpoint.mobility.aggregation.core.utilities.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * ESBHeader Appender
 * 
 * This inserts the following esb header tag
 * 
 * <pre>
 * <h:ESBHeader xmlns:h="http://wellpoint.com/esb/header" xmlns="http://wellpoint.com/esb/header">
 *     <ESBProps>
 *     	<Property>
 *    			<name>WlPntHdr.srcNvrmt</name>
 * 			<value>SYST</value>
 * 		</Property>
 * 	</ESBProps>
 * 		<Routing>
 *          <version>3</version>
 *          <FieldList>
 *             <Field>
 *                <name>trgtLgclSytm</name>
 *                <value>SYST</value>
 *             </Field>
 *          </FieldList>
 *       </Routing>
 * </h:ESBHeader>
 * </pre>
 * 
 * @author bharat.meda@wellpoint.com
 */
public class ESBHeaderSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
	/**
	 * list of Property key-value pairs
	 */
	private ArrayList<HashMap<String, String>> propertyList;

	/**
	 * check for properties control
	 */
	private String propertiesControl;

	/**
	 * check for properties control
	 */
	private String routingControl;

	/**
	 * routing Version
	 */
	private String routingVersion;

	/**
	 * routing Field name
	 */
	private String routingFieldName;

	/**
	 * routing Field value
	 */
	private String routingFieldValue;
	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ESBHeaderSOAPHandler.class);
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
			logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):ENTRY");
		}
		boolean isRequest = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (isRequest == false)
		{
			if (DEBUG_ENABLED)
			{
				logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):EXIT => false");
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
				logger.debug("ESBHeaderSOAPHandler.handleMessage() - CURRENT MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
			}
			// if both the properties and routing control indicator are switched off, no need to add this header
			if (!"OFF".equalsIgnoreCase(getPropertiesControl()) && !"OFF".equalsIgnoreCase(getRoutingControl()))
			{
				logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext): => (control is not set to to OFF) ");

				SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();
				SOAPHeader soapHeader = soapEnvelope.getHeader();
				if (soapHeader == null)
				{
					soapHeader = soapEnvelope.addHeader();
				}

				SOAPElement headerElement = soapHeader.addChildElement("ESBHeader", "h", "http://wellpoint.com/esb/header");

				// add ESBProps
				SOAPElement esbProps = headerElement.addChildElement("ESBProps");

				// if properties is on
				if ("ON".equalsIgnoreCase(getPropertiesControl()))
				{
					ArrayList<HashMap<String, String>> iterableList = getPropertyList();
					// there could be more than one key-value pairs for Property node
					for (HashMap<String, String> eachPropertyMap : iterableList)
					{
						// get the keys Set
						Set<String> keys = eachPropertyMap.keySet();
						for (String eachKey : keys)
						{
							SOAPElement propertyElement = esbProps.addChildElement("Property");

							SOAPElement propertyNameElement = propertyElement.addChildElement("name");
							logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):  => propety key:: " + eachKey);
							propertyNameElement.addTextNode(eachKey != null ? eachKey : "");

							SOAPElement propertyValueElement = propertyElement.addChildElement("value");
							logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):  => property value:: "
									+ (eachPropertyMap.get(eachKey) != null ? eachPropertyMap.get(eachKey) : ""));
							propertyValueElement.addTextNode(eachPropertyMap.get(eachKey) != null ? eachPropertyMap.get(eachKey) : "");
						}
					}
				}

				// if routing is on
				if ("ON".equalsIgnoreCase(getRoutingControl()))
				{
					// add Routing
					SOAPElement routing = headerElement.addChildElement("Routing");
					SOAPElement versionElement = routing.addChildElement("version");

					logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):  => routing version :: " + getRoutingVersion());
					versionElement.addTextNode(getRoutingVersion());

					SOAPElement fieldListElement = routing.addChildElement("FieldList");
					SOAPElement fieldElement = fieldListElement.addChildElement("Field");
					SOAPElement fieldNameElement = fieldElement.addChildElement("name");
					logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):  => routing field name :: " + getRoutingFieldName());
					fieldNameElement.addTextNode(getRoutingFieldName());
					SOAPElement fieldValueElement = fieldElement.addChildElement("value");
					logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):  => routing field value :: " + getRoutingFieldValue());
					fieldValueElement.addTextNode(getRoutingFieldValue());
				}

			}
			if (DEBUG_ENABLED)
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				SOAPMessage soapMessage = soapMessageContext.getMessage();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBHeaderSOAPHandler.handleMessage() - UPDATED MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
				logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):EXIT => true");
			}
//			logger.debug("(OPTMIZATION) ESBHeaderSOAPHandler=" + (System.currentTimeMillis() - startTime) + "ms");
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

		logger.debug("ESBHeaderSOAPHandler.handleMessage(soapMessageContext):EXIT => false");
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
	 * @return the propertyList
	 */
	public ArrayList<HashMap<String, String>> getPropertyList()
	{
		return propertyList;
	}

	/**
	 * @param propertyList
	 *            the propertyList to set
	 */
	public void setPropertyList(ArrayList<HashMap<String, String>> propertyList)
	{
		this.propertyList = propertyList;
	}

	/**
	 * @return the routingVersion
	 */
	public String getRoutingVersion()
	{
		return routingVersion;
	}

	/**
	 * @param routingVersion
	 *            the routingVersion to set
	 */
	public void setRoutingVersion(String routingVersion)
	{
		this.routingVersion = routingVersion;
	}

	/**
	 * @return the routingFieldName
	 */
	public String getRoutingFieldName()
	{
		return routingFieldName;
	}

	/**
	 * @param routingFieldName
	 *            the routingFieldName to set
	 */
	public void setRoutingFieldName(String routingFieldName)
	{
		this.routingFieldName = routingFieldName;
	}

	/**
	 * @return the routingFieldValue
	 */
	public String getRoutingFieldValue()
	{
		return routingFieldValue;
	}

	/**
	 * @param routingFieldValue
	 *            the routingFieldValue to set
	 */
	public void setRoutingFieldValue(String routingFieldValue)
	{
		this.routingFieldValue = routingFieldValue;
	}

	/**
	 * @return the propertiesControl
	 */
	public String getPropertiesControl()
	{
		return propertiesControl;
	}

	/**
	 * @param propertiesControl
	 *            the propertiesControl to set
	 */
	public void setPropertiesControl(String propertiesControl)
	{
		this.propertiesControl = propertiesControl;
	}

	/**
	 * @return the routingControl
	 */
	public String getRoutingControl()
	{
		return routingControl;
	}

	/**
	 * @param routingControl
	 *            the routingControl to set
	 */
	public void setRoutingControl(String routingControl)
	{
		this.routingControl = routingControl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ESBHeaderSOAPHandler [propertyList=" + propertyList + ", propertiesControl=" + propertiesControl + ", routingControl=" + routingControl
				+ ", routingVersion=" + routingVersion + ", routingFieldName=" + routingFieldName + ", routingFieldValue=" + routingFieldValue + "]";
	}
}