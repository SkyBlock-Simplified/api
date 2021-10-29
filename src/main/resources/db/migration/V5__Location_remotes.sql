CREATE TABLE `location_remotes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `mode` varchar(127) NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`),
    UNIQUE KEY `mode` (`mode`)
);

INSERT INTO `location_remotes` (`key`, `mode`, `name`) VALUES
    ('LOBBY', 'Network Lobby', 'LOBBY'),
    ('PRIVATE_ISLAND', 'Private Island', 'dynamic'),
    ('HUB', 'Hub', 'hub'),
    ('DARK_AUCTION', 'Dark Auction', 'dark_auction'),
    ('DUNGEON', 'Dungeons', 'dungeon'),
    ('GOLD_MINE', 'Gold Mine', 'mining_1'),
    ('DEEP_CAVERNS', 'Deep Caverns', 'mining_2'),
    ('DWARVEN_MINES', 'Dwarven Mines', 'mining_3'),
    ('CRYSTAL_HOLLOWS', 'Crystal Hollows', 'crystal_hollows'),
    ('SPIDERS_DEN', 'Spiders Den', 'combat_1'),
    ('BLAZING_FORTRESS', 'Blazing Fortress', 'combat_2'),
    ('THE_END', 'The End', 'combat_3'),
    ('FARMING_ISLANDS', 'The Farming Islands', 'farming_1'),
    ('THE_PARK', 'The Park', 'foraging_1'),
    ('JERRYS_WORKSHOP', 'Jerry\'s Workshop', 'winter');