package gg.sbs.api.data.yaml.converter;

import gg.sbs.api.data.yaml.InternalConverter;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public class ItemStackConverter extends YamlConverter {

	public ItemStackConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		/*Converter mapConverter = this.getConverter(Map.class);
		Map<String, Object> itemMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, section, null);
		Map<String, Object> metaMap = (Map<String, Object>)mapConverter.fromConfig(HashMap.class, itemMap.get("meta"), null);
		ItemStack itemStack = ItemDatabase.getInstance().get((String)itemMap.get("id"));
		itemStack.setCount((int)itemMap.get("count"));
		NbtCompound nbt = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack);
		nbt.put("HideFlags", 63);

		if (itemMap.containsKey("extra")) {
			Map<String, String> extraMap = (Map<String, String>)this.getConverter(Map.class).fromConfig(Map.class, itemMap.get("extra"), null);
			nbt.putPath("ExtraAttributes.id", extraMap.get("id"));

			if (extraMap.containsKey("uuid"))
				nbt.put("ExtraAttributes.uuid", extraMap.get("uuid"));
		}

		if (StringUtil.isNotEmpty(metaMap.get("name").toString()))
			nbt.putPath("display.Name", metaMap.get("name"));

		if (metaMap.containsKey("lore")) {
			List<String> loreList = (List<String>)this.getConverter(List.class).fromConfig(List.class, metaMap.get("lore"), null);
			nbt.putPath("display.Lore", loreList);
		}

		if (metaMap.containsKey("enchantments")) {
			Map<String, Integer> enchantmentMap = (Map<String, Integer>)this.getConverter(Map.class).fromConfig(Map.class, metaMap.get("enchantments"), null);

			List<NbtCompound> ench = new ArrayList<>();
			for (Map.Entry<String, Integer> enchEntry : enchantmentMap.entrySet()) {
				NbtCompound enchTag = SimplifiedAPI.getNbtFactory().createCompound();
				Enchantment enchantment = Enchantment.getEnchantmentByLocation("enchantment." + enchEntry.getKey());
				int id = Enchantment.getEnchantmentID(enchantment);
				enchTag.put("lvl", enchEntry.getValue().shortValue());
				enchTag.put("id", (short)id);
				ench.add(enchTag);
			}

			nbt.put("ench", ench);
		}

		if (metaMap.containsKey("skull")) {
			Map<String, String> skullMap = (Map<String, String>)this.getConverter(Map.class).fromConfig(Map.class, metaMap.get("skull"), null);
			nbt.putPath("SkullOwner.Id", skullMap.get("id"));
			NbtCompound skin = SimplifiedAPI.getNbtFactory().createCompound();
			skin.put("Value", skullMap.get("texture"));
			nbt.putPath("SkullOwner.Properties.textures", SimplifiedAPI.getNbtFactory().createList(skin));
		}

		return itemStack;*/
		return null;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		/*ConcurrentLinkedMap<String, Object> saveMap = Concurrent.newLinkedMap();
		ConcurrentLinkedMap<String, Object> metaMap = Concurrent.newLinkedMap();
		ItemStack itemStack = (ItemStack)obj;
		NbtCompound nbt = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack);
		saveMap.put("id", itemStack.getItem().getRegistryName().getPath().toLowerCase() + ((itemStack.getItemDamage() > 0) ? ":" + itemStack.getItemDamage() : ""));
		saveMap.put("count", itemStack.getCount());

		if (nbt.containsKey("ExtraAttributes")) {
			ConcurrentLinkedMap<String, String> extraMap = Concurrent.newLinkedMap();
			extraMap.put("id", nbt.getPath("ExtraAttributes.id"));

			if (nbt.containsPath("ExtraAttributes.uuid"))
				extraMap.put("uuid", nbt.getPath("ExtraAttributes.uuid"));

			saveMap.put("extra", this.getConverter(Map.class).toConfig(Map.class, extraMap, null));
		}

		if (nbt.containsKey("display")) {
			metaMap.put("name", nbt.containsPath("display.Name") ? nbt.getPath("display.Name") : "");

			if (nbt.containsPath("display.Lore")) {
				List<String> loreList = nbt.getPath("display.Lore");
				metaMap.put("lore", this.getConverter(List.class).toConfig(List.class, loreList, null));
			}
		}

		if (nbt.containsKey("ench")) {
			ConcurrentLinkedMap<String, Integer> enchantmentMap = Concurrent.newLinkedMap();
			List<NbtCompound> ench = nbt.get("ench");

			for (NbtCompound enchItem : ench) {
				Short lvl = enchItem.get("lvl");
				short id = enchItem.get("id");
				enchantmentMap.put(Enchantment.getEnchantmentByID(id).getName().replace("enchantment.", ""), lvl.intValue());
			}

			metaMap.put("enchantments", this.getConverter(Map.class).toConfig(Map.class, enchantmentMap, null));
		}

		if (nbt.containsKey("SkullOwner")) {
			ConcurrentLinkedMap<String, String> skullMap = Concurrent.newLinkedMap();
			skullMap.put("id", nbt.getPath("SkullOwner.Id"));
			skullMap.put("texture", ((NbtList<NbtCompound>)nbt.getPath("SkullOwner.Properties.textures")).get(0).get("Value"));
			metaMap.put("skull", this.getConverter(Map.class).toConfig(Map.class, skullMap, null));
		}

		saveMap.put("meta", metaMap);
		return saveMap;*/
		return null;
	}

	@Override
	public boolean supports(Class<?> type) {
		return false;
		//return ItemStack.class.isAssignableFrom(type);
	}

}