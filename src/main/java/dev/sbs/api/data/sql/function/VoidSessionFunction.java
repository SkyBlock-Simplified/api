package dev.sbs.api.data.sql.function;

import org.hibernate.Session;

import java.util.function.Consumer;

public interface VoidSessionFunction extends Consumer<Session> {

}
