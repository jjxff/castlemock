/*
 * Copyright 2018 Karl Dahlgren
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

package com.castlemock.core.mock.graphql.model.project.domain;

import com.castlemock.core.basis.model.Saveable;
import com.castlemock.core.basis.model.http.domain.HttpHeader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.19
 */
@XmlRootElement
public class GraphQLMockResponse implements Saveable<String> {

    private String id;
    private String name;
    private GraphQLMockResponseStatus status;
    private Integer httpStatusCode;
    private boolean usingExpressions;
    private List<HttpHeader> httpHeaders = new CopyOnWriteArrayList<HttpHeader>();

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
    public GraphQLMockResponseStatus getStatus() {
        return status;
    }

    public void setStatus(GraphQLMockResponseStatus status) {
        this.status = status;
    }

    @XmlElement
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @XmlElement
    public boolean isUsingExpressions() {
        return usingExpressions;
    }

    public void setUsingExpressions(boolean usingExpressions) {
        this.usingExpressions = usingExpressions;
    }

    @XmlElementWrapper(name = "httpHeaders")
    @XmlElement(name = "httpHeader")
    public List<HttpHeader> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(List<HttpHeader> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
