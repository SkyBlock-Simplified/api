CREATE TABLE `collection_item_tiers` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `collection_item` BIGINT NOT NULL,
    `tier` INT NOT NULL,
    `amount_required` INT NOT NULL,
    `unlocks` JSON NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`collection_item`) REFERENCES `collection_items`(`id`)
);