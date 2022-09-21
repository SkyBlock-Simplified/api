package dev.sbs.api.data.model.skyblock.accessory_data.accessory_enrichments;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import org.jetbrains.annotations.NotNull;

public class AccessoryEnrichmentSqlRepository extends SqlRepository<AccessoryEnrichmentSqlModel> {

    public AccessoryEnrichmentSqlRepository(@NotNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
