package gg.sbs.api.database.repositories.accessoryfamilies;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {
    public List<AccessoryFamilyModel> findAll() {
        return findAllImpl(AccessoryFamilyModel.class);
    }
}