package gg.sbs.api.yaml.converters;

import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.yaml.ConfigSection;
import gg.sbs.api.yaml.InternalConverter;
import gg.sbs.api.yaml.YamlMap;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class ConfigConverter extends Converter {

	public ConfigConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		YamlMap obj = (YamlMap)newInstance(type);
		this.getCustomConverters().forEach(obj::addCustomConverter);
		obj.loadFromMap((section instanceof Map) ? (Map<?, ?>)section : ((ConfigSection)section).getRawMap(), type);
		return obj;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		if (obj instanceof Map)
			return obj;
		else {
			YamlMap map = (YamlMap)obj;
			this.getCustomConverters().forEach(map::addCustomConverter);
			return map.saveToMap(obj.getClass());
		}
	}

	private static Object newInstance(Class<?> type) {
		Class<?> enclosingClass = type.getEnclosingClass();

		if (enclosingClass != null)
			return new Reflection(type).newInstance(newInstance(enclosingClass));

		return new Reflection(type).newInstance();
	}

	@Override
	public boolean supports(Class<?> type) {
		return YamlMap.class.isAssignableFrom(type);
	}

}