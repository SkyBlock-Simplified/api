package dev.sbs.api.data.model.skyblock.menus;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class MenuSqlRepository extends SqlRepository<MenuSqlModel> {

    public MenuSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
