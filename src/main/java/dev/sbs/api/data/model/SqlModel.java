package dev.sbs.api.data.model;

import java.time.Instant;

public interface SqlModel extends Model {

    Long getId();

    Instant getUpdatedAt();

}
