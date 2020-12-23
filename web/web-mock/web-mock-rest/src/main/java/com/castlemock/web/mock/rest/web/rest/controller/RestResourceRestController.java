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

package com.castlemock.web.mock.rest.web.rest.controller;

import com.castlemock.core.mock.rest.model.project.domain.RestResource;
import com.castlemock.core.mock.rest.service.project.input.*;
import com.castlemock.core.mock.rest.service.project.output.CreateRestResourceOutput;
import com.castlemock.core.mock.rest.service.project.output.DeleteRestResourceOutput;
import com.castlemock.core.mock.rest.service.project.output.ReadRestResourceOutput;
import com.castlemock.core.mock.rest.service.project.output.UpdateRestResourceOutput;
import com.castlemock.web.basis.web.rest.controller.AbstractRestController;
import com.castlemock.web.mock.rest.web.rest.controller.model.UpdateRestMethodForwardedEndpointsRequest;
import com.castlemock.web.mock.rest.web.rest.controller.model.UpdateRestMethodStatusesRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/rest/rest")
@Api(value="REST - Resource", description="REST Operations for Castle Mock REST Resource", tags = {"REST - Resource"})
public class RestResourceRestController extends AbstractRestController {

    @ApiOperation(value = "Get Resource", response = RestResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved REST resource")})
    @RequestMapping(method = RequestMethod.GET,
            value = "/project/{projectId}/application/{applicationId}/resource/{resourceId}")
    @PreAuthorize("hasAuthority('READER') or hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public @ResponseBody
    ResponseEntity<RestResource> getResource(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @ApiParam(name = "resourceId", value = "The id of the resource")
            @PathVariable(value = "resourceId") final String resourceId) {
        final ReadRestResourceOutput output = super.serviceProcessor.process(ReadRestResourceInput.builder()
                .restProjectId(projectId)
                .restApplicationId(applicationId)
                .restResourceId(resourceId)
                .build());
        return ResponseEntity.ok(output.getRestResource());
    }

    @ApiOperation(value = "Delete Resource", response = RestResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted REST resource")})
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/project/{projectId}/application/{applicationId}/resource/{resourceId}")
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public ResponseEntity<RestResource> deleteResource(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @ApiParam(name = "resourceId", value = "The id of the resource")
            @PathVariable(value = "resourceId") final String resourceId) {
        final DeleteRestResourceOutput output = super.serviceProcessor.process(DeleteRestResourceInput.builder()
                .restProjectId(projectId)
                .restApplicationId(applicationId)
                .restResourceId(resourceId)
                .build());
        return ResponseEntity.ok(output.getResource());
    }

    @ApiOperation(value = "Update Resource", response = RestResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved REST resource")})
    @RequestMapping(method = RequestMethod.PUT,
            value = "/project/{projectId}/application/{applicationId}/resource/{resourceId}")
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public ResponseEntity<RestResource> updateResource(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @ApiParam(name = "resourceId", value = "The id of the resource")
            @PathVariable(value = "resourceId") final String resourceId,
            @RequestBody RestResource resource) {
        final UpdateRestResourceOutput output = super.serviceProcessor.process(UpdateRestResourceInput.builder()
                .restProjectId(projectId)
                .restApplicationId(applicationId)
                .restResourceId(resourceId)
                .restResource(resource)
                .build());
        return ResponseEntity.ok(output.getUpdatedRestResource());
    }

    @ApiOperation(value = "Create Resource", response = RestResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved REST resource")})
    @RequestMapping(method = RequestMethod.POST,
            value = "/project/{projectId}/application/{applicationId}/resource")
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public ResponseEntity<RestResource> createResource(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @RequestBody RestResource resource) {
        final CreateRestResourceOutput output = super.serviceProcessor.process(CreateRestResourceInput.builder()
                .restProjectId(projectId)
                .restApplicationId(applicationId)
                .restResource(resource)
                .build());
        return ResponseEntity.ok(output.getCreatedRestResource());
    }

    @ApiOperation(value = "Update resource statuses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated REST resource statuses")})
    @RequestMapping(method = RequestMethod.PUT, value = "/project/{projectId}/application/{applicationId}/resource/{resourceId}/method/status")
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public @ResponseBody
    ResponseEntity<Void> updateResourceStatuses(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @ApiParam(name = "resourceId", value = "The id of the resource")
            @PathVariable(value = "resourceId") final String resourceId,
            @org.springframework.web.bind.annotation.RequestBody UpdateRestMethodStatusesRequest request){
        request.getMethodIds()
                .forEach(methodId -> super.serviceProcessor.process(UpdateRestMethodsStatusInput.builder()
                        .projectId(projectId)
                        .applicationId(applicationId)
                        .resourceId(resourceId)
                        .methodId(methodId)
                        .methodStatus(request.getStatus())
                        .build()));
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update Resource forwarded endpoints")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated REST Resource forwarded endpoints")})
    @RequestMapping(method = RequestMethod.PUT, value = "/project/{projectId}/application/{applicationId}/resource/{resourceId}/method/endpoint/forwarded")
    @PreAuthorize("hasAuthority('MODIFIER') or hasAuthority('ADMIN')")
    public @ResponseBody
    ResponseEntity<Void> updateResourceForwardedEndpoints(
            @ApiParam(name = "projectId", value = "The id of the project")
            @PathVariable(value = "projectId") final String projectId,
            @ApiParam(name = "applicationId", value = "The id of the application")
            @PathVariable(value = "applicationId") final String applicationId,
            @ApiParam(name = "resourceId", value = "The id of the resource")
            @PathVariable(value = "resourceId") final String resourceId,
            @org.springframework.web.bind.annotation.RequestBody UpdateRestMethodForwardedEndpointsRequest request){
        super.serviceProcessor.process(UpdateRestMethodsForwardedEndpointInput.builder()
                .projectId(projectId)
                .applicationId(applicationId)
                .resourceId(resourceId)
                .methodIds(request.getMethodIds())
                .forwardedEndpoint(request.getForwardedEndpoint())
                .build());
        return ResponseEntity.ok().build();
    }

}
