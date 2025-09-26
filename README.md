<p align="center"><img src="https://raw.githubusercontent.com/castlemock/castlemock/master/web/web-frontend/src/images/logo.png"></div></p>

<h1 align="center"> Castle Mock: <br/>Mock RESTful APIs and SOAP web services</h1>

> [!CAUTION]
> **Original Castle Mock** is no longer maintained and will not receive future updates from the original maintainers.  
> The [original GitHub repository](https://github.com/castlemock/castlemock) remains available for reference, but the original team recommends exploring alternatives for active use.
> 
> **This is a community fork** attempting to continue development with improvements and new features. This version includes enhancements not available in the original deprecated version.

<p align="center">
    <a href="LICENSE"><img src="https://img.shields.io/badge/license-Apache%202-blue.svg"></a>
    <img src="https://img.shields.io/badge/status-community%20maintained-green">
</p>

**Castle Mock** is a web application that provides the functionality to mock out RESTful APIs and SOAP web services. This functionality allows client-side developers to completely mimic a server side behaviour and shape the responses themselves.

Table Of Content
----

- [About](#about)
- [Community Fork Changes](#community-fork-changes)
- [What to Use Castle Mock for and When to Use It](#what-to-use-castle-mock-for-and-when-to-use-it)
- [Dynamic Expressions](#dynamic-expressions)
- [Installation](#installation)
- [Documentation](#documentation)
- [License](#license)

## About

**Castle Mock** can create mocked services based on WSDL, WADL, Swagger and RAML definition files. The web-services defined within the files will be mocked automatically by **Castle Mock**. Once the mocks for the web-services are created, they can be configured to mock the service or forward the request to the original endpoint. The response from the forwarded requests can be recorded automatically and used to create new mocked responses.

**Castle Mock** is completely free and open source (Apache License). It is built with Java and the application itself is deployed to an Apache Tomcat server.

## Community Fork Changes

This community fork includes enhancements and new features over the original deprecated version. 

**Latest Version: 1.69.2** - [View Full Changelog](changelog/1.69.2.md)

### Recent Highlights
- **📅 Dynamic Date Expressions (v1.69.2)**: New time-based expressions for mock responses
  - `${NOW(offset="30 seconds")}` - Current date/time with offset support
  - `${DATE(offset="5 days")}` - Current date only with offset calculations
  - `${TIME(offset="2 hours")}` - Current time only with offset support
  - All expressions support positive/negative offsets in seconds, minutes, hours, and days
- **🔧 JSON Path Body Mapping Fix (v1.69.2)**: Fixed `${BODY_JSON_PATH()}` expression to properly copy request JSON fields to responses
- **🛡️ Guard Functionality (v1.69.1)**: New header interceptor feature for REST mock responses
  - Configure guards to validate headers (JWT tokens, API keys) before processing other responses
  - Returns configured response immediately if validation fails (e.g., 401 Unauthorized)
  - Works independently of Response Strategies for maximum flexibility
- **Multiple Response Strategies**: REST methods can now use multiple response strategies simultaneously with AND logic
- **Enhanced Expression Support**: Improved conditional response matching capabilities
- **Backward Compatibility**: All changes are fully backward compatible with existing projects

## What to Use Castle Mock for and When to Use It

Use **Castle Mock** to mock out RESTful APIs and SOAP web services for testing purposes for when either performing system or integration tests. It is recommended to only use **Castle Mock** on an internal network and never be used publically. **Castle Mock** is **NOT** developed or meant for anything else other than for testing purposes.

## Dynamic Expressions

Castle Mock supports dynamic expressions in mock responses using the `${}` syntax. These expressions are processed at runtime to generate dynamic content.

### Date and Time Expressions

**NOW Expression** - Returns current date/time in ISO-8601 format:
```
${NOW}                           // Current timestamp: 2024-03-15T14:30:45Z
${NOW(offset="30 seconds")}      // 30 seconds from now
${NOW(offset="-15 minutes")}     // 15 minutes ago
${NOW(offset="2 hours")}         // 2 hours from now
${NOW(offset="5 days")}          // 5 days from now
```

**DATE Expression** - Returns current date in ISO date format:
```
${DATE}                          // Current date: 2024-03-15
${DATE(offset="7 days")}         // 7 days from today
${DATE(offset="-30 days")}       // 30 days ago
```

**TIME Expression** - Returns current time in ISO time format:
```
${TIME}                          // Current time: 14:30:45
${TIME(offset="45 minutes")}     // 45 minutes from now
${TIME(offset="-2 hours")}       // 2 hours ago
```

### Request Data Expressions

**JSON Path Body Mapping** - Copy fields from request JSON to response:
```json
// Request: {"userId": "123", "amount": 50.75}
// Response template:
{
  "transactionId": "${RANDOM_UUID}",
  "userId": "${BODY_JSON_PATH(expression=\"$.userId\")}",
  "processedAmount": "${BODY_JSON_PATH(expression=\"$.amount\")}"
}
```

**Query String Parameters**:
```
${QUERY_STRING(name="param1")}   // Get query parameter value
```

**Path Parameters**:
```
${PATH_PARAMETER(name="id")}     // Get path parameter value
```

**URL Host**:
```
${URL_HOST}                      // Get request host
```

### Random Data Expressions

**Random UUID**:
```
${RANDOM_UUID}                   // Random UUID: f47ac10b-58cc-4372-a567-0e02b2c3d479
```

**Random Integers**:
```
${RANDOM_INTEGER}                // Random integer
${RANDOM_INTEGER(min="1", max="100")}  // Random integer between 1-100
```

**Random Decimals**:
```
${RANDOM_DECIMAL}                // Random decimal
${RANDOM_DECIMAL(min="0.0", max="1.0")}  // Random decimal between 0.0-1.0
```

**Random Strings**:
```
${RANDOM_STRING(length="10")}    // Random string of length 10
```

### XPath Expressions

For XML request bodies:
```
${BODY_XPATH(expression="//user/@id")}  // Extract XML attributes/elements
```

### Usage Notes

- All expressions are case-insensitive (`NOW`, `now`, `Now` all work)
- Offset formats support: `seconds`, `minutes`, `hours`, `days` (singular or plural)
- Negative offsets use minus sign: `-30 minutes`
- Enable "Use Expression" checkbox in mock response configuration
- Expressions work in both REST and SOAP mock responses

## Installation

Upon successful installation and deployment, **Castle Mock** can be accessed from the web browser:

    http://localhost:8080/castlemock
    
This will prompt you the login screen. When logging for the first time use the following credentials: 

    Username: admin 
    Password: admin 

It is recommended that the administrator profile gets updated with a more secure password. This is accomplish by going to the user page and choosing to update the profile.

Upon successful login, you will be able to create both SOAP and REST projects. SOAP and REST resources can either be created manually or created by importing resource descriptions, such as WSDL and WADL. All created resources can be mocked multiple times. Each resource can also be configured to have different response strategies, such as random and sequence

## Documentation

Documentation can be found under our [GitHub Wiki](https://github.com/castlemock/castlemock/wiki). 

## License

**Castle Mock** is **licensed** under the **[Apache License](https://github.com/castlemock/castlemock/blob/master/LICENSE)**. The terms of the license are as follows:

    Apache License

    Copyright 2015 Karl Dahlgren and a number of other contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
