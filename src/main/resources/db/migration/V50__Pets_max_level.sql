ALTER TABLE `pets` ADD COLUMN `max_level` SMALLINT DEFAULT 100 CHECK (`max_level` > 0) AFTER `skin`;