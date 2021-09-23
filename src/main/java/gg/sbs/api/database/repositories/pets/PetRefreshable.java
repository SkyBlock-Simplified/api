package gg.sbs.api.database.repositories.pets;

import gg.sbs.api.database.repositories.SqlRefreshable;

public class PetRefreshable extends SqlRefreshable<PetModel, PetRepository> {
    public PetRefreshable() {
        super(PetRepository.class);
    }
}
