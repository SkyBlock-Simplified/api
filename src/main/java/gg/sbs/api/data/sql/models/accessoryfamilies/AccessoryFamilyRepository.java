package gg.sbs.api.data.sql.models.accessoryfamilies;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {
    public List<AccessoryFamilyModel> findAll() {
        return findAllImpl(AccessoryFamilyModel.class);
    }
}