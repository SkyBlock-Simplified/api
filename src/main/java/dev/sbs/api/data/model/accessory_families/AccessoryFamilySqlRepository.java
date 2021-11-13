package dev.sbs.api.data.model.accessory_families;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryFamilySqlRepository extends SqlRepository<AccessoryFamilySqlModel> {

    public AccessoryFamilySqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
