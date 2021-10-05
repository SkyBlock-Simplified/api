CREATE TABLE IF NOT EXISTS `formats` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `format_key` VARCHAR(127) NOT NULL,
    `code` CHAR(1) NOT NULL,
    `rgb` INT,
    `format` TINYINT(1) NOT NULL DEFAULT 0 CHECK(`format` BETWEEN 0 AND 1),
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),

    PRIMARY KEY (`id`)
);

INSERT INTO `formats` (`format_key`, `code`, `rgb`, `format`) VALUES
    ('BLACK', '0', 0x000000, 0),
    ('DARK_BLUE', '1', 0x0000AA, 0),
    ('DARK_GREEN', '2', 0x00AA00, 0),
    ('DARK_AQUA', '3', 0x00AAAA, 0),
    ('DARK_RED', '4', 0xAA0000, 0),
    ('DARK_PURPLE', '5', 0xAA00AA, 0),
    ('GOLD', '6', 0xFFAA00, 0),
    ('GRAY', '7', 0xAAAAAA, 0),
    ('DARK_GRAY', '8', 0x555555, 0),
    ('BLUE', '9', 0x5555FF, 0),
    ('GREEN', 'a', 0x55FF55, 0),
    ('AQUA', 'b', 0x55FFFF, 0),
    ('RED', 'c', 0xFF5555, 0),
    ('LIGHT_PURPLE', 'd', 0xFF55FF, 0),
    ('YELLOW', 'e', 0xFFFF55, 0),
    ('WHITE', 'f', 0xFFFFFF, 0),
    ('OBFUSCATED', 'k', NULL, 1),
    ('BOLD', 'l', NULL, 1),
    ('STRIKETHROUGH', 'm', NULL, 1),
    ('UNDERLINED', 'n', NULL, 1),
    ('ITALIC', 'o', NULL, 1),
    ('RESET', 'r', NULL, 0);