package io.github.onecx.permission.operator.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gen.io.github.onecx.permission.operator.v1.api.PermissionOperatorApi;
import io.github.onecx.permission.operator.Permission;
import io.github.onecx.permission.operator.client.mappers.PermissionClientMapper;

@ApplicationScoped
public class PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    @Inject
    PermissionClientMapper mapper;

    @Inject
    @RestClient
    PermissionOperatorApi client;

    public int updatePermission(Permission permission) {
        var spec = permission.getSpec();
        var dto = mapper.map(spec);
        try (var response = client.createOrUpdatePermission(spec.getAppId(), dto)) {
            log.info("Update permission appId {} status {}", spec.getAppId(), response.getStatus());
            return response.getStatus();
        }
    }
}
