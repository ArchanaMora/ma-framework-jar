package com.wellpoint.mobility.aggregation.core.configuration.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*
 * Copyright (c) 2014 www.wellpoint.com.  All rights reserved.
 *
 * This program contains proprietary and confidential information and trade
 * secrets of Wellpoint. This program may not be duplicated, disclosed or
 * provided to any third parties without the prior written consent of
 * Wellpoint. Disassembling or decompiling of the software and/or reverse
 * engineering of the object code are prohibited.
 * 
 * @author AD48407
 *
 */
public class EntityManagerFactoryHelper {
	
    private static EntityManagerFactory factory;

    static {
        try {
        	factory = Persistence.createEntityManagerFactory("persistenceUnit");
        } catch(ExceptionInInitializerError e) {
        	e.printStackTrace();
            throw e;
        }
    }

    /**
     * Retrieves an EntityManagerFactory
     * @return static EntityManagerFactory
     */
    public static EntityManagerFactory getFactory() {
        return factory;
    }

}
