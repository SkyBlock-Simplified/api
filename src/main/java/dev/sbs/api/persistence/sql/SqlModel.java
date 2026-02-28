package dev.sbs.api.persistence.sql;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.persistence.Model;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@SuppressWarnings("all")
public interface SqlModel extends Model {

    @NotNull Instant getUpdatedAt();

    @NotNull Instant getSubmittedAt();

    default <T> T delete() {
        return (T) ((SqlRepository) SimplifiedApi.getRepository(this.getClass())).delete(this);
    }

    default <T> T save() {
        return (T) ((SqlRepository) SimplifiedApi.getRepository(this.getClass())).save(this);
    }

    default <T> T update() {
        return (T) ((SqlRepository) SimplifiedApi.getRepository(this.getClass())).update(this);
    }

}
