/*
 * The MIT License
 *
 * Copyright 2022 Karate Labs Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate.graal;

import com.intuit.karate.BranchDataStructure;
import com.intuit.karate.JsonUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.Proxy;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 *
 * @author pthomas3
 */
public class JsValue {

    private static final Logger logger = LoggerFactory.getLogger(JsValue.class);

    public static enum Type {
        OBJECT,
        ARRAY,
        FUNCTION,
        XML,
        NULL,
        OTHER
    }

    public static final JsValue NULL = new JsValue(Value.asValue(null));

    private final Value original;
    protected final Object value;
    public final Type type;

    public JsValue(Value v) {
        BranchDataStructure bds = new BranchDataStructure(27, "jsvalue");
        bds.setFlag(0);
        if (v == null) { // Branch 1
            bds.setFlag(1);
            throw new RuntimeException("JsValue() constructor argument has to be not-null");
        }
        this.original = v;
        try { // Branch 2
            bds.setFlag(2);
            if (v.isNull()) { // Branch 3
                bds.setFlag(3);
                value = null;
                type = Type.NULL;
            } else if (v.isHostObject()) { // Branch 4
                bds.setFlag(4);
                if (v.isMetaObject()) { // java.lang.Class ! Branch 5
                    bds.setFlag(5);
                    value = v; // special case, keep around as graal value
                } else { // Branch 6
                    bds.setFlag(6);
                    value = v.asHostObject();
                }
                type = Type.OTHER;
            } else if (v.isProxyObject()) { // Branch 7
                bds.setFlag(7);
                Object o = v.asProxyObject();
                if (o instanceof JsXml) { // Branch 8
                    bds.setFlag(8);
                    value = ((JsXml) o).getNode();
                    type = Type.XML;
                } else if (o instanceof JsMap) { // Branch 9
                    bds.setFlag(9);
                    value = ((JsMap) o).getMap();
                    type = Type.OBJECT;
                } else if (o instanceof JsList) { // Branch 10
                    bds.setFlag(10);
                    value = ((JsList) o).getList();
                    type = Type.ARRAY;
                } else if (o instanceof ProxyExecutable) { // Branch 11
                    bds.setFlag(11);
                    value = o;
                    type = Type.FUNCTION;
                } else { // e.g. custom bridge, e.g. Request Branch 12
                    bds.setFlag(12);
                    value = v.as(Object.class);
                    type = Type.OTHER;
                }
            } else if (v.hasArrayElements()) { // Branch 13
                bds.setFlag(13);
                int size = (int) v.getArraySize();
                List list = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    Value child = v.getArrayElement(i);
                    list.add(new JsValue(child).value);
                }
                value = list;
                type = Type.ARRAY;
            } else if (v.hasMembers()) { // Branch 14
                bds.setFlag(14);
                if (v.canExecute()) { // Branch 15
                    bds.setFlag(15);
                    if (v.canInstantiate()) { // Branch 16
                        bds.setFlag(16);
                        // js functions have members, can be executed and are instantiable
                        value = new JsFunction.Instantiable(v);
                    } else { // Branch 17
                        bds.setFlag(17);
                        // js, but anonymous / arrow function
                        value = new JsFunction.Executable(v);
                    }
                    type = Type.FUNCTION;
                } else { // Branch 18
                    bds.setFlag(18);
                    Set<String> keys = v.getMemberKeys();
                    Map<String, Object> map = new LinkedHashMap(keys.size());
                    for (String key : keys) {
                        Value child = v.getMember(key);
                        map.put(key, new JsValue(child).value);
                    }
                    value = map;
                    type = Type.OBJECT;
                }
            } else if (v.isNumber()) { // Branch 19
                bds.setFlag(19);
                value = v.as(Number.class);
                type = Type.OTHER;
            } else if (v.isBoolean()) { // Branch 20
                bds.setFlag(20);
                value = v.asBoolean();
                type = Type.OTHER;
            } else if (v.isString()) { // Branch 21
                bds.setFlag(21);
                value = v.asString();
                type = Type.OTHER;
            } else { // Branch 22
                bds.setFlag(22);
                value = v.as(Object.class);
                if (value instanceof Function) { // Branch 23
                    bds.setFlag(23);
                    type = Type.FUNCTION;
                } else { // Branch 24
                    bds.setFlag(24);
                    type = Type.OTHER;
                }
            }
        } catch (Exception e) { // Branch 25
            bds.setFlag(25);
            if (logger.isTraceEnabled()) { // Branch 26
                bds.setFlag(26);
                logger.trace("js conversion failed", e);
            }
            throw e;
        }
    }

    public <T> T getValue() {
        return (T) value;
    }

    public Map<String, Object> getAsMap() {
        return (Map) value;
    }

    public List getAsList() {
        return (List) value;
    }

    public Value getOriginal() {
        return original;
    }

    public boolean isXml() {
        return type == Type.XML;
    }

    public boolean isNull() {
        return type == Type.NULL;
    }

    public boolean isObject() {
        return type == Type.OBJECT;
    }

    public boolean isArray() {
        return type == Type.ARRAY;
    }

    public boolean isTrue() {
        if (type != Type.OTHER || !Boolean.class.equals(value.getClass())) {
            return false;
        }
        return (Boolean) value;
    }

    public boolean isFunction() {
        return type == Type.FUNCTION;
    }

    public boolean isOther() {
        return type == Type.OTHER;
    }

    @Override
    public String toString() {
        return original.toString();
    }

    public String toJsonOrXmlString(boolean pretty) {
        return JsonUtils.toString(value, pretty);
    }

    public String getAsString() {
        return JsonUtils.toString(value);
    }

    public static Object fromJava(Object o) {
        if (o instanceof Function || o instanceof Proxy) {
            return o;
        } else if (o instanceof List) {
            return new JsList((List) o);
        } else if (o instanceof Map) {
            return new JsMap((Map) o);
        } else if (o instanceof Node) {
            return new JsXml((Node) o);
        } else {
            return o;
        }
    }

    public static Object toJava(Value v) {
        return new JsValue(v).getValue();
    }

    public static Object unWrap(Object o) {
        if (o instanceof JsXml) {
            return ((JsXml) o).getNode();
        } else if (o instanceof JsMap) {
            return ((JsMap) o).getMap();
        } else if (o instanceof JsList) {
            return ((JsList) o).getList();
        } else {
            return o;
        }
    }

    public static byte[] toBytes(Value v) {
        return JsonUtils.toBytes(toJava(v));
    }

    public static boolean isTruthy(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o);
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue() != 0.0;
        }
        return true;
    }

}
