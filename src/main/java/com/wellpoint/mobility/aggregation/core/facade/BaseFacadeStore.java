package com.wellpoint.mobility.aggregation.core.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.facade.endpoints.ESBEndPoint;
import com.wellpoint.mobility.aggregation.core.facade.env.ESBServiceEnv;
import com.wellpoint.mobility.aggregation.core.facade.esbcontext.ESBContextHeader;
import com.wellpoint.mobility.aggregation.core.facade.esbheader.ESBHeader;
import com.wellpoint.mobility.aggregation.core.facade.esbsecurity.ESBSecurityHeader;
import com.wellpoint.mobility.aggregation.persistence.domain.ServiceEndPoint;
import com.wellpoint.mobility.aggregation.persistence.domain.ServiceEnv;
import com.wellpoint.mobility.aggregation.persistence.domain.ServiceEsbContext;
import com.wellpoint.mobility.aggregation.persistence.domain.ServiceEsbHeader;
import com.wellpoint.mobility.aggregation.persistence.domain.ServiceEsbSecurity;

@Stateless
public class BaseFacadeStore
{
	/**
	 *  entityManager
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
	/**
	 * Logger
	 */
	private Logger logger = Logger.getLogger(BaseFacadeStore.class);
	
	/**
	 * Obtain the ESBContextHeader info from the DB for the given service, method and env combination
	 * @return
	 */
	public ESBContextHeader getESBContextData(String service, String method, String env)
	{
		Object obj = null;
		ESBContextHeader esbContextHeader = null;
		try
		{	
			Query query = entityManager
					.createQuery(
							"select serviceEsbContextDetails from ServiceEsbContext serviceEsbContextDetails where " +
							"serviceEsbContextDetails.serviceName =:service " +
							"and serviceEsbContextDetails.serviceMethod =:method " +
							"and serviceEsbContextDetails.env =:env")
					.setParameter("service", service).setParameter("method", method).setParameter("env", env);
			
			if (query.getResultList() != null && !query.getResultList().isEmpty())
			{
				obj = query.getResultList().get(0);
			}

			if (obj != null)
			{
				logger.debug("Obtained the service esb context details");
				esbContextHeader = (ESBContextHeader) mapTODTO((ServiceEsbContext) obj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return esbContextHeader;
	}
	
	/**
	 * Obtain the ESBSecurityHeader info from the DB for the given service, method and env combination
	 * @return
	 */
	public ESBSecurityHeader getESBSecurityData(String service, String method, String env)
	{
		Object obj = null;
		ESBSecurityHeader esbSecurityHeader = null;
		try
		{	
			Query query = entityManager
					.createQuery(
							"select serviceEsbSecurityDetails from ServiceEsbSecurity serviceEsbSecurityDetails where " +
							"serviceEsbSecurityDetails.serviceName =:service " +
							"and serviceEsbSecurityDetails.serviceMethod =:method " +
							"and serviceEsbSecurityDetails.env =:env")
					.setParameter("service", service).setParameter("method", method).setParameter("env", env);
			
			if (query.getResultList() != null && !query.getResultList().isEmpty())
			{
				obj = query.getResultList().get(0);
			}

			if (obj != null)
			{
				logger.debug("Obtained the service esb security details");
				esbSecurityHeader = (ESBSecurityHeader) mapTODTO((ServiceEsbSecurity) obj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return esbSecurityHeader;
	}
	
	/**
	 * Obtain the ESBEndPoint info from the DB for the given service, method and env combination
	 * @return
	 */
	public ESBEndPoint getServiceEndPoint(String service, String method, String env)
	{
		Object obj = null;
		ESBEndPoint esbEndPoint = null;
		try
		{	
			logger.debug("service::" + service + " method::" + method + " env::" + env);
			Query query = entityManager
					.createQuery(
							"select serviceEndPointDetails from ServiceEndPoint serviceEndPointDetails where " +
							"serviceEndPointDetails.serviceName =:service " +
							"and serviceEndPointDetails.serviceMethod =:method " +
							"and serviceEndPointDetails.env =:env")
					.setParameter("service", service).setParameter("method", method).setParameter("env", env);
						
			if (query.getResultList() != null && !query.getResultList().isEmpty())
			{
				logger.debug("Obtained the service end point details");
				obj = query.getResultList().get(0);
			}

			if (obj != null)
			{
				logger.debug("ServiceEndPoint mapping to ESBEndPoint");
				esbEndPoint = (ESBEndPoint) mapTODTO((ServiceEndPoint) obj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return esbEndPoint;
	}
	
	/**
	 * Obtain the ESBHeader info from the DB for the given service, method and env combination
	 * @return
	 */
	public ESBHeader getESBHeader(String service, String method, String env)
	{
		Object obj = null;
		ESBHeader esbEndPoint = null;
		try
		{	
			logger.debug("service::" + service + " method::" + method + " env::" + env);
			Query query = entityManager
					.createQuery(
							"select serviceEsbHeaderDetails from ServiceEsbHeader serviceEsbHeaderDetails where " +
							"serviceEsbHeaderDetails.serviceName =:service " +
							"and serviceEsbHeaderDetails.serviceMethod =:method " +
							"and serviceEsbHeaderDetails.env =:env")
					.setParameter("service", service).setParameter("method", method).setParameter("env", env);
						
			if (query.getResultList() != null && !query.getResultList().isEmpty())
			{
				logger.debug("Obtained the service esb header details");
				obj = query.getResultList().get(0);
			}

			if (obj != null)
			{
				logger.debug("ServiceEsbHeader mapping to ESBHeader");
				esbEndPoint = (ESBHeader) mapTODTO((ServiceEsbHeader) obj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return esbEndPoint;
	}
	

	/**
	 * Obtain the environment for the given service
	 * @return
	 */
	public String getEnvironment(String service)
	{
		Object obj = null;
		ESBServiceEnv esbServiceEnv = null;
		String envValue = null;
		try
		{	
			Query query = entityManager
					.createQuery(
							"select serviceEnvDetails from ServiceEnv serviceEnvDetails where " +
							"serviceEnvDetails.serviceName =:service ")
					.setParameter("service", service);
			
			if (query.getResultList() != null && !query.getResultList().isEmpty())
			{
				obj = query.getResultList().get(0);
			}

			if (obj != null)
			{
				logger.debug("Obtained the service esb security details");
				esbServiceEnv = (ESBServiceEnv) mapTODTO((ServiceEnv) obj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (esbServiceEnv !=null)
		{
			envValue = esbServiceEnv.getEnv();
		}
		
		return envValue;
	}
	
	/**
	 * Generic function to convert entity to dto
	 * @param entityObject
	 * @return
	 */
	private Object mapTODTO(Object entityObject)
	{
		Object dtoObject = new Object();
		
		// ServiceEsbSecurity to ESBSecurityHeader
		if (entityObject instanceof ServiceEsbSecurity)
		{
			logger.debug("ServiceESBSecurity instance..");
			ESBSecurityHeader esbSecurityHeader = new ESBSecurityHeader();
			esbSecurityHeader.setPassword(((ServiceEsbSecurity) entityObject).getPassword());
			esbSecurityHeader.setUserName(((ServiceEsbSecurity) entityObject).getUserName());
			esbSecurityHeader.setServiceMethod(((ServiceEsbSecurity) entityObject).getServiceMethod());
			esbSecurityHeader.setServiceName(((ServiceEsbSecurity) entityObject).getServiceName());
			esbSecurityHeader.setNonce(((ServiceEsbSecurity) entityObject).getNonce());
			esbSecurityHeader.setCreated(((ServiceEsbSecurity) entityObject).getCreated());
			esbSecurityHeader.setEnv(((ServiceEsbSecurity) entityObject).getEnv());
			dtoObject = esbSecurityHeader;
		}
		
		// ServiceEndPoint to ESBEndPoint
		else if (entityObject instanceof ServiceEndPoint)
		{
			logger.debug("ServiceEndPoint instance..");
			ESBEndPoint esbEndPoint = new ESBEndPoint();
			esbEndPoint.setEnv(((ServiceEndPoint) entityObject).getEnv());
			esbEndPoint.setServiceMethod(((ServiceEndPoint) entityObject).getServiceMethod());
			esbEndPoint.setServiceName(((ServiceEndPoint) entityObject).getServiceName());
			esbEndPoint.setServiceUrl(((ServiceEndPoint) entityObject).getServiceUrl());
			dtoObject = esbEndPoint;
		}
		
		// ServiceEsbContext to ESBContextHeader
		else if (entityObject instanceof ServiceEsbContext)
		{
			logger.debug("ServiceEsbContext instance..");
			ESBContextHeader esbContextHeader = new ESBContextHeader();
			esbContextHeader.setClientRequestId(((ServiceEsbContext) entityObject).getClientRequestId());
			esbContextHeader.setEnv(((ServiceEsbContext) entityObject).getEnv());
			esbContextHeader.setMessageType(((ServiceEsbContext) entityObject).getMessageType());
			esbContextHeader.setOperationName(((ServiceEsbContext) entityObject).getOperationName());
			esbContextHeader.setOperationVersion(((ServiceEsbContext) entityObject).getOperationVersion());
			esbContextHeader.setSenderApp(((ServiceEsbContext) entityObject).getSenderApp());
			esbContextHeader.setServiceContext(((ServiceEsbContext) entityObject).getServiceContextName());
			esbContextHeader.setServiceMethod(((ServiceEsbContext) entityObject).getServiceMethod());
			esbContextHeader.setServiceName(((ServiceEsbContext) entityObject).getServiceName());
			esbContextHeader.setServiceVersion(((ServiceEsbContext) entityObject).getServiceVersion());
			esbContextHeader.setTransactionId(((ServiceEsbContext) entityObject).getTransactionId());
			dtoObject = esbContextHeader;
		}
	
		// ServiceEnv to ESBServiceEnv
		else if (entityObject instanceof ServiceEnv)
		{
			logger.debug("ServiceEnv instance..");
			ESBServiceEnv esbServiceEnv = new ESBServiceEnv();
			esbServiceEnv.setEnv(((ServiceEnv) entityObject).getEnv());
			esbServiceEnv.setServiceName(((ServiceEnv) entityObject).getServiceName());
			dtoObject = esbServiceEnv;
		}
		
		// ServiceEsbContext to ESBContextHeader
		else if (entityObject instanceof ServiceEsbHeader)
		{
			logger.debug("ServiceEsbHeader instance..");
			ESBHeader esbHeader = new ESBHeader();
			esbHeader.setEnv(((ServiceEsbHeader) entityObject).getEnv());
			esbHeader.setServiceMethod(((ServiceEsbHeader) entityObject).getServiceMethod());
			esbHeader.setServiceName(((ServiceEsbHeader) entityObject).getServiceName());
			esbHeader.setProperties(((ServiceEsbHeader) entityObject).getProperties());
			esbHeader.setPropertiesControl(((ServiceEsbHeader) entityObject).getPropertiesControl());
			esbHeader.setRoutingVersion(((ServiceEsbHeader) entityObject).getRoutingVersion());
			esbHeader.setRoutingFieldName(((ServiceEsbHeader) entityObject).getRoutingFieldName());
			esbHeader.setRoutingFieldValue(((ServiceEsbHeader) entityObject).getRoutingFieldValue());
			esbHeader.setRoutingControl(((ServiceEsbHeader) entityObject).getRoutingControl());
			dtoObject = esbHeader;
		}
		
		
		return dtoObject;
	}
}