package dev.sbs.api.minecraft.nbt_old;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.util.Primitives;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.ListUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Factory class for wrapping NBT objects.
 */
@SuppressWarnings("unchecked")
public final class NbtFactory_old {

	private static final NbtFactory_old INSTANCE = new NbtFactory_old();

	// Convert between NBT id and the equivalent class in java
	static final BiMap<Byte, Class<?>> NBT_CLASS = HashBiMap.create();
	static final BiMap<Byte, NbtType> NBT_ENUM = HashBiMap.create();

	// Reflection
	//private static final Reflection NBT_BASE = MinecraftReflection.getCompatibleForgeReflection("NBTBase", MinecraftReflection.MINECRAFT_PACKAGE, "nbt");
	//private static final Reflection NBT_TAG_COMPOUND = MinecraftReflection.getCompatibleForgeReflection("NBTTagCompound", MinecraftReflection.MINECRAFT_PACKAGE, "nbt");
	//static final Reflection NBT_TAG_LIST = MinecraftReflection.getCompatibleForgeReflection("NBTTagList", MinecraftReflection.MINECRAFT_PACKAGE, "nbt");
	//public static final Reflection NBT_COMPRESSED_TOOLS = MinecraftReflection.getCompatibleForgeReflection("CompressedStreamTools", MinecraftReflection.MINECRAFT_PACKAGE, "nbt");
	//private static final Reflection NMS_ITEM_STACK = MinecraftReflection.getCompatibleForgeReflection("ItemStack", MinecraftReflection.MINECRAFT_PACKAGE, "item");
	//private static final Reflection NMS_TILE_ENTITY = MinecraftReflection.getCompatibleForgeReflection("TileEntity", MinecraftReflection.MINECRAFT_PACKAGE, "tileentity");
	//private static final Reflection NMS_BLOCK = new MinecraftReflection("Block", MinecraftReflection.MINECRAFT_PACKAGE, "block");

	private NbtFactory_old() { }

	static Object adjustIncoming(Object value) {
		if (value == null)
			return null;

		Class<?> clazz = Primitives.unwrap(value.getClass());

		if (WrappedList.class.isAssignableFrom(clazz) || WrappedMap.class.isAssignableFrom(clazz))
			return value;

		if (!NBT_CLASS.inverse().containsKey(clazz)) {
			if (clazz.isArray())
				value = SimplifiedApi.getNbtFactory().createList((Object[])value);
			else {
				if (value instanceof Boolean)
					value = (byte) ((boolean) value ? 1 : 0);
				else if (CharSequence.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (UUID.class.isAssignableFrom(clazz))
					value = value.toString();
				else if (BigDecimal.class.isAssignableFrom(clazz))
					value = ((BigDecimal)value).doubleValue();
				else if (BigInteger.class.isAssignableFrom(clazz))
					value = ((BigInteger)value).longValue();
				else if (clazz.isEnum())
					value = ((Enum<?>)value).name();
				else if (Collection.class.isAssignableFrom(clazz))
					value = SimplifiedApi.getNbtFactory().createList((Collection<?>)value);
				else if (Map.class.isAssignableFrom(clazz)) {
					NbtCompound compound = SimplifiedApi.getNbtFactory().createCompound();
					compound.putAll((Map<String, Object>)value);
					value = compound;
				}
			}
		}

		return value;
	}

	@SuppressWarnings("rawtypes")
	static Object adjustOutgoing(Object value, Class clazz) {
		if (value == null)
			return null;

		if (clazz != null) {
			if (boolean.class.equals(clazz))
				value = (byte)value > 0;
			else if (UUID.class.equals(clazz))
				value = UUID.fromString(value.toString());
			else if (BigDecimal.class.equals(clazz))
				value = BigDecimal.valueOf((double)value);
			else if (BigInteger.class.equals(clazz))
				value = BigInteger.valueOf((long)value);
			else if (clazz.isEnum())
				value = Enum.valueOf(clazz, value.toString());
			else if (Map.class.isAssignableFrom(clazz)) {
				NbtCompound compound = (NbtCompound)value;
				boolean adjusted = false;

				if (!Map.class.equals(clazz)) {
					Reflection refCollection = new Reflection(clazz);
					Map<String, Object> map = (Map<String, Object>)refCollection.newInstance();
					refCollection.invokeMethod("putAll", map, compound);
					adjusted = true;
				}

				if (!adjusted)
					value = compound;
			} else if (Collection.class.isAssignableFrom(clazz) || clazz.isArray()) {
				NbtList<?> nbtList = (NbtList<?>)value;

				if (!clazz.isArray()) {
					boolean adjusted = false;

					if (!Collection.class.equals(clazz)) {
						Reflection refCollection = new Reflection(clazz);
						Object collection = refCollection.newInstance();
						refCollection.invokeMethod("addAll", collection, nbtList);
						adjusted = true;
					}

					if (!adjusted)
						value = nbtList;
				} else
					value = ListUtil.toArray(nbtList, clazz.getComponentType());
			}
		}

		return value;
	}

	public static NbtFactory_old getInstance() {
		return INSTANCE;
	}

	/*
	 * Ensure that the given item can store NBT information.
	 *
	 * @param itemStack The item to check.
	 *
	private void checkItem(ItemStack itemStack) {
		Preconditions.checkArgument(itemStack != null, "ItemStack cannot be NULL!");

		if (Items.AIR.equals(itemStack.getItem()))
			throw new UnsupportedOperationException(FormatUtil.format("ItemStack type ''{0}'' cannot store NMS information!", itemStack.getItem()));
	}

	/**
	 * Ensure that the given entity can store NBT information.
	 *
	 * @param entity The entity to check.
	 *
	private void checkEntity(Entity entity) {
		Preconditions.checkArgument(entity != null, "Entity cannot be NULL!");
	}

	/**
	 * Ensure that the given tile entity can store NBT information.
	 *
	 * @param tileEntity The tile entity to check.
	 *
	private void checkTileEntity(TileEntity tileEntity) {
		Preconditions.checkArgument(tileEntity != null, "TileEntity cannot be NULL!");
	}*/

	/**
	 * Construct a new NbtList of unspecified type.
	 *
	 * @return The new NbtList.
	 */
	//@SafeVarargs
	public final <T> NbtList<T> createList(T... content) {
		return createList(Arrays.asList(content));
	}

	/**
	 * Construct a new NbtList of unspecified type.
	 *
	 * @return The new NbtList.
	 */
	public final <E> NbtList<E> createList(Iterable<E> iterable) {
		NbtList<E> list = new NbtList<>(createNbtTag(NbtType.TAG_LIST, null));
		iterable.forEach(list::add);
		return list;
	}

	/**
	 * Construct a new NbtCompound.
	 *
	 * @return The new NbtCompound.
	 */
	public final NbtCompound createCompound() {
		return new NbtCompound(createNativeCompound(), false);
	}

	/**
	 * Construct a new NMS NBT tag initialized with the given value.
	 *
	 * @param type The NBT type.
	 * @param value The value, or null to keep the original value.
	 * @return The created tag.
	 */
	private static Object createNbtTag(NbtType type, Object value) {
		Object tag = null;//NBT_BASE.invokeMethod(NBT_BASE.getClazz(), null, type.getId());

		if (value != null)
			new Reflection(tag.getClass()).setValue(type.getFieldType(), tag, value);

		return tag;
	}

	protected static Object createNativeCompound() {
		return createNbtTag(NbtType.TAG_COMPOUND, null);
	}

	/**
	 * Gets a new NBT wrapper for the given NBTTagCompound.
	 *
	 * @param handle The NBTTagCompound handle.
	 * @return A wrapper for the given NBTTagCompound.
	 */
	public final NbtCompound fromCompound(Object handle) {
		return new NbtCompound(handle, false);
	}

	/*
	 * Construct a wrapper for an NBT tag stored in an entity. This is where
	 * auxillary data such as entity data and coordinates are stored.
	 *
	 * @param entity The entity to wrap.
	 * @return A wrapper for the entitys NBT tag.
	 *
	public final NbtEntityCompound fromEntity(Entity entity) {
		this.checkEntity(entity);
		entity.getEntityData(); // Create tag if it doesn't exist
		Object handle;

		try {
			handle = entity.serializeNBT();
		} catch (Exception ignore) {
			handle = entity.writeToNBT(new NBTTagCompound());
		}

		if (handle == null)
			handle = createNativeCompound();

		return new NbtEntityCompound(entity, handle);
	}

	/**
	 * Construct a wrapper for an NBT tag stored in an item stack. This is where
	 * auxillary data such as enchanting, name and lore is stored. It does not include items
	 * material, damage value or count.
	 *
	 * @param itemStack The item to wrap.
	 * @return A wrapper for the items NBT tag.
	 *
	public final NbtItemCompound fromItemStack(ItemStack itemStack) {
		this.checkItem(itemStack);

		if (!itemStack.hasTagCompound()) // Create tag if it doesn't exist
			itemStack.setTagCompound(new NBTTagCompound());

		Object handle = itemStack.serializeNBT();

		if (handle == null)
			handle = createNativeCompound();

		return new NbtItemCompound(itemStack, handle);
	}

	/**
	 * Construct a wrapper for an NBT tag stored in a tile entity. This is where
	 * auxillary data such as tile entity data and coordinates are stored.
	 *
	 * @param tileEntity The tile entity to wrap.
	 * @return A wrapper for the entitys NBT tag.
	 *
	public final NbtTileEntityCompound fromTileEntity(TileEntity tileEntity) {
		this.checkTileEntity(tileEntity);
		tileEntity.getTileData(); // Create tag if it doesn't exist
		Object handle;

		try {
			handle = tileEntity.serializeNBT();
		} catch (Exception ignore) {
			handle = tileEntity.writeToNBT(new NBTTagCompound());
		}

		if (handle == null)
			handle = createNativeCompound();

		return new NbtTileEntityCompound(tileEntity, handle);
	}

	public final ItemStack toItemStack(NbtCompound nbtCompound) {
		if (nbtCompound.containsKey("id")) {
			Item item;
			boolean enchanted = nbtCompound.containsPath("tag.ench");

			if (nbtCompound.get("id") instanceof Short)
				item = Item.getItemById(nbtCompound.<Short>get("id").intValue());
			else
				item = Item.getByNameOrId(nbtCompound.get("id"));

			if (nbtCompound.containsPath("tag.ExtraAttributes")) {
				NbtCompound itemAttributes = nbtCompound.getPath("tag.ExtraAttributes");
				String attributeID = itemAttributes.get("id", "");
				enchanted = enchanted || itemAttributes.containsKey("enchantments");

				if (attributeID.startsWith("ENCHANTED_")) {
					// This fixes enchanted potatoes having the wrong id (potato block).
					if (nbtCompound.get("id") instanceof Short) {
						if (nbtCompound.<Short>get("id") == 142) // Potato Block
							item = Item.getItemById(392);
						else if (nbtCompound.<Short>get("id") == 141) // Carrot Block
							item = Item.getItemById(391);
					}
				}
			}

			nbtCompound.put("id", item.getRegistryName().toString());
			ItemStack itemStack = new ItemStack((NBTTagCompound)nbtCompound.getHandle());
			nbtCompound = this.fromItemStack(itemStack);

			// Fix Potions
			if (Items.POTIONITEM.equals(item)) {
				NbtCompound tag = nbtCompound.get("tag");
				ItemStack potionItemStack = new ItemStack((nbtCompound.containsPath("ExtraAttributes.splash") ? Items.SPLASH_POTION : Items.POTIONITEM), 1);
				List<PotionEffect> effects = PotionUtils.getEffectsFromTag((NBTTagCompound)tag.getHandle());
				PotionUtils.appendEffects(potionItemStack, effects);

				if (!effects.isEmpty()) {
					int pid = Potion.getIdFromPotion(effects.get(0).getPotion());
					PotionType potionType = PotionType.REGISTRY.getObjectById(pid);
					PotionUtils.addPotionToItemStack(potionItemStack, potionType);
				} else {
					PotionType potionType = PotionTypes.WATER;

					if (nbtCompound.containsPath("ExtraAttributes.potion_type"))
						potionType = PotionType.getPotionTypeForName(nbtCompound.getPath("ExtraAttributes.potion_type"));

					PotionUtils.addPotionToItemStack(potionItemStack, potionType);
				}

				NbtCompound potionNbt = SimplifiedApi.getNbtFactory().fromItemStack(potionItemStack);
				potionNbt.put("HideFlags", 63);
				potionNbt.putPath("display.Name", nbtCompound.getPath("display.Name"));
				potionNbt.putPath("display.Lore", nbtCompound.getPath("display.Lore"));
				itemStack = potionItemStack;
			}

			if (enchanted)
				itemStack.addEnchantment(Enchantments.UNBREAKING, 1);

			return itemStack;
		}


		return ItemStack.EMPTY;
	}*/

	/**
	 * Gets a new NBT wrapper for the given NBTTagList.
	 *
	 * @param handle The NBTTagList handle.
	 * @return A wrapper for the given NBTTagList.
	 */
	public final <E> NbtList<E> fromList(Object handle) {
		return new NbtList<>(handle);
	}

	/**
	 * Creates an NbtCompound from the given bytes.
	 * <br><br>
	 * Supports GZIP decompression.
	 *
	 * @param bytes The input byte array.
	 * @return A new NBT compound.
	 */
	public final NbtCompound fromBytes(byte[] bytes) {
		return this.fromStream(new ByteArrayInputStream(bytes));
	}

	/**
	 * Creates an NbtCompound from the given stream.
	 *
	 * @param stream The input stream.
	 * @return A new NBT compound.
	 */
	public final NbtCompound fromStream(InputStream stream) {
		return null;
		//return this.fromCompound(NBT_COMPRESSED_TOOLS.invokeMethod(NBT_TAG_COMPOUND.getClazz(), null, stream));
	}

	/**
	 * Retrieve the NBT class value.
	 *
	 * @param type The NBT type.
	/* @param base The NBT class instance.
	 * @return The corresponding value.
	 */
	protected static <T> T getDataField(NbtType type, Object base) {
		return (T)new Reflection(base.getClass()).getValue(type.getFieldType(), base);
	}

	/**
	 * Retrieve the NBT type from a given NBT tag.
	 *
	 * @param nms The native NBT tag.
	 * @return The corresponding type.
	 */
	protected static NbtType getNbtType(Object nms) {
		return null;
		//return NBT_ENUM.get(NBT_BASE.invokeMethod(Byte.class, nms));
	}

	/**
	 * Retrieve the nearest NBT type for a given primitive type.
	 *
	 * @param primitive The primitive type object.
	 * @return The corresponding type.
	 */
	protected static NbtType getPrimitiveType(Object primitive) {
		NbtType type = NBT_ENUM.get(NBT_CLASS.inverse().get(Primitives.unwrap(primitive.getClass())));

		if (type == null)
			throw new IllegalArgumentException(FormatUtil.format("Illegal type: {0} ({1})!", primitive.getClass(), primitive));

		return type;
	}

	/**
	 * Save an NbtCompound to the given stream.
	 *
	 * @param source The NbtCompound to save.
	 * @param stream The output stream.
	 */
	public void saveStream(NbtCompound source, OutputStream stream) {
		//NBT_COMPRESSED_TOOLS.invokeMethod(Void.class, null, source.getHandle(), stream);
	}

	/*
	 * Set the NBT compound tag of a given item stack.
	 *
	 * @param itemStack The item stack, cannot be air.
	 * @param compound The NbtCompound to save, null to remove it.
	 *
	public final void setItemTag(ItemStack itemStack, NbtCompound compound) {
		this.checkItem(itemStack);
		itemStack.deserializeNBT((NBTTagCompound)compound.getHandle());
	}

	/**
	 * Set the NBT compound tag of a given entity.
	 *
	 * @param entity The entity.
	 * @param compound The NbtCompound to save, null to remove it.
	 *
	public final void setEntityTag(Entity entity, NbtCompound compound) {
		this.checkEntity(entity);
		entity.deserializeNBT((NBTTagCompound)compound.getHandle());
	}

	/**
	 * Set the NBT compound tag of a given tile entity.
	 *
	 * @param tileEntity The tile entity.
	 * @param compound The NbtCompound to save, null to remove it.
	 *
	public final void setTileEntityTag(TileEntity tileEntity, NbtCompound compound) {
		this.checkTileEntity(tileEntity);
		tileEntity.deserializeNBT((NBTTagCompound)compound.getHandle());
	}*/

	/**
	 * Convert wrapped List and Map objects into their respective NBT counterparts.
	 *
	 * @param value The value of the element to create. Can be a List or a Map.
	 * @return The NBT element.
	 */
	protected static Object unwrapValue(Object value) {
		if (value == null)
			return null;

		if (value instanceof Wrapper)
			return ((Wrapper)value).getHandle();
		else
			return createNbtTag(getPrimitiveType(value), value);
	}

	/**
	 * Convert a given NBT element to a primitive wrapper or List/Map equivalent.
	 * <p>
	 * All changes to any mutable objects will be reflected in the underlying NBT element(s).
	 *
	 * @param nms The NBT element.
	 * @return The wrapper equivalent.
	 */
	protected static Object wrapNative(Object nms) {
		if (nms == null)
			return null;

		//if (NBT_BASE.getClazz().isAssignableFrom(nms.getClass())) {
			final NbtType type = getNbtType(nms);

			switch (type) {
				case TAG_COMPOUND:
					return new NbtCompound(nms, false);
				case TAG_LIST:
					return new NbtList<>(nms);
				default:
					return getDataField(type, nms);
			}
		//}

		//throw new IllegalArgumentException(FormatUtil.format("Unexpected type: {0}!", nms));
	}

}
