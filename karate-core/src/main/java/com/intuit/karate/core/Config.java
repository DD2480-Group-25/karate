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
package com.intuit.karate.core;

import com.intuit.karate.FileUtils;
import com.intuit.karate.StringUtils;
import com.intuit.karate.driver.DockerTarget;
import com.intuit.karate.driver.Target;
import com.intuit.karate.http.Cookies;
import com.intuit.karate.http.HttpLogModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intuit.karate.BranchDataStructure;

/**
 * @author pthomas3
 */
public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    public static final int DEFAULT_RETRY_INTERVAL = 3000;
    public static final int DEFAULT_RETRY_COUNT = 3;
    public static final int DEFAULT_TIMEOUT = 30000;
    public static final int DEFAULT_HIGHLIGHT_DURATION = 3000;
    
    public static final String DRIVER = "driver";
    public static final String ROBOT = "robot";
    public static final String KAFKA = "kafka";
    public static final String GRPC = "grpc";
    public static final String WEBSOCKET = "websocket";

    private String url;
    private boolean sslEnabled = false;
    private String sslAlgorithm = "TLS";
    private String sslKeyStore;
    private String sslKeyStorePassword;
    private String sslKeyStoreType;
    private String sslTrustStore;
    private String sslTrustStorePassword;
    private String sslTrustStoreType;
    private boolean sslTrustAll = true;
    private boolean followRedirects = true;
    private int readTimeout = DEFAULT_TIMEOUT;
    private int connectTimeout = DEFAULT_TIMEOUT;
    private Charset charset = StandardCharsets.UTF_8;
    private String proxyUri;
    private String proxyUsername;
    private String proxyPassword;
    private List<String> nonProxyHosts;
    private String localAddress;
    private int responseDelay;
    private boolean xmlNamespaceAware = false;
    private boolean lowerCaseResponseHeaders = false;
    private boolean corsEnabled = false;
    private boolean logPrettyRequest;
    private boolean logPrettyResponse;
    private boolean printEnabled = true;
    private boolean pauseIfNotPerf = false;
    private boolean abortedStepsShouldPass = false;
    private boolean matchEachEmptyAllowed = false;
    private Target driverTarget;
    private Map<String, Map<String, Object>> customOptions = new HashMap();
    private HttpLogModifier logModifier;

    private Variable afterScenario = Variable.NULL;
    private Variable afterFeature = Variable.NULL;
    private Variable headers = Variable.NULL;
    private Variable cookies = Variable.NULL;
    private Variable responseHeaders = Variable.NULL;
    private List<Method> continueOnStepFailureMethods = new ArrayList<>();
    private boolean continueAfterContinueOnStepFailure;
    private boolean abortSuiteOnFailure;

    // retry config
    private int retryInterval = DEFAULT_RETRY_INTERVAL;
    private int retryCount = DEFAULT_RETRY_COUNT;

    // report config
    private boolean showLog = true;
    private boolean showAllSteps = true;

    // call single cache config
    private int callSingleCacheMinutes = 0;
    private String callSingleCacheDir = FileUtils.getBuildDir();

    // image comparison config
    private Map<String, Object> imageComparisonOptions;

    // ntlm authentication
    private boolean ntlmEnabled = false;
    private boolean httpRetryEnabled = false;
    private String ntlmUsername;
    private String ntlmPassword;
    private String ntlmDomain;
    private String ntlmWorkstation;

    public Config() {
        // zero arg constructor
    }

    private static <T> T get(Map<String, Object> map, String key, T defaultValue) {
        Object o = map.get(key);
        return o == null ? defaultValue : (T) o;
    }

    public boolean configure(String key, Variable value) { // TODO use enum
        key = StringUtils.trimToEmpty(key);
        BranchDataStructure.instances.get("config-test").setFlag(0);
        switch (key) { // This is branch 0
            case "url": // This is branch 1
                BranchDataStructure.instances.get("config-test").setFlag(1);
                url = value.getAsString();
                return false;
            case "headers": // This is branch 2
            BranchDataStructure.instances.get("config-test").setFlag(2);
                headers = value;
                return false;
            case "cookies": // This is branch 3
            BranchDataStructure.instances.get("config-test").setFlag(3);
                if (!value.isNull()) { // This is branch 48
                    BranchDataStructure.instances.get("config-test").setFlag(48);
                    value = new Variable(Cookies.normalize(value.getValue()));
                }
                cookies = value;
                return false;
            case "responseHeaders": // This is branch 4
            BranchDataStructure.instances.get("config-test").setFlag(4);
                responseHeaders = value;
                return false;
            case "responseDelay": // This is branch 5
            BranchDataStructure.instances.get("config-test").setFlag(5);
                responseDelay = value.isNull() ? 0 : value.getAsInt();
                return false;
            case "xmlNamespaceAware": // This is branch 6
            BranchDataStructure.instances.get("config-test").setFlag(6);
                xmlNamespaceAware = value.isTrue();
                return false;
            case "lowerCaseResponseHeaders": // This is branch 7
            BranchDataStructure.instances.get("config-test").setFlag(7);
                lowerCaseResponseHeaders = value.isTrue();
                return false;
            case "cors": // This is branch 8
            BranchDataStructure.instances.get("config-test").setFlag(8);
                corsEnabled = value.isTrue();
                return false;
            case "logPrettyResponse": // This is branch 9
            BranchDataStructure.instances.get("config-test").setFlag(9);
                logPrettyResponse = value.isTrue();
                return false;
            case "logPrettyRequest": // This is branch 10
            BranchDataStructure.instances.get("config-test").setFlag(10);
                logPrettyRequest = value.isTrue();
                return false;
            case "printEnabled": // This is branch 11
            BranchDataStructure.instances.get("config-test").setFlag(11);
                printEnabled = value.isTrue();
                return false;
            case "afterScenario": // This is branch 12
            BranchDataStructure.instances.get("config-test").setFlag(12);
                afterScenario = value;
                return false;
            case "afterFeature": // This is branch 13
            BranchDataStructure.instances.get("config-test").setFlag(13);
                afterFeature = value;
                return false;
            case "report": // This is branch 14
            BranchDataStructure.instances.get("config-test").setFlag(14);
                if (value.isMap()) { // This is branch 15
                    BranchDataStructure.instances.get("config-test").setFlag(15);
                    Map<String, Object> map = value.getValue();
                    showLog = get(map, "showLog", showLog);
                    showAllSteps = get(map, "showAllSteps", showAllSteps);
                } else if (value.isTrue()) { // This is branch 49
                    BranchDataStructure.instances.get("config-test").setFlag(49);
                    showLog = true;
                    showAllSteps = true;
                } else { // This is branch 50
                    BranchDataStructure.instances.get("config-test").setFlag(50);
                    showLog = false;
                    showAllSteps = false;
                }
                return false;
            case DRIVER:
            case ROBOT:
            case KAFKA:
            case GRPC:
            case WEBSOCKET: // This is branch 16
                BranchDataStructure.instances.get("config-test").setFlag(16);
                customOptions.put(key, value.getValue());
                return false;
            case "driverTarget": // This is branch 17
                BranchDataStructure.instances.get("config-test").setFlag(17);
                if (value.isMap()) { // This is branch 18
                    BranchDataStructure.instances.get("config-test").setFlag(18);
                    Map<String, Object> map = value.getValue();
                    if (map.containsKey("docker")) { // This is branch 19
                        BranchDataStructure.instances.get("config-test").setFlag(19);
                        // todo add the working dir here
                        driverTarget = new DockerTarget(map);
                    } else { // This is branch 51
                        BranchDataStructure.instances.get("config-test").setFlag(51);
                        throw new RuntimeException("bad driverTarget config, expected key 'docker': " + map);
                    }
                } else { // This is branch 52
                    BranchDataStructure.instances.get("config-test").setFlag(52);
                    driverTarget = value.getValue();
                }
                return false;
            case "retry": // This is branch 20
                BranchDataStructure.instances.get("config-test").setFlag(20);
                if (value.isMap()) { // This is branch 21
                    BranchDataStructure.instances.get("config-test").setFlag(21);
                    Map<String, Object> map = value.getValue();
                    retryInterval = get(map, "interval", retryInterval);
                    retryCount = get(map, "count", retryCount);
                }
                return false;
            case "pauseIfNotPerf": // This is branch 22
            BranchDataStructure.instances.get("config-test").setFlag(22);
                pauseIfNotPerf = value.isTrue();
                return false;
            case "abortedStepsShouldPass": // This is branch 23
            BranchDataStructure.instances.get("config-test").setFlag(23);
                abortedStepsShouldPass = value.isTrue();
                return false;
            case "abortSuiteOnFailure": // This is branch 24
            BranchDataStructure.instances.get("config-test").setFlag(24);
                abortSuiteOnFailure = value.isTrue();
                return false;
            case "callSingleCache": // This is branch 25
                BranchDataStructure.instances.get("config-test").setFlag(25);
                if (value.isMap()) { // This is branch 26
                    BranchDataStructure.instances.get("config-test").setFlag(26);
                    Map<String, Object> map = value.getValue();
                    callSingleCacheMinutes = get(map, "minutes", callSingleCacheMinutes);
                    callSingleCacheDir = get(map, "dir", callSingleCacheDir);
                }
                return false;
            case "logModifier": // This is branch 27
                BranchDataStructure.instances.get("config-test").setFlag(27);
                logModifier = value.getValue();
                return false;
            case "imageComparison": // This is branch 28
                BranchDataStructure.instances.get("config-test").setFlag(28);
                imageComparisonOptions = value.getValue();
                return false;
            case "matchEachEmptyAllowed": // This is branch 29
                BranchDataStructure.instances.get("config-test").setFlag(29);
                matchEachEmptyAllowed = value.getValue();
                return false;
            case "continueOnStepFailure": // This is branch 30
                BranchDataStructure.instances.get("config-test").setFlag(30);
                continueOnStepFailureMethods.clear(); // clears previous configuration - in case someone is trying to chain these and forgets resetting the previous one
                boolean enableContinueOnStepFailureFeature = false;
                Boolean continueAfterIgnoredFailure = null;
                List<String> stepKeywords = null;
                if (value.isMap()) { // This is branch 31
                    BranchDataStructure.instances.get("config-test").setFlag(31);
                    Map<String, Object> map = value.getValue();
                    stepKeywords = (List<String>) map.get("keywords");
                    continueAfterIgnoredFailure = (Boolean) map.get("continueAfter");
                    enableContinueOnStepFailureFeature = map.get("enabled") != null && (Boolean) map.get("enabled");
                }
                if (value.isTrue() || enableContinueOnStepFailureFeature) { // This is branch 32
                    BranchDataStructure.instances.get("config-test").setFlag(32);
                    continueOnStepFailureMethods.addAll(stepKeywords == null ? StepRuntime.METHOD_MATCH : StepRuntime.findMethodsByKeywords(stepKeywords));
                } else { // This is branch 53
                    BranchDataStructure.instances.get("config-test").setFlag(53);
                    if (stepKeywords == null) { // This is branch 33
                        BranchDataStructure.instances.get("config-test").setFlag(33);
                        continueOnStepFailureMethods.clear();
                    } else { // This is branch 54
                        BranchDataStructure.instances.get("config-test").setFlag(54);
                        continueOnStepFailureMethods.removeAll(StepRuntime.findMethodsByKeywords(stepKeywords));
                    }
                }
                if (continueAfterIgnoredFailure != null) { // This is branch 34
                    BranchDataStructure.instances.get("config-test").setFlag(34);
                    continueAfterContinueOnStepFailure = continueAfterIgnoredFailure;
                }
                return false;
            // here on the http client has to be re-constructed ================
            // and we return true instead of false
            case "charset": // This is branch 35
                BranchDataStructure.instances.get("config-test").setFlag(35);
                charset = value.isNull() ? null : Charset.forName(value.getAsString());
                return true;
            case "ssl": // This is branch 36
                BranchDataStructure.instances.get("config-test").setFlag(36);
                if (value.isString()) { // This is branch 37
                    BranchDataStructure.instances.get("config-test").setFlag(37);
                    sslEnabled = true;
                    sslAlgorithm = value.getAsString();
                } else if (value.isMap()) { // This is branch 55
                BranchDataStructure.instances.get("config-test").setFlag(55);
                    sslEnabled = true;
                    Map<String, Object> map = value.getValue();
                    sslKeyStore = (String) map.get("keyStore");
                    sslKeyStorePassword = (String) map.get("keyStorePassword");
                    sslKeyStoreType = (String) map.get("keyStoreType");
                    sslTrustStore = (String) map.get("trustStore");
                    sslTrustStorePassword = (String) map.get("trustStorePassword");
                    sslTrustStoreType = (String) map.get("trustStoreType");
                    Boolean trustAll = (Boolean) map.get("trustAll");
                    if (trustAll != null) { // This is branch 38
                        BranchDataStructure.instances.get("config-test").setFlag(38);
                        sslTrustAll = trustAll;
                    }
                    sslAlgorithm = (String) map.get("algorithm");
                } else { // This is branch 56
                    BranchDataStructure.instances.get("config-test").setFlag(56);
                    sslEnabled = value.isTrue();
                }
                return true;
            case "followRedirects": // This is branch 39
                BranchDataStructure.instances.get("config-test").setFlag(39);
                followRedirects = value.isTrue();
                return true;
            case "connectTimeout": // This is branch 40
                BranchDataStructure.instances.get("config-test").setFlag(40);
                connectTimeout = value.getAsInt();
                return true;
            case "readTimeout": // This is branch 41
                BranchDataStructure.instances.get("config-test").setFlag(41);
                readTimeout = value.getAsInt();
                return true;
            case "proxy": // This is branch 42
                BranchDataStructure.instances.get("config-test").setFlag(42);
                if (value.isNull()) { // This is branch 43
                    BranchDataStructure.instances.get("config-test").setFlag(43);
                    proxyUri = null;
                } else if (value.isString()) { // This is branch 57
                BranchDataStructure.instances.get("config-test").setFlag(57);
                    proxyUri = value.getAsString();
                } else { // This is branch 58
                    BranchDataStructure.instances.get("config-test").setFlag(58);
                    Map<String, Object> map = value.getValue();
                    proxyUri = (String) map.get("uri");
                    proxyUsername = (String) map.get("username");
                    proxyPassword = (String) map.get("password");
                    nonProxyHosts = (List) map.get("nonProxyHosts");
                }
                return true;
            case "localAddress": // This is branch 44
                BranchDataStructure.instances.get("config-test").setFlag(44);
                localAddress = value.getAsString();
                return true;
            case "ntlmAuth": // This is branch 45
                BranchDataStructure.instances.get("config-test").setFlag(45);
                if (value.isNull()) { // This is branch 46
                    BranchDataStructure.instances.get("config-test").setFlag(46);
                    ntlmEnabled = false;
                } else { // This is branch 59
                    BranchDataStructure.instances.get("config-test").setFlag(59);
                    Map<String, Object> map = value.getValue();
                    ntlmEnabled = true;
                    ntlmUsername = (String) map.get("username");
                    ntlmPassword = (String) map.get("password");
                    ntlmDomain = (String) map.get("domain");
                    ntlmWorkstation = (String) map.get("workstation");
                }
                return true;
            default: // This is branch 47
                BranchDataStructure.instances.get("config-test").setFlag(47);
                throw new RuntimeException("unexpected 'configure' key: '" + key + "'");
        }
    }

    public Config(Config parent) {
        url = parent.url;
        sslEnabled = parent.sslEnabled;
        sslAlgorithm = parent.sslAlgorithm;
        sslTrustStore = parent.sslTrustStore;
        sslTrustStorePassword = parent.sslTrustStorePassword;
        sslTrustStoreType = parent.sslTrustStoreType;
        sslKeyStore = parent.sslKeyStore;
        sslKeyStorePassword = parent.sslKeyStorePassword;
        sslKeyStoreType = parent.sslKeyStoreType;
        sslTrustAll = parent.sslTrustAll;
        followRedirects = parent.followRedirects;
        readTimeout = parent.readTimeout;
        connectTimeout = parent.connectTimeout;
        charset = parent.charset;
        proxyUri = parent.proxyUri;
        proxyUsername = parent.proxyUsername;
        proxyPassword = parent.proxyPassword;
        nonProxyHosts = parent.nonProxyHosts;
        localAddress = parent.localAddress;
        responseDelay = parent.responseDelay;
        xmlNamespaceAware = parent.xmlNamespaceAware;
        lowerCaseResponseHeaders = parent.lowerCaseResponseHeaders;
        corsEnabled = parent.corsEnabled;
        logPrettyRequest = parent.logPrettyRequest;
        logPrettyResponse = parent.logPrettyResponse;
        printEnabled = parent.printEnabled;
        driverTarget = parent.driverTarget;
        customOptions = parent.customOptions;
        showLog = parent.showLog;
        showAllSteps = parent.showAllSteps;
        retryInterval = parent.retryInterval;
        retryCount = parent.retryCount;
        pauseIfNotPerf = parent.pauseIfNotPerf;
        abortedStepsShouldPass = parent.abortedStepsShouldPass;
        logModifier = parent.logModifier;
        callSingleCacheMinutes = parent.callSingleCacheMinutes;
        callSingleCacheDir = parent.callSingleCacheDir;
        headers = parent.headers;
        cookies = parent.cookies;
        responseHeaders = parent.responseHeaders;
        afterScenario = parent.afterScenario;
        afterFeature = parent.afterFeature;
        continueOnStepFailureMethods = parent.continueOnStepFailureMethods;
        continueAfterContinueOnStepFailure = parent.continueAfterContinueOnStepFailure;
        abortSuiteOnFailure = parent.abortSuiteOnFailure;
        imageComparisonOptions = parent.imageComparisonOptions;
        matchEachEmptyAllowed = parent.matchEachEmptyAllowed;
        ntlmEnabled = parent.ntlmEnabled;
        httpRetryEnabled = parent.httpRetryEnabled;
        ntlmUsername = parent.ntlmUsername;
        ntlmPassword = parent.ntlmPassword;
        ntlmDomain = parent.ntlmDomain;
        ntlmWorkstation = parent.ntlmWorkstation;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }        

    public void setCookies(Variable cookies) {
        this.cookies = cookies;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public String getSslAlgorithm() {
        return sslAlgorithm;
    }

    public String getSslKeyStore() {
        return sslKeyStore;
    }

    public String getSslKeyStorePassword() {
        return sslKeyStorePassword;
    }

    public String getSslKeyStoreType() {
        return sslKeyStoreType;
    }

    public String getSslTrustStore() {
        return sslTrustStore;
    }

    public String getSslTrustStorePassword() {
        return sslTrustStorePassword;
    }

    public String getSslTrustStoreType() {
        return sslTrustStoreType;
    }

    public boolean isSslTrustAll() {
        return sslTrustAll;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getProxyUri() {
        return proxyUri;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public List<String> getNonProxyHosts() {
        return nonProxyHosts;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public Variable getHeaders() {
        return headers;
    }

    public Variable getCookies() {
        return cookies;
    }

    public Variable getResponseHeaders() {
        return responseHeaders;
    }

    public int getResponseDelay() {
        return responseDelay;
    }

    public boolean isXmlNamespaceAware() {
        return xmlNamespaceAware;
    }

    public boolean isLowerCaseResponseHeaders() {
        return lowerCaseResponseHeaders;
    }

    public boolean isCorsEnabled() {
        return corsEnabled;
    }

    public boolean isLogPrettyRequest() {
        return logPrettyRequest;
    }

    public boolean isLogPrettyResponse() {
        return logPrettyResponse;
    }

    public boolean isPrintEnabled() {
        return printEnabled;
    }

    public boolean isHttpRetryEnabled()
    {
        return httpRetryEnabled;
    }


    public Map<String, Map<String, Object>> getCustomOptions() {
        return customOptions;
    }    

    public Variable getAfterScenario() {
        return afterScenario;
    }

    public void setAfterScenario(Variable afterScenario) {
        this.afterScenario = afterScenario;
    }

    public Variable getAfterFeature() {
        return afterFeature;
    }

    public void setAfterFeature(Variable afterFeature) {
        this.afterFeature = afterFeature;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public boolean isShowAllSteps() {
        return showAllSteps;
    }

    public void setShowAllSteps(boolean showAllSteps) {
        this.showAllSteps = showAllSteps;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isPauseIfNotPerf() {
        return pauseIfNotPerf;
    }

    public boolean isAbortedStepsShouldPass() {
        return abortedStepsShouldPass;
    }

    public Target getDriverTarget() {
        return driverTarget;
    }

    public void setDriverTarget(Target driverTarget) {
        this.driverTarget = driverTarget;
    }

    public HttpLogModifier getLogModifier() {
        return logModifier;
    }

    public String getCallSingleCacheDir() {
        return callSingleCacheDir;
    }

    public int getCallSingleCacheMinutes() {
        return callSingleCacheMinutes;
    }

    public List<Method> getContinueOnStepFailureMethods() {
        return continueOnStepFailureMethods;
    }

    public void setContinueOnStepFailureMethods(List<Method> continueOnStepFailureMethods) {
        this.continueOnStepFailureMethods = continueOnStepFailureMethods;
    }

    public boolean isContinueAfterContinueOnStepFailure() {
        return continueAfterContinueOnStepFailure;
    }

    public void setContinueAfterContinueOnStepFailure(boolean continueAfterContinueOnStepFailure) {
        this.continueAfterContinueOnStepFailure = continueAfterContinueOnStepFailure;
    }

    public void setAbortSuiteOnFailure(boolean abortSuiteOnFailure) {
        this.abortSuiteOnFailure = abortSuiteOnFailure;
    }

    public boolean isAbortSuiteOnFailure() {
        return abortSuiteOnFailure;
    }

    public Map<String, Object> getImageComparisonOptions() {
        return imageComparisonOptions;
    }

    public boolean isMatchEachEmptyAllowed() {
        return matchEachEmptyAllowed;
    }        

    public boolean isNtlmEnabled() {
        return ntlmEnabled;
    }

    public void setNtlmEnabled(boolean ntlmEnabled) {
        this.ntlmEnabled = ntlmEnabled;
    }

    public String getNtlmUsername() {
        return ntlmUsername;
    }

    public void setNtlmUsername(String ntlmUsername) {
        this.ntlmUsername = ntlmUsername;
    }

    public String getNtlmPassword() {
        return ntlmPassword;
    }

    public void setNtlmPassword(String ntlmPassword) {
        this.ntlmPassword = ntlmPassword;
    }

    public String getNtlmDomain() {
        return ntlmDomain;
    }

    public void setNtlmDomain(String ntlmDomain) {
        this.ntlmDomain = ntlmDomain;
    }

    public String getNtlmWorkstation() {
        return ntlmWorkstation;
    }

    public void setNtlmWorkstation(String ntlmWorkstation) {
        this.ntlmWorkstation = ntlmWorkstation;
    }

    public void setHttpRetryEnabled(boolean httpRetryEnabled)
    {
        this.httpRetryEnabled = httpRetryEnabled;
    }
}
