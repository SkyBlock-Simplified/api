package gg.sbs.api.manager;

import gg.sbs.api.util.builder.EqualsBuilder;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import lombok.Getter;

public abstract class Provider {

    @Getter private final Class<?> service;

    protected Provider(Class<?> service) {
        this.service = service;
    }

    @Override
    @SuppressWarnings("all")
    public final boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}