package dev.sbs.api.data.model;

import java.time.Instant;

public interface SqlModel extends Model {

    long getId();

    Instant getUpdatedAt();

}
