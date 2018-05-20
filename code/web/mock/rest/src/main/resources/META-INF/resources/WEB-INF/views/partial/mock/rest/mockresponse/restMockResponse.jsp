<%@ include file="../../../../includes.jspf"%>
<%--
  ~ Copyright 2015 Karl Dahlgren
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<c:url var="update_response_url"  value="/web/rest/project/${restProjectId}/application/${restApplicationId}/resource/${restResourceId}/method/${restMethodId}/response/${restMockResponseId}/update" />
<div class="navigation">
    <ol class="breadcrumb">
        <li><a href="${context}/web"><spring:message code="general.breadcrumb.home"/></a></li>
        <li><a href="${context}/web/rest/project/${restProjectId}"><spring:message code="rest.breadcrumb.project"/></a></li>
        <li><a href="${context}/web/rest/project/${restProjectId}/application/${restApplicationId}"><spring:message code="rest.breadcrumb.application"/></a></li>
        <li><a href="${context}/web/rest/project/${restProjectId}/application/${restApplicationId}/resource/${restResourceId}"><spring:message code="rest.breadcrumb.resource"/></a></li>
        <li><a href="${context}/web/rest/project/${restProjectId}/application/${restApplicationId}/resource/${restResourceId}/method/${restMethodId}"><spring:message code="rest.breadcrumb.method"/></a></li>
        <li class="active"><spring:message code="rest.restmockresponse.header.response" arguments="${restMockResponse.name}"/></li>
    </ol>
</div>
<div class="container">
    <section>
        <div class="content-top">
            <div class="title">
                <h1><spring:message code="rest.restmockresponse.header.response" arguments="${restMockResponse.name}"/></h1>
            </div>
            <div class="menu" align="right">
                <sec:authorize access="hasAuthority('ADMIN') or hasAuthority('MODIFIER')">
                    <a class="btn btn-danger demo-button-disabled" href="<c:url value="/web/rest/project/${restProjectId}/application/${restApplicationId}/resource/${restResourceId}/method/${restMethodId}/response/${restMockResponseId}/delete"/>"><i class="fa fa-trash"></i> <span><spring:message code="rest.restmockresponse.button.delete"/></span></a>
                </sec:authorize>
            </div>
        </div>
        <form:form action="${update_response_url}" method="POST" modelAttribute="restMockResponse">
            <div class="content-summary">
                <table class="formTable">
                    <tr>
                        <td class="column1"><form:label path="name"><spring:message code="rest.restmockresponse.label.name"/></form:label></td>
                        <td class="column2"><form:input path="name" id="restMockResponseNameInput" /></td>
                    </tr>
                    <tr>
                        <td class="column1"><form:label path="httpStatusCode"><spring:message code="rest.restmockresponse.label.httpstatuscode"/></form:label></td>
                        <td class="column2"><form:input path="httpStatusCode" id="restMockResponseHttpResponseCodeInput"/></td>
                        <td><label id="httpCodeDefinitionLabel"><spring:message code="soap.restmockresponse.label.httpstatuscodedefinition"/>:&nbsp;</label><label id="httpCodeLabel"></label></td>
                    </tr>
                    <tr>
                        <td class="column1"><form:label path="status"><spring:message code="rest.restmockresponse.label.status"/></form:label></td>
                        <td>
                            <form:select path="status">
                                <c:forEach items="${restMockResponseStatuses}" var="restMockResponseStatus">
                                    <spring:message var="label" code="rest.type.restmockresponsestatus.${restMockResponseStatus}"/>
                                    <form:option value="${restMockResponseStatus}" label="${label}"/>
                                </c:forEach>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="column1"><form:label path="usingExpressions"><spring:message code="rest.restmockresponse.label.useexpressions"/></form:label></td>
                        <td class="column2"><span class="checkbox"><form:checkbox path="usingExpressions"/></span></td>
                    </tr>
                </table>
            </div>

            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#tab-body"><spring:message code="rest.restmockresponse.header.body"/></a></li>
                <li><a data-toggle="tab" href="#tab-headers"><spring:message code="rest.restmockresponse.header.headers"/></a></li>
            </ul>

            <div class="tab-content">
                <div id="tab-body" class="tab-pane fade in active">
                    <div>
                        <h2 class="decorated"><span><spring:message code="rest.restmockresponse.header.body"/></span></h2>
                        <div class="editor">
                            <form:textarea id="body" path="body"/>
                            <div class="editorButtons">
                                <button id="formatXmlButton" type="button"><spring:message code="rest.restmockresponse.button.formatxml"/></button>
                                <button id="formatJsonButton" type="button"><spring:message code="rest.restmockresponse.button.formatjson"/></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="tab-headers" class="tab-pane fade">
                    <h2 class="decorated"><span><spring:message code="rest.restmockresponse.header.headers"/></span></h2>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 class="panel-title"><spring:message code="rest.restmockresponse.field.addheader"/></h3>
                        </div>
                        <div class="panel-body">
                            <table class="formTable">
                                <tr>
                                    <td class="column1"><form:label path="name"><spring:message code="rest.restmockresponse.label.headername"/></form:label></td>
                                    <td class="column2"><input type="text" name="headerName" id="headerNameInput"></td>
                                </tr>
                                <tr>
                                    <td class="column1"><form:label path="name"><spring:message code="rest.restmockresponse.label.headervalue"/></form:label></td>
                                    <td class="column2"><input type="text" name="headerValue" id="headerValueInput"></td>
                                </tr>
                            </table>
                            <button class="btn btn-success" onclick="addHeader()" type="button"><i class="fa fa-plus"></i>  <span><spring:message code="rest.restmockresponse.button.addheader"/></span></button>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table class="table table-bordered table-striped table-hover sortable" id="headerTable">
                            <col width="4%">
                            <col width="48%">
                            <col width="48%">
                            <tr>
                                <th></th>
                                <th><spring:message code="rest.restmockresponse.column.headername"/></th>
                                <th><spring:message code="rest.restmockresponse.column.headervalue"/></th>
                            </tr>
                            <c:forEach items="${restMockResponse.httpHeaders}" var="httpHeader" varStatus="loopStatus">
                                <tr class="even">
                                    <td><div class="delete" onclick="removeHeader('${httpHeader.name}')"></div></td>
                                    <td><input name="httpHeaders[${loopStatus.index}].name" id="httpHeaders[${loopStatus.index}].name" value="${httpHeader.name}" type="hidden" />${httpHeader.name}</td>
                                    <td><input name="httpHeaders[${loopStatus.index}].value" id="httpHeaders[${loopStatus.index}].value" value="${httpHeader.value}" type="hidden"/>${httpHeader.value}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
            </div>
            <sec:authorize access="hasAuthority('ADMIN') or hasAuthority('MODIFIER')">
                <button class="btn btn-success demo-button-disabled" type="submit" name="submit"><i class="fa fa-plus"></i>  <span><spring:message code="rest.restmockresponse.button.updateresponse"/></span></button>
            </sec:authorize>
            <a href="<c:url value="/web/rest/project/${restProjectId}/application/${restApplicationId}/resource/${restResourceId}/method/${restMethodId}"/>" class="btn btn-danger"><i class="fa fa-times"></i> <span><spring:message code="rest.restmockresponse.button.discardchanges"/></span></a>
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </section>
</div>
<script src=<c:url value="/resources/js/headerTable.js"/>></script>
<script src=<c:url value="/resources/js/editor.js"/>></script>
<script>
    $("#restMockResponseNameInput").attr('required', '');
    $("#restMockResponseHttpResponseCodeInput").attr('required', '');
    enableTab('body');
    initiateHttpResponseCode('restMockResponseHttpResponseCodeInput','httpCodeLabel', 'httpCodeDefinitionLabel');
    registerXmlFormat('formatXmlButton','body');
    registerJsonFormat('formatJsonButton','body');
</script>
