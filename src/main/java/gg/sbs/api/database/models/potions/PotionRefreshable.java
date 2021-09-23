package gg.sbs.api.database.models.potions;

import gg.sbs.api.database.SqlRefreshable;

public class PotionRefreshable extends SqlRefreshable<PotionModel, PotionRepository> {
    public PotionRefreshable() {
        super(PotionRepository.class);
    }
}
