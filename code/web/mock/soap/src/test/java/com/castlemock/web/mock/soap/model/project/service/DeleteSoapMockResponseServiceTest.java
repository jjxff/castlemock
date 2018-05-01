/*
 * Copyright 2016 Karl Dahlgren
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

package com.castlemock.web.mock.soap.model.project.service;

import com.castlemock.core.basis.model.ServiceResult;
import com.castlemock.core.basis.model.ServiceTask;
import com.castlemock.core.mock.soap.model.project.domain.SoapMockResponse;
import com.castlemock.core.mock.soap.model.project.domain.SoapOperation;
import com.castlemock.core.mock.soap.model.project.domain.SoapPort;
import com.castlemock.core.mock.soap.model.project.domain.SoapProject;
import com.castlemock.core.mock.soap.model.project.service.message.input.DeleteSoapMockResponseInput;
import com.castlemock.core.mock.soap.model.project.service.message.output.DeleteSoapMockResponseOutput;
import com.castlemock.web.mock.soap.model.project.SoapMockResponseGenerator;
import com.castlemock.web.mock.soap.model.project.SoapOperationGenerator;
import com.castlemock.web.mock.soap.model.project.SoapPortGenerator;
import com.castlemock.web.mock.soap.model.project.SoapProjectGenerator;
import com.castlemock.web.mock.soap.model.project.repository.SoapMockResponseRepository;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class DeleteSoapMockResponseServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private SoapMockResponseRepository repository;

    @InjectMocks
    private DeleteSoapMockResponseService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess(){
        final SoapProject soapProject = SoapProjectGenerator.generateSoapProject();
        final SoapPort soapPort = SoapPortGenerator.generateSoapPort();
        final SoapOperation soapOperation = SoapOperationGenerator.generateSoapOperation();
        final SoapMockResponse soapMockResponse = SoapMockResponseGenerator.generateSoapMockResponse();

        soapProject.getPorts().add(soapPort);
        soapPort.getOperations().add(soapOperation);
        soapOperation.getMockResponses().add(soapMockResponse);

        final DeleteSoapMockResponseInput input = new DeleteSoapMockResponseInput(soapProject.getId(), soapPort.getId(), soapOperation.getId(), soapMockResponse.getId());
        final ServiceTask<DeleteSoapMockResponseInput> serviceTask = new ServiceTask<DeleteSoapMockResponseInput>(input);
        final ServiceResult<DeleteSoapMockResponseOutput> serviceResult = service.process(serviceTask);
        serviceResult.getOutput();

        Mockito.verify(repository, Mockito.times(1)).delete(Mockito.anyString());
    }
}
