ALTER TABLE `rarities`
    ADD COLUMN `has_hypixel_name` TINYINT(1),
    ADD COLUMN `hypixel_name` VARCHAR(127);
