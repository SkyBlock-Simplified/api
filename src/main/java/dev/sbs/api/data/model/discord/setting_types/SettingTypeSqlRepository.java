package dev.sbs.api.data.model.discord.setting_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SettingTypeSqlRepository extends SqlRepository<SettingTypeSqlModel> {

    public SettingTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
