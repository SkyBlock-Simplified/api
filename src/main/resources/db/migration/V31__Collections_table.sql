CREATE TABLE `collections` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `collection_key` VARCHAR(127) NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`)
);