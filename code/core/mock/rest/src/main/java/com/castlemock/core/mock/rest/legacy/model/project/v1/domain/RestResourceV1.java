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

package com.castlemock.core.mock.rest.legacy.model.project.v1.domain;


import com.castlemock.core.basis.model.Saveable;
import com.castlemock.core.mock.rest.model.project.domain.RestMethod;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@XmlRootElement(name = "restMockResponse")
public class RestResourceV1 implements Saveable<String> {

    private String id;
    private String name;
    private String uri;
    private List<RestMethodV1> methods = new CopyOnWriteArrayList<RestMethodV1>();


    @Override
    @XmlElement
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlElementWrapper(name = "methods")
    @XmlElement(name = "method")
    public List<RestMethodV1> getMethods() {
        return methods;
    }

    public void setMethods(List<RestMethodV1> methods) {
        this.methods = methods;
    }
}
