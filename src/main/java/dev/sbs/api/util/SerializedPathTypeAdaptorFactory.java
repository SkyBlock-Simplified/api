package dev.sbs.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.ListUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@NoArgsConstructor
public final class SerializedPathTypeAdaptorFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        // Pick up the down stream type adapter to avoid infinite recursion
        final TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, typeToken);
        // Collect @JsonPathExpression-annotated fields
        final Collection<FieldInfo> fieldInfos = FieldInfo.of(typeToken.getRawType());
        // If no such fields found, then just return the delegated type adapter
        // Otherwise wrap the type adapter in order to make some annotation processing
        return fieldInfos.isEmpty()
            ? delegateAdapter
            : new JsonPathTypeAdapter<>(gson, delegateAdapter, gson.getAdapter(JsonElement.class), fieldInfos);
    }

    private static final class JsonPathTypeAdapter<T> extends TypeAdapter<T> {

        @Getter private final Gson gson;
        @Getter private final TypeAdapter<T> delegateAdapter;
        @Getter private final TypeAdapter<JsonElement> jsonElementTypeAdapter;
        @Getter private final Collection<FieldInfo> fieldInfos;

        private JsonPathTypeAdapter(Gson gson, TypeAdapter<T> delegateAdapter, TypeAdapter<JsonElement> jsonElementTypeAdapter, Collection<FieldInfo> fieldInfos) {
            this.gson = gson;
            this.delegateAdapter = delegateAdapter;
            this.jsonElementTypeAdapter = jsonElementTypeAdapter;
            this.fieldInfos = fieldInfos;
        }

        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            // JsonPath can only read by expression, but not write by expression, so we can only write it as it is...
            this.getDelegateAdapter().write(out, value);
        }

        @Override
        public T read(final JsonReader in) throws IOException {
            // Building the original JSON tree to keep *all* fields
            final JsonElement outerJsonElement = this.getJsonElementTypeAdapter().read(in).getAsJsonObject();
            // Deserialize the value, not-existing fields will be omitted
            final T value = this.getDelegateAdapter().fromJsonTree(outerJsonElement);

            for (FieldInfo fieldInfo : this.getFieldInfos()) {
                try {
                    JsonElement innerJsonElement = outerJsonElement;
                    boolean skip = false;

                    // Resolve the JSON element by a JSON path expression
                    for (String pathNode : fieldInfo.getJsonPathList()) {
                        JsonObject innerJsonObject = innerJsonElement.getAsJsonObject();

                        if (innerJsonObject.has(pathNode))
                            innerJsonElement = innerJsonObject.get(pathNode);
                        else {
                            skip = true;
                            break;
                        }
                    }

                    if (skip)
                        continue;

                    // Convert it to the field type
                    final Object innerValue = this.getGson().fromJson(innerJsonElement, fieldInfo.getField().getType());

                    // Now it can be assigned to the object field...
                    fieldInfo.getField().set(value, innerValue);
                //} catch (PathNotFoundException ignored) {
                    // if no path given, then just ignore the assignment to the field
                } catch (IllegalAccessException ex) {
                    throw new IOException(ex);
                }
            }

            return value;
        }

    }

    private static final class FieldInfo {

        @Getter private final Field field;
        @Getter private final String jsonPath;
        @Getter private final ConcurrentList<String> jsonPathList;

        private FieldInfo(Field field, String jsonPath) {
            this.field = field;
            this.jsonPath = jsonPath;
            this.jsonPathList = Concurrent.newList(StringUtil.split(jsonPath, "."));
        }

        // Scan the given class for the JsonPathExpressionAnnotation
        private static Collection<FieldInfo> of(Class<?> clazz) {
            Collection<FieldInfo> collection = new ArrayList<>();

            for (final Field field : clazz.getDeclaredFields()) {
                final SerializedPath serializedPath = field.getAnnotation(SerializedPath.class);

                if (Objects.nonNull(serializedPath)) {
                    if (ListUtil.isEmpty(collection))
                        collection = new ArrayList<>();

                    field.setAccessible(true);
                    collection.add(new FieldInfo(field, serializedPath.value()));
                }
            }

            return collection;
        }

    }

}
