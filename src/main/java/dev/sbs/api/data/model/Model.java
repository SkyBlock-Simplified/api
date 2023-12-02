package dev.sbs.api.data.model;

import java.io.Serializable;
import java.time.Instant;

public interface Model extends Serializable {

    Instant getUpdatedAt();

    Instant getSubmittedAt();

}
