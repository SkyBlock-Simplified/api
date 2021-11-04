package dev.sbs.api.data.model.minion_uniques;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.collections.CollectionModel;

public interface MinionUniqueModel extends Model {

    int getPlaceable();

    int getUniqueCrafts();

}
