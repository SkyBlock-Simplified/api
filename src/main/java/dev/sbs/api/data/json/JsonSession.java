package dev.sbs.api.data.json;

import dev.sbs.api.data.DataSession;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import org.jetbrains.annotations.NotNull;

public final class JsonSession extends DataSession<JsonModel> {

    public JsonSession(@NotNull JsonConfig jsonConfig, @NotNull ConcurrentList<Class<JsonModel>> models) {
        super(models);
    }

    @Override
    protected void addRepository(@NotNull Class<? extends JsonModel> model) {
        this.serviceManager.add(model, new JsonRepository<>(model));
    }

    @Override
    protected void build() {
        // TODO: ?
    }

}
