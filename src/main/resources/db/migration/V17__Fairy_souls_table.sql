CREATE TABLE `fairy_souls` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `x` DOUBLE NOT NULL,
    `y` DOUBLE NOT NULL,
    `z` DOUBLE NOT NULL,
    `location` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`location`) REFERENCES `locations`(`id`)
);