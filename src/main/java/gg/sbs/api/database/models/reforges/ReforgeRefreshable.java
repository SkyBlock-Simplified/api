package gg.sbs.api.database.models.reforges;

import gg.sbs.api.database.SqlRefreshable;

public class ReforgeRefreshable extends SqlRefreshable<ReforgeModel, ReforgeRepository> {
    public ReforgeRefreshable() {
        super(ReforgeRepository.class);
    }
}
