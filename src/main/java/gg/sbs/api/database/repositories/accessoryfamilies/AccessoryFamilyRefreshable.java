package gg.sbs.api.database.repositories.accessoryfamilies;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class AccessoryFamilyRefreshable extends SqlRefreshable<AccessoryFamilyModel, AccessoryFamilyRepository> {
    public AccessoryFamilyRefreshable() {
        super(AccessoryFamilyRepository.class);
    }
}
