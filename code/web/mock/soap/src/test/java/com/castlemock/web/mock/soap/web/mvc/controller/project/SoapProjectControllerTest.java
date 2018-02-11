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

package com.castlemock.web.mock.soap.web.mvc.controller.project;

import com.castlemock.core.basis.model.Input;
import com.castlemock.core.basis.model.ServiceProcessor;
import com.castlemock.core.mock.soap.model.project.dto.SoapPortDto;
import com.castlemock.core.mock.soap.model.project.dto.SoapProjectDto;
import com.castlemock.core.mock.soap.model.project.service.message.input.ReadSoapPortInput;
import com.castlemock.core.mock.soap.model.project.service.message.input.UpdateSoapPortsStatusInput;
import com.castlemock.core.mock.soap.model.project.service.message.output.ReadSoapPortOutput;
import com.castlemock.core.mock.soap.model.project.service.message.output.ReadSoapProjectOutput;
import com.castlemock.web.basis.web.mvc.controller.AbstractController;
import com.castlemock.web.mock.soap.config.TestApplication;
import com.castlemock.web.mock.soap.model.project.SoapPortDtoGenerator;
import com.castlemock.web.mock.soap.model.project.SoapProjectDtoGenerator;
import com.castlemock.web.mock.soap.web.mvc.command.port.SoapPortModifierCommand;
import com.castlemock.web.mock.soap.web.mvc.controller.AbstractSoapControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class SoapProjectControllerTest extends AbstractSoapControllerTest {

    private static final String PAGE = "partial/mock/soap/project/soapProject.jsp";
    private static final String DELETE_SOAP_PORTS_PAGE = "partial/mock/soap/port/deleteSoapPorts.jsp";
    private static final String UPDATE_SOAP_PORTS_ENDPOINT_PAGE = "partial/mock/soap/port/updateSoapPortsEndpoint.jsp";
    private static final String DELETE_SOAP_PORTS_COMMAND = "deleteSoapPortsCommand";
    private static final String UPDATE_SOAP_PORTS_ENDPOINT_COMMAND = "updateSoapPortsEndpointCommand";
    private static final String SOAP_PORTS = "soapPorts";

    @InjectMocks
    private SoapProjectController soapProjectController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return soapProjectController;
    }

    @Test
    public void getProject() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        final List<SoapPortDto> soapPortDtos = new ArrayList<SoapPortDto>();
        soapPortDtos.add(soapPortDto);
        soapProjectDto.setPorts(soapPortDtos);
        when(serviceProcessor.process(any(Input.class))).thenReturn(new ReadSoapProjectOutput(soapProjectDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT, soapProjectDto));

    }

    @Test
    public void getProjectUploadError() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        final List<SoapPortDto> soapPortDtos = new ArrayList<SoapPortDto>();
        soapPortDtos.add(soapPortDto);
        soapProjectDto.setPorts(soapPortDtos);
        when(serviceProcessor.process(any(Input.class))).thenReturn(new ReadSoapProjectOutput(soapProjectDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH)
                .param("upload", "error");
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute("upload", "error"))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT, soapProjectDto));

    }

    @Test
    public void getProjectUploadSuccess() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        final List<SoapPortDto> soapPortDtos = new ArrayList<SoapPortDto>();
        soapPortDtos.add(soapPortDto);
        soapProjectDto.setPorts(soapPortDtos);
        when(serviceProcessor.process(any(Input.class))).thenReturn(new ReadSoapProjectOutput(soapProjectDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH)
                .param("upload", "success");
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute("upload", "success"))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT, soapProjectDto));

    }

    @Test
    public void projectFunctionalityUpdate() throws Exception {
        final String projectId = "projectId";
        final String[] soapPortIds = {"soapPort1", "soapPort2"};

        final SoapPortModifierCommand soapPortModifierCommand = new SoapPortModifierCommand();
        soapPortModifierCommand.setSoapPortIds(soapPortIds);
        soapPortModifierCommand.setSoapPortStatus("MOCKED");

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH)
                        .param("action", "update").flashAttr("soapPortModifierCommand", soapPortModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/web/soap/project/" + projectId));

        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.any(UpdateSoapPortsStatusInput.class));

    }

    @Test
    public void projectFunctionalityDelete() throws Exception {
        final String projectId = "projectId";
        final String[] soapPortIds = {"soapPort1", "soapPort2"};

        final SoapPortDto soapPort1 = new SoapPortDto();
        soapPort1.setName("soapPort1");

        final SoapPortDto soapPort2 = new SoapPortDto();
        soapPort1.setName("soapPort2");

        final List<SoapPortDto> soapPorts = Arrays.asList(soapPort1, soapPort2);

        Mockito.when(serviceProcessor.process(Mockito.any(ReadSoapPortInput.class)))
                .thenReturn(new ReadSoapPortOutput(soapPort1))
                .thenReturn(new ReadSoapPortOutput(soapPort2));


        final SoapPortModifierCommand soapPortModifierCommand = new SoapPortModifierCommand();
        soapPortModifierCommand.setSoapPortIds(soapPortIds);

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH)
                        .param("action", "delete").flashAttr("soapPortModifierCommand", soapPortModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, DELETE_SOAP_PORTS_PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT_ID, projectId))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PORTS, soapPorts))
                .andExpect(MockMvcResultMatchers.model().attributeExists(DELETE_SOAP_PORTS_COMMAND));


        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.any(ReadSoapPortInput.class));

    }

    @Test
    public void projectFunctionalityUpdateEndpoint() throws Exception {
        final String projectId = "projectId";
        final String[] soapPortIds = {"soapPort1", "soapPort2"};

        final SoapPortDto soapPort1 = new SoapPortDto();
        soapPort1.setName("soapPort1");

        final SoapPortDto soapPort2 = new SoapPortDto();
        soapPort1.setName("soapPort2");

        final List<SoapPortDto> soapPorts = Arrays.asList(soapPort1, soapPort2);

        Mockito.when(serviceProcessor.process(Mockito.any(ReadSoapPortInput.class)))
                .thenReturn(new ReadSoapPortOutput(soapPort1))
                .thenReturn(new ReadSoapPortOutput(soapPort2));


        final SoapPortModifierCommand soapPortModifierCommand = new SoapPortModifierCommand();
        soapPortModifierCommand.setSoapPortIds(soapPortIds);

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH)
                        .param("action", "update-endpoint").flashAttr("soapPortModifierCommand", soapPortModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, UPDATE_SOAP_PORTS_ENDPOINT_PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT_ID, projectId))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PORTS, soapPorts))
                .andExpect(MockMvcResultMatchers.model().attributeExists(UPDATE_SOAP_PORTS_ENDPOINT_COMMAND));


        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.any(ReadSoapPortInput.class));

    }

}
