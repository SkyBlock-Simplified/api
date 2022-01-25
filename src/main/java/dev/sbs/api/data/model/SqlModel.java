package dev.sbs.api.data.model;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.SqlRepository;

import java.time.Instant;

@SuppressWarnings("all")
public interface SqlModel extends Model {

    Long getId();

    Instant getUpdatedAt();

    default <T> T save() {
        return (T) ((SqlRepository) SimplifiedApi.getRepositoryOf(this.getClass())).save(this);
    }

    default <T> T update() {
        return (T) ((SqlRepository) SimplifiedApi.getRepositoryOf(this.getClass())).update(this);
    }

}
