package dev.sbs.api.util.collection.search;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.data.tuple.pair.Pair;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.stream.triple.TriPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SearchQuery<E, T extends List<E>> {

    @NotNull Stream<E> stream() throws DataException;

    @NotNull T findAll() throws DataException;

    @NotNull T toList(@NotNull Stream<E> stream) throws DataException;

    default <S> Stream<E> compare(SearchFunction.Match match, TriPredicate<Function<E, S>, E, S> compare, Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        Stream<E> itemsCopy = this.stream();

        if (match == SearchFunction.Match.ANY) {
            itemsCopy = itemsCopy.filter(it -> {
                boolean matches = false;

                for (Pair<Function<E, S>, S> predicate : predicates)
                    matches |= compare.test(predicate.getLeft(), it, predicate.getValue());

                return matches;
            });
        } else if (match == SearchFunction.Match.ALL) {
            for (Pair<Function<E, S>, S> predicate : predicates)
                itemsCopy = itemsCopy.filter(it -> compare.test(predicate.getLeft(), it, predicate.getRight()));
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type '%s'.", match)
                .build();

        return itemsCopy;
    }

    default <S> Stream<E> contains(SearchFunction.Match match, TriPredicate<Function<E, List<S>>, E, S> compare, Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        Stream<E> itemsCopy = this.stream();

        if (match == SearchFunction.Match.ANY) {
            itemsCopy = itemsCopy.filter(it -> {
                boolean matches = false;

                for (Pair<Function<E, List<S>>, S> predicate : predicates)
                    matches |= compare.test(predicate.getLeft(), it, predicate.getValue());

                return matches;
            });
        } else if (match == SearchFunction.Match.ALL) {
            for (Pair<Function<E, List<S>>, S> predicate : predicates)
                itemsCopy = itemsCopy.filter(it -> compare.test(predicate.getLeft(), it, predicate.getRight()));
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type '%s'.", match)
                .build();

        return itemsCopy;
    }

    // --- CONTAINS ALL ---
    default <S> T containsAll(@NotNull Function<E, List<S>> function, S value) throws DataException {
        return this.containsAll(SearchFunction.Match.ALL, function, value);
    }

    default <S> T containsAll(@NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll(Concurrent.newList(predicates));
    }

    default <S> T containsAll(@NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        return this.containsAll(SearchFunction.Match.ALL, predicates);
    }

    default <S> T containsAll(@NotNull SearchFunction.Match match, @NotNull Function<E, List<S>> function, S value) throws DataException {
        return this.containsAll(match, Concurrent.newList(Pair.of(function, value)));
    }

    default <S> T containsAll(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsAll(match, Concurrent.newList(predicates));
    }

    default <S> T containsAll(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        return this.toList(this.contains(
            match,
            (predicate, it, value) -> {
                try {
                    return predicate.apply(it).contains(value);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            predicates
        ));
    }

    // --- CONTAINS FIRST ---
    default <S> Optional<E> containsFirst(@NotNull Function<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(SearchFunction.Match.ALL, function, value);
    }

    default <S> Optional<E> containsFirst(@NotNull SearchFunction.Match match, @NotNull Function<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(match, Pair.of(function, value));
    }

    default <S> Optional<E> containsFirst(@NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirst(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> containsFirst(@NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> containsFirst(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirst(match, Concurrent.newList(predicates));
    }

    default <S> Optional<E> containsFirst(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        T allMatches = this.containsAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

    default <S> E containsFirstOrNull(@NotNull SearchFunction<E, List<S>> function, S value) throws DataException {
        return this.containsFirst(function, value).orElse(null);
    }

    default <S> E containsFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Function<E, List<S>> function, S value) throws DataException {
        return this.containsFirstOrNull(match, Pair.of(function, value));
    }

    default <S> E containsFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirstOrNull(match, Concurrent.newList(predicates));
    }

    default <S> E containsFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(match, predicates).orElse(null);
    }

    default <S> E containsFirstOrNull(@NotNull Pair<Function<E, List<S>>, S>... predicates) throws DataException {
        return this.containsFirstOrNull(Concurrent.newList(predicates));
    }

    default <S> E containsFirstOrNull(@NotNull Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        return this.containsFirst(predicates).orElse(null);
    }

    // --- FIND ALL ---
    default <S> T findAll(@NotNull Function<E, S> function, S value) throws DataException {
        return this.findAll(SearchFunction.Match.ALL, function, value);
    }

    default <S> T findAll(@NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findAll(Concurrent.newList(predicates));
    }

    default <S> T findAll(@NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findAll(SearchFunction.Match.ALL, predicates);
    }

    default <S> T findAll(@NotNull SearchFunction.Match match, @NotNull Function<E, S> function, S value) throws DataException {
        return this.findAll(match, Concurrent.newList(Pair.of(function, value)));
    }

    default <S> T findAll(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findAll(match, Concurrent.newList(predicates));
    }

    default <S> T findAll(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.toList(this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return Objects.equals(predicate.apply(it), value);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            predicates
        ));
    }

    // --- FIND FIRST ---
    default <S> Optional<E> findFirst(@NotNull Function<E, S> function, S value) throws DataException {
        return this.findFirst(SearchFunction.Match.ALL, function, value);
    }

    default <S> Optional<E> findFirst(@NotNull SearchFunction.Match match, @NotNull Function<E, S> function, S value) throws DataException {
        return this.findFirst(match, Pair.of(function, value));
    }

    default <S> Optional<E> findFirst(@NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findFirst(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findFirst(@NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findFirst(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findFirst(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findFirst(match, Concurrent.newList(predicates));
    }

    default <S> Optional<E> findFirst(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return Objects.equals(predicate.apply(it), value);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            predicates
        ).findFirst();
    }

    default <S> E findFirstOrNull(@NotNull SearchFunction<E, S> function, S value) throws DataException {
        return this.findFirst(function, value).orElse(null);
    }

    default <S> E findFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Function<E, S> function, S value) throws DataException {
        return this.findFirstOrNull(match, Pair.of(function, value));
    }

    default <S> E findFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findFirstOrNull(match, Concurrent.newList(predicates));
    }

    default <S> E findFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findFirst(match, predicates).orElse(null);
    }

    default <S> E findFirstOrNull(@NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findFirstOrNull(Concurrent.newList(predicates));
    }

    default <S> E findFirstOrNull(@NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findFirst(predicates).orElse(null);
    }

    // --- FIND LAST ---
    default <S> Optional<E> findLast(@NotNull Function<E, S> function, S value) throws DataException {
        return this.findLast(SearchFunction.Match.ALL, function, value);
    }

    default <S> Optional<E> findLast(@NotNull SearchFunction.Match match, @NotNull Function<E, S> function, S value) throws DataException {
        return this.findLast(match, Pair.of(function, value));
    }

    default <S> Optional<E> findLast(@NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findLast(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findLast(@NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findLast(SearchFunction.Match.ALL, predicates);
    }

    default <S> Optional<E> findLast(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findLast(match, Concurrent.newList(predicates));
    }

    default <S> Optional<E> findLast(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return Objects.equals(predicate.apply(it), value);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            predicates
        ).reduce((first, second) -> second);
    }

    // --- FIND LAST ---
    default <S> E findLastOrNull(@NotNull SearchFunction<E, S> function, S value) throws DataException {
        return this.findLast(function, value).orElse(null);
    }

    default <S> E findLastOrNull(@NotNull SearchFunction.Match match, @NotNull Function<E, S> function, S value) throws DataException {
        return this.findLastOrNull(match, Pair.of(function, value));
    }

    default <S> E findLastOrNull(@NotNull SearchFunction.Match match, @NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findLastOrNull(match, Concurrent.newList(predicates));
    }

    default <S> E findLastOrNull(@NotNull SearchFunction.Match match, @NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findLast(match, predicates).orElse(null);
    }

    default <S> E findLastOrNull(@NotNull Pair<Function<E, S>, S>... predicates) throws DataException {
        return this.findLastOrNull(Concurrent.newList(predicates));
    }

    default <S> E findLastOrNull(@NotNull Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        return this.findLast(predicates).orElse(null);
    }

    // --- MATCH ALL ---
    default T matchAll(@NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchAll(Concurrent.newList(predicates));
    }

    default T matchAll(@NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchAll(SearchFunction.Match.ALL, predicates);
    }

    default T matchAll(@NotNull SearchFunction.Match match, @NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchAll(match, Concurrent.newList(predicates));
    }

    default T matchAll(@NotNull SearchFunction.Match match, @NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.toList(this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return predicate.apply(it);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            StreamSupport.stream(predicates.spliterator(), false)
                .map(predicate -> Pair.of(predicate, true))
                .collect(Concurrent.toList())
        ));
    }

    // --- MATCH FIRST ---
    default Optional<E> matchFirst(@NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchFirst(Concurrent.newList(predicates));
    }

    default Optional<E> matchFirst(@NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchFirst(SearchFunction.Match.ALL, predicates);
    }

    default Optional<E> matchFirst(@NotNull SearchFunction.Match match, @NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchFirst(match, Concurrent.newList(predicates));
    }

    default Optional<E> matchFirst(@NotNull SearchFunction.Match match, @NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return predicate.apply(it);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            StreamSupport.stream(predicates.spliterator(), false)
                .map(predicate -> Pair.of(predicate, true))
                .collect(Concurrent.toList())
        ).findFirst();
    }

    default E matchFirstOrNull(@NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchFirstOrNull(Concurrent.newList(predicates));
    }

    default E matchFirstOrNull(@NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchFirstOrNull(SearchFunction.Match.ALL, predicates);
    }

    default E matchFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchFirstOrNull(match, Concurrent.newList(predicates));
    }

    default E matchFirstOrNull(@NotNull SearchFunction.Match match, @NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchFirst(match, predicates).orElse(null);
    }

    // --- MATCH LAST ---
    default Optional<E> matchLast(@NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchLast(Concurrent.newList(predicates));
    }

    default Optional<E> matchLast(@NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchLast(SearchFunction.Match.ALL, predicates);
    }

    default Optional<E> matchLast(@NotNull SearchFunction.Match match, @NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchLast(match, Concurrent.newList(predicates));
    }

    default Optional<E> matchLast(@NotNull SearchFunction.Match match, @NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.compare(
            match,
            (predicate, it, value) -> {
                try {
                    return predicate.apply(it);
                } catch (NullPointerException nullPointerException) {
                    return false;
                }
            },
            StreamSupport.stream(predicates.spliterator(), false)
                .map(predicate -> Pair.of(predicate, true))
                .collect(Concurrent.toList())
        ).reduce((first, second) -> second);
    }

    default E matchLastOrNull(@NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchLastOrNull(Concurrent.newList(predicates));
    }

    default E matchLastOrNull(@NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchLastOrNull(SearchFunction.Match.ALL, predicates);
    }

    default E matchLastOrNull(@NotNull SearchFunction.Match match, @NotNull Function<E, Boolean>... predicates) throws DataException {
        return this.matchLastOrNull(match, Concurrent.newList(predicates));
    }

    default E matchLastOrNull(@NotNull SearchFunction.Match match, @NotNull Iterable<Function<E, Boolean>> predicates) throws DataException {
        return this.matchLast(match, predicates).orElse(null);
    }

}
