package dev.sbs.api.util.helper.stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Spliterator;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
abstract class MapWithIndexSpliterator<F extends Spliterator<?>, R extends Object, S extends MapWithIndexSpliterator<F, R, S>> implements Spliterator<R> {

    protected final F fromSpliterator;
    protected long index;

    abstract S createSplit(F from, long i);

    @Override
    @SuppressWarnings("unchecked")
    public S trySplit() {
        Spliterator<?> splitOrNull = this.fromSpliterator.trySplit();

        if (splitOrNull == null) {
            return null;
        }

        F split = (F) splitOrNull;
        S result = createSplit(split, index);
        this.index += split.getExactSizeIfKnown();
        return result;
    }

    @Override
    public long estimateSize() {
        return this.fromSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return this.fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
    }

}