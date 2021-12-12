package dev.sbs.api.data.model.skyblock.melodys_songs;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MelodySongSqlRepository extends SqlRepository<MelodySongPerkSqlModel> {

    public MelodySongSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
