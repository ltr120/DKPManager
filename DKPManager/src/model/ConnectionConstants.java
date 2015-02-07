package model;

public class ConnectionConstants {

    // Manager
    // MySQL
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://db4free.net:3306/mlgbwowguild";
    public static final String USER = "mlgbofficer";
    public static final String PASSWD = "blackhand";

    // Prepared statement strings
    public static final String INSERT_CHARACTER = "INSERT INTO `wowcharacter` (`name`, `realm`) VALUES (?, ?)";
    public static final String INSERT_DKPEVENT = "INSERT INTO `dkpevent` (`charId`, `lastId`, `itemId`, `type`, "
            + "`bossName`, `bossRaid`, `score`, `description`, `difficulty`) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_ITEM = "INSERT INTO `item` (`name`, `inGameId`) VALUES (?, ?)";

}
