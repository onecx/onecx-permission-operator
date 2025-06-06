package org.tkit.onecx.permission.operator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionStatus {

    @JsonProperty("observedGeneration")
    private Long observedGeneration;

    @JsonProperty("appId")
    private String appId;

    @JsonProperty("response-code")
    private int responseCode;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("message")
    private String message;

    public enum Status {

        ERROR,

        UPDATED,

        UNDEFINED;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getObservedGeneration() {
        return observedGeneration;
    }

}
