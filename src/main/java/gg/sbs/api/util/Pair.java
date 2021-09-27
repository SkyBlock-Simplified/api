package gg.sbs.api.util;

import java.util.Objects;

public class Pair<T, S> {
    private T first;
    private S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        if (!first.equals(that.first)) return false;
        return second.equals(that.second);
    }

    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}