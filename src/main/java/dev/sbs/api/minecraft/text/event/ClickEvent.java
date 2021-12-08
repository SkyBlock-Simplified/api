package dev.sbs.api.minecraft.text.event;

import com.google.gson.JsonObject;
import lombok.Getter;

public final class ClickEvent {

    public enum Action {

        OPEN_URL,
        RUN_COMMAND,
        SUGGEST_COMMAND,

        // For Books
        CHANGE_PAGE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

    }

    @Getter
    private final Action action;
    @Getter
    private final String value;

    public ClickEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("action", this.getAction().toString());

        // CHANGE_PAGE is an integer, the rest are Strings.
        if (this.getAction() == Action.CHANGE_PAGE)
            object.addProperty("value", Integer.valueOf(this.getValue()));
        else
            object.addProperty("value", this.getValue());

        return object;
    }

    public static ClickEvent fromJson(JsonObject object) {
        String action = object.getAsJsonPrimitive("action").getAsString();
        String value = object.getAsJsonPrimitive("value").getAsString();
        return new ClickEvent(Action.valueOf(action), value);
    }

}
