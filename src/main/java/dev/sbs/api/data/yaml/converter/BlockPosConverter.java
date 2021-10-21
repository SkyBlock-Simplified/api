package dev.sbs.api.data.yaml.converter;

import dev.sbs.api.data.yaml.InternalConverter;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public final class BlockPosConverter extends YamlConverter {

	public BlockPosConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		//Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		//return new BlockPos((double)map.get("x"), (double)map.get("y"), (double)map.get("z"));
		return null;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		/*ConcurrentLinkedMap<String, Object> saveMap = Concurrent.newLinkedMap();
		saveMap.put("x", ((BlockPos)obj).getX());
		saveMap.put("y", ((BlockPos)obj).getY());
		saveMap.put("z", ((BlockPos)obj).getZ());
		return saveMap;*/
		return null;
	}

	@Override
	public boolean supports(Class<?> type) {
		//return BlockPos.class.isAssignableFrom(type);
		return false;
	}

}
