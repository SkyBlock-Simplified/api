package gg.sbs.api.nbt_old;

import com.google.common.base.Preconditions;
import gg.sbs.api.util.Primitives;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gg.sbs.api.SimplifiedAPI;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.RegexUtil;
import gg.sbs.api.util.StringUtil;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a map that wraps another map and automatically
 * converts entries of its type and another exposed type.
 */
@SuppressWarnings("unused")
abstract class WrappedMap extends AbstractMap<String, Object> implements Wrapper {

	protected static final List<String> ROOT_TAGS = Arrays.asList("id", "Count", "Damage", "tag", "ForgeCaps", "Owner", "Rot", "SkullType");
	private static final String SUPPORT = "sr_support";
	private final WrappedNativeCache cache = new WrappedNativeCache();
	private final Map<String, Object> original;
	private final Object handle;
	private final boolean root;
	private final String storageKey;

	WrappedMap(Object handle, boolean root, String storageKey, Map<String, Object> original) {
		this.handle = handle;
		this.root = root;
		this.storageKey = storageKey;
		this.original = original;
	}

	private WrappedMap getStorageMap(Object key) {
		return this.storeInTag(key) ? SimplifiedAPI.getNbtFactory().fromCompound(this.original.get(this.getStorageKey())) : this;
	}

	public String getStorageKey() {
		return this.storageKey;
	}

	private NbtCompound getSupportNbt(boolean create) {
		WrappedMap map = this.getStorageMap(SUPPORT);

		if (!map.original.containsKey(SUPPORT)) {
			if (!create)
				return SimplifiedAPI.getNbtFactory().createCompound();

			map.original.put(SUPPORT, SimplifiedAPI.getNbtFactory().createCompound().getHandle());
		}

		return SimplifiedAPI.getNbtFactory().fromCompound(map.original.get(SUPPORT));
	}

	private void addSupportKey(String key, Class<?> clazz) {
		NbtCompound support = this.getSupportNbt(true);

		if (!support.containsKey(key))
			support.put(key, SimplifiedAPI.getNbtFactory().createCompound());

		clazz = Primitives.wrap(clazz);
		NbtCompound keyCompound = support.get(key);
		keyCompound.put("package", clazz.getPackage().getName());
		keyCompound.put("class", clazz.getSimpleName());
	}

	private Object adjustIncoming(String key, Object value) {
		Object adjusted = NbtFactory.adjustIncoming(value);

		if (!Objects.equals(adjusted, value))
			this.addSupportKey(key, Primitives.unwrap(value.getClass()));

		return adjusted;
	}

	@SuppressWarnings("all")
	private Object adjustOutgoing(Object key, Object value) {
		if (value == null)
			return null;

		if (this.hideSupport(key))
			return null;

		if (!this.getStorageMap(key).original.containsKey(SUPPORT))
			return value;

		NbtCompound support = this.getSupportNbt(false);

		if (support.containsKey(key)) {
			NbtCompound keyCompound = (NbtCompound)support.get(key);
			Class<?> clazz = Primitives.unwrap(new Reflection(keyCompound.get("class"), keyCompound.get("package")).getClazz());
			value = NbtFactory.adjustOutgoing(value, clazz);
		}

		return value;
	}

	@Override
	public void clear() {
		this.original.clear();
		this.save();
	}

	@Override
	public boolean containsKey(Object key) {
		if (this.hideSupport(key))
			return false;
		else {
			WrappedMap map = this.getStorageMap(key);
			return map.original.containsKey(key);
		}
	}

	private boolean hideSupport(Object key) {
		return SUPPORT.equals(key);
	}

	@Nonnull
	@Override
	public Set<Entry<String, Object>> entrySet() {
		return new AbstractSet<Entry<String, Object>>() {

			@Override
			public boolean add(Entry<String, Object> entry) {
				WrappedMap.this.put(entry.getKey(), entry.getValue());
				return true;
			}

			@Override
			public int size() {
				return WrappedMap.this.size();
			}

			@Nonnull
			@Override
			public Iterator<Entry<String, Object>> iterator() {
				return WrappedMap.this.iterator();
			}

			@Override
			public boolean remove(Object obj) {
				return WrappedMap.this.remove(obj) != null;
			}

			@Override
			public boolean removeAll(Collection<?> coll) {
				int size = this.size();
				coll.forEach(WrappedMap.this::remove);
				return this.size() < size;
			}

		};
	}

	@Override
	public Object get(Object key) {
		Preconditions.checkArgument(String.class.isAssignableFrom(key.getClass()), "Key must be of type java.lang.String!");
		WrappedMap map = this.getStorageMap(key);
		return this.wrapOutgoing(key, map.original.get(key));
	}

	@Override
	public final Object getHandle() {
		return this.handle;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	public final boolean isRoot() {
		return this.root;
	}

	@SuppressWarnings("all")
	public final boolean isRootTag(Object key) {
		return this.root && ROOT_TAGS.contains(key);
	}

	@SuppressWarnings("all")
	public final boolean storeInTag(Object key) {
		return this.root && !ROOT_TAGS.contains(key);
	}

	private Iterator<Entry<String, Object>> iterator() {
		ConcurrentSet<Entry<String, Object>> entrySet = Concurrent.newSet(this.original.entrySet());
		entrySet.stream().filter(entry -> this.hideSupport(entry.getKey())).forEach(entrySet::remove);
		final Iterator<Entry<String, Object>> proxy = entrySet.iterator();

		return new Iterator<Entry<String, Object>>() {

			private Entry<String, Object> current = null;

			@Override
			public boolean hasNext() {
				return proxy.hasNext();
			}

			@Override
			public Entry<String, Object> next() {
				Entry<String, Object> entry = proxy.next();
				String key = entry.getKey();
				return this.current = new SimpleEntry<>(key, WrappedMap.this.wrapOutgoing(key, entry.getValue()));
			}

			@Override
			public void remove() {
				if (this.current != null) {
					WrappedMap.this.remove(this.current.getKey());
					this.current = null;
					WrappedMap.this.save();
				}
			}

		};
	}

	@Nonnull
	@Override
	public Set<String> keySet() {
		return this.original.keySet().stream().filter(key -> !this.hideSupport(key)).collect(Collectors.toSet());
	}

	public boolean notEmpty() {
		return !this.isEmpty();
	}

	private Object store(String key, Object value) {
		if (this.hideSupport(key))
			return null;

		WrappedMap map = this.getStorageMap(key);
		return map.original.put(key, this.unwrapIncoming(key, value));
	}

	@Override
	public Object put(String key, Object value) {
		Object oldValue = this.wrapOutgoing(key, this.store(key, value));
		this.save();
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ?> map) {
		for (Entry<? extends String, ?> entry : map.entrySet()) {
			if (this.hideSupport(entry.getKey())) continue;
			this.store(entry.getKey(), entry.getValue());
		}

		this.save();
	}

	public void putJson(JsonObject json) {
		this.putAll(new Gson().<Map<String, Object>>fromJson(json.toString(), new TypeToken<Map<String, Object>>(){}.getType()));
	}

	@Override
	public Object remove(Object key) {
		if (this.hideSupport(key))
			return null;

		if (this.isRootTag(key))
			return null;

		WrappedMap map = this.getStorageMap(key);
		Object oldValue = this.wrapOutgoing(key, map.original.remove(key));
		this.removeSupportKey(key);
		this.save();
		return oldValue;
	}

	@SuppressWarnings("all")
	private void removeSupportKey(Object key) {
		((WrappedMap)this.getSupportNbt(false)).original.remove(key);
	}

	protected void save() { }

	@Override
	public int size() {
		return this.keySet().size();
	}

	public final String serialize() {
		List<String> output = new ArrayList<>();

		for (Entry<String, Object> entry : this.entrySet()) {
			Object value = entry.getValue();

			if (byte[].class.isAssignableFrom(Primitives.unwrap(value.getClass())))
				value = SimplifiedAPI.getNbtFactory().fromBytes((byte[])value);

			if (value.getClass().isArray())
				value = Arrays.deepToString((Object[])value);

			if (WrappedMap.class.isAssignableFrom(value.getClass()))
				value = ((WrappedMap)value).serialize();

			if (WrappedList.class.isAssignableFrom(value.getClass()))
				value = ((WrappedList<?>)value).serialize();

			output.add(RegexUtil.lameColor(FormatUtil.format("{0}:{1}", entry.getKey(), value)));
		}

		return FormatUtil.format("'{'{0}'}'", (output.isEmpty() ? "" : StringUtil.implode(", ", output)));
	}

	@Override
	public String toString() {
		return this.serialize();
	}

	/**
	 * For converting NBT value back to Java value.
	 *
	 * @param key the nbt key.
	 * @param value the nbt value.
	 * @return java value of wrapped {@code value}.
	 */
	protected final Object wrapOutgoing(Object key, Object value) {
		return this.adjustOutgoing(key, this.cache.wrap(value));
	}

	/**
	 * Converts Java value to NBT value.
	 *
	 * @param key the nbt key.
	 * @param wrapped the java value.
	 * @return nbt type of passed {@code wrapped} value.
	 */
	protected final Object unwrapIncoming(String key, Object wrapped) {
		return NbtFactory.unwrapValue(this.adjustIncoming(key, wrapped));
	}

	@Nonnull
	@Override
	public Collection<Object> values() {
		return new AbstractCollection<Object>() {

			@Nonnull
			@Override
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {

					private final Iterator<Entry<String, Object>> i = WrappedMap.this.entrySet().iterator();

					@Override
					public boolean hasNext() {
						return i.hasNext();
					}

					@Override
					public Object next() {
						return i.next().getValue();
					}

					@Override
					public void remove() {
						i.remove();
					}

				};
			}

			@Override
			public int size() {
				return WrappedMap.this.size();
			}

			@Override
			public boolean isEmpty() {
				return WrappedMap.this.isEmpty();
			}

			@Override
			public void clear() {
				WrappedMap.this.clear();
			}

			@Override
			public boolean contains(Object value) {
				return WrappedMap.this.containsValue(value);
			}

		};
	}

}