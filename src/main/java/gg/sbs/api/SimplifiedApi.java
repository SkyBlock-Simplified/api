package gg.sbs.api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.gson.DoubleToIntMapTypeAdapter;
import gg.sbs.api.apiclients.RequestInterface;
import gg.sbs.api.apiclients.converter.InstantTypeConverter;
import gg.sbs.api.apiclients.converter.NbtContentTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockRealTimeTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockTimeTypeConverter;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelPlayerData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelResourceData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockIsland;
import gg.sbs.api.apiclients.mojang.MojangApiBuilder;
import gg.sbs.api.apiclients.mojang.implementation.MojangData;
import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.accessories.AccessoryRepository;
import gg.sbs.api.data.sql.model.accessoryfamilies.AccessoryFamilyRepository;
import gg.sbs.api.data.sql.model.collectionitems.CollectionItemRepository;
import gg.sbs.api.data.sql.model.collectionitemtiers.CollectionItemTierRepository;
import gg.sbs.api.data.sql.model.collections.CollectionRepository;
import gg.sbs.api.data.sql.model.enchantments.EnchantmentRepository;
import gg.sbs.api.data.sql.model.fairysouls.FairySoulRepository;
import gg.sbs.api.data.sql.model.formats.FormatRepository;
import gg.sbs.api.data.sql.model.items.ItemRepository;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import gg.sbs.api.data.sql.model.locationareas.LocationAreaRepository;
import gg.sbs.api.data.sql.model.locations.LocationRepository;
import gg.sbs.api.data.sql.model.minionitems.MinionItemRepository;
import gg.sbs.api.data.sql.model.minions.MinionRepository;
import gg.sbs.api.data.sql.model.miniontiers.MinionTierRepository;
import gg.sbs.api.data.sql.model.miniontierupgrades.MinionTierUpgradeRepository;
import gg.sbs.api.data.sql.model.pets.PetRepository;
import gg.sbs.api.data.sql.model.potions.PotionRepository;
import gg.sbs.api.data.sql.model.rarities.RarityRepository;
import gg.sbs.api.data.sql.model.reforges.ReforgeRepository;
import gg.sbs.api.data.sql.model.skilllevels.SkillLevelRepository;
import gg.sbs.api.data.sql.model.skills.SkillRepository;
import gg.sbs.api.data.sql.model.stats.StatRepository;
import gg.sbs.api.manager.builder.BuilderManager;
import gg.sbs.api.manager.service.ServiceManager;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.util.TimeUtil;
import gg.sbs.api.util.builder.hashcode.HashCodeBuilder;
import gg.sbs.api.util.builder.string.StringBuilder;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.io.File;
import java.time.Instant;
import java.util.Map;

public class SimplifiedApi {

    private static final ServiceManager serviceManager = new ServiceManager();
    private static final BuilderManager builderManager = new BuilderManager();
    private static boolean databaseEnabled = false;
    private static boolean databaseRegistered = false;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            .registerTypeAdapter(SkyBlockIsland.NbtContent.class, new NbtContentTypeConverter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeConverter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeConverter())
            .setPrettyPrinting().create();
    private static final SimplifiedConfig config;

    static {
        // Load Config
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            config = new SimplifiedConfig(currentDir.getParentFile(), "simplified");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }

        // Provide Services
        serviceManager.provide(Scheduler.class, Scheduler.getInstance());
        serviceManager.provide(Gson.class, gson);

        // Create Api Builders
        MojangApiBuilder mojangApiBuilder = new MojangApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();
        hypixelApiBuilder.setApiKey(config.getHypixelApiKey());

        // Provide Api Builders
        builderManager.provide(MojangData.class, MojangApiBuilder.class);
        builderManager.provide(HypixelPlayerData.class, HypixelApiBuilder.class);
        builderManager.provide(HypixelResourceData.class, HypixelApiBuilder.class);
        builderManager.provide(HypixelSkyBlockData.class, HypixelApiBuilder.class);
        builderManager.provide(String.class, StringBuilder.class);
        builderManager.provide(Integer.class, HashCodeBuilder.class);

        // Provide Api Implementations
        serviceManager.provide(HypixelPlayerData.class, hypixelApiBuilder.build(HypixelPlayerData.class));
        serviceManager.provide(HypixelResourceData.class, hypixelApiBuilder.build(HypixelResourceData.class));
        serviceManager.provide(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
        serviceManager.provide(MojangData.class, mojangApiBuilder.build(MojangData.class));
    }

    public static void enableDatabase() {
        if (!databaseRegistered) {
            // Load SqlSession
            SqlSession sqlSession = new SqlSession(getConfig(), getSqlRepositoryClasses());
            serviceManager.provide(SqlSession.class, sqlSession);

            // Provide SqlRepositories
            for (Class<? extends SqlRepository<? extends SqlModel>> repository : getSqlRepositoryClasses()) {
                serviceManager.provideRaw(repository, new Reflection(repository).newInstance(sqlSession));
            }
            // TODO: This works but generates an error, see bottom of class

            databaseRegistered = true;
        }

        databaseEnabled = true;
    }

    public static void disableDatabase() {
        if (databaseEnabled) {
            SqlRepository.shutdownRefreshers();
            databaseEnabled = false;
        }
    }

    public static BuilderManager getBuilderManager() {
        return builderManager;
    }

    public static SimplifiedConfig getConfig() {
        return config;
    }

    public static File getCurrentDirectory() {
        return getConfig().getParentDirectory();
    }

    public static Gson getGson() {
        return getServiceManager().getProvider(Gson.class);
    }

    public static Scheduler getScheduler() {
        return getServiceManager().getProvider(Scheduler.class);
    }

    private static ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> tClass) {
        return getServiceManager().getProvider(tClass);
    }

    public static ConcurrentSet<Class<? extends SqlRepository<? extends SqlModel>>> getSqlRepositoryClasses() {
        return Concurrent.newSet(
                AccessoryRepository.class,
                AccessoryFamilyRepository.class,
                CollectionRepository.class,
                CollectionItemRepository.class,
                CollectionItemTierRepository.class,
                EnchantmentRepository.class,
                FairySoulRepository.class,
                FormatRepository.class,
                ItemRepository.class,
                ItemTypeRepository.class,
                LocationRepository.class,
                LocationAreaRepository.class,
                MinionRepository.class,
                MinionItemRepository.class,
                MinionTierRepository.class,
                MinionTierUpgradeRepository.class,
                PetRepository.class,
                PotionRepository.class,
                RarityRepository.class,
                ReforgeRepository.class,
                SkillRepository.class,
                SkillLevelRepository.class,
                StatRepository.class
        );
    }

    public static SqlSession getSqlSession() {
        Preconditions.checkArgument(databaseRegistered, "Database has not been registered");
        Preconditions.checkArgument(databaseEnabled, "Database has not been enabled");
        return getServiceManager().getProvider(SqlSession.class);
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return getServiceManager().getProvider(tClass);
    }

    /*
    org.hibernate.type.SerializationException: could not deserialize
	at org.hibernate.internal.util.SerializationHelper.doDeserialize(SerializationHelper.java:243)
	at org.hibernate.internal.util.SerializationHelper.deserialize(SerializationHelper.java:287)
	at org.hibernate.type.descriptor.java.SerializableTypeDescriptor.fromBytes(SerializableTypeDescriptor.java:138)
	at org.hibernate.type.descriptor.java.SerializableTypeDescriptor.wrap(SerializableTypeDescriptor.java:113)
	at org.hibernate.type.descriptor.java.SerializableTypeDescriptor.wrap(SerializableTypeDescriptor.java:29)
	at org.hibernate.type.descriptor.sql.VarbinaryTypeDescriptor$2.doExtract(VarbinaryTypeDescriptor.java:60)
	at org.hibernate.type.descriptor.sql.BasicExtractor.extract(BasicExtractor.java:47)
	at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:257)
	at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:253)
	at org.hibernate.type.AbstractStandardBasicType.nullSafeGet(AbstractStandardBasicType.java:243)
	at org.hibernate.type.AbstractStandardBasicType.hydrate(AbstractStandardBasicType.java:329)
	at org.hibernate.persister.entity.AbstractEntityPersister.hydrate(AbstractEntityPersister.java:3212)
	at org.hibernate.persister.entity.Loadable.hydrate(Loadable.java:94)
	at org.hibernate.loader.plan.exec.process.internal.EntityReferenceInitializerImpl.loadFromResultSet(EntityReferenceInitializerImpl.java:342)
	at org.hibernate.loader.plan.exec.process.internal.EntityReferenceInitializerImpl.hydrateEntityState(EntityReferenceInitializerImpl.java:269)
	at org.hibernate.loader.plan.exec.process.internal.AbstractRowReader.readRow(AbstractRowReader.java:80)
	at org.hibernate.loader.plan.exec.internal.EntityLoadQueryDetails$EntityLoaderRowReader.readRow(EntityLoadQueryDetails.java:288)
	at org.hibernate.loader.plan.exec.process.internal.ResultSetProcessorImpl.extractRows(ResultSetProcessorImpl.java:157)
	at org.hibernate.loader.plan.exec.process.internal.ResultSetProcessorImpl.extractResults(ResultSetProcessorImpl.java:94)
	at org.hibernate.loader.plan.exec.internal.AbstractLoadPlanBasedLoader.executeLoad(AbstractLoadPlanBasedLoader.java:105)
	at org.hibernate.loader.entity.plan.AbstractLoadPlanBasedEntityLoader.load(AbstractLoadPlanBasedEntityLoader.java:285)
	at org.hibernate.persister.entity.AbstractEntityPersister.doLoad(AbstractEntityPersister.java:4519)
	at org.hibernate.persister.entity.AbstractEntityPersister.load(AbstractEntityPersister.java:4509)
	at org.hibernate.event.internal.DefaultLoadEventListener.loadFromDatasource(DefaultLoadEventListener.java:571)
	at org.hibernate.event.internal.DefaultLoadEventListener.doLoad(DefaultLoadEventListener.java:539)
	at org.hibernate.event.internal.DefaultLoadEventListener.load(DefaultLoadEventListener.java:208)
	at org.hibernate.event.internal.DefaultLoadEventListener.proxyOrLoad(DefaultLoadEventListener.java:327)
	at org.hibernate.event.internal.DefaultLoadEventListener.doOnLoad(DefaultLoadEventListener.java:108)
	at org.hibernate.event.internal.DefaultLoadEventListener.onLoad(DefaultLoadEventListener.java:74)
	at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:118)
	at org.hibernate.internal.SessionImpl.fireLoadNoChecks(SessionImpl.java:1215)
	at org.hibernate.internal.SessionImpl.internalLoad(SessionImpl.java:1080)
	at org.hibernate.type.EntityType.resolveIdentifier(EntityType.java:697)
	at org.hibernate.type.EntityType.resolve(EntityType.java:464)
	at org.hibernate.type.ManyToOneType.resolve(ManyToOneType.java:240)
	at org.hibernate.engine.internal.TwoPhaseLoad$EntityResolver.lambda$static$0(TwoPhaseLoad.java:576)
	at org.hibernate.engine.internal.TwoPhaseLoad.initializeEntityEntryLoadedState(TwoPhaseLoad.java:221)
	at org.hibernate.engine.internal.TwoPhaseLoad.initializeEntity(TwoPhaseLoad.java:155)
	at org.hibernate.engine.internal.TwoPhaseLoad.initializeEntity(TwoPhaseLoad.java:126)
	at org.hibernate.loader.Loader.initializeEntitiesAndCollections(Loader.java:1201)
	at org.hibernate.loader.Loader.processResultSet(Loader.java:1009)
	at org.hibernate.loader.Loader.doQuery(Loader.java:967)
	at org.hibernate.loader.Loader.doQueryAndInitializeNonLazyCollections(Loader.java:357)
	at org.hibernate.loader.Loader.doList(Loader.java:2868)
	at org.hibernate.loader.Loader.doList(Loader.java:2850)
	at org.hibernate.loader.Loader.listIgnoreQueryCache(Loader.java:2682)
	at org.hibernate.loader.Loader.list(Loader.java:2677)
	at org.hibernate.loader.hql.QueryLoader.list(QueryLoader.java:540)
	at org.hibernate.hql.internal.ast.QueryTranslatorImpl.list(QueryTranslatorImpl.java:400)
	at org.hibernate.engine.query.spi.HQLQueryPlan.performList(HQLQueryPlan.java:219)
	at org.hibernate.internal.SessionImpl.list(SessionImpl.java:1443)
	at org.hibernate.query.internal.AbstractProducedQuery.doList(AbstractProducedQuery.java:1649)
	at org.hibernate.query.internal.AbstractProducedQuery.list(AbstractProducedQuery.java:1617)
	at org.hibernate.query.Query.getResultList(Query.java:165)
	at org.hibernate.query.criteria.internal.compile.CriteriaQueryTypeQueryAdapter.getResultList(CriteriaQueryTypeQueryAdapter.java:76)
	at gg.sbs.api.data.sql.SqlRepository.findAll(SqlRepository.java:106)
	at gg.sbs.api.data.sql.SqlSessionUtil.withSession(SqlSessionUtil.java:60)
	at gg.sbs.api.data.sql.SqlRepository.findAll(SqlRepository.java:110)
	at gg.sbs.api.data.sql.SqlRepository.refreshItems(SqlRepository.java:55)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180)
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.io.StreamCorruptedException: invalid stream header: 31363733
	at java.io.ObjectInputStream.readStreamHeader(ObjectInputStream.java:938)
	at java.io.ObjectInputStream.<init>(ObjectInputStream.java:396)
	at org.hibernate.internal.util.SerializationHelper$CustomObjectInputStream.<init>(SerializationHelper.java:309)
	at org.hibernate.internal.util.SerializationHelper$CustomObjectInputStream.<init>(SerializationHelper.java:299)
	at org.hibernate.internal.util.SerializationHelper.doDeserialize(SerializationHelper.java:218)
	... 65 more
     */

}