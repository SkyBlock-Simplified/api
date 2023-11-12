package dev.sbs.api.data.model.skyblock.accessory_data.accessory_enrichments;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.stats.StatModel;

public interface AccessoryEnrichmentModel extends Model {

    StatModel getStat();

    Double getValue();

    default String getName() {
        return String.format("%s Enrichment", this.getStat().getName());
    }

}
