package dev.sbs.api.data.model.discord.embed_types;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class EmbedTypeSqlRepository extends SqlRepository<EmbedTypeSqlModel> {

    public EmbedTypeSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
