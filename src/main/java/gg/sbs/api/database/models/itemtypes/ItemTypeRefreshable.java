package gg.sbs.api.database.models.itemtypes;

import gg.sbs.api.database.models.SqlRefreshable;

public class ItemTypeRefreshable extends SqlRefreshable<ItemTypeModel, ItemTypeRepository> {
    public ItemTypeRefreshable() {
        super(ItemTypeRepository.class);
    }
}