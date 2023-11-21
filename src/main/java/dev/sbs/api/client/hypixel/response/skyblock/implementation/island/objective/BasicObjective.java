package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objective;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.helper.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicObjective {

    @Getter private BasicObjective.Status status;
    @SerializedName("completed_at")
    @Getter private SkyBlockDate.RealTime completed;

    public enum Status {

        ACTIVE,
        COMPLETE;

        public String getName() {
            return StringUtil.capitalizeFully(this.name());
        }

    }

}