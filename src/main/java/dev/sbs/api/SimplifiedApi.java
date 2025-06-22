package dev.sbs.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import dev.sbs.api.client.Client;
import dev.sbs.api.client.request.IRequest;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.SessionManager;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.io.gson.adapter.ColorTypeAdapter;
import dev.sbs.api.io.gson.adapter.InstantTypeAdapter;
import dev.sbs.api.io.gson.adapter.UUIDTypeAdapter;
import dev.sbs.api.io.gson.factory.OptionalTypeAdapterFactory;
import dev.sbs.api.io.gson.factory.PostInitTypeAdapterFactory;
import dev.sbs.api.io.gson.factory.SerializedPathTypeAdaptorFactory;
import dev.sbs.api.manager.BuilderManager;
import dev.sbs.api.manager.CompilerManager;
import dev.sbs.api.manager.KeyManager;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.ServiceManager;
import dev.sbs.api.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.UUID;

/**
 * The {@code SimplifiedApi} is a non-instantiable utility class that
 * serves as a utility container for managing and accessing various managers,
 * services, builders, and API clients used across the application.
 * <p>
 * This class centralizes the initialization and retrieval of dependent resources to ensure
 * a simplified and consistent interface for interacting with API components.
 * <ul>
 *     <li>Management of key-value associations through {@link KeyManager}.</li>
 *     <li>Centralized service management through {@link ServiceManager}.</li>
 *     <li>Builder class management through {@link BuilderManager}.</li>
 *     <li>Class-to-class mapping configurations through {@link CompilerManager}.</li>
 *     <li>Custom serialization configurations through {@link Gson}.</li>
 *     <li>and more...</li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimplifiedApi {

    @Getter protected static final @NotNull KeyManager<String, String> keyManager = new KeyManager<>((entry, key) -> key.equalsIgnoreCase(entry.getKey()), Manager.Mode.UPDATE);
    @Getter protected static final @NotNull ServiceManager serviceManager = new ServiceManager(Manager.Mode.UPDATE);
    @Getter protected static final @NotNull BuilderManager builderManager = new BuilderManager(Manager.Mode.UPDATE);
    @Getter protected static final @NotNull CompilerManager compilerManager = new CompilerManager(Manager.Mode.UPDATE);

    static {
        // Provide Services
        serviceManager.add(Gson.class, new GsonBuilder()
            //.registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapterFactory(new SerializedPathTypeAdaptorFactory())
            .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
            .registerTypeAdapterFactory(new PostInitTypeAdapterFactory())
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setPrettyPrinting()
            .serializeNulls()
            .create()
        );
        serviceManager.add(Scheduler.class, new Scheduler());
        serviceManager.add(SessionManager.class, new SessionManager());
    }

    @SneakyThrows
    public static @NotNull File getCurrentDirectory() {
        return new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

    public static @NotNull Gson getGson() {
        return serviceManager.get(Gson.class);
    }

    public static @NotNull Scheduler getScheduler() {
        return serviceManager.get(Scheduler.class);
    }

    /**
     * Gets the built API client for the given class of client type {@link A}.
     *
     * @param tClass Client to locate.
     * @param <T> Request type to match.
     * @param <A> Client type to match.
     */
    public static <T extends IRequest, A extends Client<T>> @NotNull A getApiClient(@NotNull Class<A> tClass) {
        return serviceManager.get(tClass);
    }

    /**
     * Gets the built API request proxy for the given class of request type {@link T}.
     * @param tClass Request proxy to locate.
     * @param <T> Request type to match.
     */
    public static <T extends IRequest> @NotNull T getApiRequest(@NotNull Class<T> tClass) {
        return serviceManager.get(tClass);
    }

    /**
     * Gets the {@link Repository<T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    public static <T extends Model> @NotNull Repository<T> getRepositoryOf(@NotNull Class<T> tClass) {
        return getSessionManager().getRepository(tClass);
    }

    public static @NotNull SessionManager getSessionManager() {
        return serviceManager.get(SessionManager.class);
    }

    /**
     * Configures the global Gson instance with new serializers and deserializers.
     *
     * @param type the type definition for the type adapter being registered
     * @param typeAdapter This object must implement at least one of the {@link TypeAdapter},
     * {@link InstanceCreator}, {@link JsonSerializer}, and a {@link JsonDeserializer} interfaces.
     */
    public static void registerGsonTypeAdapter(@NotNull Type type, @NotNull Object typeAdapter) {
        serviceManager.update(Gson.class, getGson().newBuilder().registerTypeAdapter(type, typeAdapter).create());
    }

}
