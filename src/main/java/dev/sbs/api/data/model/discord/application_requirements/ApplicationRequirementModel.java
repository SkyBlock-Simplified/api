package dev.sbs.api.data.model.discord.application_requirements;

import dev.sbs.api.data.model.Model;

public interface ApplicationRequirementModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    Integer getOrdinal();

}
