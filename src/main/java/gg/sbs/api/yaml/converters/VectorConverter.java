package gg.sbs.api.yaml.converters;

import gg.sbs.api.util.Vector;
import gg.sbs.api.yaml.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class VectorConverter extends Converter {

	public VectorConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		return Vector.deserialize(map);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return ((Vector)obj).serialize();
	}

	@Override
	public boolean supports(Class<?> type) {
		return Vector.class.isAssignableFrom(type);
	}

}