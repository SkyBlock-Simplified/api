package gg.sbs.api.database.repositories.rarities;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class RarityRefreshable extends SqlRefreshable<RarityModel, RarityRepository> {
    public RarityRefreshable() {
        super(RarityRepository.class);
    }
}
