package dev.sbs.api.util.search;

import dev.sbs.api.SimplifiedException;
import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.search.function.FilterFunction;
import dev.sbs.api.util.search.function.SortFunction;
import dev.sbs.api.util.search.function.TriFunction;
import dev.sbs.api.util.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SuppressWarnings({ "unchecked", "unused" })
public interface SearchQuery<E, T extends List<E>> {

    T findAll() throws DataException;

    default <C extends Comparable<C>, S> T compare(FilterFunction.Match match, SortFunction<E, C> sortFunction, TriFunction<FilterFunction<E, S>, E, S, Boolean> compare, Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        T itemsCopy = this.findAll();

        if (match == FilterFunction.Match.ANY) {
            itemsCopy.removeIf(it -> {
                boolean matches = false;

                for (Pair<FilterFunction<E, S>, S> predicate : predicates)
                    matches |= compare.apply(predicate.getLeft(), it, predicate.getValue());

                return !matches;
            });
        } else if (match == FilterFunction.Match.ALL) {
            for (Pair<FilterFunction<E, S>, S> predicate : predicates)
                itemsCopy.removeIf(it -> !compare.apply(predicate.getLeft(), it, predicate.getRight()));

            if (sortFunction != null)
                itemsCopy.sort((s1, s2) -> Comparator.comparing(sortFunction).compare(s1, s2));

            return itemsCopy;
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type ''{0}''.", match)
                .build();

        return itemsCopy;
    }

    default <C extends Comparable<C>, S> T contains(FilterFunction.Match match, SortFunction<E, C> sortFunction, TriFunction<FilterFunction<E, List<S>>, E, S, Boolean> compare, Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        T itemsCopy = this.findAll();

        if (match == FilterFunction.Match.ANY) {
            itemsCopy.removeIf(it -> {
                boolean matches = false;

                for (Pair<FilterFunction<E, List<S>>, S> predicate : predicates)
                    matches |= compare.apply(predicate.getLeft(), it, predicate.getValue());

                return !matches;
            });
        } else if (match == FilterFunction.Match.ALL) {
            for (Pair<FilterFunction<E, List<S>>, S> predicate : predicates)
                itemsCopy.removeIf(it -> !compare.apply(predicate.getLeft(), it, predicate.getRight()));

            if (sortFunction != null)
                itemsCopy.sort((s1, s2) -> Comparator.comparing(sortFunction).compare(s1, s2));
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type ''{0}''.", match)
                .build();

        return itemsCopy;
    }

    default <C extends Comparable<C>> T containsAll(@NotNull SortFunction<E, C> sortFunction) {
        return this.containsAll(sortFunction, Concurrent.newList());
    }

    default <S> T containsAll(@NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsAll((SortFunction<E, ?>) null, function, value);
    }

    default <C extends Comparable<C>, S> T containsAll(SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsAll(FilterFunction.Match.ALL, sortFunction, function, value);
    }

    default <S> T containsAll(@NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T containsAll(SortFunction<E, C> sortFunction, @NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll(sortFunction, Concurrent.newList(predicates));
    }

    default <S> T containsAll(@NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T containsAll(SortFunction<E, C> sortFunction, @NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsAll(FilterFunction.Match.ALL, sortFunction, predicates);
    }

    default <S> T containsAll(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsAll(match, (SortFunction<E, ?>) null, function, value);
    }

    default <C extends Comparable<C>, S> T containsAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsAll(match, sortFunction, Pair.of(function, value));
    }

    default <S> T containsAll(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T containsAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll(match, sortFunction, Concurrent.newList(predicates));
    }

    default <S> T containsAll(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T containsAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.contains(
            match,
            sortFunction,
            (predicate, it, value) -> (predicate.apply(it)).contains(value),
            predicates
        );
    }

    default <S> Optional<E> containsFirst(@NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(FilterFunction.Match.ALL, function, value);
    }

    default <S> Optional<E> containsFirst(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(match, Pair.of(function, value));
    }

    default <S> Optional<E> containsFirst(@NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirst(FilterFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> containsFirst(@NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(FilterFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> containsFirst(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirst(match, Concurrent.newList(predicates));
    }

    default <S> Optional<E> containsFirst(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        T allMatches = this.containsAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    default <S> E containsFirstOrNull(@NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(function, value).orElse(null);
    }

    default <S> E containsFirstOrNull(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, List<S>> function, S value) throws DataException {
        return this.containsFirstOrNull(match, Pair.of(function, value));
    }

    default <S> E containsFirstOrNull(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirstOrNull(match, Concurrent.newList(predicates));
    }

    default <S> E containsFirstOrNull(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(match, predicates).orElse(null);
    }

    default <S> E containsFirstOrNull(@NotNull Pair<FilterFunction<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirstOrNull(Concurrent.newList(predicates));
    }

    default <S> E containsFirstOrNull(@NotNull Iterable<Pair<FilterFunction<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(predicates).orElse(null);
    }

    default <C extends Comparable<C>> T findAll(@NotNull SortFunction<E, C> sortFunction) {
        return this.findAll(sortFunction, Concurrent.newList());
    }

    default <S> T findAll(@NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findAll((SortFunction<E, ?>) null, function, value);
    }

    default <C extends Comparable<C>, S> T findAll(SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findAll(FilterFunction.Match.ALL, sortFunction, function, value);
    }

    default <S> T findAll(@NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T findAll(SortFunction<E, C> sortFunction, @NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findAll(sortFunction, Concurrent.newList(predicates));
    }

    default <S> T findAll(@NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T findAll(SortFunction<E, C> sortFunction, @NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findAll(FilterFunction.Match.ALL, sortFunction, predicates);
    }

    default <S> T findAll(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findAll(match, (SortFunction<E, ?>) null, function, value);
    }

    default <C extends Comparable<C>, S> T findAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findAll(match, sortFunction, Pair.of(function, value));
    }

    default <S> T findAll(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T findAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findAll(match, sortFunction, Concurrent.newList(predicates));
    }

    default <S> T findAll(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>, S> T findAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.compare(
            match,
            sortFunction,
            (predicate, it, value) -> Objects.equals(predicate.apply(it), value),
            predicates
        );
    }

    default <S> Optional<E> findFirst(@NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findFirst(FilterFunction.Match.ALL, function, value);
    }

    default <S> Optional<E> findFirst(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findFirst(match, Pair.of(function, value));
    }

    default <S> Optional<E> findFirst(@NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findFirst(FilterFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findFirst(@NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findFirst(FilterFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findFirst(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findFirst(match, Concurrent.newList(predicates));
    }

    default <S> Optional<E> findFirst(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        T allMatches = this.findAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    default <S> E findFirstOrNull(@NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findFirst(function, value).orElse(null);
    }

    default <S> E findFirstOrNull(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, S> function, S value) throws DataException {
        return this.findFirstOrNull(match, Pair.of(function, value));
    }

    default <S> E findFirstOrNull(@NotNull FilterFunction.Match match, @NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findFirstOrNull(match, Concurrent.newList(predicates));
    }

    default <S> E findFirstOrNull(@NotNull FilterFunction.Match match, @NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findFirst(match, predicates).orElse(null);
    }

    default <S> E findFirstOrNull(@NotNull Pair<FilterFunction<E, S>, S>... predicates) throws DataException {
        return this.findFirstOrNull(Concurrent.newList(predicates));
    }

    default <S> E findFirstOrNull(@NotNull Iterable<Pair<FilterFunction<E, S>, S>> predicates) throws DataException {
        return this.findFirst(predicates).orElse(null);
    }

    default T matchAll(@NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>> T matchAll(SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchAll(sortFunction, Concurrent.newList(predicates));
    }

    default T matchAll(@NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchAll((SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>> T matchAll(SortFunction<E, C> sortFunction, @NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchAll(FilterFunction.Match.ALL, sortFunction, predicates);
    }

    default T matchAll(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>> T matchAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchAll(match, sortFunction, Concurrent.newList(predicates));
    }

    default T matchAll(@NotNull FilterFunction.Match match, @NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchAll(match, (SortFunction<E, ?>) null, predicates);
    }

    default <C extends Comparable<C>> T matchAll(@NotNull FilterFunction.Match match, SortFunction<E, C> sortFunction, @NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.compare(
            match,
            sortFunction,
            (predicate, it, value) -> predicate.apply(it),
            StreamSupport.stream(predicates.spliterator(), false)
                .map(predicate -> Pair.of(predicate, true))
                .collect(Concurrent.toList())
        );
    }

    default Optional<E> matchFirst(@NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchFirst(Concurrent.newList(predicates));
    }

    default Optional<E> matchFirst(@NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchFirst(FilterFunction.Match.ALL, predicates);
    }

    default Optional<E> matchFirst(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchFirst(match, Concurrent.newList(predicates));
    }

    default Optional<E> matchFirst(@NotNull FilterFunction.Match match, @NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        T allMatches = this.matchAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    default E matchFirstOrNull(@NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchFirstOrNull(Concurrent.newList(predicates));
    }

    default E matchFirstOrNull(@NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchFirstOrNull(FilterFunction.Match.ALL, predicates);
    }

    default E matchFirstOrNull(@NotNull FilterFunction.Match match, @NotNull FilterFunction<E, Boolean>... predicates) throws DataException {
        return this.matchFirstOrNull(match, Concurrent.newList(predicates));
    }

    default E matchFirstOrNull(@NotNull FilterFunction.Match match, @NotNull Iterable<FilterFunction<E, Boolean>> predicates) throws DataException {
        return this.matchFirst(match, predicates).orElse(null);
    }

}
