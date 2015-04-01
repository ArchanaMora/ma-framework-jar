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
import java.util.Iterator;
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
 * ESB Context Header Appender
 * 
 * This inserts the following esb context tag
 * 
 * <pre>
 *   <h:Context xmlns:h="http://wellpoint.com/esb/context" xmlns="http://wellpoint.com/esb/context" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
 *        <srvcName>ProviderUtilities</srvcName>
 *        <srvcVersion>1.4</srvcVersion>
 *        <operName>Get_ListOfCodes</operName>
 *        <operVersion>1.0</operVersion>
 *        <senderApp>SOAPUI</senderApp>
 *        <clientReqId>c937c6b5-fd2d-4d8b-9a42-de6826e1e3c2</clientReqId>
 *        <transId>5abf9e4c-ac08-4fb2-9bed-bdd1db355b26</transId>
 *        <msgTyp>REQUEST</msgTyp>
 *        <CtxProps>
 * 			<Property>
 * 				<name>requestPageNumber</name>
 * 				<value>0</value>
 * 			</Property>
 * 			<Property>
 * 				<name>clientRowsPerPage</name>
 * 				<value>10</value>
 * 			</Property>
 * 		</CtxProps>
 * 	</h:Context>
 *   </h:Context>
 * </pre>
 * 
 * This provides a configurable username and password
 * 
 * @author edward.biton@wellpoint.com
 */
public class ESBContextSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
	/**
	 * Service Name
	 */
	private String serviceName;
	/**
	 * Service Version
	 */
	private String serviceVersion;
	/**
	 * Operation Name
	 */
	private String operationName;
	/**
	 * Operation Version
	 */
	private String operationVersion;
	/**
	 * Sender
	 */
	private String sender;
	/**
	 * Client Request Id
	 */
	private String clientRequestId;
	/**
	 * Transaction Id
	 */
	private String transactionId;
	/**
	 * Message type
	 */
	private String messageType;
	/**
	 * page number
	 */
	private Long pageNo;
	/**
	 * number of rows per page
	 */
	private Long rowsPerPage;
	/**
	 * total rows - response from the header if it exists
	 */
	private Long totalRows;
	/**
	 * total pages - response from the header if it exists
	 */
	private Long totalPages;
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(ESBContextSOAPHandler.class);
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
			logger.debug("ESBContextSOAPHandler.handleMessage(soapMessageContext):ENTRY");
		}
		boolean isRequest = (Boolean) soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (isRequest == true)
		{
			if (DEBUG_ENABLED)
			{
				logger.debug("ESBContextSOAPHandler.handleMessage(soapMessageContext):EXIT");
			}
			return processRequest(soapMessageContext);
		}
		else
		{
			if (DEBUG_ENABLED)
			{
				logger.debug("ESBContextSOAPHandler.handleMessage(soapMessageContext):EXIT");
			}
			return processResponse(soapMessageContext);
		}
	}

	/**
	 * Process the outgoing request
	 * 
	 * @param soapMessageContext
	 * @return
	 */
	protected boolean processRequest(SOAPMessageContext soapMessageContext)
	{
		if (DEBUG_ENABLED)
		{
			logger.debug("ESBContextSOAPHandler.processRequest(soapMessageContext):ENTRY");
		}
		// re-initialize this value if it is a request, so a new request will always ensure to get the right values
		this.totalRows = null;
		this.totalPages = null;
		try
		{
//			long startTime = System.currentTimeMillis();
			if (DEBUG_ENABLED)
			{
				SOAPMessage soapMessage = soapMessageContext.getMessage();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBContextSOAPHandler.processRequest() - CURRENT MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
			}
			SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnvelope.getHeader();
			if (soapHeader == null)
			{
				soapHeader = soapEnvelope.addHeader();
			}
			SOAPElement contextElement = null;
			if (contextElement == null)
			{
				contextElement = soapHeader.addChildElement("Context", "h", "http://wellpoint.com/esb/context");
				contextElement.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
				contextElement.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				contextElement.addNamespaceDeclaration("", "http://wellpoint.com/esb/context");
			}
			if (this.getServiceName() != null)
			{
				SOAPElement serviceNameElement = contextElement.addChildElement("srvcName");
				serviceNameElement.addTextNode(this.getServiceName());
				contextElement.addChildElement(serviceNameElement);
			}
			if (this.getServiceVersion() != null)
			{
				SOAPElement serviceVersionElement = contextElement.addChildElement("srvcVersion");
				serviceVersionElement.addTextNode(this.getServiceVersion());
				contextElement.addChildElement(serviceVersionElement);
			}
			if (this.getOperationName() != null)
			{
				SOAPElement operationNameElement = contextElement.addChildElement("operName");
				operationNameElement.addTextNode(this.getOperationName());
				contextElement.addChildElement(operationNameElement);
			}
			if (this.getOperationVersion() != null)
			{
				SOAPElement operationVersionElement = contextElement.addChildElement("operVersion");
				operationVersionElement.addTextNode(this.getOperationVersion());
				contextElement.addChildElement(operationVersionElement);
			}
			if (this.getSender() != null)
			{
				SOAPElement senderElement = contextElement.addChildElement("senderApp");
				senderElement.addTextNode(this.getSender());
				contextElement.addChildElement(senderElement);
			}
			if (this.getClientRequestId() != null)
			{
				SOAPElement clientIdElement = contextElement.addChildElement("clientReqId");
				clientIdElement.addTextNode(this.getClientRequestId());
				contextElement.addChildElement(clientIdElement);
			}
			if (this.getTransactionId() != null)
			{
				SOAPElement transactionIdElement = contextElement.addChildElement("transId");
				transactionIdElement.addTextNode(this.getTransactionId());
				contextElement.addChildElement(transactionIdElement);
			}
			if (this.getMessageType() != null)
			{
				SOAPElement messageTypeElement = contextElement.addChildElement("msgTyp");
				messageTypeElement.addTextNode(this.getMessageType());
				contextElement.addChildElement(messageTypeElement);
			}

			if (this.getPageNo() != null && this.getRowsPerPage() != null)
			{
				SOAPElement ctxPropsElement = contextElement.addChildElement("CtxProps");
				SOAPElement propertyElementPageNumber = ctxPropsElement.addChildElement("Property");
				SOAPElement nameElementPageNumber = propertyElementPageNumber.addChildElement("name");
				nameElementPageNumber.addTextNode("requestPageNumber");
				SOAPElement valueElementPageNumber = propertyElementPageNumber.addChildElement("value");
				valueElementPageNumber.addTextNode(this.getPageNo().toString());
				SOAPElement propertyElementRows = ctxPropsElement.addChildElement("Property");
				SOAPElement nameElementRows = propertyElementRows.addChildElement("name");
				nameElementRows.addTextNode("clientRowsPerPage");
				SOAPElement valueElement = propertyElementRows.addChildElement("value");
				valueElement.addTextNode(this.getRowsPerPage().toString());
			}

			if (DEBUG_ENABLED)
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				SOAPMessage soapMessage = soapMessageContext.getMessage();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBContextSOAPHandler.processRequest() - UPDATED MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
				logger.debug("ESBContextSOAPHandler.processRequest(soapMessageContext):EXIT => true");
			}
//			logger.debug("(OPTMIZATION) ESBContextSOAPHandler.processRequest=" + (System.currentTimeMillis() - startTime) + "ms");
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
		return true;
	}

	/**
	 * Process the incoming response
	 * 
	 * @param soapMessageContext
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean processResponse(SOAPMessageContext soapMessageContext)
	{
		logger.debug("ESBContextSOAPHandler.processResponse():ENTRY");
		try
		{
//			long startTime = System.currentTimeMillis();
			SOAPMessage soapMessage = soapMessageContext.getMessage();
			if (DEBUG_ENABLED)
			{
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				soapMessage.writeTo(byteArrayOutputStream);
				logger.debug("ESBContextSOAPHandler.processResponse() - RESPONSE MESSAGE =>" + new String(byteArrayOutputStream.toByteArray()));
			}
			// TODO grab the following values
			// <ns5:CtxProps>
			// <ns5:Property>
			// <ns5:name>responseTotalRows</ns5:name>
			// <ns5:value>11200</ns5:value>
			// </ns5:Property>
			// <ns5:Property>
			// <ns5:name>requestTotalPages</ns5:name>
			// <ns5:value>225</ns5:value>
			// </ns5:Property>
			// </ns5:CtxProps>
			Iterator<SOAPElement> contextIterator = soapMessage.getSOAPHeader().getChildElements(new QName("http://wellpoint.com/esb/context", "Context"));
			while (contextIterator.hasNext())
			{
				SOAPElement contextElement = contextIterator.next();
				Iterator<SOAPElement> ctxPropsIterator = contextElement.getChildElements(new QName("http://wellpoint.com/esb/context", "CtxProps"));
				while (ctxPropsIterator.hasNext())
				{
					SOAPElement ctxPropsElement = ctxPropsIterator.next();
					Iterator<SOAPElement> propertyIterator = ctxPropsElement.getChildElements(new QName("http://wellpoint.com/esb/context", "Property"));
					while (propertyIterator.hasNext())
					{
						SOAPElement propertyElement = propertyIterator.next();
						String name = null;
						String value = null;
						Iterator<SOAPElement> nameIterator = propertyElement.getChildElements(new QName("http://wellpoint.com/esb/context", "name"));
						while (nameIterator.hasNext())
						{
							SOAPElement nameElement = nameIterator.next();
							name = nameElement.getValue();
						}
						Iterator<SOAPElement> valueIterator = propertyElement.getChildElements(new QName("http://wellpoint.com/esb/context", "value"));
						while (valueIterator.hasNext())
						{
							SOAPElement valueElement = valueIterator.next();
							value = valueElement.getValue();
						}

						if ((name != null) && (value != null))
						{
							if (name.equals("responseTotalRows"))
							{
								try
								{
									Long longValue = Long.parseLong(value);
									this.setTotalRows(longValue);
								}
								catch (NumberFormatException e)
								{
									e.printStackTrace();
								}
							}
							if (name.equals("requestTotalPages"))
							{
								try
								{
									Long longValue = Long.parseLong(value);
									this.setTotalPages(longValue);
								}
								catch (NumberFormatException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
//			logger.debug("(OPTMIZATION) ESBContextSOAPHandler.processResponse=" + (System.currentTimeMillis() - startTime) + "ms");
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		logger.debug("ESBContextSOAPHandler.processResponse():EXIT");
		return true;
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
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion()
	{
		return serviceVersion;
	}

	/**
	 * @param serviceVersion
	 *            the serviceVersion to set
	 */
	public void setServiceVersion(String serviceVersion)
	{
		this.serviceVersion = serviceVersion;
	}

	/**
	 * @return the operationName
	 */
	public String getOperationName()
	{
		return operationName;
	}

	/**
	 * @param operationName
	 *            the operationName to set
	 */
	public void setOperationName(String operationName)
	{
		this.operationName = operationName;
	}

	/**
	 * @return the operationVersion
	 */
	public String getOperationVersion()
	{
		return operationVersion;
	}

	/**
	 * @param operationVersion
	 *            the operationVersion to set
	 */
	public void setOperationVersion(String operationVersion)
	{
		this.operationVersion = operationVersion;
	}

	/**
	 * @return the sender
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender)
	{
		this.sender = sender;
	}

	/**
	 * @return the clientRequestId
	 */
	public String getClientRequestId()
	{
		return clientRequestId;
	}

	/**
	 * @param clientRequestId
	 *            the clientRequestId to set
	 */
	public void setClientRequestId(String clientRequestId)
	{
		this.clientRequestId = clientRequestId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType()
	{
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType)
	{
		this.messageType = messageType;
	}

	/**
	 * @return the pageNo
	 */
	public Long getPageNo()
	{
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the pageNo to set
	 */
	public void setPageNo(Long pageNo)
	{
		this.pageNo = pageNo;
	}

	/**
	 * @return the rowsPerPage
	 */
	public Long getRowsPerPage()
	{
		return rowsPerPage;
	}

	/**
	 * @param rowsPerPage
	 *            the rowsPerPage to set
	 */
	public void setRowsPerPage(Long rowsPerPage)
	{
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @return the totalRows
	 */
	public Long getTotalRows()
	{
		return totalRows;
	}

	/**
	 * @param totalRows
	 *            the totalRows to set
	 */
	public void setTotalRows(Long totalRows)
	{
		this.totalRows = totalRows;
	}

	/**
	 * @return the totalPages
	 */
	public Long getTotalPages()
	{
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(Long totalPages)
	{
		this.totalPages = totalPages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ESBContextSOAPHandler [serviceName=" + serviceName + ", serviceVersion=" + serviceVersion + ", operationName=" + operationName
				+ ", operationVersion=" + operationVersion + ", sender=" + sender + ", clientRequestId=" + clientRequestId + ", transactionId=" + transactionId
				+ ", messageType=" + messageType + ", pageNo=" + pageNo + ", rowsPerPage=" + rowsPerPage + ", totalRows=" + totalRows + ", totalPages="
				+ totalPages + ", logger=" + logger + "]";
	}

}
