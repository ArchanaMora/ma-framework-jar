package com.wellpoint.mobility.aggregation.core.configuration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({  TestPersistConfiguration.class, TestMergeConfiguration.class, TestLoadConfiguration.class})
public class ConfigurationManagerTestSuite {
}
