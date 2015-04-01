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
package com.wellpoint.mobility.aggregation.core.mapofblobmanager;



import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.wellpoint.mobility.aggregation.core.utilities.Utils;
import com.wellpoint.mobility.aggregation.persistence.domain.MapOfBlob;



/**
 * Manage interaction with the Properties object.
 * 
 * @author frank.garber@wellpoint.com
 *
 */
@Stateless
public class MapOfBlobManagerEjb
{
	
	/**
	 * Logger instance for all logging
	 */
	protected static final Logger logger = Logger.getLogger(MapOfBlobManagerEjb.class);
	/**
	 * Used for checking if 'debug' logging should be performed.
	 */
	protected static final boolean DEBUG_LOGGER_ENABLED = logger.isDebugEnabled();
	/**
	 * Used for checking if 'info' logging should be performed.
	 */
	protected static final boolean INFO_LOGGER_ENABLE = logger.isInfoEnabled();

	/**
	 * The entityManager will be injected and is used for all persistence calls.
	 */
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	transient private EntityManager entityManager;

	/**
	 * Empty default ctor. DO NOT USE DIRECTLY. TO USE THIS CLASS USE INJECTION
	 */
	public MapOfBlobManagerEjb() {
	}
	
	
	/**
	 * Does a lookup for a Blob with the given key and the latest version number. 
	 * @param key non-null value used for the lookup
	 * @return a MapOfBlob object or null if not found
	 */
	public MapOfBlob getBlobByKey(final String key) {
		final Query namedQuery = entityManager.createNamedQuery("MapOfBlob.get.by.key.latestVersion");
		MapOfBlob mob = null;
		try
		{
			namedQuery.setParameter("key", key);
			mob = (MapOfBlob) namedQuery.getSingleResult();
		}
		catch (NoResultException e)
		{
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("MapOfBlobManagerEjb.getBlobByKey(): no property found for key=" + key);
			}
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("MapOfBlobManagerEjb.getBlobByKey(): mob=" + mob);
		}
		return mob;
		
	} // of getBlobByKey
	
	
	/**
	 * Does a lookup for a Blob with the given key and the version number. 
	 * @param key non-null value used for the lookup
	 * @return a MapOfBlob object or null if not found
	 */
	public MapOfBlob getBlobByKeyAndVersion(final String key, final Long version) {
		final Query namedQuery = entityManager.createNamedQuery("MapOfBlob.get.by.key.and.version");
		MapOfBlob mob = null;
		try
		{
			namedQuery.setParameter("key", key).setParameter("version", version);
			mob = (MapOfBlob) namedQuery.getSingleResult();
		}
		catch (NoResultException e)
		{
			if (INFO_LOGGER_ENABLE)
			{
				logger.info("MapOfBlobManagerEjb.getBlobByKeyAndVersion(): no property found for key=" + key + ", version=" + version);
			}
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("MapOfBlobManagerEjb.getBlobByKeyAndVersion(): mob=" + mob);
		}
		
		return mob;
		
	} // of getBlobByKeyAndVersion
	
	
	/**
	 * Creates and saves a new MapOfBlob object. The version will be set to +1 of any
	 * existing with the same 'key'.
	 * @param key non-null key value
	 * @param value non-null value
	 * @param createdBy the name stored as the creator(don't get a big head now...) / updater.
	 * @return a persisted Properties object
	 */
	public MapOfBlob createMapOfBlob(final String key, final byte[] value, final String createdBy) {
		MapOfBlob blobByKey = getBlobByKey(key);
		final MapOfBlob mob = new MapOfBlob();
		mob.setTheKey(key);
		mob.setValue(value);
		mob.setVersion(blobByKey == null ? 1l : blobByKey.getVersion() + 1l); // add 1 (one)
		mob.setAuditFieldsOnCreation(createdBy);
		entityManager.persist(mob);
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("MapOfBlobManagerEjb.createMapOfBlob(): existing mob=" + blobByKey + ", new blob=" + mob);
		}
		
		return mob;
	} // of createMapOfBlob


	/**
	 * Updates an existing MapOfBlob object.
	 */
	public void updateMapOfBlob(final MapOfBlob mob, final String updatedBy) {
		mob.setAuditFieldsOnUpdate(updatedBy);
		entityManager.merge(mob);
	} // of updateMapOfBlob
	

	/**
	 * Updates an existing MapOfBlob object.
	 */
	public void updateMapOfBlobAsNewVersion(final MapOfBlob mob, final String createdBy) {
		// Load the latest and add one to the version.
		MapOfBlob blobByKey = getBlobByKey(mob.getTheKey());
		Long newVersion = blobByKey == null ? 1l : (blobByKey.getVersion() + 1l);
		if (newVersion == 1) {
			mob.setVersion(newVersion);
			mob.setAuditFieldsOnCreation(createdBy);
			entityManager.persist(mob);
		} else {
			entityManager.detach(mob);
			mob.setVersion(newVersion);
			mob.setAuditFieldsOnCreation(createdBy);
			mob.setId(null);
			entityManager.persist(mob);
		}

		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("MapOfBlobManagerEjb.updateMapOfBlobAsNewVersion(): mob=" + mob);
		}
		
		
	} // of updateMapOfBlob
	
	
	/**
	 * Performs a 'soft' search of the entity using the provided 'key'. 
	 * @param key used for the search. See the actual NamedQuery for details
	 * @return null or a valid object.
	 */
	@SuppressWarnings("unchecked")
	public List<MapOfBlob> getBlobLikeKey(final String key) {
		final Query namedQuery = entityManager.createNamedQuery("MapOfBlob.get.by.like.key");
		List<MapOfBlob> mob = null;
		try
		{
			namedQuery.setParameter("key", "%" + key + "%");
			mob = (List<MapOfBlob>) namedQuery.getResultList();
		}
		catch (NoResultException e)
		{
			if (DEBUG_LOGGER_ENABLED)
			{
				logger.debug("MapOfBlobManagerEjb.getBlobLikeKey(): no property found for key=" + key);
			}
		}
		
		if (DEBUG_LOGGER_ENABLED)
		{
			logger.debug("MapOfBlobManagerEjb.getBlobLikeKey(): mob=" + mob);
		}
		
		return mob;
		
	} // of getBlobLikeKey
	
	
	/**
	 * Delete all the entities for the given ids.
	 * @param idsForDeletion a non-null list of ids for deletion
	 */
	public int deleteForIds(final List<Long> idsForDeletion) {
		final Query namedQuery = entityManager.createNamedQuery("MapOfBlob.delete.by.idList");
		int deleteCount = -1;
		try
		{
			deleteCount = namedQuery.setParameter("IdList", idsForDeletion).executeUpdate();
			if (deleteCount != idsForDeletion.size()) {
				logger.error("MapOfBlobManagerEjb.deleteForIds(): Expected to delete " + idsForDeletion.size() + ", actually deleted " + deleteCount + ". Id List=" + idsForDeletion.toString());
			} else {
				logger.info("MapOfBlobManagerEjb.deleteForIds(): Deleted " + deleteCount + " entities.");
			}
		}
		catch (Exception e)
		{
			logger.error("MapOfBlobManagerEjb.deleteForIds(): error during deletion.", e);
		}
		
		return deleteCount;
		
	} // of deleteForIds
	
	
	
	/**
	 * Calls getBlobByKey(key) to load the object and then attempts to rehydrate the array of bytes
	 * using the ObjectInputStream().readObject() method. Callers should downcast the returned object
	 * to the expected type.
	 * 
	 * @param key the key used to store the blob.
	 * @return an object or null if the blob could not be found
	 */
	public Object getBlobByKeyAsObject(final String key) {
		final MapOfBlob mob = getBlobByKey(key);
		Object returnObject = null;
		if (mob != null) {
			try {
				returnObject = Utils.toObject(mob.getValue());
			}
			catch (Exception e) {
				logger.error("MapOfBlobManagerEjb.getBlobByKeyAsObject(): An error was encountered while trying to rehydrate the object for key=" + key);
			}
		}
		
		return returnObject;
		
	} // of getBlobByKeyAsObject
	
	
} // of class
