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

package com.castlemock.core.mock.soap.model.project.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class SoapPort {

    private String id;
    private String name;
    private String uri;
    private String projectId;

    private List<SoapOperation> operations = new CopyOnWriteArrayList<SoapOperation>();

    private String invokeAddress;

    private Map<SoapOperationStatus, Integer> statusCount = new HashMap<SoapOperationStatus, Integer>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SoapOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<SoapOperation> operations) {
        this.operations = operations;
    }

    public Map<SoapOperationStatus, Integer> getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(Map<SoapOperationStatus, Integer> statusCount) {
        this.statusCount = statusCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getInvokeAddress() {
        return invokeAddress;
    }

    public void setInvokeAddress(String invokeAddress) {
        this.invokeAddress = invokeAddress;
    }
}
