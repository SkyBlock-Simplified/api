package gg.sbs.api.database.repositories.reforges;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class ReforgeRefreshable extends SqlRefreshable<ReforgeModel, ReforgeRepository> {
    public ReforgeRefreshable() {
        super(ReforgeRepository.class);
    }
}
