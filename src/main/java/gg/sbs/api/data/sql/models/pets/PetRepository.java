package gg.sbs.api.data.sql.models.pets;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class PetRepository extends SqlRepository<PetModel> {
    public List<PetModel> findAll() {
        return findAllImpl(PetModel.class);
    }
}
