package dev.sbs.api.data.json;

import dev.sbs.api.data.DataSession;
import dev.sbs.api.scheduler.Scheduler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class JsonSession extends DataSession<JsonModel> {

    private final @NotNull JsonConfig config;
    private final @NotNull Scheduler scheduler;

    public JsonSession(@NotNull JsonConfig config) {
        super(config.getModels(), Type.JSON);
        this.config = config;
        this.scheduler = new Scheduler();
    }

    @Override
    protected <U extends JsonModel> void addRepository(@NotNull Class<U> model) {
        this.getServiceManager().addRepository(model, new JsonRepository<>(this, model));
    }

    @Override
    protected void build() {
        // Doesn't do anything
    }

    @Override
    public void shutdown() {
        super.shutdown();
        super.serviceManager.getAll(JsonRepository.class).forEach(repository -> repository.getTask().cancel(true));
        super.serviceManager.clear();
    }

}
