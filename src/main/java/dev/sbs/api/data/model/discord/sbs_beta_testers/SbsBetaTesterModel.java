package dev.sbs.api.data.model.discord.sbs_beta_testers;

import dev.sbs.api.data.model.Model;

public interface SbsBetaTesterModel extends Model {

    Long getDiscordId();

    boolean isEarly();

}
