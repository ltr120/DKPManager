package model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BNAccount implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1019415482776030231L;
    
    private List<Character> characterList_;
    private String nickName_;
    private String battleTag_;
    
    public BNAccount(String nickName, String battleTag) {
        nickName_ = nickName;
        battleTag_ = battleTag;
        characterList_ = new LinkedList<Character>();
    }
    
    public String getNickName() {
        return nickName_;
    }
    
    public String getBattleTag() {
        return battleTag_;
    }
    
    public List<Character> getCharacterList() {
        return characterList_;
    }
    
    public void addCharacter(Character character) {
        characterList_.add(character);
    }
    
    public int getTotalDKP() {
        int score = 0;
        Iterator<Character> it = characterList_.iterator();
        Character tmp;
        while (it.hasNext()) {
            tmp = it.next();
            score += tmp.getTotalDKP();
        }
        return score;
    }
    
}
