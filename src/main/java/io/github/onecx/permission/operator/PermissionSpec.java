package io.github.onecx.permission.operator;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionSpec {

    @JsonProperty(value = "appId", required = true)
    private String appId;

    @JsonProperty("permissions")
    private Map<String, Map<String, String>> permissions;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Map<String, Map<String, String>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Map<String, String>> permissions) {
        this.permissions = permissions;
    }

}
