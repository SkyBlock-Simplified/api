ALTER TABLE `items`
    ADD COLUMN `private_island` VARCHAR(127) AFTER `essence`,
    ADD COLUMN `crystal` VARCHAR(127) AFTER `essence`,
    ADD COLUMN `enchantments` JSON AFTER `essence`,
    ADD COLUMN `ability_damage_scaling` DOUBLE AFTER `essence`,
    ADD COLUMN `description` VARCHAR(1023) AFTER `essence`;