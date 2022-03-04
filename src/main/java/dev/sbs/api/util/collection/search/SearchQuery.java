package dev.sbs.api.util.collection.search;

import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.search.function.SearchFunction;
import dev.sbs.api.util.collection.search.function.TriFunction;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.ListUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings({ "unchecked", "unused" })
public interface SearchQuery<E, T extends List<E>> {

    Stream<E> stream() throws DataException;

    T findAll() throws DataException;

    T toList(@NotNull Stream<E> stream) throws DataException;

    default <S> Stream<E> compare(SearchFunction.Match match, TriFunction<Function<E, S>, E, S, Boolean> compare, Iterable<Pair<Function<E, S>, S>> predicates) throws DataException {
        Stream<E> itemsCopy = this.stream();

        if (match == SearchFunction.Match.ANY) {
            itemsCopy = itemsCopy.filter(it -> {
                boolean matches = false;

                for (Pair<Function<E, S>, S> predicate : predicates)
                    matches |= compare.apply(predicate.getLeft(), it, predicate.getValue());

                return matches;
            });
        } else if (match == SearchFunction.Match.ALL) {
            for (Pair<Function<E, S>, S> predicate : predicates)
                itemsCopy = itemsCopy.filter(it -> compare.apply(predicate.getLeft(), it, predicate.getRight()));
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type ''{0}''.", match)
                .build();

        return itemsCopy;
    }

    default <S> Stream<E> contains(SearchFunction.Match match, TriFunction<Function<E, List<S>>, E, S, Boolean> compare, Iterable<Pair<Function<E, List<S>>, S>> predicates) throws DataException {
        Stream<E> itemsCopy = this.stream();

        if (match == SearchFunction.Match.ANY) {
            itemsCopy = itemsCopy.filter(it -> {
                boolean matches = false;

                for (Pair<Function<E, List<S>>, S> predicate : predicates)
                    matches |= compare.apply(predicate.getLeft(), it, predicate.getValue());

                return matches;
            });
        } else if (match == SearchFunction.Match.ALL) {
            for (Pair<Function<E, List<S>>, S> predicate : predicates)
                itemsCopy = itemsCopy.filter(it -> compare.apply(predicate.getLeft(), it, predicate.getRight()));
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Invalid match type ''{0}''.", match)
                .build();

        return itemsCopy;
    }

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

}
