package model;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import managers.JAXBManager;
import managers.MySQLManager;
import model.Constants.DKPEventType;
import model.Constants.Difficulty;

public class QueryHandler {

    private static QueryHandler instance_ = null;

    private List<WOWCharacter> characters_ = null;

    MySQLManager dbManager_;
    JAXBManager xmlManager_;

    private QueryHandler() {
        dbManager_ = new MySQLManager();
        xmlManager_ = new JAXBManager();

        // mock values
        characters_ = dbManager_.loadData();
    }

    public static void saveToDatabase() {
        QueryHandler.getInstance().dbManager_.saveData(QueryHandler
                .getInstance().characters_);
    }

    public static void saveToFile(File f) {
        QueryHandler.getInstance().xmlManager_.setFile(f);
        QueryHandler.getInstance().xmlManager_.saveData(QueryHandler
                .getInstance().characters_);
    }

    public static void loadFromDatabase() {
        QueryHandler.getInstance().characters_ = QueryHandler.getInstance().dbManager_
                .loadData();
    }

    public static void loadFromFile(File f) {
        QueryHandler.getInstance().xmlManager_.setFile(f);
        QueryHandler.getInstance().characters_ = QueryHandler.getInstance().xmlManager_
                .loadData();
    }

    public static void clearDatabase() {
        QueryHandler.getInstance().dbManager_.clearDatabase();
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
