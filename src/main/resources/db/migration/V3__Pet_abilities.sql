INSERT INTO `pet_abilities` (`key`, `name`, `pet_key`, `priority`, `description`) VALUES
    ('HIVE', 'Hive', 'BEE', 1, 'Gain +%{VALUE:INTELLIGENCE,0} %{STAT:INTELLIGENCE} and +%{VALUE:STRENGTH,0} %(STAT:STRENGTH} for each nearby bee.\n%{FORMAT:DARK_GRAY}(Max 15 bees)'),
    ('BUSY_BUZZ_BUZZ', 'Busy Buzz Buzz', 'BEE', 2, 'Has %{FORMAT:GREEN}%{VALUE:PERCENTAGE} chance for flowers to drop an extra one.'),
    ('WEAPONIZED_HONEY', 'Weaponized Honey', 'BEE', 3, 'Gain %{VALUE:PERCENTAGE,1} of received damage as %{EFFECT:ABSORPTION}.'),

    ('LIGHT_FEET', 'Light Feet', 'CHICKEN', 1, 'Reduces fall damage by %{VALUE:PERCENTAGE,1}.'),
    ('EGGSTRA', 'Eggstra', 'CHICKEN', 2, 'Killing chickens has a %{VALUE:PERCENTAGE,1} chance to drop an egg.'),
    ('MIGHTY_CHICKENS', 'Mighty Chickens', 'CHICKEN', 3, 'Chicken Minions work %{VALUE:PERCENTAGE,1} faster while on your island.'),

    ('STOMP', 'Stomp', 'ELEPHANT', 1, 'Gain %{VALUE,1} %{STAT:DEFENSE} for every 100 %{STAT:SPEED}.'),
    ('WALKING_FORTRESS', 'Walking Fortress', 'ELEPHANT', 2, 'Gain %{VALUE:HEALTH,2} %{STAT:HEALTH} for every %{VALUE:DEFENSE,1} %{STAT_DEFENSE}.'),
    ('TRUNK_EFFICIENCY', 'Trunk Efficiency', 'ELEPHANT', 3, 'Grants +%{VALUE,1} %{STAT:FARMING_FORTUNE}, which increases your chance for multiple drops.'),

    ('RIDABLE_PIG', 'Ridable', 'PIG', 1, 'Right-click your summoned pet to ride it!'),
    ('RUN', 'Run', 'PIG', 2, 'Increases the speed of your mount by %{VALUE:PERCENTAGE,1}.'),
    ('SPRINT', 'Sprint', 'PIG', 3, 'While holding an %{FORMAT:GOLD}Enchanted Carrot on a Stick%{FORMAT:GRAY}, increase the speed of your mount by %{VALUE:PERCENTAGE,1}.'),
    ('TRAMPLE', 'Trample', 'PIG', 4, 'While on your private island, break all the crops your pig rides over.'),

    ('HAPPY_FEET', 'Happy Feet', 'RABBIT', 1, 'Jump potions also give +%{VALUE,1} %{STAT:SPEED}.'),
    ('FARMING_EXP_BOOST', 'Farming Exp Boost', 'RABBIT', 2, 'Boosts your Farming exp by %{VALUE:PERCENTAGE,1}.'),
    ('EFFICIENT_FARMING', 'Efficient Farming', 'RABBIT', 3, 'Farming minions work %{VALUE:PERCENTAGE,1} faster while on your island.'),

    ('RIDABLE_ARMADILLO', '', 'ARMADILLO', 1, 'Right-click your summoned pet to ride it!'),
    ('TUNNELER', 'Tunneller', 'ARMADILLO', 2, 'The Armadillo breaks all stone or ore in its path while you are riding it in the Crystal Hollows.'),
    ('EARTH_SURFER', 'Earth Surfer', 'ARMADILLO', 3, 'The Armadillo moves faster based on your Speed.'),
    ('ROLLING_MINER', 'Rolling Miner', 'ARMADILLO', 4, 'Every %{VALUE,1} seconds, the next gemstone you mine gives 2x drops.'),
    ('MOBILE_TANK', 'Mobile Tank', 'ARMADILLO', 5, 'For every %{VALUE,1} %{STAT:DEFENSE} gain +1 %{STAT:SPEED} and +1 %{STAT:MINING_SPEED}.'),

    ('CANDY_LOVER', 'Candy Lover', 'BAT', 1, 'Increases drop chance of candies from mobs by %{VALUE:PERCENTAGE,1}.'),
    ('NIGHTMARE', 'Nightmare', 'BAT', 2, 'During night, gain %{VALUE:INTELLIGENCE,1} %{STAT:INTELLIGENCE}, %{VALUE:SPEED,1} %{STAT:SPEED}, and night vision.'),
    ('WINGS_OF_STEEL', 'Wings of Steel', 'BAT', 3, 'Deals +%{VALUE:PERCENTAGE,1} damage to Spooky enemies during the Spooky Festival.'),
    ('SONAR', 'Sonar', 'BAT', 4, '+%{VALUE:PERCENTAGE,2} chance to fish up spooky sea creatures.'),

    ('MORE_STONKS', 'More Stonks', 'ENDERMITE', 1, 'Gain more exp orbs for breaking end stone and gain a +%{VALUE:PERCENTAGE,1} chance to get an extra block dropped.'),
    ('PEARL_MUNCHER', 'Pearl Muncher', 'ENDERMITE', 2, 'Upon picking up an ender pearl, consume it and gain %{VALUE,2} %{FORMAT:GOLD}coins%{FORMAT:GRAY}.'),
    ('PEARL_POWERED', 'Pearl Powered', 'ENDERMITE', 3, 'Upon consuming an ender pearl, gain +%{VALUE,1} %{STAT:SPEED} for 10 seconds.'),

    ('RIDABLE_ROCK', 'Ridable', 'ROCK', 1, 'Right-click on your summoned pet to ride it!'),
    ('SAILING_STONE', 'Sailing Stone', 'ROCK', 2, 'Sneak to move your rock to your location (15s cooldown)'),
    ('FORTIFY', 'Fortify', 'ROCK', 3, 'While sitting on your rock, gain +%{VALUE:PERCENTAGE,1} %{STAT:DEFENSE}.'),
    ('STEADY_GROUND', 'Steady Ground', 'ROCK', 4, 'While sitting on your rock, gain +%{VALUE:PERCENTAGE,1} %{STAT:DAMAGE}.'),

    ('GROUNDED', 'Grounded', 'SCATHA', 1, 'Gain +%{VALUE} %{STAT:MINING_FORTUNE}.'),
    ('BURROWING', 'Burrowing', 'SCATHA', 2, 'When mining, there is a %{VALUE:PERCENTAGE,3} chance to mine up a treasure burrow.'),
    ('WORMHOLE', 'Wormhole', 'SCATHA', 3, 'Gives a %{VALUE:PERCENTAGE} chance to mine 2 adjacent stone or hard stone.'),

    ('TRUE_DEFENSE_BOOST', 'True Defense Boost', 'SILVERFISH', 1, 'Boosts your %{STAT:TRUE_DEFENSE} by %{VALUE,2}.'),
    ('MINING_EXP_BOOST', 'Mining Exp Boost', 'SILVERFISH', 2, 'Boosts your Mining exp by %{VALUE:PERCENTAGE,2}.'),
    ('DEXTERITY', 'DEXTERITY', 'SILVERFISH', 3, 'Gives permanent haste III.'),

    ('STRONGER_BONES', 'Stronger Bones', 'WITHER_SKELETON', 1, 'Take %{VALUE:PERCENTAGE,1} less damage from Skeletons.'),
    ('WITHER_BLOOD', 'Wither Blood', 'WITHER_SKELETON', 2, 'Deal %{VALUE:PERCENTAGE,1} more damage against wither mobs.'),
    ('DEATHS_TOUCH', 'Death\'s Touch', 'WITHER_SKELETON', 3, 'Upon hitting an enemy inflict the wither effect for %{VALUE} damage over 3 seconds.\n%{FORMAT:DARK_GRAY}Does not stack'),

    ('MITHRIL_AFFINITY', 'Mithril Affinity', 'MITHRIL_GOLEM', 1, 'Gain %{VALUE,1} %{STAT:MINING_SPEED} when mining Mithril.'),
    ('THE_SMELL_OF_POWDER', 'The Smell Of Powder', 'MITHRIL_GOLEM', 2, 'Gain %{VALUE:PERCENTAGE,1} chance to gain extra %{FORMAT:DARK_GREEN}Mithril Powder %{FORMAT:GRAY}while mining.'),
    ('DANGER_AVERSE', 'Danger Averse', 'MITHRIL_GOLEM', 3, 'Increases your combat stats by %{VALUE:PERCENTAGE,1} on mining islands.'),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),

    ('', '', '', 1, ''),
    ('', '', '', 2, ''),
    ('', '', '', 3, ''),
    ();