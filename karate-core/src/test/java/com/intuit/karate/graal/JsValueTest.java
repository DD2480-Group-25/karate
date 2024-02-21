package com.intuit.karate.graal;

import java.math.BigInteger;
import java.util.Collections;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import com.intuit.karate.BranchDataStructure;

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

    @Test
    void testIsNull() {
        // Test the isNull method
        Value nullValue = Value.asValue(null);
        JsValue jsValue = new JsValue(nullValue);
        assertTrue(jsValue.isNull());
    }

    @Test
    void testToJavaString() {
        // Test the toJava method with string
        Value stringValue = Value.asValue("Hello");
        Object javaObject = JsValue.toJava(stringValue);
        assertEquals("Hello", javaObject);
    }

    @Test
    void testToJavaInt() {
        // Test the toJava method with int
        Value intValue = Value.asValue(2);
        Object javaObject = JsValue.toJava(intValue);
        assertEquals(2, javaObject);
    }

    @Test
    void testToJavaBoolean() {
        // Test the toJava method with int
        Value intValue = Value.asValue(true);
        Object javaObject = JsValue.toJava(intValue);
        assertEquals(true, javaObject);
    }

    @Test
    void testToJavaArray() {
        // Test the toJava method with int
        int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
        Value intValue = Value.asValue(intArray);
        Object javaObject = JsValue.toJava(intValue);
        assertEquals(intArray, javaObject);
    }
}
