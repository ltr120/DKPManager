package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WOWCharacter implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2216740042870567547L;

    private String name_;
    private String realm_;
    private List<DKPEvent> log_;

    public WOWCharacter(String name, String realm) {
        name_ = name;
        realm_ = realm;
        log_ = new LinkedList<DKPEvent>();
    }

    public WOWCharacter(String name) {
        this(name, Constants.DEFAULT_REALM);
    }

    public String getName() {
        return this.name_;
    }

    public String getRealm() {
        return this.realm_;
    }

    public List<DKPEvent> getEventList() {
        return new ArrayList<DKPEvent>(log_);
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
}
