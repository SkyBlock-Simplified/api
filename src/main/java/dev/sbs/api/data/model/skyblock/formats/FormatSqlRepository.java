package dev.sbs.api.data.model.skyblock.formats;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class FormatSqlRepository extends SqlRepository<FormatSqlModel> {

    public FormatSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
