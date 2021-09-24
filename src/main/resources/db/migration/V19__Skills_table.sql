CREATE TABLE `skills` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `has_collection` TINYINT(1) NOT NULL,
    `cosmetic` TINYINT(1) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);