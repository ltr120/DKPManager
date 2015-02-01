package model;

public class Constants {

    // Character
    public static final String DEFAULT_REALM = "Aggramar";
    public static final int UNDERLIMIT_DKP = -100;
    
    // DKPEventType Constants
    public static final DKPEventType DEFAULT_EVENT_TYPE = DKPEventType.MODIFIED_BY_OFFICER;
    public static final String DEFAULT_EVENT_DESCRIPTION = "None";
    
    // Item
    public static final int REG_STARTING_PRICE = 30;
    
    public enum DKPEventType {
        ARRIVE_ON_TIME,
        DISSMISS_ON_TIME,
        WIN_A_LOOT,
        MODIFIED_BY_OFFICER
    }
}
