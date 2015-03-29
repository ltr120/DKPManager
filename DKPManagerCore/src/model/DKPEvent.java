package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import model.Constants.DKPEventType;
import model.Constants.Difficulty;

@XmlAccessorType(XmlAccessType.FIELD)
public class DKPEvent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6732527002273501035L;

    @XmlElement(name = "score")
    private int score_;
    @XmlElement(name = "description")
    private String description_;
    @XmlTransient
    private WOWCharacter character;
    @XmlElement(name = "type")
    private DKPEventType type;
    @XmlTransient
    private Boss boss_;
    @XmlElement(name = "difficulty")
    private Difficulty difficulty;
    @XmlElement(name = "item")
    private Item item;
    @XmlElement(name = "boss")
    private String bossName;

    public DKPEvent(int score, DKPEventType t, String description,
            WOWCharacter ch, Item i, Boss boss, Difficulty diff) {
        score_ = score;
        type = t;
        description_ = description;
        character = ch;
        item = i;
        difficulty = diff;
        boss_ = boss;
        if (boss_ != null) {
            bossName = boss_.getName();
        } else {
            bossName = null;
        }
    }

    public DKPEvent(int score, DKPEventType type, WOWCharacter character,
            Boss boss, Difficulty diff) {
        this(score, type, Constants.DEFAULT_EVENT_DESCRIPTION, character, null,
                boss, diff);
    }

    // JAXB uses this method as well
    public int getScore() {
        return score_;
    }

    // JAXB uses this method as well
    public String getDescription() {
        return description_;
    }

    // JAXB uses this method as well
    @XmlTransient
    public WOWCharacter getCharacter() {
        return character;
    }

    // JAXB uses this method as well
    public DKPEventType getType() {
        return type;
    }

    // JAXB uses this method as well
    public Item getItem() {
        return item;
    }

    // JAXB uses this method as well
    @XmlTransient
    public Boss getBoss() {
        return boss_;
    }

    // JAXB uses this method as well
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /*
     * The following code shouldn't be used by model. Only JAXB should use it.
     */
    public DKPEvent() {

    }

    public void setScore(int s) {
        score_ = s;
    }

    public void setDescription(String d) {
        description_ = d;
    }

    public void setCharacter(WOWCharacter c) {
        character = c;
    }

    public void setType(DKPEventType t) {
        type = t;
    }

    public void setItem(Item i) {
        System.out.println("setItem invoked");
        item = i;
    }

    public void setBoss(Boss b) {
        boss_ = b;
        if (boss_ != null) {
            bossName = boss_.getName();
        } else {
            bossName = null;
        }
    }

    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bN) {
        System.out.println("setBossName invoked");
        bossName = bN;
        boss_ = Constants.bossMap.get(bossName);
    }
}
