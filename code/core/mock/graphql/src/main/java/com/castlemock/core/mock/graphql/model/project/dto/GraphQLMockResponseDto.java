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

package com.castlemock.core.mock.graphql.model.project.dto;

import com.castlemock.core.basis.model.http.dto.HttpHeaderDto;
import com.castlemock.core.mock.graphql.model.project.domain.GraphQLMockResponseStatus;
import org.dozer.Mapping;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.19
 */
public class GraphQLMockResponseDto {

    @Mapping("id")
    private String id;

    @Mapping("name")
    private String name;

    @Mapping("httpStatusCode")
    private Integer httpStatusCode;

    @Mapping("status")
    private GraphQLMockResponseStatus status;

    @Mapping("usingExpressions")
    private boolean usingExpressions;

    @Mapping("httpHeaders")
    private List<HttpHeaderDto> httpHeaders = new CopyOnWriteArrayList<HttpHeaderDto>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public GraphQLMockResponseStatus getStatus() {
        return status;
    }

    public void setStatus(GraphQLMockResponseStatus status) {
        this.status = status;
    }

    public boolean isUsingExpressions() {
        return usingExpressions;
    }

    public void setUsingExpressions(boolean usingExpressions) {
        this.usingExpressions = usingExpressions;
    }

    public List<HttpHeaderDto> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(List<HttpHeaderDto> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
