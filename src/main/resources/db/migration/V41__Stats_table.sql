CREATE TABLE `stats` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `stat_key` VARCHAR(127) NOT NULL,
    `name` VARCHAR(127) NOT NULL,
    `symbol_code` VARCHAR(4) NOT NULL,
    `format` BIGINT NOT NULL,
    `priority` TINYINT(2) NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`),
    FOREIGN KEY (`format`) REFERENCES `formats`(`id`)
);

INSERT INTO `stats` (`stat_key`, `name`, `symbol_code`, `format`, `priority`) VALUES
    ('DAMAGE', 'Damage', '2741', 13, 0),
    ('HEALTH', 'Health', '2764', 13, 1),
    ('DEFENSE', 'Defense', '2748', 11, 2),
    ('STRENGTH', 'Strength', '2741', 13, 3),
    ('SPEED', 'Speed', '2726', 16, 4),
    ('CRIT_CHANCE', 'Crit Chance', '2623', 10, 5),
    ('CRIT_DAMAGE', 'Crit Damage', '2620', 10, 6),
    ('INTELLIGENCE', 'Intelligence', '270e', 12, 7),
    ('MINING_SPEED', 'Mining Speed', '2e15', 7, 8),
    ('BONUS_ATTACK_SPEED', 'Bonus Attack Speed', '2694', 15, 9),
    ('SEA_CREATURE_CHANCE', 'Sea Creature Chance', '03b1', 4, 10),
    ('MAGIC_FIND', 'Magic Find', '272f', 12, 11),
    ('PET_LUCK', 'Pet Luck', '2663', 14, 12),
    ('TRUE_DEFENSE', 'True Defense', '2742', 16, 13),
    ('FEROCITY', 'Ferocity', '2afd', 14, 14),
    ('ABILITY_DAMAGE', 'Ability Damage', '0e51', 13, 15),
    ('MINING_FORTUNE', 'Mining Fortune', '2618', 7, 16),
    ('FARMING_FORTUNE', 'Farming Fortune', '2618', 7, 17),
    ('FORAGING_FORTUNE', 'Foraging Fortune', '2618', 7, 18),
    ('PRISTINE', 'Pristine', '2727', 6, 19);