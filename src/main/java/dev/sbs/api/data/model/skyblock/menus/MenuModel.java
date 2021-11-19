package dev.sbs.api.data.model.skyblock.menus;

import dev.sbs.api.data.model.Model;

public interface MenuModel extends Model {

    String getKey();

    String getName();

    Boolean getHasCommand();

    Boolean getHasSubMenu();

}
