/*
 * Copyright 2024 Karl Dahlgren
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

package com.castlemock.model.mock.rest.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The multiple response strategy defines how mocked instances should decide which response
 * should be returned to the consumer when using multiple strategies with AND logic.
 * @author Karl Dahlgren
 * @since 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonDeserialize(builder = RestMultipleResponseStrategy.Builder.class)
public class RestMultipleResponseStrategy {

    @XmlElementWrapper(name = "strategies")
    @XmlElement(name = "strategy")
    @JacksonXmlElementWrapper(localName = "strategies")
    @JacksonXmlProperty(localName = "strategy")
    private List<RestResponseStrategy> strategies;

    public RestMultipleResponseStrategy() {
        this.strategies = new ArrayList<>();
    }

    private RestMultipleResponseStrategy(final Builder builder){
        this.strategies = Objects.requireNonNull(builder.strategies, "strategies");
    }

    public List<RestResponseStrategy> getStrategies() {
        return strategies;
    }

    public boolean isEmpty() {
        return strategies == null || strategies.isEmpty();
    }

    public boolean contains(RestResponseStrategy strategy) {
        return strategies != null && strategies.contains(strategy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestMultipleResponseStrategy that = (RestMultipleResponseStrategy) o;
        return Objects.equals(strategies, that.strategies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strategies);
    }

    @Override
    public String toString() {
        return "RestMultipleResponseStrategy{" +
                "strategies=" + strategies +
                '}';
    }

    public Builder toBuilder() {
        return builder()
                .strategies(strategies);
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private List<RestResponseStrategy> strategies;

        private Builder() {
        }

        public Builder strategies(final List<RestResponseStrategy> strategies) {
            this.strategies = strategies;
            return this;
        }

        // For Jackson XML deserialization - ignore empty field
        public Builder empty(final boolean empty) {
            // This field is ignored, just for compatibility
            return this;
        }

        public RestMultipleResponseStrategy build() {
            return new RestMultipleResponseStrategy(this);
        }
    }
}
