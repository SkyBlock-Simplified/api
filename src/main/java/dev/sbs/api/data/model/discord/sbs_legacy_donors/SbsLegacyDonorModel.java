package dev.sbs.api.data.model.discord.sbs_legacy_donors;

import dev.sbs.api.data.model.Model;

public interface SbsLegacyDonorModel extends Model {

    Long getDiscordId();

    Double getAmount();

}
