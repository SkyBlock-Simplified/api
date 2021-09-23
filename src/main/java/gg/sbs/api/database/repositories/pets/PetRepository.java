package gg.sbs.api.database.repositories.pets;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class PetRepository extends SqlRepository<PetModel> {
    public List<PetModel> findAll() {
        return findAllImpl(PetModel.class);
    }
}
