package gg.sbs.api.data.sql.model.accessoryfamilies;

import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryFamilyRepository extends SqlRepository<AccessoryFamilyModel> {

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

    public AccessoryFamilyRepository(@NonNull SqlSession sqlSession, long fixedUpdateRateMs) {
        super(sqlSession, fixedUpdateRateMs);
    }

}