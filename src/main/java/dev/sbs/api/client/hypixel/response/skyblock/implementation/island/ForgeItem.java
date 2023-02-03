package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ForgeItem {

    @Getter private String type;
    private String id;
    @Getter private SkyBlockDate.RealTime startTime;
    @Getter private int slot;
    @Getter private boolean notified;

    public ItemModel getItem() {
        return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirstOrNull(ItemModel::getItemId, this.id);
    }

}