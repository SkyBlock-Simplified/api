package dev.sbs.api.data.model.skyblock_menus;

import dev.sbs.api.data.model.Model;

public interface SkyBlockMenuModel extends Model {

    String getKey();

    String getName();

    Boolean getHasCommand();

    Boolean getHasSubMenu();

}
