package com.intuit.karate.core;

import com.intuit.karate.BranchDataStructure;
import com.intuit.karate.LogAppender;
import com.intuit.karate.Logger;
import com.intuit.karate.core.Config;
import com.intuit.karate.core.DummyClient;
import com.intuit.karate.core.MockHandler;
import com.intuit.karate.core.Variable;
import com.intuit.karate.shell.StringLogAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.intuit.karate.TestUtils.FeatureBuilder;
import static com.intuit.karate.TestUtils.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

/**
 * Test body and content type handling for request and response logging.
 * @author edwardsph
 */
class ConfigTest {
    Config config;

    @BeforeEach
    void beforeEach() {
        config = new Config();
    }

    @BeforeAll
    static void beforeAll() {
        new BranchDataStructure(60, "config-test");
    }

    @AfterAll
    static void afterAll() {
        // Logging the coverage result once all tests ran
        BranchDataStructure.instances.get("config-test").logResults();
    }

    // Requirement: the url needs to be set correctly
    @Test
    void testConfigureUrl() {        
        config.configure("url", new Variable("www.site.com"));
        assertEquals("www.site.com", config.getUrl());
    }

    // Requirement: the responseHeaders needs to be set correctly
    @Test
    void testConfigureResponseHeaders() {
        config.configure("responseHeaders", new Variable("header"));
        assertEquals("header", config.getResponseHeaders().getValue());
    }


    // Requirement: the nmtlAuth configuration should correctly unwrap the input Map
    @Test
    void testConfigureNtlmAuth1() {
        Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("username", "dummyUsername");
        dummyMap.put("password", "dummyPassword");
        dummyMap.put("domain", "dummyDomain");
        dummyMap.put("workstation", "dummyWorkstation");
        config.configure("ntlmAuth", new Variable(dummyMap));
        assertEquals("dummyDomain", config.getNtlmDomain());
    }

    // Requirement: the ntlmAuth configuration should disable ntlm if the map is Null
    @Test
    void testConfigureNtlmAuth2() {
        config.configure("ntlmAuth", new Variable(null));
        assertEquals(null, config.getNtlmDomain());
    }

    // Requirement: the responseHeaders needs to be set correctly
    @Test
    void testLocalAddress() {
        config.configure("localAddress", new Variable("address"));
        assertEquals("address", config.getLocalAddress());
    }

    // Requirement: the lowerCaseResponseHeaders needs to be set correctly
    @Test
    void testLowerCaseResponseHeaders() {
        config.configure("lowerCaseResponseHeaders", new Variable(true));
        assertTrue(config.isLowerCaseResponseHeaders());
    }

    // Requirement: the printEnabled needs to be set correctly
    @Test
    void testPrintEnabled() {
        config.configure("printEnabled", new Variable(true));
        assertTrue(config.isPrintEnabled());
    }
    
    // Requirement: the pauseIfNotPerf needs to be set correctly
    @Test
    void testPauseIfNotPerf() {
        config.configure("pauseIfNotPerf", new Variable(true));
        assertTrue(config.isPauseIfNotPerf());
    }
}
