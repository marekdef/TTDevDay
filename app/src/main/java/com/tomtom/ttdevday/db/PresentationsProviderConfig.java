package com.tomtom.ttdevday.db;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

@SimpleSQLConfig(
       name = "PresentationsProvider",
       authority = "com.tomtom.ttdevday.presentations.provider",
       database = "presentations.db",
       version = 1)

public class PresentationsProviderConfig implements ProviderConfig {

    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}