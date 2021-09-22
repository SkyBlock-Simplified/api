package gg.sbs.api.yaml;

import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.yaml.annotations.ConfigMode;
import gg.sbs.api.yaml.annotations.Path;
import gg.sbs.api.yaml.annotations.PreserveStatic;
import gg.sbs.api.yaml.converters.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class YamlMap {

	final transient InternalConverter converter = new InternalConverter();

	public final void addCustomConverter(Class<? extends Converter> converter) {
		this.converter.addCustomConverter(converter);
	}

	static ConfigSection convertFromMap(Map<?, ?> config) {
		ConfigSection section = new ConfigSection();
		section.map.putAll(config);
		return section;
	}

	static boolean doSkip(Field field) {
		return Modifier.isTransient(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) ||
				Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(PreserveStatic.class) && !field.getAnnotation(PreserveStatic.class).value();
	}

	public final Set<Class<? extends Converter>> getCustomConverters() {
		return this.converter.getCustomConverters();
	}

	protected final String getPathMode(Field field) {
		String path = field.getName();

		if (this.getClass().isAnnotationPresent(ConfigMode.class)) {
			switch (this.getClass().getAnnotation(ConfigMode.class).value()) {
				case FIELD_IS_KEY:
					path = path.replace("_", ".");
					break;
				case PATH_BY_UNDERSCORE:
					break;
				case DEFAULT:
				default:
					if (path.contains("_"))
						path = path.replace("_", ".");

					break;
			}
		}

		return path;
	}

	public void loadFromMap(Map<?, ?> section, Class<?> clazz) throws Exception {
		if (!clazz.getSuperclass().equals(YamlMap.class))
			this.loadFromMap(section, clazz.getSuperclass());

		for (Field field : this.getClass().getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = this.getPathMode(field);

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			this.converter.fromConfig(this, field, convertFromMap(section), path);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveToMap(Class<?> clazz) throws Exception {
		ConcurrentMap<String, Object> returnMap = Concurrent.newMap();

		if (!clazz.getSuperclass().equals(YamlMap.class) && !clazz.getSuperclass().equals(Object.class)) {
			Map<String, Object> map = this.saveToMap(clazz.getSuperclass());

			for (Map.Entry<String, Object> entry : map.entrySet())
				returnMap.put(entry.getKey(), entry.getValue());
		}

		for (Field field : this.getClass().getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = this.getPathMode(field);

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			try {
				returnMap.put(path, field.get(this));
			} catch (IllegalAccessException ignore) { }
		}

		Converter converter = this.converter.getConverter(Map.class);
		assert converter != null;
		return (Map<String, Object>)converter.toConfig(HashMap.class, returnMap, null);
	}

}