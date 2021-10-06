ALTER TABLE `items`
    ADD UNIQUE (`item_id`);

ALTER TABLE `collection_items`
    ADD UNIQUE (`item_key`);

ALTER TABLE `collections`
    ADD UNIQUE (`collection_key`);

ALTER TABLE `rarities`
    RENAME COLUMN `has_hypixel_name` TO `has_hypixel_name_old`,
    RENAME COLUMN `hypixel_name` TO `hypixel_name_old`;

ALTER TABLE `rarities`
    ADD COLUMN `has_hypixel_name` TINYINT(1) CHECK (`has_hypixel_name` BETWEEN 0 AND 1) AFTER `ordinal`,
    ADD COLUMN `rarity_tag` VARCHAR(127) AFTER `ordinal`;

UPDATE `rarities`
    SET `rarity_tag` = `hypixel_name_old`;

CREATE TABLE `minions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `item_id` VARCHAR(127) NOT NULL UNIQUE,
    `collection` VARCHAR(127),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`collection`) REFERENCES `collections`(`collection_key`)
);

CREATE TABLE `minion_tiers` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `minion` VARCHAR(127) NOT NULL,
    `tier` VARCHAR(127) NOT NULL UNIQUE,
    `speed` TINYINT NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`minion`) REFERENCES `minions`(`item_id`),
    FOREIGN KEY (`tier`) REFERENCES `items`(`item_id`)
);

CREATE TABLE `minion_tier_upgrades` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `minion_tier` VARCHAR(127) NOT NULL,
    `coin_cost` INT NOT NULL DEFAULT 0,
    `item_cost` VARCHAR(127),
    `quantity` SMALLINT NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`minion_tier`) REFERENCES `minion_tiers`(`tier`),
    FOREIGN KEY (`item_cost`) REFERENCES `collection_items`(`item_key`)
);

CREATE TABLE `minion_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `minion` VARCHAR(127) NOT NULL,
    `collection_item_key` VARCHAR(127) NOT NULL,
    `yield` DECIMAL NOT NULL DEFAULT 1.0,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`minion`) REFERENCES `minions`(`item_id`),
    FOREIGN KEY (`collection_item_key`) REFERENCES `collection_items`(`item_key`)
);