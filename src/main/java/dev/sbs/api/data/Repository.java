package dev.sbs.api.data;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.search.SearchQuery;
import lombok.Getter;

public abstract class Repository<T extends Model> implements SearchQuery<T, ConcurrentList<T>> {

    @Getter private final Class<T> tClass;

    public Repository() {
        this.tClass = Reflection.getSuperClass(this);
    }

}
