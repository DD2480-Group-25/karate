package com.intuit.karate.http;

import com.intuit.karate.BranchDataStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;



class RequestTest {
    static Request request;

    // === TESTS ADDED TO IMPROVE COVERAGE ===
    // comment them out to see the before/after difference

    @BeforeEach
    @Test
    void setup() {
        request = new Request();
    }
    
    // Requirement: the method needs to be set up correctly
    @Test
    void testMethod() {
        request.setMethod("test");
        assertEquals("test", request.getMember("method"));
    }

    // Requirement: the body needs to be set up correctly
    @Test
    void testBodyAsString() {
        byte[] body = "banana".getBytes();
        request.setBody(body);
        assertEquals("banana", request.getMember("bodyString"));
    }
    
    // Requirement: the body needs to be set up correctly
    @Test
    void testBodyAsBytes() {
        byte[] body = "banana".getBytes();
        request.setBody(body);
        assertEquals(body, request.getMember("bodyBytes"));
    }

    // Requirement: the URL base needs to be set up correctly
    @Test
    void testUrlBase() {
        request.setUrlBase("http://banana.com");
        assertEquals("http://banana.com", request.getMember("urlBase"));
    }

    // Requirement: the path needs to be set up correctly
    @Test
    void testPath() {
        request.setPath("thepath");
        assertEquals("/thepath", request.getMember("path"));
    }

    // Requirement: the input needs to be set up correctly
    @Test
    void testBadInput() {
        assertNull(request.getMember("banana"));
    }

    // Requirement: the start time needs to be set up correctly
    @Test
    void testStartTime(){
        request.setStartTime(100);
        assertEquals(100L, request.getMember("startTime"));
    }

    // Requirement: the end time needs to be set up correctly
    @Test
    void testEndTime(){
        request.setEndTime(200);
        assertEquals(200L, request.getMember("endTime"));
    }


    // === END OF ADDED TESTS ===

    @Test
    void logCoverageResult() {
        BranchDataStructure bds = new BranchDataStructure(26, "getmember");
        bds.logResults();
    }

    @Test
    @Disabled
    void delete_file() {
        BranchDataStructure bds = new BranchDataStructure(26, "getmember");
        bds.delete_file();
    }
}
