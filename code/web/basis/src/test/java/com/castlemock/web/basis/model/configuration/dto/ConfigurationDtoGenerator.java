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

package com.castlemock.web.basis.model.configuration.dto;


import com.castlemock.core.basis.model.configuration.domain.Configuration;
import com.castlemock.core.basis.model.configuration.domain.ConfigurationType;
import com.castlemock.core.basis.model.event.domain.Event;

/**
 * @author Karl Dahlgren
 * @since 1.0
 * @see Event
 * @see ConfigurationDtoGenerator
 */
public class ConfigurationDtoGenerator {

    public static Configuration generateConfigurationDto(){
        final Configuration configurationDto = new Configuration();
        configurationDto.setType(ConfigurationType.STRING);
        configurationDto.setKey("Key");
        configurationDto.setValue("Value");
        return configurationDto;
    }

    public static Configuration generateConfiguration(){
        final Configuration configuration = new Configuration();
        configuration.setType(ConfigurationType.STRING);
        configuration.setKey("Key");
        configuration.setValue("Value");
        return configuration;
    }

}
