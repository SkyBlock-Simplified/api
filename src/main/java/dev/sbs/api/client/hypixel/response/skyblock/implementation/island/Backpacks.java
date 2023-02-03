package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Backpacks {

    @Getter private final ConcurrentMap<Integer, NbtContent> contents;
    @Getter private final ConcurrentMap<Integer, NbtContent> icons;

}