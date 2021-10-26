package dev.sbs.api.data.sql.converter;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.model.sql.rarities.RarityRepository;
import dev.sbs.api.model.sql.rarities.RaritySqlModel;

import javax.persistence.AttributeConverter;
import java.util.List;

public class RaritySqlModelListConverter implements AttributeConverter<List<RaritySqlModel>, String> {

    @Override
    @SuppressWarnings("unchecked")
    public List<RaritySqlModel> convertToEntityAttribute(String attr) {
        if (attr == null) {
            return null;
        }
        try {
            return null;
            //return SimplifiedApi.getSqlRepository(RarityRepository.class).findFirstOrNullCached(RaritySqlModel::getOrdinal, attr);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseColumn(List<RaritySqlModel> attr) {
        try {
            return SimplifiedApi.getGson().toJson(attr);
        } catch (Exception e) {
            return "[]";
        }
    }

}
