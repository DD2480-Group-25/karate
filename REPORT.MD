# Report for assignment 3

## Project

Name: karate

URL: https://github.com/karatelabs/karate

Karate is an open-source tool that combine API test-automation, mocks, performance-testing and UI automation into a single, unified framework. The syntax is language-neutral, and easy for even non-programmers.

## Onboarding experience

**Did it build and run as documented? How easily can you build the project? Briefly describe if everything worked as documented or not:**

It was quite easy to build the project, we just cloned it and opened it in VSCode and clicked the run buttons. 

**Did you have to install a lot of additional tools to build the software? Were those tools well documented?**

No, nothing more was required.

**Were other components installed automatically by the build script?**

Yes, the build script took care of everything.

**Did the build conclude automatically without errors?**

Yes.

**How well do examples and tests run on your system(s)?**

We did not run any examples but the tests run seamlessly.

## Complexity

### Config::configure
1. We ran lizard twice, once with default settings and once with the modified settings, counting the "switch" as one decision point. The first run gave a CCN of 60, the second one gave a CCN of 24. We obtained the same values by hand. We got 60 by counting the decision points for the function + 1, and we got 24 by adding the number of cases with the number of if-statements and then subtracting that value with the number of exit points, and then adding 2. ((1+37+17)-33+2). We think the results are quite clear.
2. This function is not very complex, it is just very long
3. The purpose of the function is to configure the program.
4. Exceptions are taken into account as exit points.
5. The function was not clearly documented.

### Request::getMember
1. We ran lizard twice, once with default settings and once with the modified settings, counting the "switch" as one decision point. The first run gave a CCN of 35, the second one gave a CCN of 2. We obtained the same values by hand. We got 35 by counting the decision points for the function + 1, and we got 2 by counting the number of cases and then subtracting that value with the number of exit points, and then adding 2. (26-26+2). We think the results are quite clear.
2. This function is not very complex, it is just very long again.
3. The purpose of the function is get the requests attribute with a string rather than with a getter
4. There are no exceptions
5. The function was not clearly documented, but is quite self-explanatory

**GUIDELINES**
1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?
3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?
5. Is the documentation clear w.r.t. all the possible outcomes?

## Refactoring

### Config::configure
Our plan for refactoring the code involves breaking it down into distinct setter functions, instead of one large switch monster function. The estimated impact of this refactoring is a minimized CC and no loss of practicality.

### Request::getMethod
Our plan for refactoring the code is similar to config as it too is a large collection of cases, which can be broken down into setter functions. Again, the estimated impact is a minimized CC and no loss of practicality.

**GUIDELINES**
Plan for refactoring complex code:

Estimated impact of refactoring (lower CC, but other drawbacks?).

Carried out refactoring (optional, P+):

git diff ...

## Coverage

### Tools

We used OpenClover with all four functions. It has been horrible to set up, as if any test from the test suite fails, the report isn't generated at all, so we had to comment out some of them so that everything could get through, and also modify stlightly some Java file that were causing errors. Also, the documentation regarding the setup with maven isn't extensive at all, making it hard to troubleshoot any issue. However, once everything is working, it is an extremely reliable and useful tool to have for a project.

### Your own coverage tool

Show a patch (or link to a branch) that shows the instrumented code to
gather coverage measurements.

The patch is probably too long to be copied here, so please add
the git command that is used to obtain the patch instead:

git diff ...

What kinds of constructs does your tool support, and how accurate is
its output?

### Evaluation

1. How detailed is your coverage measurement?

2. What are the limitations of your own tool?

3. Are the results of your tool consistent with existing coverage tools?

## Coverage improvement

### Overall: 

Show the comments that describe the requirements for the coverage.

Report of old coverage: [link]

Report of new coverage: [link]

Test cases added:

git diff ...

Number of test cases added: two per team member (P) or at least four (P+).

### Config::configure

**Old coverage:**

![alt text](images/configure-old1.png)
![alt text](images/configure-old2.png))

**New coverage:**

![alt text](images/configure-new1.png)
![alt text](images/configure-new2.png)

![alt text](image.png)

**Test cases added:**

We added the following 8 test cases

```json
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
```

### Request::getMember

**Old coverage:**

![alt text](images/getmember-old1.png)
![alt text](images/getmember-old2.png)

**New coverage:**

![alt text](images/getmember-new1.png)
![alt text](images/getmember-new2.png)

**Test cases added:**

We added the following 8 test cases:

```json
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
```

## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?