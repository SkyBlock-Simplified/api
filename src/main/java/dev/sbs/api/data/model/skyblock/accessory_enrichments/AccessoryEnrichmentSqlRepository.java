package dev.sbs.api.data.model.skyblock.accessory_enrichments;

import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import lombok.NonNull;

public class AccessoryEnrichmentSqlRepository extends SqlRepository<AccessoryEnrichmentSqlModel> {

    public AccessoryEnrichmentSqlRepository(@NonNull SqlSession sqlSession) {
        super(sqlSession);
    }

}
