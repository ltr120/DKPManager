package model;

import java.io.Serializable;

import model.Constants.DKPEventType;
import model.Constants.Difficulty;

public class DKPEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6732527002273501035L;

    private int score_;
    private String description_;
    private WOWCharacter character_;
    private DKPEventType type_;
    private Boss boss_;
    private Difficulty difficulty_;
    private Item item_;

    public DKPEvent(int score, DKPEventType type, String description,
            WOWCharacter character, Item item, Boss boss, Difficulty difficulty) {
        score_ = score;
        type_ = type;
        description_ = description;
        character_ = character;
        item_ = item;
        difficulty_ = difficulty;
        boss_ = boss;
    }

    public DKPEvent(int score, DKPEventType type, WOWCharacter character,
            Boss boss, Difficulty difficulty) {
        this(score, type, Constants.DEFAULT_EVENT_DESCRIPTION, character, null,
                boss, difficulty);
    }

    public int getScore() {
        return score_;
    }

    public String getDescription() {
        return description_;
    }

    public WOWCharacter getCharacter() {
        return character_;
    }

    public DKPEventType getType() {
        return type_;
    }

    public Item getItem() {
        return item_;
    }

    public Boss getBoss() {
        return boss_;
    }

    public Difficulty getDifficulty() {
        return difficulty_;
    }
}
