package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.PreparedStatement;

import model.Boss;
import model.ConnectionConstants;
import model.Constants.DKPEventType;
import model.Constants;
import model.Constants.Difficulty;
import model.DKPEvent;
import model.Item;
import model.NotEnoughDKPException;
import model.WOWCharacter;

public class MySQLManager {

    private Connection conn = null;
    private Statement stmt = null;
    private Statement charStmt = null;
    private Statement eventStmt = null;
    private Statement itemStmt = null;
    private PreparedStatement insertCharacter = null;
    private PreparedStatement insertDKPEvent = null;
    private PreparedStatement insertItem = null;

    public MySQLManager() {

    }

    private void startConnection() throws ClassNotFoundException, SQLException {
        Class.forName(ConnectionConstants.JDBC_DRIVER);
        conn = DriverManager.getConnection(ConnectionConstants.DB_URL,
                ConnectionConstants.USER, ConnectionConstants.PASSWD);
    }

    private void closeConnection() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (insertCharacter != null) {
                insertCharacter.close();
            }
            if (insertDKPEvent != null) {
                insertDKPEvent.close();
            }
            if (insertItem != null) {
                insertItem.close();
            }
            if (charStmt != null) {
                charStmt.close();
            }
            if (eventStmt != null) {
                eventStmt.close();
            }
            if (itemStmt != null) {
                itemStmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveData(List<WOWCharacter> characters) {
        // Clear database
        clearDatabase();
        // save data
        try {
            startConnection();
            stmt = conn.createStatement();
            List<DKPEvent> eventList;
            int nextCharacterId;
            int nextDKPEventId;
            int nextItemId;
            nextCharacterId = getNextAutoIncrementId(stmt, "wowcharacter");
            nextDKPEventId = getNextAutoIncrementId(stmt, "dkpevent");
            nextItemId = getNextAutoIncrementId(stmt, "item");
            for (WOWCharacter c : characters) {
                // Save character
                saveCharacter(c);
                ++nextCharacterId;

                // Save all dkp event under this character
                eventList = c.getEventList();
                boolean firstEvent = true;
                boolean hasItem;
                for (DKPEvent e : eventList) {
                    if (e.getItem() != null) {
                        saveItem(e.getItem());
                        ++nextItemId;
                        hasItem = true;
                    } else {
                        hasItem = false;
                    }
                    saveDKPEvent(e, nextCharacterId - 1, firstEvent ? -1
                            : nextDKPEventId - 1, hasItem ? nextItemId - 1 : -1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public List<WOWCharacter> loadData() {
        List<WOWCharacter> characters = new LinkedList<WOWCharacter>();
        try {
            startConnection();
            stmt = conn.createStatement();
            charStmt = conn.createStatement();
            eventStmt = conn.createStatement();
            itemStmt = conn.createStatement();
            ResultSet rs = charStmt.executeQuery("SELECT * FROM `wowcharacter`");
            ResultSet dkpeventRs;
            String cName;
            String cRealm;
            int cid;
            WOWCharacter tmpC;
            while (rs.next()) {
                cName = rs.getString("name");
                cRealm = rs.getString("realm");
                cid = rs.getInt("id");
                tmpC = new WOWCharacter(cName, cRealm);
                // get all dkp event for this character
                dkpeventRs = eventStmt
                        .executeQuery("SELECT * FROM `dkpevent` WHERE `charId`="
                                + cid + " ORDER BY `lastId`");
                addEventToCharacter(dkpeventRs, tmpC);
                characters.add(tmpC);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return characters;
    }
    
    private void addEventToCharacter(ResultSet rs, WOWCharacter c) throws SQLException {
        DKPEvent e;
        if (rs.next()) {
            int score = rs.getInt("score");
            DKPEventType type = DKPEventType.valueOf(rs.getString("type"));
            String bossName = rs.getString("bossName");
            Boss boss = null;
            if (!bossName.equals("-")) {
                boss = Constants.bossMap.get(rs.getString("bossName"));
            }
            Difficulty difficulty = Difficulty.valueOf(rs.getString("difficulty"));
            String description = rs.getString("description");
            int itemId = rs.getInt("itemId");
            Item item = null;
            if (itemId != -1) {
                item = loadItem(itemId);
            }
            e = new DKPEvent(score, type, description, c, item, boss, difficulty);
            try {
                c.addDKPEvent(e);
            } catch (NotEnoughDKPException e1) {
                // This shouldn't happen
                e1.printStackTrace();
            }
            addEventToCharacter(rs, c);
        }
    }
    
    private Item loadItem(int itemId) throws SQLException {
        ResultSet rs = itemStmt.executeQuery("SELECT * FROM `item` WHERE `id`=`" + itemId + "`");
        if (rs.next()) {
            int inGameId = rs.getInt("inGameId");
            String name = rs.getString("name");
            return new Item(inGameId, name);
        } else {
            return null;
        }
    }

    private void saveCharacter(WOWCharacter c) throws SQLException {
        insertCharacter = conn
                .prepareStatement(ConnectionConstants.INSERT_CHARACTER);
        insertCharacter.setString(1, c.getName());
        insertCharacter.setString(2, c.getRealm());
        insertCharacter.executeUpdate();
    }

    private void saveItem(Item e) throws SQLException {
        insertItem = conn.prepareStatement(ConnectionConstants.INSERT_ITEM);
        insertItem.setString(1, e.getName());
        insertItem.setInt(2, 0);
        insertItem.executeUpdate();
    }

    private void saveDKPEvent(DKPEvent e, int charId, int lastId, int itemId)
            throws SQLException {
        // if the value of lastId or itemId happened to be negative number,
        // then means no last event or no item for this event.
        insertDKPEvent = conn
                .prepareStatement(ConnectionConstants.INSERT_DKPEVENT);
        insertDKPEvent.setInt(1, charId);
        insertDKPEvent.setInt(2, lastId);
        insertDKPEvent.setInt(3, itemId);
        insertDKPEvent.setString(4, e.getType().name());
        insertDKPEvent.setString(5, e.getBoss() == null ? "-" : e.getBoss()
                .getName());
        insertDKPEvent.setString(6, e.getBoss() == null ? "-" : e.getBoss()
                .getRaid());
        insertDKPEvent.setInt(7, e.getScore());
        insertDKPEvent.setString(8, e.getDescription());
        insertDKPEvent.setString(9, e.getDifficulty().name());
        insertDKPEvent.executeUpdate();
    }

    private int getNextAutoIncrementId(Statement st, String tableName)
            throws SQLException {
        // assume connection has already been started
        String sql = "SHOW TABLE STATUS LIKE \"" + tableName + "\"";
        ResultSet rs = st.executeQuery(sql);
        if (rs.next()) {
            return rs.getInt("Auto_increment");
        } else {
            return -1;
        }
    }

    public void clearDatabase() {
        try {
            startConnection();
            stmt = conn.createStatement();
            String sql = "TRUNCATE `wowcharacter`";
            stmt.executeUpdate(sql);
            sql = "TRUNCATE `item`";
            stmt.executeUpdate(sql);
            sql = "TRUNCATE `dkpevent`";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
