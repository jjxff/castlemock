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

package com.castlemock.web.mock.rest.service.event;

import com.castlemock.web.basis.repository.Repository;
import com.castlemock.core.basis.model.ServiceResult;
import com.castlemock.core.basis.model.ServiceTask;
import com.castlemock.core.mock.rest.model.event.domain.RestEvent;
import com.castlemock.core.mock.rest.service.event.input.CreateRestEventInput;
import com.castlemock.core.mock.rest.service.event.output.CreateRestEventOutput;
import com.castlemock.web.mock.rest.model.project.RestEventGenerator;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class CreateRestEventServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private Repository repository;

    @InjectMocks
    private CreateRestEventService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "restMaxEventCount", 5);
    }

    @Test
    public void testProcess(){
        final RestEvent restEvent = RestEventGenerator.generateRestEvent();
        Mockito.when(repository.save(Mockito.any(RestEvent.class))).thenReturn(RestEventGenerator.generateRestEvent());

        final CreateRestEventInput input = new CreateRestEventInput(restEvent);
        input.setRestEvent(restEvent);

        final ServiceTask<CreateRestEventInput> serviceTask = new ServiceTask<CreateRestEventInput>(input);
        final ServiceResult<CreateRestEventOutput> serviceResult = service.process(serviceTask);
        final CreateRestEventOutput createRestApplicationOutput = serviceResult.getOutput();
        final RestEvent returnedRestEvent = createRestApplicationOutput.getCreatedRestEvent();

        Assert.assertEquals(restEvent.getApplicationId(), returnedRestEvent.getApplicationId());
        Assert.assertEquals(restEvent.getMethodId(), returnedRestEvent.getMethodId());
        Assert.assertEquals(restEvent.getProjectId(), returnedRestEvent.getProjectId());
        Assert.assertEquals(restEvent.getResourceId(), returnedRestEvent.getResourceId());
    }

}
