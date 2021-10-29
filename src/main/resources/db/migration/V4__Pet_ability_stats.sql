INSERT INTO `pet_ability_stats` (`ability_key`, `rarities`, `stat_key`, `base_value`, `expression`) VALUES
    -- Bee
    ('HIVE', '[1]', 'INTELLIGENCE', 1.0, 'level * 0.02'),
    ('HIVE', '[2]', 'INTELLIGENCE', 1.0, 'level * 0.04'),
    ('HIVE', '[3]', 'INTELLIGENCE', 1.0, 'level * 0.09'),
    ('HIVE', '[4]', 'INTELLIGENCE', 1.0, 'level * 0.14'),
    ('HIVE', '[5]', 'INTELLIGENCE', 1.0, 'level * 0.19'),
    ('HIVE', '[1]', 'STRENGTH', 1.0, 'level * 0.02'),
    ('HIVE', '[2]', 'STRENGTH', 1.0, 'level * 0.04'),
    ('HIVE', '[3]', 'STRENGTH', 1.0, 'level * 0.07'),
    ('HIVE', '[4]', 'STRENGTH', 1.0, 'level * 0.11'),
    ('HIVE', '[5]', 'STRENGTH', 1.0, 'level * 0.14'),
    ('BUSY_BUZZ_BUZZ', '[3]', NULL, 0.0, 'level * 0.5'),
    ('BUSY_BUZZ_BUZZ', '[4, 5]', NULL, 0.0, 'level * 1.0'),
    ('WEAPONIZED_HONEY', '[5]', NULL, 5.0, 'level * 0.2'),
    -- Chicken
    ('LIGHT_FEET', '[1]', NULL, 0.0, 'level * 0.3'),
    ('LIGHT_FEET', '[2, 3]', NULL, 0.0, 'level * 0.4'),
    ('LIGHT_FEET', '[4, 5]', NULL, 0.0, 'level * 0.5'),
    ('EGGSTRA', '[3]', NULL, 0.0, 'level * 0.8'),
    ('EGGSTRA', '[4, 5]', NULL, 0.0, 'level * 1.0'),
    ('MIGHTY_CHICKENS', '[5]', NULL, 0.3, 'level * 0.3'),
    -- Elephant
    ('STOMP', '[1, 2, 3]', NULL, 0.0, 'level * 0.15'),
    ('STOMP', '[4, 5]', NULL, 0.0, 'level * 0.2'),
    ('WALKING_FORTRESS', '[3, 4, 5]', NULL, 0.0, 'level * 0.01'),
    ('TRUNK_EFFICIENCY', '[5]', NULL, 0.0, 'level * 1.8'),
    -- Pig
    ('RIDABLE_PIG', '[1, 2, 3, 4, 5]', NULL, 0.0, ''),
    ('RUN', '[1, 2]', NULL, 0.0, 'level * 0.3'),
    ('RUN', '[3, 4]', NULL, 0.0, 'level * 0.4'),
    ('RUN', '[5]', NULL, 0.0, 'level * 0.5'),
    ('SPRINT', '[3]', NULL, 0.0, 'level * 0.4'),
    ('SPRINT', '[4, 5]', NULL, 0.0, 'level * 0.5'),
    ('TRAMPLE', '[5]', NULL, 0.0, ''),
    -- Rabbit
    ('HAPPY_FEET', '[1, 2]', NULL, 0.0, 'level * 0.3'),
    ('HAPPY_FEET', '[3, 4]', NULL, 0.0, 'level * 0.4'),
    ('HAPPY_FEET', '[5]', NULL, 0.0, 'level * 0.5'),
    ('FARMING_EXP_BOOST', '[3]', NULL, 0.0, 'level * 0.25'),
    ('FARMING_EXP_BOOST', '[4, 5]', NULL, 0.0, 'level * 0.3'),
    ('EFFICIENT_FARMING', '[5]', NULL, 0.0, 'level * 0.3'),

    -- Armadillo
    ('ROLLING_MINER', '[3]', NULL, 60, 'level * -0.2'),
    ('ROLLING_MINER', '[4, 5]', NULL, 60, 'level * -0.3'),
    ('MOBILE_TANK', '[5]', NULL, 100, 'level * -0.5'),

    -- Bat
    ('CANDY_LOVER', '[1]', NULL, 0.0, 'level * 0.1'),
    ('CANDY_LOVER', '[2, 3]', NULL, 0.0, 'level * 0.15'),
    ('CANDY_LOVER', '[4, 5, 6]', NULL, 0.0, 'level * 0.2'),
    ('NIGHTMARE', '[3]', 'INTELLIGENCE', 0.0, 'level * 0.2'),
    ('NIGHTMARE', '[4, 5, 6]', 'INTELLIGENCE', 0.0, 'level * 0.3'),
    ('NIGHTMARE', '[3]', 'SPEED', 0.0, 'level * 0.4'),
    ('NIGHTMARE', '[4, 5, 6]', 'SPEED', 0.0, 'level * 0.5'),
    ('WINGS_OF_STEEL', '[5, 6]', NULL, 0.0, 'level * 0.5'),
    ('SONAR', '[6]', NULL, 0.0, 'level * 0.25'),

    -- Endermite

    ();