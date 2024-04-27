package dev.sbs.api.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public final class Benchmark {

    private final @NotNull Runnable runnable;
    private final @NotNull Type type;
    private long startedAt;
    private long endedAt;

    /**
     * Gets the duration of the most recent execution.
     */
    public long getDuration() {
        return this.getEndedAt() - this.getStartedAt();
    }

    private long getTime() {
        return this.getType() == Type.MILLI ? System.currentTimeMillis() : System.nanoTime();
    }

    /**
     * Runs the benchmark. Can be called multiple times.
     */
    public @NotNull Benchmark run() {
        this.startedAt = this.getTime();
        this.runnable.run();
        this.endedAt = this.getTime();
        return this;
    }

    public enum Type {

        MILLI,
        NANO

    }

}
