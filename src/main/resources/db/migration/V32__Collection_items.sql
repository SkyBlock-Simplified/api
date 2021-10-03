CREATE TABLE `collection_items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `collection` BIGINT NOT NULL,
    `item_key` VARCHAR(127) NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `max_tiers` INT NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`collection`) REFERENCES `collections`(`id`)
);