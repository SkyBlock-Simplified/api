package dev.sbs.api.data.sql.model.accessoryfamilies;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}