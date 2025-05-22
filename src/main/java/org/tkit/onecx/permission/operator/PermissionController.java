package org.tkit.onecx.permission.operator;

import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tkit.onecx.permission.operator.client.PermissionService;

import io.javaoperatorsdk.operator.api.config.informer.Informer;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnAddFilter;
import io.javaoperatorsdk.operator.processing.event.source.filter.OnUpdateFilter;

@ControllerConfiguration(name = "permission", generationAwareEventProcessing = false, informer = @Informer(name = "parameter", namespaces = Constants.WATCH_CURRENT_NAMESPACE, onAddFilter = PermissionController.AddFilter.class, onUpdateFilter = PermissionController.UpdateFilter.class))
public class PermissionController implements Reconciler<Permission> {

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    @Inject
    PermissionService service;

    @Override
    public ErrorStatusUpdateControl<Permission> updateErrorStatus(Permission permission, Context<Permission> context,
            Exception e) {
        int responseCode = -1;
        if (e.getCause() instanceof WebApplicationException re) {
            responseCode = re.getResponse().getStatus();
        }

        log.error("Error reconcile resource", e);
        var status = new PermissionStatus();
        status.setAppId(null);
        status.setResponseCode(responseCode);
        status.setStatus(PermissionStatus.Status.ERROR);
        status.setMessage(e.getMessage());
        permission.setStatus(status);
        return ErrorStatusUpdateControl.patchStatus(permission);
    }

    @Override
    public UpdateControl<Permission> reconcile(Permission permission, Context<Permission> context) throws Exception {
        log.info("Reconcile resource: {} appId: {}", permission.getMetadata().getName(), permission.getSpec().getAppId());

        int responseCode = service.updatePermission(permission);
        updateStatusPojo(permission, responseCode);
        log.info("Product '{}' reconciled - updating status", permission.getMetadata().getName());
        return UpdateControl.patchStatus(permission);
    }

    private void updateStatusPojo(Permission permission, int responseCode) {
        PermissionStatus result = new PermissionStatus();
        PermissionSpec spec = permission.getSpec();
        result.setAppId(spec.getAppId());
        result.setResponseCode(responseCode);
        var status = responseCode == 200 ? PermissionStatus.Status.UPDATED : PermissionStatus.Status.UNDEFINED;
        result.setStatus(status);
        permission.setStatus(result);
    }

    public static class AddFilter implements OnAddFilter<Permission> {

        @Override
        public boolean accept(Permission resource) {
            return resource.getSpec() != null;
        }
    }

    public static class UpdateFilter implements OnUpdateFilter<Permission> {

        private static final String TOUCH_ANNOTATION = "org.tkit.onecx.touchedAt";

        @Override
        public boolean accept(Permission newResource, Permission oldResource) {

            if (newResource.getSpec() == null) {
                return false;
            }

            if (touchAnnotationChanged(newResource, oldResource)) {
                return true;
            }

            return generationHasChanged(newResource, oldResource);
        }

        private boolean touchAnnotationChanged(Permission newResource, Permission oldResource) {
            return !Objects.equals(getTouchAnnotation(newResource),
                    getTouchAnnotation(oldResource));
        }

        private boolean generationHasChanged(Permission newResource, Permission oldResource) {
            return !Objects.equals(newResource.getMetadata().getGeneration(),
                    oldResource.getMetadata().getGeneration());
        }

        private String getTouchAnnotation(Permission resource) {
            return resource.getMetadata().getAnnotations().get(TOUCH_ANNOTATION);
        }
    }

}
