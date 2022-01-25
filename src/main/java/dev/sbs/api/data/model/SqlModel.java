package dev.sbs.api.data.model;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.sql.SqlRepository;

import java.time.Instant;

public interface SqlModel extends Model {

    Long getId();

    Instant getUpdatedAt();

    @SuppressWarnings("all")
    default void update() {
        ((SqlRepository) SimplifiedApi.getRepositoryOf(this.getClass())).update(this);
    }

}
