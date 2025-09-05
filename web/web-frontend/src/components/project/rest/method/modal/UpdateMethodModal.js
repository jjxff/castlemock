/*
 Copyright 2020 Karl Dahlgren

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import React, {PureComponent} from "react";
import axios from "axios";
import validateErrorResponse from "../../../../../utility/HttpResponseValidator";
import preventEnterEvent from "../../../../../utility/KeyboardUtility";
import {
    methodResponseStrategyFormatter,
    methodMultipleResponseStrategyFormatter,
    methodStatusFormatter
} from "../../utility/RestFormatter";
import {faCheckCircle} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

class UpdateMethodModal extends PureComponent {

    constructor(props) {
        super(props);
        this.onNameChange = this.onNameChange.bind(this);
        this.onTypeChange = this.onTypeChange.bind(this);
        this.onStatusChange = this.onStatusChange.bind(this);
        this.onResponseStrategyChange = this.onResponseStrategyChange.bind(this);
        this.onMultipleStrategyChange = this.onMultipleStrategyChange.bind(this);
        this.onForwardedEndpointChange = this.onForwardedEndpointChange.bind(this);
        this.onSimulateNetworkDelayChange = this.onSimulateNetworkDelayChange.bind(this);
        this.onAutomaticForward = this.onAutomaticForward.bind(this);
        this.onNetworkDelayChange = this.onNetworkDelayChange.bind(this);
        this.onUpdateMethodClick = this.onUpdateMethodClick.bind(this);
        this.onDefaultMockResponseIdChange = this.onDefaultMockResponseIdChange.bind(this);

        this.getMethod = this.getMethod.bind(this);

        this.state = {
            updateMethod: {},
            mockResponses: [],
            selectedMultipleStrategies: []
        };

        this.getMethod();
    }

    onNameChange(name){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                name: name
            }
        });
    }

    onTypeChange(httpMethod){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                httpMethod: httpMethod
            }
        });
    }

    onStatusChange(status){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                status: status
            }
        });
    }

    onResponseStrategyChange(responseStrategy){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                responseStrategy: responseStrategy
            }
        });
    }

    onMultipleStrategyChange(strategy, isChecked){
        let selectedStrategies = [...this.state.selectedMultipleStrategies];
        
        if(isChecked) {
            if(!selectedStrategies.includes(strategy)) {
                selectedStrategies.push(strategy);
            }
        } else {
            selectedStrategies = selectedStrategies.filter(s => s !== strategy);
        }
        
        this.setState({ 
            selectedMultipleStrategies: selectedStrategies,
            updateMethod: {
                ...this.state.updateMethod,
                multipleResponseStrategy: selectedStrategies.length > 0 ? {
                    strategies: selectedStrategies
                } : null
            }
        });
    }

    onForwardedEndpointChange(forwardedEndpoint){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                forwardedEndpoint: forwardedEndpoint,
                automaticForward: forwardedEndpoint ? this.state.updateMethod.automaticForward : false
            }
        });
    }

    onSimulateNetworkDelayChange(simulateNetworkDelay){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                simulateNetworkDelay: simulateNetworkDelay
            }
        });
    }

    onAutomaticForward(automaticForward){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                automaticForward: automaticForward
            }
        });
    }

    onNetworkDelayChange(networkDelay){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                networkDelay: networkDelay
            }
        });
    }

    getMethod() {
        axios
            .get(process.env.PUBLIC_URL + "/api/rest/rest/project/" + this.props.projectId + "/application/" + this.props.applicationId + "/resource/" + this.props.resourceId + "/method/" + this.props.methodId)
            .then(response => {
                let strategies = [];
                if (response.data.responseStrategy && response.data.responseStrategy !== "MULTIPLE") {
                    strategies = [response.data.responseStrategy];
                } else if (response.data.multipleResponseStrategy && response.data.multipleResponseStrategy.strategies) {
                    strategies = response.data.multipleResponseStrategy.strategies;
                }
                
                this.setState({
                    updateMethod: {
                        name: response.data.name,
                        httpMethod: response.data.httpMethod,
                        status: response.data.status,
                        responseStrategy: "MULTIPLE",
                        multipleResponseStrategy: strategies.length > 0 ? { strategies: strategies } : null,
                        forwardedEndpoint: response.data.forwardedEndpoint,
                        simulateNetworkDelay: response.data.simulateNetworkDelay,
                        networkDelay: response.data.networkDelay,
                        defaultMockResponseId: response.data.defaultMockResponseId,
                        automaticForward: response.data.automaticForward
                    },
                    mockResponses: response.data.mockResponses,
                    selectedMultipleStrategies: strategies
                });
            })
            .catch(error => {
                validateErrorResponse(error)
            });
    }

    onDefaultMockResponseIdChange(defaultMockResponseId){
        this.setState({ updateMethod: {
                ...this.state.updateMethod,
                defaultMockResponseId: defaultMockResponseId,
                automaticForward: defaultMockResponseId == "-- select an option --" ? this.state.updateMethod.automaticForward : false
            }
        });
    }

    onUpdateMethodClick(){
        axios
            .put(process.env.PUBLIC_URL + "/api/rest/rest/project/" + this.props.projectId + "/application/" +
                this.props.applicationId + "/resource/" + this.props.resourceId + "/method/" + this.props.methodId, this.state.updateMethod)
            .then(response => {
                this.props.getMethod();
            })
            .catch(error => {
                validateErrorResponse(error)
            });
    }

    canEnableAutomaticForward() {
        return this.state.updateMethod.forwardedEndpoint
            && (this.state.updateMethod.defaultMockResponseId == null || this.state.updateMethod.defaultMockResponseId == "-- select an option --")
    }

    render() {
        return (

            <div className="modal fade" id="updateMethodModal" tabIndex="-1" role="dialog"
                 aria-labelledby="updateMethodModalLabel" aria-hidden="true">
                <div className="modal-dialog modal-dialog-centered modal-lg" role="document">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="updateMethodModalLabel">Update method</h5>
                            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Name</label>
                                <div className="col-sm-9">
                                    <input className="form-control" type="text" value={this.state.updateMethod.name}
                                           onChange={event => this.onNameChange(event.target.value)} onKeyDown={preventEnterEvent}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Type</label>
                                <div className="col-sm-9">
                                    <select id="inputStatus" className="form-control" value={this.state.updateMethod.httpMethod}
                                            onChange={event => this.onTypeChange(event.target.value)}>
                                        <option value={"GET"}>GET</option>
                                        <option value={"POST"}>POST</option>
                                        <option value={"PUT"}>PUT</option>
                                        <option value={"HEAD"}>HEAD</option>
                                        <option value={"DELETE"}>DELETE</option>
                                        <option value={"OPTIONS"}>OPTIONS</option>
                                        <option value={"TRACE"}>TRACE</option>
                                        <option value={"PATCH"}>PATCH</option>

                                    </select>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Status</label>
                                <div className="col-sm-9">
                                    <select id="inputStatus" className="form-control" value={this.state.updateMethod.status}
                                            onChange={event => this.onStatusChange(event.target.value)}>
                                        <option value={"MOCKED"}>{methodStatusFormatter("MOCKED")}</option>
                                        <option value={"DISABLED"}>{methodStatusFormatter("DISABLED")}</option>
                                        <option value={"FORWARDED"}>{methodStatusFormatter("FORWARDED")}</option>
                                        <option value={"RECORDING"}>{methodStatusFormatter("RECORDING")}</option>
                                        <option value={"RECORD_ONCE"}>{methodStatusFormatter("RECORD_ONCE")}</option>
                                        <option value={"ECHO"}>{methodStatusFormatter("ECHO")}</option>
                                    </select>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Response strategies</label>
                                <div className="col-sm-9">
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategyRandom" 
                                                   checked={this.state.selectedMultipleStrategies.includes("RANDOM")}
                                                   onChange={event => this.onMultipleStrategyChange("RANDOM", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategyRandom">
                                                Random
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategySequence" 
                                                   checked={this.state.selectedMultipleStrategies.includes("SEQUENCE")}
                                                   onChange={event => this.onMultipleStrategyChange("SEQUENCE", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategySequence">
                                                Sequence
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategyXPath" 
                                                   checked={this.state.selectedMultipleStrategies.includes("XPATH")}
                                                   onChange={event => this.onMultipleStrategyChange("XPATH", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategyXPath">
                                                XPath
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategyJsonPath" 
                                                   checked={this.state.selectedMultipleStrategies.includes("JSON_PATH")}
                                                   onChange={event => this.onMultipleStrategyChange("JSON_PATH", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategyJsonPath">
                                                JSON Path
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategyQueryMatch" 
                                                   checked={this.state.selectedMultipleStrategies.includes("QUERY_MATCH")}
                                                   onChange={event => this.onMultipleStrategyChange("QUERY_MATCH", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategyQueryMatch">
                                                Parameter query match
                                            </label>
                                        </div>
                                        <div className="form-check">
                                            <input className="form-check-input" type="checkbox" 
                                                   id="strategyHeaderQueryMatch" 
                                                   checked={this.state.selectedMultipleStrategies.includes("HEADER_QUERY_MATCH")}
                                                   onChange={event => this.onMultipleStrategyChange("HEADER_QUERY_MATCH", event.target.checked)}/>
                                            <label className="form-check-label" htmlFor="strategyHeaderQueryMatch">
                                                Header query match
                                            </label>
                                        </div>
                                        {this.state.selectedMultipleStrategies.length > 0 && (
                                            <small className="form-text text-muted">
                                                Selected: {methodMultipleResponseStrategyFormatter(this.state.selectedMultipleStrategies)}
                                            </small>
                                        )}
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Forwarded endpoint</label>
                                <div className="col-sm-9">
                                    <input className="form-control" type="text"
                                           value={this.state.updateMethod.forwardedEndpoint}
                                           onChange={event => this.onForwardedEndpointChange(event.target.value)} onKeyDown={preventEnterEvent}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Automatic forward with no match</label>
                                <div className="col-sm-9">
                                    <input type="checkbox"
                                            checked={this.canEnableAutomaticForward() && this.state.updateMethod.automaticForward}
                                            disabled={!this.canEnableAutomaticForward()}
                                            onChange={event => this.onAutomaticForward(event.target.checked)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Simulate network delay</label>
                                <div className="col-sm-9">
                                    <input type="checkbox" checked={this.state.updateMethod.simulateNetworkDelay}
                                           onChange={event => this.onSimulateNetworkDelayChange(event.target.checked)}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Network delay</label>
                                <div className="col-sm-9">
                                    <input className="form-control" type="text" value={this.state.updateMethod.networkDelay}
                                           onChange={event => this.onNetworkDelayChange(event.target.value)} onKeyDown={preventEnterEvent}/>
                                </div>
                            </div>
                            <div className="form-group row">
                                <label className="col-sm-3 col-form-label">Default response</label>
                                <div className="col-sm-9">
                                    <select id="inputStatus" className="form-control"
                                            onChange={event => this.onDefaultMockResponseIdChange(event.target.value)}>
                                        <option value={null}> -- select an option -- </option>
                                        {this.state.mockResponses.map(mockResponse =>
                                            <option key={mockResponse.id} value={mockResponse.id} selected={mockResponse.id === this.state.updateMethod.defaultMockResponseId}>{mockResponse.name}</option>
                                        )};
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button className="btn btn-success" data-dismiss="modal" onClick={this.onUpdateMethodClick}><FontAwesomeIcon icon={faCheckCircle} className="button-icon"/>Update</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default UpdateMethodModal;