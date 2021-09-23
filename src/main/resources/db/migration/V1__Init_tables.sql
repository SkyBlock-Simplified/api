CREATE TABLE `rarities` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);

CREATE TABLE `item_types` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);

CREATE TABLE `tool_types` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);

CREATE TABLE `reforges` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `item_type` BIGINT NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `rarity` BIGINT NOT NULL,
    `is_stone` TINYINT(1) NOT NULL,
    `effects` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`item_type`) REFERENCES `item_types`(`id`),
    FOREIGN KEY (`rarity`) REFERENCES `rarities`(`id`)
);

CREATE TABLE `enchantments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `tool_type` BIGINT NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `item_level` INT NOT NULL,
    `effects` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`tool_type`) REFERENCES `tool_types`(`id`)
);

CREATE TABLE `accessory_families` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `reforges_stack` TINYINT(1) DEFAULT 0,
    `items_stack` TINYINT(1) DEFAULT 0,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);

CREATE TABLE `accessories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `family` BIGINT NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `rarity` BIGINT NOT NULL,
    `effects` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`family`) REFERENCES `accessory_families`(`id`),
    FOREIGN KEY (`rarity`) REFERENCES `rarities`(`id`)
);

CREATE TABLE `potions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `item_level` INT NOT NULL,
    `effects` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);

CREATE TABLE `pets` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `rarity` BIGINT NOT NULL,
    `effects_base` JSON NOT NULL,
    `effects_per_level` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`rarity`) REFERENCES `rarities`(`id`)
);
