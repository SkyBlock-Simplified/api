package dev.sbs.api.model.sql.accessories.accessoryfamilies;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilySqlModel> {

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}
