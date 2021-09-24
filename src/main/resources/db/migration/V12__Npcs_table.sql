CREATE TABLE `npcs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `x` DOUBLE NOT NULL,
    `y` DOUBLE NOT NULL,
    `z` DOUBLE NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `is_merchant` TINYINT(1) NOT NULL,
    `location` BIGINT NOT NULL,
    `sublocation` BIGINT,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`location`) REFERENCES `locations`(`id`),
    FOREIGN KEY (`sublocation`) REFERENCES `sublocations`(`id`)
);