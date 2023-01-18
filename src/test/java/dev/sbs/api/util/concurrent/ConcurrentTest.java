package dev.sbs.api.util.concurrent;

import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public class ConcurrentTest {

    @Test
    public void getConcurrentModificationException_ok() {
        ArrayList<Double> randomNumbers = new ArrayList<>();
        long addStart = System.currentTimeMillis();
        int total = 100000;
        new Random().doubles(total, 1, 100000).forEach(randomNumbers::add);
        long addEnd = System.currentTimeMillis();
        long addDuration = addEnd - addStart;
        System.out.println("Took " + addDuration + "ms to add " + total + " numbers");

        for (double number : randomNumbers)
            randomNumbers.remove(number); // THROWS java.util.ConcurrentModificationException

        long removeEnd = System.currentTimeMillis();
        long removeDuration = removeEnd - addEnd;

        System.out.println("Took " + removeDuration + "ms to remove " + total + " numbers concurrently");
        assert randomNumbers.size() == 0;
    }

    @Test
    public void getNoConcurrentModificationException_ok() {
        ConcurrentSet<Double> randomNumbers = new ConcurrentSet<>();
        long addStart = System.currentTimeMillis();
        int total = 100000;
        new Random().doubles(total, 1, 100000).forEach(randomNumbers::add);
        long addEnd = System.currentTimeMillis();
        long addDuration = addEnd - addStart;
        System.out.println("Took " + addDuration + "ms to add " + total + " numbers");

        for (double number : randomNumbers)
            randomNumbers.remove(number); // DOES NOT THROW java.util.ConcurrentModificationException

        long removeEnd = System.currentTimeMillis();
        long removeDuration = removeEnd - addEnd;

        System.out.println("Took " + removeDuration + "ms to remove " + total + " numbers concurrently");
        assert randomNumbers.size() == 0;
    }

    @Test
    public void getCMETest3_ok() {
        CopyOnWriteArrayList<Double> randomNumbers = new CopyOnWriteArrayList<>();
        long addStart = System.currentTimeMillis();
        int total = 100000;
        new Random().doubles(total, 1, 100000).forEach(randomNumbers::add);
        long addEnd = System.currentTimeMillis();
        long addDuration = addEnd - addStart;
        System.out.println("Took " + addDuration + "ms to add " + total + " numbers"); // 7810ms

        for (double number : randomNumbers)
            randomNumbers.remove(number); // DOES NOT THROW java.util.ConcurrentModificationException

        long removeEnd = System.currentTimeMillis();
        long removeDuration = removeEnd - addEnd;

        System.out.println("Took " + removeDuration + "ms to remove " + total + " numbers concurrently"); // 3743ms
        assert randomNumbers.size() == 0;
    }

    @Test
    public void getCMETest5_ok() {
        ConcurrentMap<Integer, Double> randomNumbers = Concurrent.newMap();
        long addStart = System.currentTimeMillis();
        int total = 100000;
        new Random().doubles(total, 1, 100000).forEach(value -> randomNumbers.put(randomNumbers.size(), value));
        long addEnd = System.currentTimeMillis();
        long addDuration = addEnd - addStart;
        System.out.println("Took " + addDuration + "ms to add " + total + " numbers"); // 7810ms

        for (Map.Entry<Integer, Double> number : randomNumbers)
            randomNumbers.remove(number.getKey()); // DOES NOT THROW java.util.ConcurrentModificationException

        long removeEnd = System.currentTimeMillis();
        long removeDuration = removeEnd - addEnd;

        System.out.println("Took " + removeDuration + "ms to remove " + total + " numbers concurrently"); // 3743ms
        assert randomNumbers.size() == 0;
    }

}
