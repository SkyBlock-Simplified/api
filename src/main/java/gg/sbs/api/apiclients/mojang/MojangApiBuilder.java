package gg.sbs.api.apiclients.mojang;

import gg.sbs.api.apiclients.ApiBuilder;
import gg.sbs.api.apiclients.mojang.implementation.MojangDataInterface;

public final class MojangApiBuilder extends ApiBuilder<MojangDataInterface> {

    public MojangApiBuilder() {
        super("api.skyblocksimplified.com");
    }

}