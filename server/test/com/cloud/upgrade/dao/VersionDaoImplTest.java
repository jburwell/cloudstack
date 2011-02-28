/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.cloud.upgrade.dao;


import javax.naming.ConfigurationException;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.cloud.upgrade.dao.VersionVO.Step;
import com.cloud.utils.component.ComponentLocator;
import com.cloud.utils.db.DbTestUtils;

public class VersionDaoImplTest extends TestCase {
    private static final Logger s_logger = Logger.getLogger(VersionDaoImplTest.class);

    @Override
    @Before
    public void setUp() throws Exception {
        VersionVO version = new VersionVO("2.1.7");
        version.setStep(Step.Cleanup);
        DbTestUtils.executeScript("VersionDaoImplTest/clean-db.sql", false, true);
    }
    
    @Override
    @After
    public void tearDown() throws Exception {
    }
    
    public void test217to22Upgrade() {
        s_logger.debug("Finding sample data from 2.1.7");
        DbTestUtils.executeScript("VersionDaoImplTest/2.1.7/2.1.7.sample.sql", false, true);

        VersionDaoImpl dao = ComponentLocator.inject(VersionDaoImpl.class);
        
        String version = dao.getCurrentVersion();
        assert version.equals("2.1.7") : "Version returned is not 2.1.7 but " + version;
        
        try {
            dao.upgrade("2.1.7", "2.2.1");
        } catch (ConfigurationException e) {
            s_logger.warn("Exception: ", e);
            assert false : "The test failed.  Check logs"; 
        }
    }
    
}
