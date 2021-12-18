package dev.sbs.api.data;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.tuple.Pair;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class Repository<T extends Model> {

    @Getter
    private final Class<T> tClass;

    public Repository() {
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class<T>) superClass.getActualTypeArguments()[0];
    }

    public abstract ConcurrentList<T> findAll();

    public final <S> ConcurrentList<T> findAll(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll(FilterFunction.Match.ALL, function, value);
    }

    public final <S> ConcurrentList<T> findAll(FilterFunction.Match match, @NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll(match, Pair.of(function, value));
    }

    public final <S> ConcurrentList<T> findAll(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findAll(FilterFunction.Match.ALL, predicates);
    }

    public final <S> ConcurrentList<T> findAll(FilterFunction.Match match, @NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = this.findAll();

        if (ListUtil.notEmpty(itemsCopy)) {
            if (match == FilterFunction.Match.ANY) {
                return itemsCopy
                    .stream()
                    .filter(it -> {
                        boolean matches = false;

                        for (Pair<FilterFunction<T, S>, S> pair : predicates)
                            matches |= Objects.equals(pair.getKey().apply(it), pair.getValue());

                        return matches;
                    })
                    .collect(Concurrent.toList());
            } else if (match == FilterFunction.Match.ALL) {
                for (Pair<FilterFunction<T, S>, S> pair : predicates) {
                    itemsCopy = itemsCopy.stream()
                        .filter(it -> Objects.equals(pair.getKey().apply(it), pair.getValue()))
                        .collect(Concurrent.toList());
                }

                return itemsCopy;
            } else
                throw new SqlException(FormatUtil.format("Invalid match type ''{0}''.", match));
        } else
            throw new SqlException(FormatUtil.format("Unable to load all items of type ''{0}''.", this.getTClass().getSimpleName()));
    }

    public final <S> T findFirstOrNull(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findFirst(function, value).orElse(null);
    }

    public final <S> Optional<T> findFirst(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll()
            .stream()
            .filter(model -> Objects.equals(function.apply(model), value))
            .findFirst();
    }

    public final <S> T findFirstOrNull(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirst(predicates).orElse(null);
    }

    public final <S> T findFirstOrNull(FilterFunction.Match match, @NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirst(match, predicates).orElse(null);
    }

    public final <S> Optional<T> findFirst(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirst(FilterFunction.Match.ALL, predicates);
    }

    public final <S> Optional<T> findFirst(FilterFunction.Match match, @NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> allMatches = this.findAll(match, predicates);
        return Optional.ofNullable(ListUtil.isEmpty(allMatches) ? null : allMatches.get(0));
    }

}
