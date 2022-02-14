package dev.sbs.api.data.model.discord.settings;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SettingSqlRepository extends SqlRepository<SettingSqlModel> {

    public SettingSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
