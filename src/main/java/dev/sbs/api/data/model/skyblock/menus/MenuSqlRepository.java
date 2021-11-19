package dev.sbs.api.data.model.skyblock.menus;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class MenuSqlRepository extends SqlRepository<MenuSqlModel> {

    public MenuSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
