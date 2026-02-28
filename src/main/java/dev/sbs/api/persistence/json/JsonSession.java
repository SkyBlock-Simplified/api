package dev.sbs.api.persistence.json;

import dev.sbs.api.persistence.Repository;
import dev.sbs.api.persistence.Session;
import dev.sbs.api.scheduler.Scheduler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class JsonSession extends Session<JsonModel> {

    private final @NotNull JsonConfig config;
    private final @NotNull Scheduler scheduler;

    public JsonSession(@NotNull JsonConfig config) {
        super(config.getModels());
        this.config = config;
        this.scheduler = new Scheduler();
    }

    @Override
    protected void build() {
        // Doesn't do anything
    }

    @Override
    protected @NotNull <U extends JsonModel> Repository<U> createRepository(@NotNull Class<U> model) {
        return new JsonRepository<>(this, model);
    }

}
