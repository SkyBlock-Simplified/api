package gg.sbs.api.database.repositories.enchantments;

import gg.sbs.api.database.repositories.SqlRepository;

import java.util.List;

public class EnchantmentRepository extends SqlRepository<EnchantmentModel> {
    public List<EnchantmentModel> findAll() {
        return findAllImpl(EnchantmentModel.class);
    }
}
