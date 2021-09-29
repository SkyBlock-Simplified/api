ALTER TABLE `enchantments` DROP FOREIGN KEY `enchantments_ibfk_1`;

ALTER TABLE `enchantments` RENAME COLUMN `tool_type` TO `item_type`;

ALTER TABLE `enchantments` ADD FOREIGN KEY (`item_type`) REFERENCES `item_types`(`id`);

DROP TABLE `tool_types`;