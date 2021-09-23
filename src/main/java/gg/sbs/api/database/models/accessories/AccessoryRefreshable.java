package gg.sbs.api.database.models.accessories;

import gg.sbs.api.database.models.SqlRefreshable;

public class AccessoryRefreshable extends SqlRefreshable<AccessoryModel, AccessoryRepository> {
    public AccessoryRefreshable() {
        super(AccessoryRepository.class);
    }
}