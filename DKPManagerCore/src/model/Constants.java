package model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {

    // General
    public static final String VERSION_STRING = "v0.0.2 (Alpha)";
    public static final String GUILD_NAME = "My Lifes Getting Better";
    public static final double DECAY_RATE = 0.1;

    // Character
    public static final String DEFAULT_REALM = "Aggramar";
    public static final int UNDERLIMIT_DKP = -100;
    
    public enum WOWCharacterClass {
        DEATH_KNIGHT("#C41F3B"),
        DRUID("#FF7D0A"),
        HUNTER("#ABD473"),
        MAGE("#69CCF0"),
        MONK("#00FF96"),
        PALADIN("#F58CBA"),
        PRIEST("#FFFFFF"),
        ROGUE("#FFF569"),
        SHAMAN("#0070DE"),
        WARLOCK("#9482C9"),
        WARRIOR("#C79C6E");
        
        private final String color_;
        
        private WOWCharacterClass(final String color) {
            color_ = color;
        }
        
        public String getColor() {
            return this.color_;
        }
        
        public static String[] names() {
            WOWCharacterClass[] classes = values();
            String[] names = new String[classes.length];
            int i = 0;
            for (WOWCharacterClass c : classes) {
                names[i] = c.name();
                ++i;
            }
            return names;
        }
    }

    // DKPEventType Constants
    public static final DKPEventType DEFAULT_EVENT_TYPE = DKPEventType.MODIFIED_BY_OFFICER;
    public static final String DEFAULT_EVENT_DESCRIPTION = "None";

    // Item
    public static final int REG_STARTING_PRICE = 30;
    public static final int DEFAULT_ITEM_ID = 0;

    public enum DKPEventType {
        ARRIVE_ON_TIME,
        DISMISS_ON_TIME,
        WIN_A_LOOT,
        MODIFIED_BY_OFFICER,
        BOSS_DOWN,
        BOSS_FIRST_DOWN,
        WEEKLY_DECAY;

        public static String[] names() {
            DKPEventType[] types = values();
            String[] names = new String[types.length - 1];
            int i = 0;
            for (DKPEventType t : types) {
                if (t != DKPEventType.WEEKLY_DECAY) {
                    names[i] = t.name();
                    ++i;
                }
            }
            return names;
        }

        public static DKPEventType getDKPEventTypeByString(String s) {
            for (DKPEventType t : values()) {
                if (t.name().equals(s)) {
                    return t;
                }
            }
            return null;
        }
    }

    // Boss
    public enum Difficulty {
        NORMAL,
        HEROIC,
        MYTHIC;

        public static String[] names() {
            Difficulty[] diffs = values();
            String[] names = new String[diffs.length];
            int i = 0;
            for (Difficulty d : diffs) {
                names[i] = d.name();
                ++i;
            }
            return names;
        }

        public static Difficulty getDifficultyString(String s) {
            for (Difficulty t : values()) {
                if (t.name().equals(s)) {
                    return t;
                }
            }
            return null;
        }
    }

    public static final Map<String, Boss> bossMap;
    static {
        Map<String, Boss> tmpMap = new LinkedHashMap<String, Boss>();
        Boss tmpBoss;
        // Blackrock Foundry bosses
        tmpBoss = new Boss("Gruul", "Blackrock Foundry");
        tmpMap.put("Gruul", tmpBoss);
        tmpBoss = new Boss("Oregorger", "Blackrock Foundry");
        tmpMap.put("Oregorger", tmpBoss);
        tmpBoss = new Boss("Beastlord Darmac", "Blackrock Foundry");
        tmpMap.put("Beastlord Darmac", tmpBoss);
        tmpBoss = new Boss("Flamebender Ka'graz", "Blackrock Foundry");
        tmpMap.put("Flamebender Ka'graz", tmpBoss);
        tmpBoss = new Boss("Hans'gar and Franzok", "Blackrock Foundry");
        tmpMap.put("Hans'gar and Franzok", tmpBoss);
        tmpBoss = new Boss("Operator Thogar", "Blackrock Foundry");
        tmpMap.put("Operator Thogar", tmpBoss);
        tmpBoss = new Boss("The Blast Furnace", "Blackrock Foundry");
        tmpMap.put("The Blast Furnace", tmpBoss);
        tmpBoss = new Boss("Kromog", "Blackrock Foundry");
        tmpMap.put("Kromog", tmpBoss);
        tmpBoss = new Boss("The Iron Maidens", "Blackrock Foundry");
        tmpMap.put("The Iron Maidens", tmpBoss);
        tmpBoss = new Boss("Blackhand", "Blackrock Foundry");
        tmpMap.put("Blackhand", tmpBoss);
        
        // Highmaul bosses
        tmpBoss = new Boss("Kargath Bladefist", "Highmaul");
        tmpMap.put("Kargath Bladefist", tmpBoss);
        tmpBoss = new Boss("The Butcher", "Highmaul");
        tmpMap.put("The Butcher", tmpBoss);
        tmpBoss = new Boss("Tectus", "Highmaul");
        tmpMap.put("Tectus", tmpBoss);
        tmpBoss = new Boss("Brackenspore", "Highmaul");
        tmpMap.put("Brackenspore", tmpBoss);
        tmpBoss = new Boss("Twin Ogron", "Highmaul");
        tmpMap.put("Twin Ogron", tmpBoss);
        tmpBoss = new Boss("Ko'ragh", "Highmaul");
        tmpMap.put("Ko'ragh", tmpBoss);
        tmpBoss = new Boss("Imperator Mar'gok", "Highmaul");
        tmpMap.put("Imperator Mar'gok", tmpBoss);
        
        bossMap = Collections.unmodifiableMap(tmpMap);
    }

    // GUI
    // TableDisplay
    public static final String TABLEDISPLAY_CARD1 = "1";
    public static final String TABLEDISPLAY_CARD2 = "2";

    // ManipulationPanel
    public static final String MANIPULATION_ADD_CHAR_BUTT_NAME = "Add New Character to Character List";
    public static final String MANIPULATION_DEL_CHAR_BUTT_NAME = "Delete selected Character(s)";
    public static final String MANIPULATION_DECAY_BUTT_NAME = "Weekly DKP Decay";
    public static final String MANIPULATION_ADD_EVENT_BUTT_NAME = "Submit DKP Event";
}
