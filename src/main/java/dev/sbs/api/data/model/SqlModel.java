package dev.sbs.api.data.model;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.SqlRepository;

import java.time.Instant;

@SuppressWarnings("all")
public interface SqlModel extends Model {

    Long getId();

    Instant getUpdatedAt();

    default void save() {
        ((SqlRepository) SimplifiedApi.getRepositoryOf(this.getClass())).save(this);
    }

    default void update() {
        ((SqlRepository) SimplifiedApi.getRepositoryOf(this.getClass())).update(this);
    }

}
