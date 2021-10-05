ALTER TABLE `items`
    ADD COLUMN `rarity` BIGINT AFTER `tier`;

ALTER TABLE `items`
    ADD FOREIGN KEY (`rarity`) REFERENCES `rarities`(`id`);

UPDATE `items`
    SET
        `rarity` = (CASE
            WHEN `tier` IN ('UNCOMMON') THEN 2
            WHEN `tier` IN ('RARE') THEN 3
            WHEN `tier` IN ('EPIC') THEN 4
            WHEN `tier` IN ('LEGENDARY') THEN 5
            WHEN `tier` IN ('SPECIAL') THEN 8
        END),
        `updated_at` = CURRENT_TIMESTAMP()
    WHERE `tier` IN ('UNCOMMON', 'RARE', 'EPIC', 'LEGENDARY', 'SPECIAL')
;

ALTER TABLE `items`
    DROP COLUMN `tier`;
