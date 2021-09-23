package gg.sbs.api.database.models.pets;

import gg.sbs.api.database.SqlRefreshable;

public class PetRefreshable extends SqlRefreshable<PetModel, PetRepository> {
    public PetRefreshable() {
        super(PetRepository.class);
    }
}
