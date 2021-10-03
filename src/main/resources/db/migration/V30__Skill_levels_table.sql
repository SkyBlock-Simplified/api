CREATE TABLE `skill_levels` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `skill` BIGINT NOT NULL,
    `skill_level` INT NOT NULL,
    `total_exp_required` DOUBLE NOT NULL,
    `unlocks` JSON NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`skill`) REFERENCES `skills`(`id`)
);