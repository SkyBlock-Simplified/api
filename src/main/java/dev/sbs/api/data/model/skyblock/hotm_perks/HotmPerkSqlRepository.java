package dev.sbs.api.data.model.skyblock.hotm_perks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class HotmPerkSqlRepository extends SqlRepository<HotmPerkSqlModel> {

    public HotmPerkSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
