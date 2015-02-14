package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import managers.MySQLManager;
import model.Constants.DKPEventType;
import model.Constants.Difficulty;

public class QueryHandler {

    private static QueryHandler instance_ = null;

    private List<WOWCharacter> characters_ = null;

    MySQLManager manager_;

    private QueryHandler() {
        manager_ = new MySQLManager();

        // mock values
//        characters_ = new LinkedList<WOWCharacter>();
//        characters_.add(new WOWCharacter("testCharacter1"));
//        characters_.add(new WOWCharacter("testCharacter2"));
//        characters_.add(new WOWCharacter("testCharacter3"));
//        WOWCharacter c1 = new WOWCharacter("Woshiyigenai");
//        DKPEvent e = new DKPEvent(100, DKPEventType.ARRIVE_ON_TIME,
//                Constants.DEFAULT_EVENT_DESCRIPTION, c1, null,
//                Constants.bossMap.get("Gruul"), Difficulty.HEROIC);
//        try {
//            c1.addDKPEvent(e);
//        } catch (NotEnoughDKPException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        characters_.add(c1);
        characters_ = manager_.loadData();
    }

    public static void saveToDatabase() {
        QueryHandler.getInstance().manager_
                .saveData(QueryHandler.getInstance().characters_);
    }

    public static void loadFromDatabase() {
        QueryHandler.getInstance().characters_ = QueryHandler.getInstance().manager_
                .loadData();
    }
    
    public static void clearDatabase() {
        QueryHandler.getInstance().manager_.clearDatabase();
    }

    public static QueryHandler getInstance() {
        if (instance_ == null) {
            instance_ = new QueryHandler();
        }
        return instance_;
    }

    public static List<WOWCharacter> getAllCharacters() {
        return Collections
                .unmodifiableList(QueryHandler.getInstance().characters_);
    }

    public static void addCharacter(WOWCharacter c) {
        QueryHandler.getInstance().characters_.add(c);
    }

    public static void deleteCharacter(WOWCharacter c) {
        QueryHandler.getInstance().characters_.remove(c);
    }

    public static void weeklyDecay() {
        int totalScore, decayScore;
        for (WOWCharacter c : QueryHandler.getInstance().characters_) {
            totalScore = c.getTotalDKP();
            decayScore = calcDecayScore(totalScore);
            DKPEvent e = new DKPEvent(decayScore, DKPEventType.WEEKLY_DECAY, c,
                    null, Difficulty.NOT_APPLICABLE);
            try {
                c.addDKPEvent(e);
            } catch (NotEnoughDKPException e1) {
                // This really shouldn't happen. Or something is seriously
                // wrong.
                e1.printStackTrace();
            }
        }
    }

    private static int calcDecayScore(int score) {
        if (score < 0) {
            return 0;
        } else {
            return Integer.valueOf((int) Math.round((-Constants.DECAY_RATE)
                    * score));
        }
    }
}
