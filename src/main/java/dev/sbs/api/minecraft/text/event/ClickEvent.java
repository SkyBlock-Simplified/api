package dev.sbs.api.minecraft.text.event;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ClickEvent {

    @Getter private final Action action;
    @Getter private final String value;

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

}
