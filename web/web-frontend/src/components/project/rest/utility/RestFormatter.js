/*
 Copyright 2020 Karl Dahlgren

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

const mockResponseStatusFormatter = (status) => {
    if(status === "ENABLED"){
        return "Enabled";
    } else if(status === "DISABLED"){
        return "Disabled"
    }

    return status;
};

const methodResponseStrategyFormatter = (responseStrategy) => {
    if(responseStrategy === "RANDOM"){
        return "Random";
    } else if(responseStrategy === "SEQUENCE"){
        return "Sequence"
    } else if(responseStrategy === "XPATH"){
        return "XPath"
    } else if(responseStrategy === "JSON_PATH"){
        return "JSON Path"
    } else if(responseStrategy === "QUERY_MATCH"){
        return "Parameter query match"
    } else if(responseStrategy === "HEADER_QUERY_MATCH"){
        return "Header query match"
    } else if(responseStrategy === "MULTIPLE"){
        return "Multiple strategies (AND)"
    }

    return responseStrategy;
};

const methodMultipleResponseStrategyFormatter = (strategies) => {
    if(!strategies || strategies.length === 0) {
        return "No strategies selected";
    }
    
    const strategyNames = strategies.map(strategy => {
        switch(strategy) {
            case "RANDOM": return "Random";
            case "SEQUENCE": return "Sequence";
            case "XPATH": return "XPath";
            case "JSON_PATH": return "JSON Path";
            case "QUERY_MATCH": return "Parameter query match";
            case "HEADER_QUERY_MATCH": return "Header query match";
            default: return strategy;
        }
    });
    
    return strategyNames.join(" + ");
};

const methodStatusFormatter = (status) => {
    if(status === "MOCKED"){
        return "Mocked";
    } else if(status === "DISABLED"){
        return "Disabled"
    } else if(status === "FORWARDED"){
        return "Forwarded"
    } else if(status === "RECORDING"){
        return "Recording"
    } else if(status === "RECORD_ONCE"){
        return "Record once"
    } else if(status === "ECHO"){
        return "Echo"
    }

    return status;
};

const definitionTypeFormatter = (status) => {
    if(status === "SWAGGER"){
        return "Swagger";
    } else if(status === "WADL"){
        return "WADL"
    } else if(status === "RAML"){
        return "RAML"
    }

    return status;
};

export {
    mockResponseStatusFormatter,
    methodResponseStrategyFormatter,
    methodMultipleResponseStrategyFormatter,
    methodStatusFormatter,
    definitionTypeFormatter
};