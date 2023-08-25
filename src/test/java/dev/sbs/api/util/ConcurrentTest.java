package dev.sbs.api.util;

import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("all")
public class ConcurrentTest {

    private static int HOW_MANY = 1_000_000;
    private static int NUM_THREADS = 16;

    @Test
    public void getNoCME_ok() throws Exception {
        for (int trial = 0; trial < 5; trial++) {
            List<Thread> threads = new ArrayList<>();

            ConcurrentMap<Integer, String> randomNumbers = new ConcurrentMap<>();
            for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
                final Thread thread = new Thread(() -> {
                    int x = 0;

                    for (int i = 0; i < HOW_MANY; i++) {
                        if (randomNumbers.size() >= 10_000) {
                            randomNumbers.clear();
                            x = 0;
                        }

                        randomNumbers.putIfAbsent(x++, UUID.randomUUID().toString());
                    }
                });
                threads.add(thread);
                thread.start();
            }
            long start = System.currentTimeMillis();
            for (Thread t : threads) t.join();
            long end = System.currentTimeMillis();
            System.out.println("v1: " + (end - start));

            /*ConcurrentMap2<Integer, String> randomNumbers2 = new ConcurrentMap2<>();
            for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
                final Thread thread = new Thread(() -> {
                    int x = 0;

                    for (int i = 0; i < HOW_MANY; i++) {
                        if (randomNumbers2.size() >= 10_000) {
                            randomNumbers2.clear();
                            x = 0;
                        }

                        randomNumbers2.putIfAbsent(x++, UUID.randomUUID().toString());
                    }
                });
                threads.add(thread);
                thread.start();
            }
            start = System.currentTimeMillis();
            for (Thread t : threads) t.join();
            end = System.currentTimeMillis();
            System.out.println("v2: " + (end - start));*/

            ConcurrentList<Integer> randomNumbers3 = new ConcurrentList<>();
            for (int threadNum = 0; threadNum < NUM_THREADS; threadNum++) {
                final Thread thread = new Thread(() -> {
                    int x = 0;

                    for (int i = 0; i < HOW_MANY; i++) {
                        if (randomNumbers3.size() >= 10_000) {
                            randomNumbers3.clear();
                            x = 0;
                        }

                        randomNumbers3.add(x++);
                    }
                });
                threads.add(thread);
                thread.start();
            }
            start = System.currentTimeMillis();
            for (Thread t : threads) t.join();
            end = System.currentTimeMillis();
            System.out.println("v3: " + (end - start));

            System.out.println("---");
        }
    }

}
