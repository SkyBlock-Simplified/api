package dev.sbs.api.data.model;

import java.io.Serializable;
import java.time.Instant;

public interface Model extends Serializable {

    Long getId();

    Instant getUpdatedAt();

}
