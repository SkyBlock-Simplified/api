package dev.sbs.api.data.model.skyblock.melodys_songs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MelodySongSqlRepository extends SqlRepository<MelodySongSqlModel> {

    public MelodySongSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
