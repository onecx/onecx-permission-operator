package io.github.onecx.permission.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.onecx.permission.operator.client.PermissionService;
import io.github.onecx.permission.test.AbstractTest;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class PermissionControllerResponseTest extends AbstractTest {

    @InjectMock
    PermissionService productStoreService;

    @Inject
    PermissionController reconciler;

    @BeforeEach
    void beforeAll() {
        Mockito.when(productStoreService.updatePermission(any())).thenReturn(404);
    }

    @Test
    void testWrongResponse() throws Exception {
        Map<String, Map<String, String>> p1 = new HashMap<>();
        p1.put("r1", Map.of("a2", "d1"));

        var s = new PermissionSpec();
        s.setAppId("test-3");
        s.setPermissions(p1);

        Permission m = new Permission();
        m.setSpec(s);

        UpdateControl<Permission> result = reconciler.reconcile(m, null);
        assertThat(result).isNotNull();
        assertThat(result.getResource()).isNotNull();
        assertThat(result.getResource().getStatus()).isNotNull();
        assertThat(result.getResource().getStatus().getStatus()).isNotNull().isEqualTo(PermissionStatus.Status.UNDEFINED);

    }
}
