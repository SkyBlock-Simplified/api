CREATE TABLE `craftingtable_slots` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `slot` TINYINT(1) NOT NULL,
    `quick_craft` TINYINT(1) NOT NULL DEFAULT 0,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`)
);

INSERT INTO `craftingtable_slots` (`key`, `name`, `slot`, `quick_craft`) VALUES
    ('TOP_LEFT', 'Top Left', 10, 0),
    ('TOP_CENTER', 'Top Center', 11, 0),
    ('TOP_RIGHT', 'Top Right', 12, 0),
    ('MIDDLE_LEFT', 'Middle Left', 19, 0),
    ('MIDDLE_CENTER', 'Middle Center', 20, 0),
    ('MIDDLE_RIGHT', 'Middle Right', 21, 0),
    ('BOTTOM_LEFT', 'Bottom Left', 28, 0),
    ('BOTTOM_CENTER', 'Bottom Center', 29, 0),
    ('BOTTOM_RIGHT', 'Bottom Right', 30, 0),
    ('RESULT', 'Result', 23, 0, 0),
    ('QUICK_CRAFT_TOP', 'Quick Craft Top', 16, 1),
    ('QUICK_CRAFT_CENTER', 'Quick Craft Center', 25, 1),
    ('QUICK_CRAFT_BOTTOM', 'Quick Craft Bottom', 34, 1);

CREATE TABLE `craftingtable_recipes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `key` varchar(127) NOT NULL,
    `name` varchar(127) NOT NULL,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `key` (`key`)
);

INSERT INTO `craftingtable_recipes` (`key`, `name`) VALUES
    ('ALL', 'All'),
    ('RING', 'Ring'),
    ('STAR', 'Star'),
    ('SINGLE_ROW', 'Single Row'),
    ('DOUBLE_ROW', 'Double Row'),
    ('TOP_ROW', 'Top Row'),
    ('BOX', 'Box'),
    ('MIDDLE2', 'Middle 2'),
    ('CENTER2', 'Center 2'),
    ('CENTER3', 'Center 3'),
    ('TRIANGLE', 'Triangle'),
    ('BUCKET', 'Bucket'),
    ('HOPPER', 'Hopper'),
    ('DIAGONAL', 'Diagonal'),
    ('ENCHANT', 'Enchant'),
    ('ENCHANT_FISH', 'Enchant Fish'),
    ('SINGLE', 'Single');

CREATE TABLE `craftingtable_recipe_slots` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `recipe_key` varchar(127) NOT NULL,
    `slot_key` varchar(127) NOT NULL,
    `ordinal` TINYINT(1) NOT NULL DEFAULT -1,
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),

    PRIMARY KEY(`id`),
    UNIQUE KEY `recipe_slot` (`recipe_key`, `slot_key`),
    UNIQUE KEY `recipe_ordinal` (`recipe_key`, `ordinal`),
    CONSTRAINT `craftingtable_recipe_slots_ibfk_1` FOREIGN KEY (`recipe_key`) REFERENCES `craftingtable_recipes` (`key`),
    CONSTRAINT `craftingtable_recipe_slots_ibfk_2` FOREIGN KEY (`slot_key`) REFERENCES `craftingtable_slots` (`key`)
);

INSERT INTO `craftingtable_recipe_slots` (`recipe_key`, `slot_key`, `ordinal`) VALUES
    ('ALL', 'TOP_LEFT', 0),
    ('ALL', 'TOP_CENTER', 1),
    ('ALL', 'TOP_RIGHT', 2),
    ('ALL', 'MIDDLE_LEFT', 3),
    ('ALL', 'MIDDLE_CENTER', 4),
    ('ALL', 'MIDDLE_RIGHT', 5),
    ('ALL', 'BOTTOM_LEFT', 6),
    ('ALL', 'BOTTOM_CENTER', 7),
    ('ALL', 'BOTTOM_RIGHT', 8),
    ('RING', 'TOP_LEFT', 0),
    ('RING', 'TOP_CENTER', 1),
    ('RING', 'TOP_RIGHT', 2),
    ('RING', 'MIDDLE_LEFT', 3),
    ('RING', 'MIDDLE_RIGHT', 4),
    ('RING', 'BOTTOM_LEFT', 5),
    ('RING', 'BOTTOM_CENTER', 6),
    ('RING', 'BOTTOM_RIGHT', 7),
    ('STAR', 'TOP_CENTER', 0),
    ('STAR', 'MIDDLE_LEFT', 1),
    ('STAR', 'MIDDLE_RIGHT', 2),
    ('STAR', 'BOTTOM_CENTER', 3),
    ('STAR', 'MIDDLE_CENTER', 4),
    ('SINGLE_ROW', 'TOP_LEFT', 0),
    ('SINGLE_ROW', 'TOP_CENTER', 1),
    ('SINGLE_ROW', 'TOP_RIGHT', 2),
    ('DOUBLE_ROW', 'TOP_LEFT', 0),
    ('DOUBLE_ROW', 'TOP_CENTER', 1),
    ('DOUBLE_ROW', 'TOP_RIGHT', 2),
    ('DOUBLE_ROW', 'MIDDLE_LEFT', 3),
    ('DOUBLE_ROW', 'MIDDLE_CENTER', 4),
    ('DOUBLE_ROW', 'MIDDLE_RIGHT', 5),
    ('TOP_ROW', 'TOP_LEFT', 0),
    ('TOP_ROW', 'TOP_CENTER', 1),
    ('TOP_ROW', 'TOP_RIGHT', 2),
    ('BOX', 'TOP_LEFT', 0),
    ('BOX', 'TOP_CENTER', 1),
    ('BOX', 'MIDDLE_LEFT', 2),
    ('BOX', 'MIDDLE_CENTER', 3),
    ('MIDDLE2', 'MIDDLE_LEFT', 0),
    ('MIDDLE2', 'MIDDLE_CENTER', 1),
    ('CENTER2', 'MIDDLE_CENTER', 0),
    ('CENTER2', 'BOTTOM_CENTER', 0),
    ('CENTER3', 'TOP_CENTER', 0),
    ('CENTER3', 'MIDDLE_CENTER', 1),
    ('CENTER3', 'BOTTOM_CENTER', 2),
    ('TRIANGLE', 'MIDDLE_LEFT', 0),
    ('TRIANGLE', 'MIDDLE_CENTER', 1),
    ('TRIANGLE', 'BOTTOM_LEFT', 2),
    ('BUCKET', 'MIDDLE_LEFT', 0),
    ('BUCKET', 'BOTTOM_CENTER', 1),
    ('BUCKET', 'MIDDLE_RIGHT', 2),
    ('HOPPER', 'TOP_LEFT', 0),
    ('HOPPER', 'MIDDLE_LEFT', 1),
    ('HOPPER', 'BOTTOM_CENTER', 2),
    ('HOPPER', 'MIDDLE_RIGHT', 3),
    ('HOPPER', 'TOP_RIGHT', 4),
    ('HOPPER', 'MIDDLE_CENTER', 5),
    ('DAIGONAL', 'BOTTOM_LEFT', 0),
    ('DAIGONAL', 'MIDDLE_CENTER', 1),
    ('DAIGONAL', 'TOP_RIGHT', 2),
    ('ENCHANT', 'MIDDLE_CENTER', 0),
    ('ENCHANT', 'MIDDLE_RIGHT', 1),
    ('ENCHANT', 'BOTTOM_CENTER', 2),
    ('ENCHANT', 'BOTTOM_RIGHT', 3),
    ('ENCHANT_FISH', 'TOP_LEFT', 0),
    ('ENCHANT_FISH', 'MIDDLE_CENTER', 1),
    ('ENCHANT_FISH', 'MIDDLE_RIGHT', 2),
    ('ENCHANT_FISH', 'BOTTOM_CENTER', 3),
    ('ENCHANT_FISH', 'BOTTOM_RIGHT', 4),
    ('SINGLE', 'TOP_LEFT', 0);