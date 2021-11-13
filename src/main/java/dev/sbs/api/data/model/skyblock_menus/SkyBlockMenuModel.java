package dev.sbs.api.data.model.skyblock_menus;

import dev.sbs.api.data.model.Model;

public interface SkyBlockMenuModel extends Model {

    String getKey();

    String getName();

    boolean getHasCommand();

    boolean getHasSubMenu();

}
