package dev.sbs.api.data.sql.function;

import org.hibernate.Session;

import java.util.function.Function;

public interface ReturnSessionFunction<R> extends Function<Session, R> {

}
