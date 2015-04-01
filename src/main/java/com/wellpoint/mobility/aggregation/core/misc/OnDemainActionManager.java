/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * 
 */
package com.wellpoint.mobility.aggregation.core.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.wellpoint.mobility.aggregation.core.misc.pojo.OnDemandActionPojo;
import com.wellpoint.mobility.aggregation.persistence.domain.OnDemandAction;

/**
 * 
 * @author frank.garber@anthem.com
 *
 */
@Stateless
public class OnDemainActionManager {
	
	@PersistenceContext(unitName = "persistenceUnit", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<OnDemandAction> loadByAction(final OnDemandAction.ACTION action) {
		return (List<OnDemandAction>) entityManager.createNamedQuery("OnDemandAction.get.by.action")
			.setParameter("action", action).getResultList();
	} // of loadByAction
	
	
	public OnDemandAction save(final OnDemandAction onDemandAction) {
		if (onDemandAction.getId() == null) {
			onDemandAction.setAuditFieldsOnCreation("SYSTEM");
			entityManager.persist(onDemandAction);
		}
		else {
			onDemandAction.setAuditFieldsOnUpdate("SYSTEM");
			entityManager.merge(onDemandAction);
		}
		
		return onDemandAction;
		
	} // of save

	
	public Collection<OnDemandAction> save(final Collection<OnDemandAction> onDemandActionCollection) {
		for (OnDemandAction onDemandAction : onDemandActionCollection) {
			save(onDemandAction);
		}
		
		return onDemandActionCollection;
		
	} // of save
	
	
	public OnDemandActionPojo toPoJo(final OnDemandAction onDemandAction) {
		final OnDemandActionPojo onDemandActionPojo = new OnDemandActionPojo();
		onDemandActionPojo.setAction(onDemandAction.getAction());
		onDemandActionPojo.setActionInfo(onDemandAction.getActionInfo());
		onDemandActionPojo.setId(onDemandAction.getId());
		onDemandActionPojo.setTakeAction(onDemandAction.shouldTakeAction());
		onDemandActionPojo.setTheKey(onDemandAction.getTheKey());
		
		return onDemandActionPojo;
	} // of toPoJo
	
	
	public List<OnDemandActionPojo> toPoJo(final Collection<OnDemandAction> onDemandActionCollection) {
		final ArrayList<OnDemandActionPojo> returnCollection = new ArrayList<OnDemandActionPojo>(onDemandActionCollection.size());
		for (final OnDemandAction onDemandAction : onDemandActionCollection) {
			returnCollection.add(toPoJo(onDemandAction));
		}
		
		return returnCollection;
	} // of toPoJo

	
	public OnDemandAction load(final Long id) {
		return entityManager.find(OnDemandAction.class, id);
	}
	
} // of OnDemainAction
