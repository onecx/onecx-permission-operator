package org.tkit.onecx.permission.operator;

import jakarta.inject.Singleton;

import io.javaoperatorsdk.operator.api.config.LeaderElectionConfiguration;

@Singleton
public class LeaderConfiguration extends LeaderElectionConfiguration {

    @SuppressWarnings("java:S5738")
    public LeaderConfiguration(PermissionConfig config) {
        super(config.leaderElectionConfig().leaseName());
    }
}
