package gg.sbs.api.database.repositories.accessories;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class AccessoryRefreshable extends SqlRefreshable<AccessoryModel, AccessoryRepository> {
    public AccessoryRefreshable() {
        super(AccessoryRepository.class);
    }
}