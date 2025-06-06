package dev.sbs.api.data.yaml;

import dev.sbs.api.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class ConfigSection {

    protected final Map<Object, Object> map = new LinkedHashMap<>();
    private final String fullPath;

    public ConfigSection() {
        this.fullPath = "";
    }

    public ConfigSection(ConfigSection root, String key) {
        this.fullPath = StringUtil.isNotEmpty(root.fullPath) ? (root.fullPath + ".") : key;
    }

    public ConfigSection create(String path) {
        if (path == null) throw new IllegalArgumentException("Cannot create section at empty path!");
        int i1 = -1, i2;
        ConfigSection section = this;

        while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigSection subSection = section.getConfigSection(node);
            section = (subSection == null ? section.create(node) : subSection);
        }

        String key = path.substring(i2);
        if (Objects.equals(section, this)) {
            ConfigSection result = new ConfigSection(this, key);
            map.put(key, result);
            return result;
        }

        return section.create(key);
    }

    private ConfigSection getConfigSection(String node) {
        return (map.containsKey(node) && map.get(node) instanceof ConfigSection) ? (ConfigSection) map.get(node) : null;
    }

    public void set(String path, Object value) {
        this.set(path, value, true);
    }

    public void set(String path, Object value, boolean searchForSubNodes) {
        if (path == null) throw new IllegalArgumentException("Cannot set a value at empty path!");
        int i1 = -1, i2 = 0;
        ConfigSection section = this;

        if (searchForSubNodes) {
            while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
                String node = path.substring(i2, i1);
                ConfigSection subSection = section.getConfigSection(node);
                section = (subSection == null ? section.create(node) : subSection);
            }
        }

        String key = path.substring(i2);
        if (Objects.equals(section, this)) {
            if (value == null)
                map.remove(key);
            else
                map.put(key, value);
        } else
            section.set(key, value);
    }

    protected void mapChildrenValues(Map<Object, Object> output, ConfigSection section, boolean deep) {
        if (section != null) {
            for (Map.Entry<Object, Object> entry : section.map.entrySet()) {
                if (entry.getValue() instanceof ConfigSection) {
                    Map<Object, Object> result = new LinkedHashMap<>();
                    output.put(entry.getKey(), result);
                    if (deep) mapChildrenValues(result, (ConfigSection) entry.getValue(), true);
                } else
                    output.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<Object, Object> getValues(boolean deep) {
        Map<Object, Object> result = new LinkedHashMap<>();
        mapChildrenValues(result, this, deep);
        return result;
    }

    public void remove(String path) {
        this.set(path, null);
    }

    public boolean has(String path) {
        if (path == null) throw new IllegalArgumentException("Cannot remove a Value at empty path");
        int i1 = -1, i2;
        ConfigSection section = this;

        while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigSection subSection = section.getConfigSection(node);
            if (subSection == null) return false;
            section = subSection;
        }

        String key = path.substring(i2);
        return (Objects.equals(section, this) ? map.containsKey(key) : section.has(key));
    }

    public <T> T get(String path) {
        if (path == null) throw new IllegalArgumentException("Cannot remove a Value at empty path");
        int i1 = -1, i2;
        ConfigSection section = this;

        while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigSection subSection = section.getConfigSection(node);
            section = (subSection == null ? section.create(node) : subSection);
        }

        String key = path.substring(i2);
        return (T) (Objects.equals(section, this) ? map.get(key) : section.get(key));
    }

    public Map<?, ?> getRawMap() {
        return map;
    }

}
