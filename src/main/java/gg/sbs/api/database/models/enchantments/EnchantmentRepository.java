package gg.sbs.api.database.models.enchantments;

import gg.sbs.api.database.SqlRepository;

import java.util.List;

public class EnchantmentRepository extends SqlRepository<EnchantmentModel> {
    public List<EnchantmentModel> findAll() {
        return findAllImpl(EnchantmentModel.class);
    }
}
