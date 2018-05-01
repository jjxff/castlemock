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

package com.castlemock.core.mock.soap.model.project.service.message.output;

import com.castlemock.core.basis.model.Output;
import com.castlemock.core.basis.model.validation.NotNull;
import com.castlemock.core.mock.soap.model.project.domain.SoapOperation;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class IdentifySoapOperationOutput implements Output{

    @NotNull
    private String soapProjectId;

    @NotNull
    private String soapPortId;

    @NotNull
    private String soapOperationId;

    @NotNull
    private SoapOperation soapOperation;

    public IdentifySoapOperationOutput(String soapProjectId, String soapPortId, String soapOperationId, SoapOperation soapOperation) {
        this.soapProjectId = soapProjectId;
        this.soapPortId = soapPortId;
        this.soapOperationId = soapOperationId;
        this.soapOperation = soapOperation;
    }

    public SoapOperation getSoapOperation() {
        return soapOperation;
    }

    public void setSoapOperation(SoapOperation soapOperation) {
        this.soapOperation = soapOperation;
    }

    public String getSoapProjectId() {
        return soapProjectId;
    }

    public void setSoapProjectId(String soapProjectId) {
        this.soapProjectId = soapProjectId;
    }

    public String getSoapPortId() {
        return soapPortId;
    }

    public void setSoapPortId(String soapPortId) {
        this.soapPortId = soapPortId;
    }

    public String getSoapOperationId() {
        return soapOperationId;
    }

    public void setSoapOperationId(String soapOperationId) {
        this.soapOperationId = soapOperationId;
    }
}
