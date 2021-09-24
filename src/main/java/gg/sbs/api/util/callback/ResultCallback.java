package gg.sbs.api.util.callback;

public interface ResultCallback<R, T extends Throwable> {

	void handle(R result, T error);

}