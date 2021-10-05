UPDATE `rarities`
    SET
        `has_hypixel_name` = (CASE
            WHEN `id` IN (2, 3, 4, 5, 6, 8) THEN 1
            WHEN `id` IN (1, 7, 9) THEN 0
        END),
        `hypixel_name` = (CASE
            WHEN `id` = 1 THEN NULL
            WHEN `id` = 2 THEN 'UNCOMMON'
            WHEN `id` = 3 THEN 'RARE'
            WHEN `id` = 4 THEN 'EPIC'
            WHEN `id` = 5 THEN 'LEGENDARY'
            WHEN `id` = 6 THEN 'MYTHIC'
            WHEN `id` = 7 THEN NULL
            WHEN `id` = 8 THEN 'SPECIAL'
            WHEN `id` = 9 THEN NULL
        END),
        `updated_at` = CURRENT_TIMESTAMP()
    WHERE `id` IN (1, 2, 3, 4, 5, 6, 7, 8, 9)
;