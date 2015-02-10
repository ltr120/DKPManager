/* 
 * This is a mysql script to create necessary tables.
 */
 
-- Create WOWCharacter table
CREATE TABLE IF NOT EXISTS `wowcharacter` (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100),
        realm VARCHAR(100),
        class VARCHAR(100),
    );

-- Create Item table
CREATE TABLE IF NOT EXISTS `item` (
        id INT AUTO_INCREMENT PRIMARY KEY,
        inGameId INT,
        name VARCHAR(100)
    );

-- Create DKPEvent table
CREATE TABLE IF NOT EXISTS `dkpevent` (
        id INT AUTO_INCREMENT PRIMARY KEY,
        lastId INT,
        charId INT,
        itemId INT,
        type VARCHAR(100),
        bossName VARCHAR(100),
        bossRaid VARCHAR(100),
        score INT,
        description VARCHAR(1024),
        difficulty VARCHAR(100)
    );