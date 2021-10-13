package gg.sbs.api.data.sql.function;

import gg.sbs.api.util.function.VoidParameterFunction;
import org.hibernate.Session;

public interface VoidSessionFunction extends VoidParameterFunction<Session> {

    void handle(Session session);

}