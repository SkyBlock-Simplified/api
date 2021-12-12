package dev.sbs.api.data.model.skyblock.hotm_perks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class HotmPerkSqlRepository extends SqlRepository<HotmPerkSqlModel> {

    public HotmPerkSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
