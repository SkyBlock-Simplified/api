package dev.sbs.api.minecraft.nbt.tags.array;

import dev.sbs.api.minecraft.nbt.registry.TagTypeRegistry;
import dev.sbs.api.minecraft.nbt.tags.Tag;
import dev.sbs.api.util.helper.ArrayUtil;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Abstract class for implementing NBT array tags.
 *
 * @param <T> the type held in the array.
 */
public abstract class ArrayTag<T> extends Tag<T[]> implements Iterable<T> {

    public static final Pattern NUMBER_PATTERN = Pattern.compile("[-0-9]+");

    protected ArrayTag(String name, T[] value) {
        super(name, value, new TagTypeRegistry());
    }

    /**
     * Appends the specified element(s) to the end of the array tag.
     *
     * @param elements element(s) to be added.
     */
    @SafeVarargs
    public final void add(T... elements) {
        this.insert(this.size() - 1, elements);
    }

    /**
     * Removes all the elements from this array tag. The array tag will be empty after this call returns.
     */
    public abstract void clear();

    @Override
    public final void forEach(Consumer<? super T> action) {
        Arrays.asList(this.getValue()).forEach(action);
    }

    /**
     * Returns the element at the specified position in this array tag.
     *
     * @param index index of the element to return.
     * @return the element at the specified position in this array tag.
     */
    public final T get(int index) {
        return this.getValue()[index];
    }

    /**
     * Inserts the specified element(s) at the specified position in this array tag.
     * Shifts the element(s) currently at that position and any subsequent elements to the right.
     *
     * @param index    index at which the element(s) are to be inserted.
     * @param elements element(s) to be inserted.
     */
    @SafeVarargs
    public final void insert(int index, @NonNull T... elements) {
        this.value = ArrayUtil.insert(index, this.value, elements);
    }

    @NotNull
    @Override
    public final Iterator<T> iterator() {
        return Arrays.asList(this.getValue()).iterator();
    }

    /**
     * Removes the element at the specified position in this array tag.
     * Shifts any subsequent elements to the left. Returns the element that was removed from the array tag.
     *
     * @param index the index of the element to be removed.
     * @return the element previously at the specified position.
     */
    public final T remove(int index) {
        T previous = this.value[index];
        this.value = ArrayUtil.remove(this.value, index);
        return previous;
    }


    /**
     * Replaces the element at the specified position in this array tag with the specified element.
     *
     * @param index   index of the element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     */
    public final T set(int index, @NonNull T element) {
        return this.value[index] = element;
    }

    /**
     * Returns the number of elements in this array tag.
     *
     * @return the number of elements in this array tag.
     */
    public final int size() {
        return this.getValue().length;
    }

    @Override
    public final Spliterator<T> spliterator() {
        return Arrays.asList(this.getValue()).spliterator();
    }

}
