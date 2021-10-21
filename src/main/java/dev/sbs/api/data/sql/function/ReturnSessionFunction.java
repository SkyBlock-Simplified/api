package dev.sbs.api.data.sql.function;

import dev.sbs.api.util.function.ReturnParameterFunction;
import org.hibernate.Session;

public interface ReturnSessionFunction<R> extends ReturnParameterFunction<R, Session> {

    R handle(Session session);

}