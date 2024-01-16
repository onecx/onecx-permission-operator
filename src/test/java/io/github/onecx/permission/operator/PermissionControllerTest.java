package io.github.onecx.permission.operator;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.awaitility.Awaitility.await;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.github.onecx.permission.test.AbstractTest;
import io.javaoperatorsdk.operator.Operator;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class PermissionControllerTest extends AbstractTest {

    final static Logger log = LoggerFactory.getLogger(PermissionControllerTest.class);

    @Inject
    Operator operator;

    @Inject
    KubernetesClient client;

    @BeforeAll
    public static void init() {
        Awaitility.setDefaultPollDelay(2, SECONDS);
        Awaitility.setDefaultPollInterval(2, SECONDS);
        Awaitility.setDefaultTimeout(10, SECONDS);
    }

    @Test
    void productEmptySpecTest() {

        operator.start();

        Permission data = new Permission();
        data.setMetadata(new ObjectMetaBuilder().withName("empty-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(new PermissionSpec());

        log.info("Creating test permission object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            PermissionStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(PermissionStatus.Status.ERROR);
        });
    }

    @Test
    void productNullSpecTest() {

        operator.start();

        Permission data = new Permission();
        data.setMetadata(new ObjectMetaBuilder().withName("null-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(null);

        log.info("Creating test permission object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            PermissionStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNull();
        });

    }

    @Test
    void productUpdateEmptySpecTest() {

        operator.start();

        Map<String, Map<String, String>> p1 = new HashMap<>();
        p1.put("r1", Map.of("a2", "d1"));

        var m = new PermissionSpec();
        m.setAppId("test-3");
        m.setPermissions(p1);

        var data = new Permission();
        data
                .setMetadata(new ObjectMetaBuilder().withName("to-update-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(m);

        log.info("Creating test permission object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            PermissionStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(PermissionStatus.Status.UPDATED);
        });

        client.resource(data).inNamespace(client.getNamespace())
                .edit(s -> {
                    s.setSpec(null);
                    return s;
                });

        await().pollDelay(4, SECONDS).untilAsserted(() -> {
            PermissionStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(PermissionStatus.Status.UPDATED);
        });
    }

    @Test
    void productUpdateNoDescriptionTest() {

        operator.start();

        Map<String, Map<String, String>> p1 = new HashMap<>();
        var a = new HashMap<String, String>();
        a.put("a2", null);
        p1.put("r1", a);

        var m = new PermissionSpec();
        m.setAppId("test-3");
        m.setPermissions(p1);

        var data = new Permission();
        data
                .setMetadata(new ObjectMetaBuilder().withName("to-update-spec").withNamespace(client.getNamespace()).build());
        data.setSpec(m);

        log.info("Creating test permission object: {}", data);
        var exception = catchThrowableOfType(() -> client.resource(data).serverSideApply(), KubernetesClientException.class);
        assertThat(exception).isNotNull();

    }

    @Test
    void productRestClientExceptionTest() {

        operator.start();

        Map<String, Map<String, String>> p1 = new HashMap<>();
        p1.put("r1", Map.of("a2", "d1"));

        var m = new PermissionSpec();
        m.setAppId("test-error-1");
        m.setPermissions(p1);

        var data = new Permission();
        data
                .setMetadata(new ObjectMetaBuilder().withName("client-error").withNamespace(client.getNamespace()).build());
        data.setSpec(m);

        log.info("Creating test permission object: {}", data);
        client.resource(data).serverSideApply();

        log.info("Waiting 4 seconds and status muss be still null");

        await().pollDelay(2, SECONDS).untilAsserted(() -> {
            PermissionStatus mfeStatus = client.resource(data).get().getStatus();
            assertThat(mfeStatus).isNotNull();
            assertThat(mfeStatus.getStatus()).isNotNull().isEqualTo(PermissionStatus.Status.ERROR);
        });

    }
}