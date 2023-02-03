package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Banking {

    @Getter private double balance;
    @Getter private ConcurrentList<Transaction> transactions = Concurrent.newList();

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Transaction {

        @Getter private double amount;
        @Getter private SkyBlockDate.RealTime timestamp;
        @Getter private Banking.Transaction.Action action;
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
