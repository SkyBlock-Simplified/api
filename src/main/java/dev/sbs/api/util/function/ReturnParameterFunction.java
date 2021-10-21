package dev.sbs.api.util.function;

public interface ReturnParameterFunction<R, P> {

    R handle(P parameter);

}