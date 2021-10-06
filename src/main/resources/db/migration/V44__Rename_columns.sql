ALTER TABLE `pets`
    RENAME COLUMN `rarity` TO `lowest_rarity`,
    DROP COLUMN `effects_base`,
    DROP COLUMN `effects_per_level`;

ALTER TABLE `stats`
    RENAME COLUMN `priority` TO `ordinal`;

ALTER TABLE `rarities`
    RENAME COLUMN `priority` TO `ordinal`;

ALTER TABLE `pet_stats`
    DROP COLUMN `base_value`;

ALTER TABLE `pet_ability_stats`
    DROP COLUMN `base_value`;

ALTER TABLE `pet_stats`
    ADD COLUMN `base_value` DECIMAL DEFAULT 0.0 NOT NULL AFTER `rarities`;

ALTER TABLE `pet_ability_stats`
    ADD COLUMN `base_value` DECIMAL DEFAULT 0.0 NOT NULL AFTER `rarities`;

ALTER TABLE `sublocations`
    RENAME TO `location_areas`;

ALTER TABLE `skills`
    ADD COLUMN `item_id` VARCHAR(127) AFTER `max_level`;