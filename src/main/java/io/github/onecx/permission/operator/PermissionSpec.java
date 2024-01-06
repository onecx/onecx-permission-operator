package io.github.onecx.permission.operator;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionSpec {

    @JsonProperty(value = "appId", required = true)
    private String appId;

    @JsonProperty("permissions")
    private List<PermissionItemSpec> permissions;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<PermissionItemSpec> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionItemSpec> permissions) {
        this.permissions = permissions;
    }

    public static class PermissionItemSpec {
        @JsonProperty(value = "name")
        private String name;
        @JsonProperty(value = "resource", required = true)
        private String resource;
        @JsonProperty(value = "action", required = true)
        private String action;
        @JsonProperty(value = "description")
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
