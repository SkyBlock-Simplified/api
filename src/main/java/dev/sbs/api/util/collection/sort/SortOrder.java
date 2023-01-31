package dev.sbs.api.util.collection.sort;

import dev.sbs.api.util.helper.WordUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum SortOrder {

    ASCENDING("ASC"),
    DESCENDING("DESC");

    @Getter private final @NotNull String shortName;

    public String getName() {
        return WordUtil.capitalizeFully(this.name());
    }

}
