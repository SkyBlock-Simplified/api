package dev.sbs.api.data.sql.function;

import dev.sbs.api.util.function.VoidParameterFunction;
import org.hibernate.Session;

public interface VoidSessionFunction extends VoidParameterFunction<Session> {

    void handle(Session session);

}
