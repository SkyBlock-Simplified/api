package gg.sbs.api.database.models.pets;

import gg.sbs.api.database.models.SqlRepository;

import java.util.List;

public class PetRepository extends SqlRepository<PetModel> {
    public List<PetModel> findAll() {
        return findAllImpl(PetModel.class);
    }
}
