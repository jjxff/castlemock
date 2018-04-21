/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.web.mock.soap.model.project;

import com.castlemock.core.basis.model.http.domain.HttpMethod;
import com.castlemock.core.mock.soap.model.project.domain.SoapMockResponse;
import com.castlemock.core.mock.soap.model.project.domain.SoapOperation;
import com.castlemock.core.mock.soap.model.project.domain.SoapOperationStatus;
import com.castlemock.core.mock.soap.model.project.domain.SoapVersion;
import com.castlemock.core.mock.soap.model.project.dto.SoapMockResponseDto;
import com.castlemock.core.mock.soap.model.project.dto.SoapOperationDto;

import java.util.ArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class SoapOperationDtoGenerator {

    public static SoapOperationDto generateSoapOperationDto(){
        final SoapOperationDto soapOperationDto = new SoapOperationDto();
        soapOperationDto.setId("SOAP OPERATION");
        soapOperationDto.setName("Soap operation name");
        soapOperationDto.setCurrentResponseSequenceIndex(1);
        soapOperationDto.setDefaultBody("Default body");
        soapOperationDto.setForwardedEndpoint("Forwarded event");
        soapOperationDto.setInvokeAddress("Invoke address");
        soapOperationDto.setOriginalEndpoint("Original endpoint");
        soapOperationDto.setHttpMethod(HttpMethod.POST);
        soapOperationDto.setStatus(SoapOperationStatus.MOCKED);
        soapOperationDto.setSoapVersion(SoapVersion.SOAP11);
        soapOperationDto.setMockResponses(new ArrayList<SoapMockResponseDto>());
        return soapOperationDto;
    }

    public static SoapOperation generateSoapOperation(){
        final SoapOperation soapOperation = new SoapOperation();
        soapOperation.setId("SOAP OPERATION");
        soapOperation.setName("Soap operation name");
        soapOperation.setCurrentResponseSequenceIndex(1);
        soapOperation.setDefaultBody("Default body");
        soapOperation.setForwardedEndpoint("Forwarded event");
        soapOperation.setOriginalEndpoint("Original endpoint");
        soapOperation.setHttpMethod(HttpMethod.POST);
        soapOperation.setStatus(SoapOperationStatus.MOCKED);
        soapOperation.setSoapVersion(SoapVersion.SOAP11);
        return soapOperation;
    }
}
