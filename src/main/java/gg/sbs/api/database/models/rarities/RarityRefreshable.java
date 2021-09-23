package gg.sbs.api.database.models.rarities;

import gg.sbs.api.database.SqlRefreshable;

public class RarityRefreshable extends SqlRefreshable<RarityModel, RarityRepository> {
    public RarityRefreshable() {
        super(RarityRepository.class);
    }
}
