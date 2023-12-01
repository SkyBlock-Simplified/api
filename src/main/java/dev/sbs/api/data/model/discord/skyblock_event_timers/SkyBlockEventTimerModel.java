package dev.sbs.api.data.model.discord.skyblock_event_timers;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventModel;
import dev.sbs.api.data.model.skyblock.seasons.SeasonModel;

public interface SkyBlockEventTimerModel extends Model {

    SkyBlockEventModel getEvent();

    SeasonModel getStart();

    Integer getStartDay();

    SeasonModel getEnd();

    Integer getEndDay();

}
