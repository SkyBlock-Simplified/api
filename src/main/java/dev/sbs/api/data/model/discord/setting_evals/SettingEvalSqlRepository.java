package dev.sbs.api.data.model.discord.setting_evals;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class SettingEvalSqlRepository extends SqlRepository<SettingEvalSqlModel> {

    public SettingEvalSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
