package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.collection.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.data.tuple.Pair;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MelodyHarp {

    @Getter private final boolean talismanClaimed;
    @Getter private final String selectedSong;
    @Getter private final SkyBlockDate.RealTime selectedSongTimestamp;
    @Getter private final ConcurrentMap<String, Song> songs;

    MelodyHarp(ConcurrentMap<String, Object> harpQuest) {
        this.talismanClaimed = (boolean) harpQuest.removeOrGet("claimed_talisman", false);
        this.selectedSong = (String) harpQuest.removeOrGet("selected_song", "");
        long epoch = NumberUtil.createNumber(String.valueOf(harpQuest.removeOrGet("selected_song_epoch", 0))).longValue();
        this.selectedSongTimestamp = new SkyBlockDate.RealTime(epoch * 1000) ;

        ConcurrentLinkedMap<String, ConcurrentMap<String, Integer>> songMap = Concurrent.newLinkedMap();
        harpQuest.forEach((harpKey, harpValue) -> {
            if (harpValue instanceof Number) {
                String songKey = harpKey.replace("song_", "");
                String songName = songKey.replaceAll("_((best|perfect)_)?completions?", "");
                String category = songKey.replace(FormatUtil.format("{0}_", songName), "");

                if (!songMap.containsKey(songName))
                    songMap.put(songName, Concurrent.newMap());

                songMap.get(songName).put(category, NumberUtil.createNumber(harpValue.toString()).intValue());
            }
        });

        this.songs = Concurrent.newUnmodifiableMap(
            songMap.stream()
                .map(entry -> Pair.of(
                    entry.getKey(),
                    new Song(
                        entry.getValue().getOrDefault("best_completion", 0),
                        entry.getValue().getOrDefault("completions", 0),
                        entry.getValue().getOrDefault("perfect_completions", 0)
                    )
                ))
                .collect(Concurrent.toMap())
        );
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Song {

        @Getter private final int bestCompletion;
        @Getter private final int completions;
        @Getter private final int perfectCompletions;

    }

}
