package dev.sbs.api.data.model.discord.setting_evals;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class SettingEvalSqlRepository extends SqlRepository<SettingEvalSqlModel> {

    public SettingEvalSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
