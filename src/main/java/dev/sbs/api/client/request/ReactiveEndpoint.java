package dev.sbs.api.client.request;

import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public interface ReactiveEndpoint<E extends Endpoint> {

    /**
     * Retrieves the endpoints associated with the current {@code Client}.
     *
     * @return the endpoints of type {@code E}.
     */
    @NotNull E getEndpoints();

    /**
     * Wraps a blocking endpoint call in a {@link CompletableFuture}, allowing the operation
     * to be executed asynchronously.
     *
     * @param <T> The type of the result to be returned by the {@link CompletableFuture}.
     * @param endpointCall A {@link Function} that represents the blocking operation to
     *                     be executed. It takes the endpoint instance of type {@code E}
     *                     and returns a result of type {@code T}.
     * @return A {@link CompletableFuture} that wraps the result of the blocking operation
     *         once it completes.
     */
    default <T> @NotNull CompletableFuture<T> fromBlocking(@NotNull Function<E, T> endpointCall) {
        return CompletableFuture.supplyAsync(
            () -> endpointCall.apply(this.getEndpoints())
        );
    }

    /**
     * Wraps a blocking endpoint call in a {@link CompletableFuture}, allowing the
     * operation to be executed asynchronously using the provided {@link Executor}.
     *
     * @param <T> The type of the result to be returned by the {@link CompletableFuture}.
     * @param endpointCall A {@link Function} that represents the blocking operation to
     *                     be executed. It takes the endpoint instance of type {@code E}
     *                     and returns a result of type {@code T}.
     * @param executor The {@link Executor} that will be used to execute the asynchronous
     *                 operation.
     * @return A {@link CompletableFuture} that wraps the result of the blocking
     *         operation once it completes.
     */
    default <T> @NotNull CompletableFuture<T> fromBlocking(@NotNull Function<E, T> endpointCall, @NotNull Executor executor) {
        return CompletableFuture.supplyAsync(
            () -> endpointCall.apply(this.getEndpoints()),
            executor
        );
    }

    /**
     * Wraps a blocking collection-returning endpoint call in a {@link CompletableFuture},
     * allowing the operation to be executed asynchronously and returning an unmodifiable {@link ConcurrentList}.
     *
     * @param <T> The type of elements in the collection returned by the endpoint call.
     * @param endpointCall A {@link Function} that represents the blocking operation to
     *                     be executed. It takes the endpoint instance of type {@code E}
     *                     and returns a collection of elements of type {@code T}.
     * @return A {@link CompletableFuture} that wraps the result of the blocking
     *         operation once it completes with an unmodifiable {@link ConcurrentList}.
     */
    default <T> @NotNull CompletableFuture<ConcurrentList<T>> fluxFromBlocking(@NotNull Function<E, ? extends Collection<T>> endpointCall) {
        return CompletableFuture.supplyAsync(
            () -> Concurrent.newUnmodifiableList(endpointCall.apply(this.getEndpoints()))
        );
    }

}
