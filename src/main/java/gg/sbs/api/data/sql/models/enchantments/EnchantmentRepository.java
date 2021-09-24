package gg.sbs.api.data.sql.models.enchantments;

import gg.sbs.api.data.sql.SqlRepository;

import java.util.List;

public class EnchantmentRepository extends SqlRepository<EnchantmentModel> {
    public List<EnchantmentModel> findAll() {
        return findAllImpl(EnchantmentModel.class);
    }
}
