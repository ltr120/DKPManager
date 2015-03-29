package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.Constants.WOWCharacterClass;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WOWCharacter implements Serializable {

    private static final long serialVersionUID = -2216740042870567547L;

    @XmlElement(name="name")
    private String name_;
    @XmlElement(name="realm")
    private String realm_;
    @XmlElement(name="class")
    private WOWCharacterClass class_;
    @XmlElementWrapper(name="DKPEvent")
    @XmlElement(name="Event")
    private List<DKPEvent> log_;

    public WOWCharacter(String name, String realm,
            WOWCharacterClass characterClass) {
        name_ = name;
        realm_ = realm;
        class_ = characterClass;
        log_ = new LinkedList<DKPEvent>();
    }

    // JAXB uses this method as well
    public String getName() {
        return this.name_;
    }

    // JAXB uses this method as well
    public String getRealm() {
        return this.realm_;
    }

    // JAXB uses this method as well
    public WOWCharacterClass getWOWCharacterClass() {
        return this.class_;
    }

    public List<DKPEvent> getEventList() {
        return log_;
    }

    public void addDKPEvent(DKPEvent event) throws NotEnoughDKPException {
        if (canAddEvent(event)) {
            log_.add(event);
        } else {
            throw new NotEnoughDKPException();
        }
    }

    private boolean canAddEvent(DKPEvent event) {
        if (event.getType().equals(Constants.DKPEventType.MODIFIED_BY_OFFICER)) {
            return true;
        } else {
            return getTotalDKP() + event.getScore() > Constants.UNDERLIMIT_DKP;
        }
    }

    public int getTotalDKP() {
        int score = 0;
        Iterator<DKPEvent> it = log_.iterator();
        DKPEvent tmpEvent;
        while (it.hasNext()) {
            tmpEvent = it.next();
            score += tmpEvent.getScore();
        }
        return score;
    }

    /*
     * The following code shouldn't be used by model. Only JAXB should use it.
     */
    public WOWCharacter() {
        
    }
    
    public void setName(String name) {
        name_ = name;
    }

    public void setRealm(String realm) {
        realm_ = realm;
    }

    public void setWOWCharacterClass(WOWCharacterClass c) {
        class_ = c;
    }
}
