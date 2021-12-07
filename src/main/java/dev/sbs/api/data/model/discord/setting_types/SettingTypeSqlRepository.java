package dev.sbs.api.data.model.discord.setting_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SettingTypeSqlRepository extends SqlRepository<SettingTypeSqlModel> {

    public SettingTypeSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
