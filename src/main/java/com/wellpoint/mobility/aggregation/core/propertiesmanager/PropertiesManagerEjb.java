package com.wellpoint.mobility.aggregation.core.propertiesmanager;

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


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.persistence.domain.Properties;




/**
 * Manage interaction with the Properties object.
 * 
 * @author frank.garber@wellpoint.com
 *
 */
@Stateless
public class PropertiesManagerEjb
{
	
	/**
	 * Logger instance for all logging
	 */
	protected static final Logger LOGGER = Logger.getLogger(PropertiesManagerEjb.class);
	/**
	 * Used for checking if 'debug' logging should be performed.
	 */
	protected static final boolean IS_DEBUG_ENABLE = LOGGER.isDebugEnabled();
	/**
	 * Used for checking if 'info' logging should be performed.
	 */
	protected static final boolean IS_INFO_ENABLE = LOGGER.isInfoEnabled();

	/**
	 * The entityManager will be injected and is used for all persistence calls.
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	transient private EntityManager entityManager;

	/**
	 * Empty default ctor. DO NOT USE DIRECTLY. TO USE THIS CLASS USE INJECTION
	 */
	public PropertiesManagerEjb() {
	}
	
	
	/**
	 * Does a lookup for a property with the given key. 
	 * @param key non-null value used for the lookup
	 * @return a Properties object or null if not found
	 */
	public Properties getPropertiesByKey(final String key) {
		final Query namedQuery = entityManager.createNamedQuery("Properties.get.by.key");
		Properties property = null;
		try
		{
			namedQuery.setParameter("key", key);
			property = (Properties) namedQuery.getSingleResult();
		}
		catch (NoResultException e)
		{
			if (IS_DEBUG_ENABLE)
			{
				LOGGER.debug("PropertiesManagerEjb.getProperties(): no property found for key=" + key);
			}
		}
		
		return property;
		
	} // of getProperties
	
	
	/**
	 * Creates and saves a new Properties object.
	 * @param key non-null key value
	 * @param value non-null value
	 * @return a persisted Properties object
	 */
	public Properties createProperty(final String key, final String value) {
		final Properties properties = new Properties();
		properties.setKey(key);
		properties.setValue(value);
		entityManager.persist(properties);
		return properties;
	} // of createProperty

	
	/**
	 * Updates an existing Properties object.
	 */
	public void updateProperty(final Properties properties) {
		entityManager.merge(properties);
	} // of updateProperty
	

} // of class BSMManagerEjb
