package gg.sbs.api.database.repositories.potions;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class PotionRefreshable extends SqlRefreshable<PotionModel, PotionRepository> {
    public PotionRefreshable() {
        super(PotionRepository.class);
    }
}
