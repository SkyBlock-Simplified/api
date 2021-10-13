package gg.sbs.api.util.function;

public interface VoidParameterErrorFunction<R, T extends Throwable> {

	void handle(R result, T error);

}