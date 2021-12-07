package dev.sbs.api.data.model.discord.skyblock_event_timers;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventModel;

import java.time.Instant;

public interface SkyBlockEventTimerModel extends Model {

    SkyBlockEventModel getEvent();

    SkyBlockDate.Season getStart();

    Integer getStartDay();

    SkyBlockDate.Season getEnd();

    Integer getEndDay();

    Instant getSubmittedAt();

}
