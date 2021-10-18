package gg.sbs.api.minecraft.text.event;

import com.google.gson.JsonObject;
import lombok.Getter;

public final class HoverEvent {

    public enum Action {

        SHOW_TEXT,
        SHOW_ITEM,
        SHOW_ENTITY;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }

    @Getter final Action action;
    @Getter private final String value;

    public HoverEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("action", this.getAction().toString());
        object.addProperty("value", this.getValue());
        return object;
    }

    public static HoverEvent fromJson(JsonObject object) {
        String action = object.getAsJsonPrimitive("action").getAsString();
        String value = object.getAsJsonPrimitive("value").getAsString();
        return new HoverEvent(Action.valueOf(action), value);
    }

}