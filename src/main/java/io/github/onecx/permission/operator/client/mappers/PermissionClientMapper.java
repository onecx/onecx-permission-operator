package io.github.onecx.permission.operator.client.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import gen.io.github.onecx.permission.operator.v1.model.Permission;
import gen.io.github.onecx.permission.operator.v1.model.PermissionRequest;
import io.github.onecx.permission.operator.PermissionSpec;

@Mapper
public interface PermissionClientMapper {

    PermissionRequest map(PermissionSpec spec);

    List<Permission> map(List<PermissionSpec.PermissionItemSpec> spec);
}
