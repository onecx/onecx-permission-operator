package org.tkit.onecx.permission.operator.client.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.tkit.onecx.permission.operator.PermissionSpec;

import gen.org.tkit.onecx.permission.operator.v1.model.Permission;
import gen.org.tkit.onecx.permission.operator.v1.model.PermissionRequest;

@Mapper
public interface PermissionClientMapper {

    PermissionRequest map(PermissionSpec spec);

    default List<Permission> map(Map<String, Map<String, String>> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return List.of();
        }
        List<Permission> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> permission : permissions.entrySet()) {
            result.addAll(map(permission.getKey(), permission.getValue()));
        }
        return result;
    }

    default List<Permission> map(String resource, Map<String, String> actions) {
        if (actions == null || actions.isEmpty()) {
            return List.of();
        }
        List<Permission> result = new ArrayList<>();
        for (Map.Entry<String, String> action : actions.entrySet()) {
            result.add(map(resource, action.getKey(), action.getValue()));
        }
        return result;
    }

    Permission map(String resource, String action, String description);
}
