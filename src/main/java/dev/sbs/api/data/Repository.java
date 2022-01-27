package dev.sbs.api.data;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.data.sql.function.SortFunction;
import dev.sbs.api.data.sql.function.TriFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.tuple.Pair;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public abstract class Repository<T extends Model> {

    @Getter private final Class<T> tClass;

    public Repository() {
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class<T>) superClass.getActualTypeArguments()[0];
    }

    private <C extends Comparable<C>, S> ConcurrentList<T> compare(FilterFunction.Match match, SortFunction<T, C> sortFunction, TriFunction<FilterFunction<T, S>, T, S, Boolean> compare, Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = this.findAll();

        if (match == FilterFunction.Match.ANY) {
            return itemsCopy
                .stream()
                .filter(it -> {
                    boolean matches = false;

                    for (Pair<FilterFunction<T, S>, S> predicate : predicates)
                        matches |= compare.apply(predicate.getLeft(), it, predicate.getValue());

                    return matches;
                })
                .collect(Concurrent.toList());
        } else if (match == FilterFunction.Match.ALL) {
            for (Pair<FilterFunction<T, S>, S> predicate : predicates) {
                itemsCopy = itemsCopy.stream()
                    .filter(it -> compare.apply(predicate.getLeft(), it, predicate.getRight()))
                    .collect(Concurrent.toList());
            }

            if (sortFunction != null)
                itemsCopy.sort((s1, s2) -> Comparator.comparing(sortFunction).compare(s1, s2));

            return itemsCopy;
        } else
            throw new SqlException(FormatUtil.format("Invalid match type ''{0}''.", match));
    }

    public abstract ConcurrentList<T> findAll();

    public final <C extends Comparable<C>> ConcurrentList<T> findAll(@NonNull SortFunction<T, C> sortFunction) {
        return this.findAll(sortFunction, Concurrent.newList());
    }

    public final <S> ConcurrentList<T> findAll(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll((SortFunction<T, ?>) null, function, value);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(SortFunction<T, C> sortFunction, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll(FilterFunction.Match.ALL, sortFunction, function, value);
    }

    public final <S> ConcurrentList<T> findAll(@NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findAll((SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(SortFunction<T, C> sortFunction, @NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findAll(sortFunction, Concurrent.newList(predicates));
    }

    public final <S> ConcurrentList<T> findAll(@NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findAll((SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(SortFunction<T, C> sortFunction, @NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findAll(FilterFunction.Match.ALL, sortFunction, predicates);
    }

    public final <S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll(match, (SortFunction<T, ?>) null, function, value);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, SortFunction<T, C> sortFunction, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll(match, sortFunction, Pair.of(function, value));
    }

    public final <S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, @NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findAll(match, (SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, SortFunction<T, C> sortFunction, @NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findAll(match, sortFunction, Concurrent.newList(predicates));
    }

    public final <S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, @NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findAll(match, (SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>, S> ConcurrentList<T> findAll(@NonNull FilterFunction.Match match, SortFunction<T, C> sortFunction, @NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.compare(
            match,
            sortFunction,
            (predicate, it, value) -> Objects.equals(predicate.apply(it), value),
            predicates
        );
    }

    public final <S> Optional<T> findFirst(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findFirst(FilterFunction.Match.ALL, function, value);
    }

    public final <S> Optional<T> findFirst(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findFirst(match, Pair.of(function, value));
    }

    public final <S> Optional<T> findFirst(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirst(FilterFunction.Match.ALL, predicates);
    }

    public final <S> Optional<T> findFirst(@NonNull Iterable<Pair<@NonNull FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findFirst(FilterFunction.Match.ALL, predicates);
    }

    public final <S> Optional<T> findFirst(@NonNull FilterFunction.Match match, @NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirst(match, Concurrent.newList(predicates));
    }

    public final <S> Optional<T> findFirst(@NonNull FilterFunction.Match match, @NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        ConcurrentList<T> allMatches = this.findAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    public final <S> T findFirstOrNull(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findFirst(function, value).orElse(null);
    }

    public final <S> T findFirstOrNull(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findFirstOrNull(match, Pair.of(function, value));
    }

    public final <S> T findFirstOrNull(@NonNull FilterFunction.Match match, @NonNull Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirstOrNull(match, Concurrent.newList(predicates));
    }

    public final <S> T findFirstOrNull(@NonNull FilterFunction.Match match, @NonNull Iterable<Pair<FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findFirst(match, predicates).orElse(null);
    }

    public final <S> T findFirstOrNull(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirstOrNull(Concurrent.newList(predicates));
    }

    public final <S> T findFirstOrNull(@NonNull Iterable<Pair<@NonNull FilterFunction<T, S>, S>> predicates) throws SqlException {
        return this.findFirst(predicates).orElse(null);
    }

    public final ConcurrentList<T> matchAll(@NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchAll((SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>> ConcurrentList<T> matchAll(SortFunction<T, C> sortFunction, @NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchAll(sortFunction, Concurrent.newList(predicates));
    }

    public final ConcurrentList<T> matchAll(@NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchAll((SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>> ConcurrentList<T> matchAll(SortFunction<T, C> sortFunction, @NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchAll(FilterFunction.Match.ALL, sortFunction, predicates);
    }

    public final ConcurrentList<T> matchAll(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchAll(match, (SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>> ConcurrentList<T> matchAll(@NonNull FilterFunction.Match match, SortFunction<T, C> sortFunction, @NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchAll(match, sortFunction, Concurrent.newList(predicates));
    }

    public final ConcurrentList<T> matchAll(@NonNull FilterFunction.Match match, @NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchAll(match, (SortFunction<T, ?>) null, predicates);
    }

    public final <C extends Comparable<C>> ConcurrentList<T> matchAll(@NonNull FilterFunction.Match match, SortFunction<T, C> sortFunction, @NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.compare(
            match,
            sortFunction,
            (predicate, it, value) -> predicate.apply(it),
            StreamSupport.stream(predicates.spliterator(), false)
                .map(predicate -> Pair.of(predicate, true))
                .collect(Concurrent.toList())
        );
    }

    public final Optional<T> matchFirst(@NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchFirst(Concurrent.newList(predicates));
    }

    public final Optional<T> matchFirst(@NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchFirst(FilterFunction.Match.ALL, predicates);
    }

    public final Optional<T> matchFirst(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchFirst(match, Concurrent.newList(predicates));
    }

    public final Optional<T> matchFirst(@NonNull FilterFunction.Match match, @NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        ConcurrentList<T> allMatches = this.matchAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    public final T matchFirstOrNull(@NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchFirstOrNull(Concurrent.newList(predicates));
    }

    public final T matchFirstOrNull(@NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchFirstOrNull(FilterFunction.Match.ALL, predicates);
    }

    public final T matchFirstOrNull(@NonNull FilterFunction.Match match, @NonNull FilterFunction<T, Boolean>... predicates) throws SqlException {
        return this.matchFirstOrNull(match, Concurrent.newList(predicates));
    }

    public final T matchFirstOrNull(@NonNull FilterFunction.Match match, @NonNull Iterable<FilterFunction<T, Boolean>> predicates) throws SqlException {
        return this.matchFirst(match, predicates).orElse(null);
    }

}
