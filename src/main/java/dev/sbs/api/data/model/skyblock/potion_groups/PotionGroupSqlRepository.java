package dev.sbs.api.data.model.skyblock.potion_groups;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class PotionGroupSqlRepository extends SqlRepository<PotionGroupSqlModel> {

    public PotionGroupSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
