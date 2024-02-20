package com.intuit.karate.graal;

import java.math.BigInteger;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.intuit.karate.BranchDataStructure;

import org.graalvm.polyglot.Value;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

/**
 *
 * @author pthomas3
 */
public class JsValueTest {
    @BeforeAll
    static void beforeAll() {
        new BranchDataStructure(27, "config-test");
    }

    @AfterAll
    static void afterAll() {
        // Logging the coverage result once all tests ran
        BranchDataStructure.instances.get("config-test").logResults();
    }

    @Test
    void testTruthy() {
        assertFalse(JsValue.isTruthy(null));
        assertFalse(JsValue.isTruthy(false));
        assertFalse(JsValue.isTruthy(Boolean.FALSE));
        assertTrue(JsValue.isTruthy(true));
        assertTrue(JsValue.isTruthy(Boolean.TRUE));
        assertFalse(JsValue.isTruthy(0));
        assertFalse(JsValue.isTruthy(0.0));
        assertFalse(JsValue.isTruthy(BigInteger.ZERO));
        assertTrue(JsValue.isTruthy(1));
        assertTrue(JsValue.isTruthy(1.0));        
        assertTrue(JsValue.isTruthy(BigInteger.ONE));
        assertTrue(JsValue.isTruthy(""));
        assertTrue(JsValue.isTruthy(Collections.emptyList()));
        assertTrue(JsValue.isTruthy(Collections.emptyMap()));
    }
}
