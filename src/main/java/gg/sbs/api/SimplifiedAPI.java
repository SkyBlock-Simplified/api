package gg.sbs.api;

import gg.sbs.api.database.repositories.SqlModel;
import gg.sbs.api.database.repositories.SqlRefreshable;
import gg.sbs.api.database.repositories.SqlRepository;
import gg.sbs.api.database.repositories.accessories.AccessoryRefreshable;
import gg.sbs.api.database.repositories.accessories.AccessoryRepository;
import gg.sbs.api.database.repositories.accessoryfamilies.AccessoryFamilyRefreshable;
import gg.sbs.api.database.repositories.accessoryfamilies.AccessoryFamilyRepository;
import gg.sbs.api.database.repositories.enchantments.EnchantmentRefreshable;
import gg.sbs.api.database.repositories.enchantments.EnchantmentRepository;
import gg.sbs.api.database.repositories.itemtypes.ItemTypeRefreshable;
import gg.sbs.api.database.repositories.itemtypes.ItemTypeRepository;
import gg.sbs.api.database.repositories.pets.PetRefreshable;
import gg.sbs.api.database.repositories.pets.PetRepository;
import gg.sbs.api.database.repositories.potions.PotionRefreshable;
import gg.sbs.api.database.repositories.potions.PotionRepository;
import gg.sbs.api.database.repositories.rarities.RarityRefreshable;
import gg.sbs.api.database.repositories.rarities.RarityRepository;
import gg.sbs.api.database.repositories.reforges.ReforgeRefreshable;
import gg.sbs.api.database.repositories.reforges.ReforgeRepository;
import gg.sbs.api.mojang.MojangProfile;
import gg.sbs.api.mojang.MojangRepository;
import gg.sbs.api.nbt.NbtFactory;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.service.ServiceManager;
import gg.sbs.api.util.ResourceUtil;

public class SimplifiedAPI {

    private static final ServiceManager serviceManager = new ServiceManager();

    static {
        //serviceManager.provide(MojangRepository.class, new MojangRepository<MojangProfile>());
        serviceManager.provide(NbtFactory.class, NbtFactory.getInstance());
        serviceManager.provide(ResourceUtil.class, new ResourceUtil());
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
    }

    @SuppressWarnings("unchecked")
    public static MojangRepository<MojangProfile> getMojangRepository() {
        return (MojangRepository<MojangProfile>) getServiceManager().getProvider(MojangRepository.class);
    }

    public static ResourceUtil getResourceUtil() {
        return getServiceManager().getProvider(ResourceUtil.class);
    }

    public static NbtFactory getNbtFactory() {
        return getServiceManager().getProvider(NbtFactory.class);
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
