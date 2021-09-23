package gg.sbs.api.database.repositories.itemtypes;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class ItemTypeRefreshable extends SqlRefreshable<ItemTypeModel, ItemTypeRepository> {
    public ItemTypeRefreshable() {
        super(ItemTypeRepository.class);
    }
}