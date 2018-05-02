package com.predera.dmml.web.rest.vm;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplicationFactorDTO {

    @NotNull
    @JsonProperty("replication_factor")
    private int replicationFactor;

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }

    @Override
    public String toString() {
        return "ReplicationFactorDTO [replicationFactor=" + replicationFactor + "]";
    }
    
    
    
}
