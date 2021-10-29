package dev.sbs.api.data.sql.model;

import dev.sbs.api.data.Model;

import java.time.Instant;

public interface SqlModel extends Model {

    long getId();

    Instant getUpdatedAt();

}
