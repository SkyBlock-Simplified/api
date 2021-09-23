package gg.sbs.api.database.models.accessoryfamilies;

import gg.sbs.api.database.SqlRefreshable;

public class AccessoryFamilyRefreshable extends SqlRefreshable<AccessoryFamilyModel, AccessoryFamilyRepository> {
    public AccessoryFamilyRefreshable() {
        super(AccessoryFamilyRepository.class);
    }
}
