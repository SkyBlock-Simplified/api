package dev.sbs.api.data.yaml;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.yaml.annotation.Flag;
import dev.sbs.api.data.yaml.converter.YamlConverter;
import dev.sbs.api.data.yaml.exception.InvalidConfigurationException;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.helper.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;

public class YamlConfig extends ConfigMapper implements Runnable {

    protected static final ConcurrentSet<Class<? extends YamlConverter>> GLOBAL_CUSTOM_CONVERTERS = Concurrent.newSet();

    private transient boolean suppressFailedConversions = false;
    private transient int taskId = -1;
    private transient WatchService watchService;
    private transient WatchKey watchKey;
    private transient boolean reloadProcessing = false;

    public YamlConfig(@NotNull String fileName, @NotNull String... header) {
        this(fileName, Arrays.asList(header));
    }

    public YamlConfig(@NotNull String fileName, @NotNull Iterable<String> header) {
        this(fileName, SimplifiedApi.getCurrentDirectory(), header);
    }

    public YamlConfig(@NotNull String fileName, @NotNull File configDir, @NotNull String... header) {
        this(fileName, configDir, Arrays.asList(header));
    }

    public YamlConfig(@NotNull String fileName, @NotNull File configDir, @NotNull Iterable<String> header) {
        this(fileName, configDir, false, header);
    }

    public YamlConfig(@NotNull String fileName, @NotNull File configDir, boolean suppressFailedConversions, @NotNull String... header) {
        this(fileName, configDir, suppressFailedConversions, Arrays.asList(header));
    }

    public YamlConfig(@NotNull String fileName, @NotNull File configDir, boolean suppressFailedConversions, @NotNull Iterable<String> header) {
        super(configDir, fileName, header);
        this.setSuppressFailedConversions(suppressFailedConversions);
        GLOBAL_CUSTOM_CONVERTERS.forEach(this::addCustomConverter);
    }

    public static void addGlobalCustomConverter(Class<? extends YamlConverter> converter) {
        GLOBAL_CUSTOM_CONVERTERS.add(converter);
    }

    public boolean delete() {
        return this.configFile.delete();
    }

    public boolean exists() {
        return this.configFile.exists();
    }

    public void init() throws InvalidConfigurationException {
        if (!this.exists()) {
            if (this.configFile.getParentFile() != null)
                this.configFile.getParentFile().mkdirs();

            try {
                this.configFile.createNewFile();
                this.save();
            } catch (IOException ex) {
                throw new InvalidConfigurationException("Could not create new empty config!", ex);
            }
        } else
            this.load();
    }

    private void internalLoad(Class<?> clazz) throws InvalidConfigurationException {
        if (!clazz.getSuperclass().equals(YamlMap.class))
            this.internalLoad(clazz.getSuperclass());

        boolean save = false;

        for (Field field : clazz.getDeclaredFields()) {
            if (doSkip(field)) continue;
            String path = this.getPathMode(field);

            if (field.isAnnotationPresent(Flag.class)) {
                Flag flag = field.getAnnotation(Flag.class);

                if (StringUtil.isNotEmpty(flag.path()))
                    path = flag.path();
            }

            if (Modifier.isPrivate(field.getModifiers()))
                field.setAccessible(true);

            if (this.root.has(path)) {
                try {
                    this.converter.fromConfig(this, field, this.root, path);
                } catch (Exception ex) {
                    if (!this.isSuppressingFailedConversions())
                        throw new InvalidConfigurationException(String.format("Could not set field %s!", field.getName()), ex);
                }
            } else {
                try {
                    this.converter.toConfig(this, field, this.root, path);
                    this.converter.fromConfig(this, field, this.root, path);
                    save = true;
                } catch (Exception ex) {
                    if (!this.isSuppressingFailedConversions())
                        throw new InvalidConfigurationException(String.format("Could not get field %s!", field.getName()), ex);
                }
            }
        }

        if (save)
            this.saveToYaml();
    }

    private void internalSave(Class<?> clazz) throws InvalidConfigurationException {
        if (!clazz.getSuperclass().equals(YamlMap.class))
            this.internalSave(clazz.getSuperclass());

        for (Field field : clazz.getDeclaredFields()) {
            if (doSkip(field)) continue;
            String path = this.getPathMode(field);
            ArrayList<String> comments = new ArrayList<>();

            if (field.isAnnotationPresent(Flag.class)) {
                Flag flag = field.getAnnotation(Flag.class);

                if (StringUtil.isNotEmpty(flag.comment()))
                    comments.addAll(Arrays.asList(StringUtil.split(flag.comment(), '\n')));

                if (StringUtil.isNotEmpty(flag.path()))
                    path = flag.path();
            }

            if (!comments.isEmpty()) {
                for (String comment : comments)
                    this.addComment(path, comment);
            }

            if (Modifier.isPrivate(field.getModifiers()))
                field.setAccessible(true);

            try {
                this.converter.toConfig(this, field, root, path);
                this.converter.fromConfig(this, field, root, path);
            } catch (Exception ex) {
                if (!this.isSuppressingFailedConversions())
                    throw new InvalidConfigurationException(String.format("Could not save field %s!", field.getName()), ex);
            }
        }
    }

    public boolean isSuppressingFailedConversions() {
        return this.suppressFailedConversions;
    }

    public void load() throws InvalidConfigurationException {
        this.loadFromYaml();
        if (this.update(this.root)) this.save();
        this.internalLoad(this.getClass());
    }

    public void reload() throws InvalidConfigurationException {
        this.loadFromYaml();
        if (this.update(this.root)) this.save();
        this.internalLoad(this.getClass());
    }

    @Override
    public void run() {
        WatchKey key = this.watchService.poll();
        if (key == null) return;

        for (WatchEvent<?> event : key.pollEvents()) {
            if (StandardWatchEventKinds.OVERFLOW.equals(event.kind()))
                continue;
            else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
                this.stopWatcher();
                break;
            } else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
                java.nio.file.Path context = (java.nio.file.Path) event.context();
                String path = ((java.nio.file.Path) this.watchKey.watchable()).resolve(context).toString();

                if (path.equals(this.configFile.toString())) {
                    if (!this.reloadProcessing) {
                        this.reloadProcessing = true;

                        while (true) {
                            try {
                                this.reload();
                                break;
                            } catch (Exception ex) {
                                Scheduler.sleep(1000);
                            }
                        }

                        this.reloadProcessing = false;
                    }
                }
            }

            if (!key.reset()) break;
        }
    }

    public void save() throws InvalidConfigurationException {
        if (this.root == null) this.root = new ConfigSection();
        this.clearComments();
        this.internalSave(this.getClass());
        this.saveToYaml();
    }

    public void setSuppressFailedConversions() {
        this.setSuppressFailedConversions(true);
    }

    public void setSuppressFailedConversions(boolean suppress) {
        this.suppressFailedConversions = suppress;
    }

    public void startWatcher() {
        if (this.taskId == -1) {
            try {
                this.watchService = FileSystems.getDefault().newWatchService();
                this.watchKey = this.configFile.toPath().getParent().register(this.watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                this.taskId = SimplifiedApi.getScheduler().scheduleAsync(this, 0, 250).getId();
            } catch (Exception ex) {
                throw new RuntimeException("Unable to start watch service!", ex);
            }
        }
    }

    public void stopWatcher() {
        if (this.taskId != -1) {
            SimplifiedApi.getScheduler().cancel(this.taskId);
            this.taskId = -1;
            this.watchKey.cancel();

            try {
                this.watchService.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Called after the file is loaded but before the converter gets it.
     * <p>
     * Used to manually edit the passed root node when you updated the config.
     *
     * @param section The root ConfigSection with all sub-nodes.
     */
    public boolean update(ConfigSection section) throws InvalidConfigurationException {
        return false;
    }

}
