package dev.sbs.api.data.model.skyblock.essence_perks;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EssencePerkSqlRepository extends SqlRepository<EssencePerkSqlModel> {

    public EssencePerkSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
