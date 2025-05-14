package dev.sbs.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import dev.sbs.api.client.Client;
import dev.sbs.api.client.impl.hypixel.HypixelClient;
import dev.sbs.api.client.impl.hypixel.request.HypixelRequest;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.client.impl.mojang.MojangProxy;
import dev.sbs.api.client.impl.mojang.client.MojangApiClient;
import dev.sbs.api.client.impl.mojang.client.MojangSessionClient;
import dev.sbs.api.client.impl.mojang.request.MinecraftServerRequest;
import dev.sbs.api.client.impl.mojang.request.MojangApiRequest;
import dev.sbs.api.client.impl.mojang.request.MojangSessionRequest;
import dev.sbs.api.client.impl.mojang.response.MojangMultiUsernameResponse;
import dev.sbs.api.client.impl.sbs.SbsClient;
import dev.sbs.api.client.impl.sbs.request.SbsRequest;
import dev.sbs.api.client.impl.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.client.impl.sbs.response.SkyBlockImagesResponse;
import dev.sbs.api.client.impl.sbs.response.SkyBlockItemsResponse;
import dev.sbs.api.client.request.IRequest;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.SessionManager;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.manager.BuilderManager;
import dev.sbs.api.manager.ClassBuilderManager;
import dev.sbs.api.manager.KeyManager;
import dev.sbs.api.manager.Manager;
import dev.sbs.api.manager.ServiceManager;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.minecraft.text.segment.ColorSegment;
import dev.sbs.api.minecraft.text.segment.LineSegment;
import dev.sbs.api.minecraft.text.segment.TextSegment;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.gson.adapter.ColorTypeAdapter;
import dev.sbs.api.util.gson.adapter.InstantTypeAdapter;
import dev.sbs.api.util.gson.adapter.NbtContentTypeAdapter;
import dev.sbs.api.util.gson.adapter.SkyBlockDateTypeAdapter;
import dev.sbs.api.util.gson.adapter.UUIDTypeAdapter;
import dev.sbs.api.util.gson.factory.OptionalTypeAdapterFactory;
import dev.sbs.api.util.gson.factory.SerializedPathTypeAdaptorFactory;
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
 * The Official SkyBlock Simplified Api.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SimplifiedApi {

    @Getter private static final @NotNull KeyManager<String, UUID> keyManager = new KeyManager<>((entry, key) -> key.equalsIgnoreCase(entry.getKey()), Manager.Mode.UPDATE);
    @Getter private static final @NotNull ServiceManager serviceManager = new ServiceManager(Manager.Mode.UPDATE);
    @Getter private static final @NotNull BuilderManager builderManager = new BuilderManager(Manager.Mode.UPDATE);
    @Getter private static final @NotNull ClassBuilderManager classBuilderManager = new ClassBuilderManager(Manager.Mode.UPDATE);

    static {
        // Provide Services
        serviceManager.add(Gson.class, new GsonBuilder()
            //.registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(NbtContent.class, new NbtContentTypeAdapter())
            .registerTypeAdapter(MojangMultiUsernameResponse.class, new MojangMultiUsernameResponse.Deserializer())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockDateTypeAdapter.RealTime())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockDateTypeAdapter.SkyBlockTime())
            .registerTypeAdapter(SkyBlockEmojisResponse.class, new SkyBlockEmojisResponse.Deserializer())
            .registerTypeAdapter(SkyBlockImagesResponse.class, new SkyBlockImagesResponse.Deserializer())
            .registerTypeAdapter(SkyBlockItemsResponse.class, new SkyBlockItemsResponse.Deserializer())
            .registerTypeAdapterFactory(new SerializedPathTypeAdaptorFactory())
            .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
            .setPrettyPrinting()
            .create()
        );
        serviceManager.add(NbtFactory.class, new NbtFactory());
        serviceManager.add(Scheduler.class, new Scheduler());
        serviceManager.add(SessionManager.class, new SessionManager());

        // Provide Class Builders
        classBuilderManager.add(MojangApiRequest.class, MojangApiClient.class);
        classBuilderManager.add(MojangSessionRequest.class, MojangSessionClient.class);
        classBuilderManager.add(SbsRequest.class, SbsClient.class);
        classBuilderManager.add(HypixelRequest.class, HypixelClient.class);

        // Provide Builders
        builderManager.add(LineSegment.class, LineSegment.Builder.class);
        builderManager.add(ColorSegment.class, ColorSegment.Builder.class);
        builderManager.add(TextSegment.class, TextSegment.Builder.class);

        // Create Api Handlers & Feign Proxies
        MojangProxy mojangProxy = new MojangProxy();
        serviceManager.add(MojangProxy.class, mojangProxy);
        serviceManager.add(MojangApiRequest.class, mojangProxy.getApiRequest());
        serviceManager.add(MojangSessionRequest.class, mojangProxy.getSessionRequest());
        serviceManager.add(MinecraftServerRequest.class, new MinecraftServerRequest());

        SbsClient sbsApiClient = new SbsClient();
        serviceManager.add(SbsClient.class, sbsApiClient);
        serviceManager.add(SbsRequest.class, sbsApiClient.build(SbsRequest.class));

        HypixelClient hypixelApiClient = new HypixelClient();
        serviceManager.add(HypixelClient.class, hypixelApiClient);
        serviceManager.add(HypixelRequest.class, hypixelApiClient.build(HypixelRequest.class));
    }

    @SneakyThrows
    public static @NotNull File getCurrentDirectory() {
        return new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

    public static @NotNull Gson getGson() {
        return serviceManager.get(Gson.class);
    }

    public static @NotNull NbtFactory getNbtFactory() {
        return serviceManager.get(NbtFactory.class);
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
     * Gets the {@link MojangProxy} used to interact with the Mojang API.
     */
    public static @NotNull MojangProxy getMojangProxy() {
        return serviceManager.get(MojangProxy.class);
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
