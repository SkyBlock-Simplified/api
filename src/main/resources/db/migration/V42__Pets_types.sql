CREATE TABLE `pet_types` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `type_key` VARCHAR(127) NOT NULL,
    `name` VARCHAR(127) NOT NULL,

    PRIMARY KEY (`id`)
);

INSERT INTO `pet_types` (`type_key`, `name`) VALUES
    ('PET', 'Pet'),
    ('MOUNT', 'Mount'),
    ('MORPH', 'Morph');

ALTER TABLE `pets`
    ADD COLUMN `type` BIGINT NOT NULL AFTER `rarity`,
    ADD COLUMN `skill` BIGINT NOT NULL AFTER `rarity`,
    ADD COLUMN `skin` LONGTEXT NOT NULL AFTER `effects_per_level`,
    ADD COLUMN `item_id` VARCHAR(127) NOT NULL AFTER `id`;

ALTER TABLE `pets`
    ADD FOREIGN KEY (`skill`) REFERENCES `skills`(`id`),
    ADD FOREIGN KEY (`type`) REFERENCES `pet_types`(`id`);

ALTER TABLE `rarities`
    ADD COLUMN `priority` TINYINT(1) AFTER `NAME`;