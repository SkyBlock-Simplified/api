package dev.sbs.api.minecraft.text.segment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LineSegment {

    @Getter private final @NotNull ConcurrentList<ColorSegment> segments;

    public static Builder builder() {
        return new Builder();
    }

    public int length() {
        return this.getSegments()
            .stream()
            .mapToInt(colorSegment -> StringUtil.length(colorSegment.getText()))
            .sum();
    }

    public JsonElement toJson() {
        JsonArray rootArray = new JsonArray();
        rootArray.add("");
        this.getSegments().forEach(segment -> rootArray.add(segment.toJson()));
        return rootArray;
    }

    public static class Builder implements dev.sbs.api.util.builder.Builder<LineSegment> {

        private final ConcurrentList<ColorSegment> segments = Concurrent.newList();

        public Builder withSegments(@NotNull ColorSegment... segments) {
            return this.withSegments(Arrays.asList(segments));
        }

        public Builder withSegments(@NotNull Iterable<ColorSegment> segments) {
            segments.forEach(this.segments::add);
            return this;
        }

        @Override
        public @NotNull LineSegment build() {
            return new LineSegment(
                this.segments.toUnmodifiableList()
            );
        }

    }

}
