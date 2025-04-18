package dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.island.mining;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ForgeItem {

    private String type;
    @SerializedName("id")
    private String itemId;
    @SerializedName("startTime")
    private SkyBlockDate.RealTime started;
    private int slot;
    private boolean notified;

    /**
     * Gets the {@link ItemModel} for the given {@link #getItemId()}.
     * <br><br>
     * Requires an active database session.
     */
    public @NotNull ItemModel getItemModel() {
        return SimplifiedApi.getRepositoryOf(ItemModel.class).findFirstOrNull(ItemModel::getItemId, this.getItemId());
    }

}