package dev.sbs.api.data.sql;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.FilterFunction;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.tuple.Pair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public abstract class SqlRepository<T extends SqlModel> {

    private final Class<T> tClass;
    private final SqlSession sqlSession;

    /**
     * Creates a new repository of type {@link T}.
     *
     * @param sqlSession the sql session to use
     */
    @SuppressWarnings("unchecked")
    public SqlRepository(@NonNull SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        ParameterizedType superClass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class<T>) superClass.getActualTypeArguments()[0];
        this.findAll();
    }

    public ConcurrentList<T> findAll() throws SqlException {
        return this.sqlSession.with((ReturnSessionFunction<ConcurrentList<T>>) this::findAll);
    }

    public ConcurrentList<T> findAll(@NonNull Session session) throws SqlException {
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(this.tClass);
            Root<T> rootEntry = cq.from(this.tClass);
            CriteriaQuery<T> all = cq.select(rootEntry);

            return Concurrent.newList(session
                    .createQuery(all)
                    .setCacheable(true)
                    .getResultList()
            );
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> ConcurrentList<T> findAllOrEmpty(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        try {
            ConcurrentList<T> itemsCopy = this.findAll();
            ConcurrentList<T> allItems = Concurrent.newList();

            if (ListUtil.notEmpty(itemsCopy)) {
                allItems.addAll(itemsCopy.stream()
                        .filter(it -> {
                            boolean match = false;

                            for (Pair<FilterFunction<T, S>, S> pair : predicates)
                                match |= Objects.equals(pair.getKey().apply(it), pair.getValue());

                            return match;
                        })
                        .collect(Concurrent.toList())
                );
            }

            return allItems;
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public <S> T findFirstOrNull(@NonNull FilterFunction<T, S> function, S value) throws SqlException {
        return this.findAll()
                .stream()
                .filter(model -> Objects.equals(function.apply(model), value))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(@NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        return this.findFirstOrNull(FilterFunction.Match.ALL, predicates);
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T findFirstOrNull(FilterFunction.Match match, @NonNull Pair<@NonNull FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> allMatches = this.findAll();
        ConcurrentList<T> anyMatches = Concurrent.newList();

        if (ListUtil.notEmpty(allMatches)) {
            for (Pair<FilterFunction<T, S>, S> pair : predicates) {
                ConcurrentList<T> tempList = allMatches.stream()
                        .filter(it -> Objects.equals(pair.getKey().apply(it), pair.getValue()))
                        .collect(Concurrent.toList());

                if (match == FilterFunction.Match.ALL)
                    allMatches = tempList; // This must only shorten allMatches if matching ALL, otherwise ANY will fail
                else if (match == FilterFunction.Match.ANY)
                    anyMatches.addAll(tempList);
            }
        }

        ConcurrentList<T> returnMatches = (match == FilterFunction.Match.ALL ? allMatches : anyMatches);
        return ListUtil.isEmpty(returnMatches) ? null : returnMatches.get(0);
    }

    @SuppressWarnings({"unchecked", "varargs"}) // Written safely
    public <S> T matchAnyFirstOrNullCached(Pair<FilterFunction<T, S>, S>... predicates) throws SqlException {
        ConcurrentList<T> itemsCopy = this.findAll();

        if (ListUtil.notEmpty(itemsCopy)) {
            for (Pair<FilterFunction<T, S>, S> pair : predicates) {
                itemsCopy = itemsCopy.stream()
                        .filter(it -> Objects.equals(pair.getKey().apply(it), pair.getValue()))
                        .collect(Concurrent.toList());
            }
        }

        return ListUtil.isEmpty(itemsCopy) ? null : itemsCopy.get(0);
    }

    public long save(T model) throws SqlException {
        return this.sqlSession.transaction(session -> {
            return this.save(session, model);
        });
    }

    public long save(Session session, T model) throws SqlException {
        try {
            return (long) session.save(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void update(T model) throws SqlException {
        this.sqlSession.transaction(session -> {
            this.update(session, model);
        });
    }

    public void update(@NonNull Session session, T model) throws SqlException {
        try {
            session.update(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void saveOrUpdate(T model) throws SqlException {
        this.sqlSession.transaction(session -> {
            this.saveOrUpdate(session, model);
        });
    }

    public void saveOrUpdate(@NonNull Session session, T model) throws SqlException {
        try {
            session.saveOrUpdate(model);
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

}
