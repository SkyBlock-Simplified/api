CREATE TABLE `sublocations` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(127) NOT NULL,
    `location` BIGINT NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`location`) REFERENCES `locations`(`id`)
);