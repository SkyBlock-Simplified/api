package gg.sbs.api.apiclients.converter;

import com.google.gson.*;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockIsland;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.NumberUtil;
import gg.sbs.api.util.StringUtil;

import java.lang.reflect.Type;

public class JacobsFarmingDataTypeConverter extends TypeConverter<SkyBlockIsland.JacobsFarming.Data> {

    @Override
    public SkyBlockIsland.JacobsFarming.Data deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Reflection reflection = new Reflection(SkyBlockIsland.JacobsFarming.Data.class);
        SkyBlockIsland.JacobsFarming.Data farmingData = (SkyBlockIsland.JacobsFarming.Data) new Reflection(SkyBlockIsland.JacobsFarming.Data.class).newInstance();
        String[] dataString = json.getAsString().split(":");
        String[] calendarString = dataString[1].split("_");
        int year = NumberUtil.toInt(dataString[0]);
        int month = NumberUtil.toInt(calendarString[0]);
        int day = NumberUtil.toInt(calendarString[1]);
        String collectionName = StringUtil.join(dataString, ":", 2, dataString.length);

        reflection.setValue(SkyBlockDate.class, farmingData, new SkyBlockDate(year, month, day));
        reflection.setValue(String.class, farmingData, collectionName);
        return farmingData;
    }

    @Override
    public JsonElement serialize(SkyBlockIsland.JacobsFarming.Data src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FormatUtil.format("{0}:{1}_{2}:{3}", src.getSkyBlockDate().getYear(), src.getSkyBlockDate().getMonth(), src.getSkyBlockDate().getDay(), src.getCollectionName()));
    }

}