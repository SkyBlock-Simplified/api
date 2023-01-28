package dev.sbs.api.util.helper;

import com.google.common.collect.Iterables;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Array and List Checking/Converting
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtil {

	/**
	 * Gets if the {@code value} is empty or null.
	 *
	 * @param array to check
	 * @return true if empty or null, otherwise false
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Gets if the {@code iterable} is empty or null.
	 *
	 * @param iterable to check
	 * @return true if empty or null, otherwise false
	 */
	public static <T> boolean isEmpty(Iterable<T> iterable) {
		if (iterable instanceof Collection)
			return ((Collection<?>) iterable).isEmpty();

		return iterable == null || !iterable.iterator().hasNext();
	}

	/**
	 * Ensures the given array is not null and does not contain a null element.
	 *
	 * @param array The array to check.
	 * @param <T> The type of elements.
	 * @throws IllegalArgumentException If the given array is null or contains a null element.
	 */
	public static <T> void noNullElements(T[] array) throws IllegalArgumentException {
		noNullElements(array, "The validated array is NULL!", "The validated array contains NULL element at index {0}!");
	}

	/**
	 * Ensures the given collection is not null and does not contain a null element.
	 *
	 * @param collection The collection to check.
	 * @param <T> The type of elements.
	 * @throws IllegalArgumentException If the given collection is null or contains a null element.
	 */
	public static <T> void noNullElements(Collection<? extends T> collection) throws IllegalArgumentException {
		noNullElements(collection, "The validated collection is NULL!", "The validated collection contains NULL element at index {0}!");
	}

	/**
	 * Ensures the given array is not null and does not contain a null element.
	 *
	 * @param array The array to check.
	 * @param <T> The type of elements.
	 * @param message The custom message to display if an IllegalArgumentException is thrown.
	 * @throws IllegalArgumentException If the given array is null or contains a null element.
	 */
	public static <T> void noNullElements(T[] array, String message) throws IllegalArgumentException {
		noNullElements(array, message, message);
	}

	/**
	 * Ensures the given collection is not null and does not contain a null element.
	 *
	 * @param collection The collection to check.
	 * @param <T> The type of elements.
	 * @param message The custom message to display if an IllegalArgumentException is thrown.
	 * @throws IllegalArgumentException If the given collection is null or contains a null element.
	 */
	public static <T> void noNullElements(Collection<? extends T> collection, String message) throws IllegalArgumentException {
		noNullElements(collection, message, message);
	}

	private static <T> void noNullElements(T[] array, String message, String elementMessage) throws IllegalArgumentException {
		if (array == null)
			throw new IllegalArgumentException(message);

		for (int i = 0; i < array.length; i++) {
			if (array[i] == null)
				throw new IllegalArgumentException(FormatUtil.format(elementMessage, i));
		}
	}

	private static <T> void noNullElements(Collection<? extends T> collection, String message, String elementMessage) throws IllegalArgumentException {
		if (collection == null)
			throw new IllegalArgumentException(message);

		Iterator<? extends T> iterator = collection.iterator();
		int i = -1;

		while (iterator.hasNext()) {
			T obj = iterator.next();
			i++;

			if (obj == null)
				throw new IllegalArgumentException(FormatUtil.format(elementMessage, i));
		}
	}

	/**
	 * Gets if the {@code array} is not empty.
	 *
	 * @param array to check
	 * @return true if not empty or null, otherwise false
	 */
	public static <T> boolean notEmpty(T[] array) {
		return !isEmpty(array);
	}

	/**
	 * Gets if the {@code iterable} is not empty.
	 *
	 * @param iterable to check
	 * @return true if not empty or null, otherwise false
	 */
	public static <T> boolean notEmpty(Iterable<T> iterable) {
		return !isEmpty(iterable);
	}

	/**
	 * Gets the number of elements in this array.
	 *
	 * @param array Array to retrieve size of.
	 * @return Number of elements in this array.
	 */
	public static <T> int sizeOf(T[] array) {
		return array.length;
	}

	/**
	 * Gets the number of elements in this collection.
	 *
	 * @param collection Collection to retrieve size of.
	 * @return Number of elements in this collection.
	 */
	public static <T> int sizeOf(Collection<? extends T> collection) {
		return collection.size();
	}

	/**
	 * Converts the given iterable into an array of the given type.
	 *
	 * @param iterable The iterable to convert to array.
	 * @param type The type of elements the iterable contains.
	 * @return The converted array of the passed collection.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
		try {
			return Iterables.toArray(iterable, type);
		} catch (NullPointerException npe) {
			return (T[]) Array.newInstance(type, 0);
		}
	}

	public static ConcurrentList<String> appendEach(ConcurrentList<String> stringList, String entryValue, String lastEntry) {
		return StreamUtil.appendEach(stringList.stream(), entryValue, lastEntry).collect(Concurrent.toList());
	}

	public static ConcurrentList<String> prependEach(ConcurrentList<String> stringList, String entryValue, String lastEntry) {
		return StreamUtil.prependEach(stringList.stream(), entryValue, lastEntry).collect(Concurrent.toList());
	}

	/**
	 * Collect a stream into a single item, only if there is 1 item expected.
	 *
	 * @return Singleton object from stream.
	 * @throws IllegalStateException If result size is not equal to 1.
	 */
	public static <T> Collector<T, ?, T> toSingleton() {
		return Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.isEmpty())
						return null;

					return list.get(0);
				}
		);
	}
}
