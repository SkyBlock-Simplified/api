CREATE TABLE `skyblock_menus` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `has_command` tinyint(1) NOT NULL DEFAULT 0,
    `has_sub_menu` tinyint(1) NOT NULL DEFAULT 0,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`)
);

INSERT INTO `skyblock_menus` (`key`, `name`, `has_command`, `has_sub_menu`) VALUES
    ('PLAYER', 'Player Inventory', false, false),
    ('SKYBLOCK_MENU', 'SkyBlock Menu', true, false),
    ('SKILLS', 'Your Skills', true, true),
    ('COLLECTION', 'Collection', true, true),
    ('RECIPE_BOOK', 'Recipe Book', true, true),
    ('ANVIL', 'Anvil', false, false),
    ('ENCHANTMENT_TABLE', 'Enchant Item', false, false),
    ('REFORGE', 'Reforge Item', false, false),
    ('CRAFTING_TABLE', 'Craft Item', false, false),
    ('ENDER_CHEST', 'Ender Chest', true, true),
    ('POTION_BAG', 'Potion Bag', false, true),
    ('FISHING_BAG', 'Fishing Bag', false, true),
    ('QUIVER', 'Quiver', false, true),
    ('ACCESSORY_BAG', 'Accessory Bag', false, true),
    ('AUCTION_HOUSE', 'Auction House', false, false),
    ('AUCTION_BROWSER', 'Auction Browser', false, false),
    ('SETTINGS', 'Settings', true, true),
    ('WARDROBE', 'Wardrobe', true, true),
    ('BANK', 'Bank', false, true),
    ('PERSONAL_BANK', 'Personal Bank', false, true),
    ('PETS', 'Pets', true, true),
    ('QUEST_LOG', 'Quest Log', true, true),
    ('CALENDAR', 'Calendar and Events', true, true),
    ('TRADES', 'Trades', true, true),
    ('PROFILES', 'Profiles Management', false, true),
    ('NPC', 'NPC', false, false),
    ('BACKPACK', 'Backpack', false, false);

CREATE TABLE `skyblock_bags` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `collection_item_id` varchar(127) NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`),
    CONSTRAINT `skyblock_bags_ibfk_1` FOREIGN KEY (`collection_item_id`) REFERENCES `collection_items` (`item_id`)
);

INSERT INTO `skyblock_bags` (`key`, `name`, `collection_item_id`) VALUES
    ('ENDER_CHEST', 'Ender Chest', 'OBSIDIAN'),
    ('FISHING_BAG', 'Fishing Bag', 'RAW_FISH'),
    ('POTION_BAG', 'Potion Bag', 'NETHER_STALK'),
    ('QUIVER', 'Quiver', 'STRING'),
    ('ACCESSORY_BAG', 'Accessory Bag', 'REDSTONE');

CREATE TABLE `skyblock_bag_sizes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `collection_tier` TINYINT NOT NULL,
    `slot_count` SMALLINT NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `bag_size` (`key`, `collection_tier`),
    CONSTRAINT `skyblock_bag_sizes_ibfk_1` FOREIGN KEY (`key`) REFERENCES `skyblock_bags` (`key`)
);

INSERT INTO `skyblock_bag_sizes` (`key`, `collection_tier`, `slot_count`) VALUES
    ('ENDER_CHEST', 0, 27),
    ('ENDER_CHEST', 5, 36),
    ('ENDER_CHEST', 7, 45),
    ('ENDER_CHEST', 9, 54),
    ('FISHING_BAG', 3, 9),
    ('FISHING_BAG', 7, 18),
    ('FISHING_BAG', 9, 27),
    ('FISHING_BAG', 10, 36),
    ('FISHING_BAG', 11, 45),
    ('POTION_BAG', 2, 9),
    ('POTION_BAG', 5, 18),
    ('POTION_BAG', 8, 27),
    ('POTION_BAG', 10, 36),
    ('POTION_BAG', 11, 45),
    ('QUIVER', 3, 27),
    ('QUIVER', 6, 36),
    ('QUIVER', 9, 45),
    ('ACCESSORY_BAG', 2, 3),
    ('ACCESSORY_BAG', 6, 9),
    ('ACCESSORY_BAG', 9, 15),
    ('ACCESSORY_BAG', 10, 21),
    ('ACCESSORY_BAG', 11, 27),
    ('ACCESSORY_BAG', 12, 33),
    ('ACCESSORY_BAG', 13, 39),
    ('ACCESSORY_BAG', 14, 45);

CREATE TABLE `slayers` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`)
);

INSERT INTO `slayers` (`key`, `name`) VALUES
    ('ZOMBIE', 'Revenant Horror'),
    ('SPIDER', 'Tarantula Broodfather'),
    ('WOLF', 'Sven Packmaster'),
    ('ENDERMAN', 'Voidgloom Seraph');

CREATE TABLE `slayer_levels` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `slayer_key` varchar(127) NOT NULL,
    `level` INT NOT NULL,
    `total_exp_required` double NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `slayer_level` (`slayer_key`, `level`),
    CONSTRAINT `slayer_levels_ibfk_1` FOREIGN KEY (`slayer_key`) REFERENCES `slayers` (`key`)
);

INSERT INTO `slayer_levels` (`slayer_key`, `level`, `total_exp_required`) VALUES
    ('ZOMBIE', 1, 5),
    ('ZOMBIE', 2, 15),
    ('ZOMBIE', 3, 200),
    ('ZOMBIE', 4, 1000),
    ('ZOMBIE', 5, 5000),
    ('ZOMBIE', 6, 20000),
    ('ZOMBIE', 7, 100000),
    ('ZOMBIE', 8, 400000),
    ('ZOMBIE', 9, 1000000),
    ('SPIDER', 1, 5),
    ('SPIDER', 2, 15),
    ('SPIDER', 3, 200),
    ('SPIDER', 4, 1000),
    ('SPIDER', 5, 5000),
    ('SPIDER', 6, 20000),
    ('SPIDER', 7, 100000),
    ('SPIDER', 8, 400000),
    ('SPIDER', 9, 1000000),
    ('WOLF', 1, 5),
    ('WOLF', 2, 15),
    ('WOLF', 3, 200),
    ('WOLF', 4, 1500),
    ('WOLF', 5, 5000),
    ('WOLF', 6, 20000),
    ('WOLF', 7, 100000),
    ('WOLF', 8, 400000),
    ('WOLF', 9, 1000000),
    ('ENDERMAN', 1, 5),
    ('ENDERMAN', 2, 15),
    ('ENDERMAN', 3, 200),
    ('ENDERMAN', 4, 1000),
    ('ENDERMAN', 5, 5000),
    ('ENDERMAN', 6, 20000),
    ('ENDERMAN', 7, 100000),
    ('ENDERMAN', 8, 400000),
    ('ENDERMAN', 9, 1000000);

CREATE TABLE `pet_scores` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `breakpoint` smallint NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `breakpoint` (`breakpoint`)
);

INSERT INTO `pet_scores` (`breakpoint`) VALUES
    (10),
    (25),
    (50),
    (75),
    (100),
    (130),
    (175);

CREATE TABLE `pet_exp_scale` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `value` INT NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `value` (`value`)
);

INSERT INTO `pet_exp_scale` (`value`) VALUES
    (0), (100), (110), (120), (130), (145), (160), (175), (190), (210), (230), (250), (275), (300), (330), (360),
    (400), (440), (490), (540), (600), (660), (730), (800), (880), (960), (1050), (1150), (1260), (1380), (1510), (1650),
    (1800), (1960), (2130), (2310), (2500), (2700), (2920), (3160), (3420), (3700), (4000), (4350), (4750), (5200),
    (5700), (6300), (7000), (7800), (8700), (9700), (10800), (12000), (13300), (14700), (16200), (17800), (19500),
    (21300), (23200), (25200), (27400), (29800), (32400), (35200), (38200), (41400), (44800), (48400), (52200),
    (56200), (60400), (64800), (69400), (74200), (79200), (84700), (90700), (97200), (104200), (111700), (119700),
    (128200), (137200), (146700), (156700), (167700), (179700), (192700), (206700), (221700), (237700), (254700),
    (272700), (291700), (311700), (333700), (357700), (383700), (411700), (441700), (476700), (516700), (561700),
    (611700), (666700), (726700), (791700), (861700), (936700), (1016700), (1101700), (1191700), (1286700),
    (1386700), (1496700), (1616700), (1746700), (1886700);

ALTER TABLE `rarities` ADD COLUMN `pet_exp_offset` TINYINT(1) DEFAULT 0 AFTER `key_valid`;

UPDATE `rarities` SET `pet_exp_offset` = 0 WHERE `key` = 'COMMON';
UPDATE `rarities` SET `pet_exp_offset` = 6 WHERE `key` = 'UNCOMMON';
UPDATE `rarities` SET `pet_exp_offset` = 11 WHERE `key` = 'RARE';
UPDATE `rarities` SET `pet_exp_offset` = 16 WHERE `key` = 'EPIC';
UPDATE `rarities` SET `pet_exp_offset` = 20 WHERE `key` = 'LEGENDARY';
UPDATE `rarities` SET `pet_exp_offset` = 20 WHERE `key` = 'MYTHIC';

CREATE TABLE `minion_uniques` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `placeable` TINYINT NOT NULL,
    `unique_crafts` SMALLINT NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `placeable` (`placeable`)
);

INSERT INTO `minion_uniques` (`placeable`, `unique_crafts`) VALUES
    (5, 0),
    (6, 5),
    (7, 15),
    (8, 30),
    (9, 50),
    (10, 75),
    (11, 100),
    (12, 125),
    (13, 150),
    (14, 175),
    (15, 200),
    (16, 225),
    (17, 250),
    (18, 275),
    (19, 300),
    (20, 350),
    (21, 400),
    (22, 450),
    (23, 500),
    (24, 550),
    (25, 600),
    (26, 650);