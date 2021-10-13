package gg.sbs.api.data.sql.function;

import gg.sbs.api.util.function.ReturnParameterFunction;
import org.hibernate.Session;

public interface ReturnSessionFunction<R> extends ReturnParameterFunction<R, Session> {

    R handle(Session session);

}