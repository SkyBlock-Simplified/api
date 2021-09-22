package gg.sbs.api;

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
    }

    @SuppressWarnings("unchecked")
    public static MojangRepository<MojangProfile> getMojangRepository() {
        return (MojangRepository<MojangProfile>)getServiceManager().getProvider(MojangRepository.class);
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

}
