package dev.sbs.api.data.model.skyblock.accessory_data.accessory_families;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class AccessoryFamilySqlRepository extends SqlRepository<AccessoryFamilySqlModel> {

    public AccessoryFamilySqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
