package gg.sbs.api.data.sql.models.enchantments;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.util.Pair;

import java.util.List;

public class EnchantmentRepository extends SqlRepository<EnchantmentModel> {
    public List<EnchantmentModel> findAll() {
        return findAllImpl(EnchantmentModel.class);
    }

    @Override
    public <S> EnchantmentModel findFirstOrNull(String field, S value) {
        return findFirstOrNullImpl(EnchantmentModel.class, field, value);
    }

    @Override
    @SuppressWarnings({"unchecked", "varargs"})
    public <S> EnchantmentModel findFirstOrNull(Pair<String, S>... predicates) {
        return findFirstOrNullImpl(EnchantmentModel.class, predicates);
    }
}
