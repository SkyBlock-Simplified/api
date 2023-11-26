package dev.sbs.api.reflection.info;

import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckForNull;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents a class path resource that can be either a class file or any other resource file
 * loadable from the class path.
 */
@Getter
public class ResourceInfo {

    private final @NotNull File file;

    /**
     * Returns the fully qualified name of the resource. Such as "com/mycomp/foo/bar.txt".
     */
    private final @NotNull String resourceName;

    @Getter(AccessLevel.PROTECTED)
    private final @NotNull ClassLoader loader;

    static @NotNull ResourceInfo of(File file, String resourceName, ClassLoader loader) {
        if (resourceName.endsWith(".class")) {
            return new ClassInfo(file, resourceName, loader);
        } else {
            return new ResourceInfo(file, resourceName, loader);
        }
    }

    ResourceInfo(@NotNull File file, @NotNull String resourceName, @NotNull ClassLoader loader) {
        this.file = Objects.requireNonNull(file);
        this.resourceName = Objects.requireNonNull(resourceName);
        this.loader = Objects.requireNonNull(loader);
    }

    /**
     * Returns the url identifying the resource.
     *
     * <p>See {@link ClassLoader#getResource}
     *
     * @throws NoSuchElementException if the resource cannot be loaded through the class loader,
     *                                despite physically existing in the class path.
     */
    public final @NotNull URL url() {
        URL url = this.loader.getResource(this.getResourceName());

        if (url == null)
            throw new NoSuchElementException(this.getResourceName());

        return url;
    }

    /**
     * Returns a {@link ByteSource} view of the resource from which its bytes can be read.
     *
     * @throws NoSuchElementException if the resource cannot be loaded through the class loader,
     *                                despite physically existing in the class path.
     */
    public final @NotNull ByteSource asByteSource() {
        return Resources.asByteSource(url());
    }

    /**
     * Returns a {@link CharSource} view of the resource from which its bytes can be read as
     * characters decoded with the given {@code charset}.
     *
     * @throws NoSuchElementException if the resource cannot be loaded through the class loader,
     *                                despite physically existing in the class path.
     */
    public final @NotNull CharSource asCharSource(@NotNull Charset charset) {
        return Resources.asCharSource(url(), charset);
    }

    @Override
    public int hashCode() {
        return this.getResourceName().hashCode();
    }

    @Override
    public boolean equals(@CheckForNull Object obj) {
        if (obj instanceof ResourceInfo that)
            return this.getResourceName().equals(that.getResourceName()) && this.loader == that.loader;

        return false;
    }

    @Override
    public @NotNull String toString() {
        return this.getResourceName();
    }

}