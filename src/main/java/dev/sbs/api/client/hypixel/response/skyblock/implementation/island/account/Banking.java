package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.account;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Banking {

    private double balance;
    private @NotNull ConcurrentList<Transaction> transactions = Concurrent.newList();

    @Getter
    public static class Transaction {

        private double amount;
        private SkyBlockDate.RealTime timestamp;
        private Action action;
        @Getter(AccessLevel.NONE)
        @SerializedName("initiator_name")
        private String initiatorName;

        public String getInitiatorName() {
            return this.initiatorName.replace("Â", ""); // API Artifact
        }

        public enum Action {

            WITHDRAW,
            DEPOSIT

        }

    }

}