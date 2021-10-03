CREATE TABLE `skills` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `skill_key` VARCHAR(127) NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `description` VARCHAR(127) NOT NULL,
    `max_level` INT NOT NULL,
    PRIMARY KEY (`id`)
);