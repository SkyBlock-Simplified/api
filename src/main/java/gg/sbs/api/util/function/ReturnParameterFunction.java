package gg.sbs.api.util.function;

public interface ReturnParameterFunction<R, P> {

    R handle(P parameter);

}