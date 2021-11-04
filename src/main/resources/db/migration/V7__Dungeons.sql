DROP TABLE IF EXISTS `dungeons`;
CREATE TABLE `dungeons` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`key` VARCHAR(127) NOT NULL,
	`name` VARCHAR(127) NOT NULL,
	`weight_multiplier` DOUBLE(15,13) NOT NULL DEFAULT 0,
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),

	PRIMARY KEY (`id`),
	UNIQUE KEY `key` (`key`)
);

INSERT INTO `dungeons` (`key`, `name`, `weight_multiplier`) VALUES
    ('CATACOMBS', 'Catacombs', 0.0002149604615);

DROP TABLE IF EXISTS `dungeon_classes`;
CREATE TABLE `dungeon_classes` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`key` VARCHAR(127) NOT NULL,
	`name` VARCHAR(127) NOT NULL,
	`weight_multiplier` DOUBLE(15,13) NOT NULL DEFAULT 0,
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),

	PRIMARY KEY (`id`),
	UNIQUE KEY `key` (`key`)
);

INSERT INTO `dungeon_classes` (`key`, `name`, `weight_multiplier`) VALUES
    ('HEALER', 'Healer', 0.0000045254834),
    ('MAGE', 'Mage', 0.0000045254834),
    ('BERSERK', 'Berserk', 0.0000045254834),
    ('ARCHER', 'Archer', 0.0000045254834),
    ('TANK', 'Tank', 0.0000045254834);

DROP TABLE IF EXISTS `dungeon_floor_sizes`;
CREATE TABLE `dungeon_floor_sizes` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`key` VARCHAR(127) NOT NULL,
	`name` VARCHAR(127) NOT NULL,
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),

	PRIMARY KEY (`id`),
	UNIQUE KEY `key` (`key`)
);

INSERT INTO `dungeon_floor_sizes` (`key`, `name`) VALUES
    ('TINY', 'Tiny'),
    ('SMALL', 'Small'),
    ('MEDIUM', 'Medium'),
    ('LARGE', 'Large');

DROP TABLE IF EXISTS `dungeon_bosses`;
CREATE TABLE `dungeon_bosses` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`key` VARCHAR(127) NOT NULL,
	`name` VARCHAR(127) NOT NULL,
	`description` LONGTEXT NOT NULL DEFAULT '' COLLATE 'utf8mb4_bin',
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),

	PRIMARY KEY (`id`),
	UNIQUE KEY `key` (`key`)
);

INSERT INTO `dungeon_bosses` (`key`, `name`, `description`) VALUES
    ('WATCHER', 'Watcher', 'This strange creature is roaming the Catacombs to add powerful adventurers to its collection.'),
    ('BONZO', 'Bonzo', 'Involved in his dark arts due to his parents\' insistence. Originally worked as a Circus clown.'),
    ('SCARF', 'Scarf', 'First of his class. His teacher said he will do "great things".'),
    ('THE_PROFESSOR', 'The Professor', 'Despite his great technique, he failed the Masters exam three times. Works from 8 to 5. Cares about his students.'),
    ('THORN', 'Thorn', 'Powerful Necromancer that specializes in animals. Calls himself a vegetarian, go figure.'),
    ('LIVID', 'Livid', 'Strongly believes he will become the Lord one day. Subject of the mockeries, even from his disciples.'),
    ('SADAN', 'Sadan', 'Necromancy was always strong in his family. Says he once beat a Wither in a duel. Likes to brag.'),
    ('NECRON', 'Necron', 'Right hand of the Wither King. Inherited the Catacombs eons ago. never defeated, feared by anything living AND dead.');

DROP TABLE IF EXISTS `dungeon_floors`;
CREATE TABLE `dungeon_floors` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`dungeon_key` VARCHAR(127) NOT NULL,
	`floor` TINYINT(1) NOT NULL,
	`floor_size_key` VARCHAR(127) NOT NULL,
	`floor_boss_key` VARCHAR(127) NOT NULL,
	`updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),

	PRIMARY KEY (`id`),
	UNIQUE KEY `dungeon_floor` (`dungeon_key`, `floor`),
	CONSTRAINT `dungeon_floors_ibfk_1` FOREIGN KEY (`dungeon_key`) REFERENCES `dungeons` (`key`),
	CONSTRAINT `dungeon_floors_ibfk_2` FOREIGN KEY (`floor_size_key`) REFERENCES `dungeon_floor_sizes` (`key`),
	CONSTRAINT `dungeon_floors_ibfk_3` FOREIGN KEY (`floor_boss_key`) REFERENCES `dungeon_bosses` (`key`)
);

INSERT INTO `dungeon_floors` (`dungeon_key`, `floor`, `floor_size_key`, `floor_boss_key`) VALUES
    ('CATACOMBS', 0, 'TINY', 'WATCHER'),
    ('CATACOMBS', 1, 'TINY', 'BONZO'),
    ('CATACOMBS', 2, 'SMALL', 'SCARF'),
    ('CATACOMBS', 3, 'SMALL', 'THE_PROFESSOR'),
    ('CATACOMBS', 4, 'SMALL', 'THORN'),
    ('CATACOMBS', 5, 'MEDIUM', 'LIVID'),
    ('CATACOMBS', 6, 'MEDIUM', 'SADAN'),
    ('CATACOMBS', 7, 'LARGE', 'NECRON');