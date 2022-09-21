package dev.sbs.api.data.model.skyblock.accessory_data.accessory_enrichments;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import dev.sbs.api.util.helper.FormatUtil;

public interface AccessoryEnrichmentModel extends Model {

    StatModel getStat();

    Double getValue();

    default String getName() {
        return FormatUtil.format("{0} Enrichment", this.getStat().getName());
    }

}
