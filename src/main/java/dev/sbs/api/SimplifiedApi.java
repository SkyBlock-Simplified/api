package dev.sbs.api;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import dev.sbs.api.client.ApiBuilder;
import dev.sbs.api.client.RequestInterface;
import dev.sbs.api.client.hypixel.HypixelApiBuilder;
import dev.sbs.api.client.hypixel.request.HypixelPlayerRequest;
import dev.sbs.api.client.hypixel.request.HypixelResourceRequest;
import dev.sbs.api.client.hypixel.request.HypixelSkyBlockRequest;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.client.sbs.SbsApiBuilder;
import dev.sbs.api.client.sbs.request.MojangRequest;
import dev.sbs.api.client.sbs.request.SkyBlockRequest;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.client.sbs.response.SkyBlockImagesResponse;
import dev.sbs.api.client.sbs.response.SkyBlockItemsResponse;
import dev.sbs.api.data.DataSession;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.exception.DataException;
import dev.sbs.api.data.json.JsonSession;
import dev.sbs.api.data.model.JsonModel;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.manager.BuilderManager;
import dev.sbs.api.manager.KeyManager;
import dev.sbs.api.manager.ServiceManager;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.minecraft.text.segment.LineSegment;
import dev.sbs.api.minecraft.text.segment.TextSegment;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.sort.Graph;
import dev.sbs.api.util.gson.SerializedPathTypeAdaptorFactory;
import dev.sbs.api.util.gson.adapter.ColorTypeAdapter;
import dev.sbs.api.util.gson.adapter.InstantTypeAdapter;
import dev.sbs.api.util.gson.adapter.NbtContentTypeAdapter;
import dev.sbs.api.util.gson.adapter.SkyBlockDateTypeAdapter;
import dev.sbs.api.util.gson.adapter.UUIDTypeAdapter;
import feign.gson.DoubleToIntMapTypeAdapter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * The Official SkyBlock Simplified Api.
 */
public final class SimplifiedApi {

    @Getter private static final @NotNull KeyManager<String, UUID> keyManager = new KeyManager<>((entry, key) -> key.equalsIgnoreCase(entry.getKey()));
    @Getter private static final @NotNull ServiceManager serviceManager = new ServiceManager();
    @Getter private static final @NotNull BuilderManager builderManager = new BuilderManager();

    static {
        // Provide Services
        serviceManager.add(Gson.class, new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(NbtContent.class, new NbtContentTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockDateTypeAdapter.RealTime())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockDateTypeAdapter.SkyBlockTime())
            .registerTypeAdapter(SkyBlockEmojisResponse.class, new SkyBlockEmojisResponse.Deserializer())
            .registerTypeAdapter(SkyBlockImagesResponse.class, new SkyBlockImagesResponse.Deserializer())
            .registerTypeAdapter(SkyBlockItemsResponse.class, new SkyBlockItemsResponse.Deserializer())
            .registerTypeAdapterFactory(new SerializedPathTypeAdaptorFactory())
            .setPrettyPrinting()
            .create()
        );
        serviceManager.add(NbtFactory.class, new NbtFactory());
        serviceManager.add(Scheduler.class, new Scheduler());

        // Provide Builders
        builderManager.add(MojangRequest.class, SbsApiBuilder.class);
        builderManager.add(SkyBlockRequest.class, SbsApiBuilder.class);
        builderManager.add(HypixelPlayerRequest.class, HypixelApiBuilder.class);
        builderManager.add(HypixelResourceRequest.class, HypixelApiBuilder.class);
        builderManager.add(HypixelSkyBlockRequest.class, HypixelApiBuilder.class);
        builderManager.add(String.class, StringBuilder.class);
        builderManager.add(LineSegment.class, LineSegment.Builder.class);
        builderManager.add(ColorSegment.class, ColorSegment.Builder.class);
        builderManager.add(TextSegment.class, TextSegment.Builder.class);

        // Create Api Builders
        SbsApiBuilder sbsApiBuilder = new SbsApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();
        serviceManager.add(SbsApiBuilder.class, sbsApiBuilder);
        serviceManager.add(HypixelApiBuilder.class, hypixelApiBuilder);

        // Provide Client Api Implementations
        serviceManager.add(HypixelPlayerRequest.class, hypixelApiBuilder.build(HypixelPlayerRequest.class));
        serviceManager.add(HypixelResourceRequest.class, hypixelApiBuilder.build(HypixelResourceRequest.class));
        serviceManager.add(HypixelSkyBlockRequest.class, hypixelApiBuilder.build(HypixelSkyBlockRequest.class));
        serviceManager.add(MojangRequest.class, sbsApiBuilder.build(MojangRequest.class));
        serviceManager.add(SkyBlockRequest.class, sbsApiBuilder.build(SkyBlockRequest.class));
    }

    /**
     * Connects to a database as defined in the provided {@link SqlConfig}.
     *
     * @param sqlConfig Database to connect to.
     */
    public static void connectSession(@NotNull DataSession.Type sessionType, @NotNull SqlConfig sqlConfig) {
        if (!isSessionActive()) {
            // Create Session
            DataSession<?> session = switch (sessionType) {
                case SQL -> new SqlSession(sqlConfig, getModels(SqlModel.class));
                case JSON -> new JsonSession(getModels(JsonModel.class));
            };
            serviceManager.replace(DataSession.class, session);

            // Initialize Session
            session.initialize();

            // Cache Repositories
            session.cacheRepositories();
        } else
            serviceManager.get(DataSession.class).initialize(); // Reinitialize Session
    }

    /**
     * Disconnects from a connected database.
     */
    public static void disconnectSession() {
        if (serviceManager.isRegistered(DataSession.class)) {
            DataSession<?> dataSession = serviceManager.get(DataSession.class);

            if (dataSession.isActive())
                dataSession.shutdown();
        } else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session is not active!")
                .build();
    }

    @SneakyThrows
    public static @NotNull File getCurrentDirectory() {
        return new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

    public static @NotNull Gson getGson() {
        return serviceManager.get(Gson.class);
    }

    public static @NotNull Logger getLog(Class<?> tClass) {
        return (Logger) LoggerFactory.getLogger(tClass);
    }

    public static @NotNull Logger getLog(String name) {
        return (Logger) LoggerFactory.getLogger(name);
    }

    @SuppressWarnings("unchecked")
    private static <T> @NotNull ConcurrentList<Class<T>> getModels(@NotNull Class<T> clazz) {
        return Graph.builder(clazz)
            .withValues(
                Reflection.getResources()
                    .filterPackage(clazz)
                    .getSubtypesOf(clazz)
            )
            .withEdgeFunction(type -> Arrays.stream(type.getDeclaredFields())
                .map(Field::getType)
                .filter(clazz::isAssignableFrom)
                .map(fieldType -> (Class<T>) fieldType)
            )
            .build()
            .topologicalSort();
    }

    public static @NotNull NbtFactory getNbtFactory() {
        return serviceManager.get(NbtFactory.class);
    }

    public static @NotNull Scheduler getScheduler() {
        return serviceManager.get(Scheduler.class);
    }

    /**
     * Gets the {@link Repository<T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    public static <T extends Model> @NotNull Repository<T> getRepositoryOf(Class<T> tClass) {
        return getSession().getRepositoryOf(tClass);
    }

    /**
     * Gets the active {@link DataSession}.
     */
    @SuppressWarnings("unchecked")
    public static <T extends DataSession<?>> T getSession() {
        if (serviceManager.isRegistered(DataSession.class))
            return (T) serviceManager.get(DataSession.class);
        else
            throw SimplifiedException.of(DataException.class)
                .withMessage("Session has not been initialized!")
                .build();
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return serviceManager.get(tClass);
    }

    public static <T extends RequestInterface, A extends ApiBuilder<T>> A getApiBuilder(Class<A> tClass) {
        return serviceManager.get(tClass);
    }

    public static boolean isSessionActive() {
        return serviceManager.isRegistered(DataSession.class) && serviceManager.get(DataSession.class).isActive();
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
