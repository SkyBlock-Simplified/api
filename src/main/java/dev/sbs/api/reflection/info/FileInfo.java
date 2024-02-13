package dev.sbs.api.reflection.info;

import dev.sbs.api.util.builder.hash.EqualsBuilder;
import dev.sbs.api.util.builder.hash.HashCodeBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents a file resource that can be either a class file or any other resource file
 * loadable from the class path.
 */
@RequiredArgsConstructor
public abstract class FileInfo {

    @Getter private final @NotNull File file;
    @Getter(AccessLevel.PROTECTED)
    private final @NotNull ClassLoader classLoader;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        return new EqualsBuilder()
            .append(this.getFile(), fileInfo.getFile())
            .append(this.getClassLoader(), fileInfo.getClassLoader())
            .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getFile())
            .append(this.getClassLoader())
            .build();
    }

    @Override
    public @NotNull String toString() {
        return this.getFile().toString();
    }

}