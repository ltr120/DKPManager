package model;

import java.io.Serializable;

import model.Constants.DKPEventType;

public class DKPEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6732527002273501035L;
    
    private int score_;
    private String description_;
    private Character character_;
    private DKPEventType type_;
    private Item item_;
    
    public DKPEvent(int score, DKPEventType type, String description, Character character, Item item) {
        score_ = score;
        type_ = type;
        description_ = description;
        character_ = character;
        item_ = item;
    }
    
    public DKPEvent(int score, DKPEventType type, Character character) {
        this(score, type, Constants.DEFAULT_EVENT_DESCRIPTION, character, null);
    }
    
    public int getScore() {
        return score_;
    }
    
    public String getDescription() {
        return description_;
    }
    
    public Character getCharacter() {
        return character_;
    }
    
    public DKPEventType getType() {
        return type_;
    }
    
    public Item getItem() {
        return item_;
    }
}
