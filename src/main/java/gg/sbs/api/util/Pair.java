package gg.sbs.api.util;

import lombok.Getter;
import lombok.Setter;

public class Pair<T, S> {

    @Getter @Setter private T first;
    @Getter @Setter private S second;

    public Pair(T first, S second) {
        this.first = first;
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