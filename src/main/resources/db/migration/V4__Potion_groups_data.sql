INSERT INTO `potion_groups` (`key`, `name`) VALUES
    ('GOD_POTION', 'God Potion'),
    ('DUNGEON_POTION_1', 'Dungeon I Potion'),
    ('DUNGEON_POTION_2', 'Dungeon II Potion'),
    ('DUNGEON_POTION_3', 'Dungeon III Potion'),
    ('DUNGEON_POTION_4', 'Dungeon IV Potion'),
    ('DUNGEON_POTION_5', 'Dungeon V Potion'),
    ('DUNGEON_POTION_6', 'Dungeon VI Potion'),
    ('DUNGEON_POTION_7', 'Dungeon VII Potion');

INSERT INTO `potion_mixins` (`key`, `name`, `slayer_key`, `slayer_level`, `effects`, `buff_effects`) VALUES
    ('ZOMBIE_BRAIN', 'Zombie Brain', 'ZOMBIE', 8, '{"FEROCITY":10}', '{}'),
    ('SPIDER_EGG', 'Spider Egg', 'SPIDER', 8, '{}', '{"DODGE":5}'),
    ('WOLF_FUR', 'Wolf Fur', 'WOLF', 8, '{"MAGIC_FIND":7}', '{}'),
    ('END_PORTAL_FUMES', 'End Portal Fumes', 'ENDERMAN', 8, '{}', '{"OVERFLOW":30}');

INSERT INTO `potion_brews` (`key`, `name`, `rarity_key`, `description`, `source_npc_key`, `coin_cost`) VALUES
    ('CHEAP_COFFEE', 'Cheap Coffee', 'COMMON', 'Adds %{VALUE:PLUS} %{STAT:SPEED} to potions with this stat.', 'BARTENDER', 1000),
    ('TEPID_GREEN_TEA', 'Tepid Green Tea', 'COMMON', 'Buffs the %{STAT:DEFENSE} value of potions by %{VALUE:PLUS:PERCENT}.', 'BARTENDER', 1000),
    ('PULPOUS_ORANGE_JUICE', 'Pulpous Orange Juice', 'COMMON', 'Buffs the %{STAT:HEALTH} value of potions by %{VALUE:PLUS:PERCENT}.', 'BARTENDER', 1000),
    ('BITTER_ICE_TEA', 'Bitter Ice Tea', 'COMMON', 'Buffs the %{SYMBOL:INTELLIGENCE}%{FORMAT:AQUA}Mana%{FORMAT:GRAY} and %{FORMAT:AQUA}Experience %{FORMAT:GRAY}values of potions by %{VALUE:PLUS:PERCENT}.', 'BARTENDER', 1200),
    ('KNOCKOFF_COLA', 'KnockOff™ Cola', 'COMMON', 'Buffs the %{STAT:STRENGTH} value of potions by %{VALUE:PLUS:PERCENT}.', 'BARTENDER', 1500),
    ('DECENT_COFFEE', 'Decent Coffee', 'UNCOMMON', 'Adds %{VALUE:PLUS} %{STAT:SPEED} to potions with that stat.', 'BARTENDER', 5000),
    ('VIKINGS_TEAR', 'Viking\'s Tear', 'RARE', 'Experience potions:\n %{VALUE:EXPERIENCE_ORBS:PLUS:PERCENT} experience orbs.\n %{VALUE:COMBAT_XP_BREW:PLUS:PERCENT} Combat XP.\n %{VALUE:HEALTH_PER_SECOND_BREW:PLUS}%{SYMBOL:HEALTH} per second.', 'MELANCHOLIC_VIKING', 15000),
    ('TUTTI_FRUTTI_FLAVORED_POISON', 'Tutti-Frutti Flavored Poison', 'COMMON', 'Adds %{VALUE:PLUS:PERCENT} to the damage of Archery potions.', 'SHIFTY', 1000),
    ('DOCTOR_PEPPER', 'Dctr. Paper', 'UNCOMMON', 'Adds %{VALUE:PLUS} %{FORMAT:GOLD}Absorption %{SYMBOL:HEALTH} to potions with that stat.', 'SHIFTY', 4000),
    ('SLAYER_ENERGY_DRINK', 'Slayer© Energy Drink', 'RARE', 'Adds %{VALUE:+}%{STAT:MAGIC_FIND} to Critical potions.', 'SHIFTY', 10000);

INSERT INTO `potion_brew_buffs` (`potion_brew_key`, `buff_key`, `buff_value`, `percentage`) VALUES
    ('CHEAP_COFFEE', 'SPEED', 5, 0),
    ('TEPID_GREEN_TEA', 'DEFENSE', 10, 1),
    ('PULPOUS_ORANGE_JUICE', 'HEALTH', 5, 1),
    ('BITTER_ICE_TEA', 'MANA', 20, 1),
    ('BITTER_ICE_TEA', 'EXPERIENCE', 20, 1),
    ('KNOCKOFF_COLA', 'STRENGTH', 5, 1),
    ('DECENT_COFFEE', 'SPEED', 8, 0),
    ('VIKINGS_TEAR', 'EXPERIENCE_ORBS', 20, 0),
    ('VIKINGS_TEAR', 'COMBAT_XP_BREW', 10, 0),
    ('VIKINGS_TEAR', 'HEALTH_PER_SECOND_BREW', 10, 0),
    ('TUTTI_FRUTTI_FLAVORED_POISON', 'BOW_DAMAGE', 5, 0),
    ('DOCTOR_PEPPER', 'ABSORPTION', 75, 0),
    ('SLAYER_ENERGY_DRINK', 'MAGIC_FIND_BREW', 10, 0);