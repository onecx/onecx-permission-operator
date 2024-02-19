package org.tkit.onecx.permission.operator.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.permission.operator.Permission;
import org.tkit.onecx.permission.operator.client.mappers.PermissionClientMapper;

import gen.org.tkit.onecx.permission.operator.v1.api.PermissionOperatorApi;

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
        try (var response = client.createOrUpdatePermission(spec.getProductName(), spec.getAppId(), dto)) {
            log.info("Update permission appId {} status {}", spec.getAppId(), response.getStatus());
            return response.getStatus();
        }
    }
}
