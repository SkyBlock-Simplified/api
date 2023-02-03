package dev.sbs.api.client.hypixel.response.skyblock.implementation.playerstats.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Data {

    @Getter double base;
    @Getter double bonus;

    public Data() {
        this(0, 0);
    }

    void addBase(double value) {
        this.base += value;
    }

    void addBonus(double value) {
        this.bonus += value;
    }

    public final double getTotal() {
        return this.getBase() + this.getBonus();
    }

}