package gg.sbs.api.data.yaml;

import gg.sbs.api.data.yaml.annotation.PreserveStatic;
import gg.sbs.api.data.yaml.converter.*;
import gg.sbs.api.data.yaml.exception.InvalidConverterException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class InternalConverter {

	private final transient LinkedHashSet<YamlConverter> converters = new LinkedHashSet<>();
	private final transient LinkedHashSet<YamlConverter> customConverters = new LinkedHashSet<>();
	private final transient Set<Class<? extends YamlConverter>> customConverterClasses = new HashSet<>();

	public InternalConverter() {
		this.addConverter(PrimitiveConverter.class);
		this.addConverter(ConfigConverter.class);
		this.addConverter(ArrayConverter.class);
		this.addConverter(ListConverter.class);
		this.addConverter(EnumConverter.class);
		this.addConverter(MapConverter.class);
		this.addConverter(SetConverter.class);
		this.addConverter(VectorConverter.class);
	}

	private void addConverter(Class<? extends YamlConverter> converter) throws InvalidConverterException {
		this.addConverter(converter, this.converters);
	}

	private void addConverter(Class<? extends YamlConverter> converter, LinkedHashSet<YamlConverter> converters) throws InvalidConverterException {
		try {
			converters.add(converter.getConstructor(InternalConverter.class).newInstance(this));
		} catch (NoSuchMethodException nsmException) {
			throw new InvalidConverterException("Converter does not implement a constructor which takes the InternalConverter instance!", nsmException);
		} catch (InvocationTargetException intException) {
			throw new InvalidConverterException("Converter could not be invoked!", intException);
		} catch (InstantiationException insException) {
			throw new InvalidConverterException("Converter could not be instantiated!", insException);
		} catch (IllegalAccessException ilaException) {
			throw new InvalidConverterException("Converter does not implement a public Constructor which takes the InternalConverter instance!", ilaException);
		}
	}

	protected void addCustomConverter(Class<? extends YamlConverter> converter) throws InvalidConverterException {
		this.addConverter(converter, this.customConverters);
		this.customConverterClasses.add(converter);
	}

	public void fromConfig(YamlMap yamlMap, Field field, ConfigSection root, String path) throws Exception {
		Object obj = field.get(yamlMap);
		YamlConverter converter;

		if (obj != null) {
			converter = this.getConverter(obj.getClass());

			if (converter != null) {
				if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(PreserveStatic.class)) {
					if (!field.getAnnotation(PreserveStatic.class).value())
						return;
				}

				field.set(yamlMap, converter.fromConfig(obj.getClass(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			}
		}

		converter = this.getConverter(field.getType());
		if (converter != null) {
			if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(PreserveStatic.class)) {
				if (!field.getAnnotation(PreserveStatic.class).value())
					return;
			}

			field.set(yamlMap, converter.fromConfig(field.getType(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
			return;
		}

		if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(PreserveStatic.class)) {
			if (!field.getAnnotation(PreserveStatic.class).value())
				return;
		}

		field.set(yamlMap, root.get(path));
	}

	public final YamlConverter getConverter(Class<?> type) {
		for (YamlConverter converter : this.customConverters) {
			if (converter.supports(type))
				return converter;
		}

		for (YamlConverter converter : this.converters) {
			if (converter.supports(type))
				return converter;
		}

		return null;
	}

	public final Set<Class<? extends YamlConverter>> getCustomConverters() {
		return Collections.unmodifiableSet(this.customConverterClasses);
	}

	public void toConfig(YamlMap yamlMap, Field field, ConfigSection root, String path) throws Exception {
		Object obj = field.get(yamlMap);
		YamlConverter converter;

		if (obj != null) {
			converter = this.getConverter(obj.getClass());

			if (converter != null) {
				root.set(path, converter.toConfig(obj.getClass(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			}

			converter = this.getConverter(field.getType());

			if (converter != null) {
				root.set(path, converter.toConfig(field.getType(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			}
		}

		root.set(path, obj);
	}

}