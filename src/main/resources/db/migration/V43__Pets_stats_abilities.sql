CREATE TABLE `pet_stats` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `pet` BIGINT NOT NULL,
    `stat` BIGINT NOT NULL,
    `rarities` JSON,
    `base_value` INT NOT NULL DEFAULT 0,
    `expression` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`pet`) REFERENCES `pets`(`id`),
    FOREIGN KEY (`stat`) REFERENCES `stats`(`id`)
);

CREATE TABLE `pet_abilities` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `pet` BIGINT NOT NULL,
    `description` LONGTEXT NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`pet`) REFERENCES `pets`(`id`)
);

CREATE TABLE `pet_ability_stats` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `pet` BIGINT NOT NULL,
    `ability` BIGINT NOT NULL,
    `description` LONGTEXT NOT NULL,
    `priority` TINYINT(1) NOT NULL,
    `rarities` JSON,
    `base_value` INT NOT NULL DEFAULT 0,
    `expression` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`pet`) REFERENCES `pets`(`id`),
    FOREIGN KEY (`ability`) REFERENCES `pet_abilities`(`id`)
);

CREATE TABLE `pet_item_stats` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `item_id` BIGINT NOT NULL,
    `stat` BIGINT NOT NULL,
    `stat_value` INT NOT NULL,
    `percentage` TINYINT NOT NULL DEFAULT 0 CHECK (`percentage` BETWEEN 0 AND 1),
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `items`(`id`),
    FOREIGN KEY (`stat`) REFERENCES `stats`(`id`)
);