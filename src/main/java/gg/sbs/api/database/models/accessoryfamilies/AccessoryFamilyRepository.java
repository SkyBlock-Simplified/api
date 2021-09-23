package gg.sbs.api.database.models.accessoryfamilies;

import gg.sbs.api.database.models.SqlRepository;

import java.util.List;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {
    public List<AccessoryFamilyModel> findAll() {
        return findAllImpl(AccessoryFamilyModel.class);
    }
}