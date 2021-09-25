package gg.sbs.api;

import gg.sbs.api.data.sql.SqlModel;
import gg.sbs.api.data.sql.SqlRefreshable;
import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.models.accessories.AccessoryRefreshable;
import gg.sbs.api.data.sql.models.accessories.AccessoryRepository;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyRefreshable;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyRepository;
import gg.sbs.api.data.sql.models.enchantments.EnchantmentRefreshable;
import gg.sbs.api.data.sql.models.enchantments.EnchantmentRepository;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRefreshable;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeRepository;
import gg.sbs.api.data.sql.models.pets.PetRefreshable;
import gg.sbs.api.data.sql.models.pets.PetRepository;
import gg.sbs.api.data.sql.models.potions.PotionRefreshable;
import gg.sbs.api.data.sql.models.potions.PotionRepository;
import gg.sbs.api.data.sql.models.rarities.RarityRefreshable;
import gg.sbs.api.data.sql.models.rarities.RarityRepository;
import gg.sbs.api.data.sql.models.reforges.ReforgeRefreshable;
import gg.sbs.api.data.sql.models.reforges.ReforgeRepository;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.hypixel.HypixelPlayerDataApi;
import gg.sbs.api.apiclients.mojang.MojangApiBuilder;
import gg.sbs.api.apiclients.mojang.MojangProfileApi;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.service.ServiceManager;

public class SimplifiedAPI {

    private static final ServiceManager serviceManager = new ServiceManager();

    static {
        serviceManager.provide(Scheduler.class, Scheduler.getInstance());

        //serviceManager.provide(ModLogger.class, instance.getLogger());

        serviceManager.provide(AccessoryRepository.class, new AccessoryRepository());
        serviceManager.provide(AccessoryFamilyRepository.class, new AccessoryFamilyRepository());
        serviceManager.provide(EnchantmentRepository.class, new EnchantmentRepository());
        serviceManager.provide(ItemTypeRepository.class, new ItemTypeRepository());
        serviceManager.provide(PetRepository.class, new PetRepository());
        serviceManager.provide(PotionRepository.class, new PotionRepository());
        serviceManager.provide(RarityRepository.class, new RarityRepository());
        serviceManager.provide(ReforgeRepository.class, new ReforgeRepository());

        serviceManager.provide(AccessoryRefreshable.class, new AccessoryRefreshable());
        serviceManager.provide(AccessoryFamilyRefreshable.class, new AccessoryFamilyRefreshable());
        serviceManager.provide(EnchantmentRefreshable.class, new EnchantmentRefreshable());
        serviceManager.provide(ItemTypeRefreshable.class, new ItemTypeRefreshable());
        serviceManager.provide(PetRefreshable.class, new PetRefreshable());
        serviceManager.provide(PotionRefreshable.class, new PotionRefreshable());
        serviceManager.provide(RarityRefreshable.class, new RarityRefreshable());
        serviceManager.provide(ReforgeRefreshable.class, new ReforgeRefreshable());

        serviceManager.provide(HypixelPlayerDataApi.class, HypixelApiBuilder.buildApi(HypixelPlayerDataApi.class));
        serviceManager.provide(MojangProfileApi.class, MojangApiBuilder.buildApi(MojangProfileApi.class));
    }

    public static Scheduler getScheduler() {
        return getServiceManager().getProvider(Scheduler.class);
    }

    private static ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> rClass) {
        return getServiceManager().getProvider(rClass);
    }

    public static <T extends SqlModel,
            R extends SqlRepository<T>,
            E extends SqlRefreshable<T, R>>
    E getSqlRefreshable(Class<E> eClass) {
        return getServiceManager().getProvider(eClass);
    }
}
