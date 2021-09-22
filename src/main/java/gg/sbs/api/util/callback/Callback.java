package gg.sbs.api.util.callback;

public interface Callback<R, T extends Throwable> {

	void handle(R result, T error);

}