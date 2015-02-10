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
    
    private List<WOWCharacter> characterList_;
    private String nickName_;
    private String battleTag_;
    
    public BNAccount(String nickName, String battleTag) {
        nickName_ = nickName;
        battleTag_ = battleTag;
        characterList_ = new LinkedList<WOWCharacter>();
    }
    
    public String getNickName() {
        return nickName_;
    }
    
    public String getBattleTag() {
        return battleTag_;
    }
    
    public List<WOWCharacter> getCharacterList() {
        return characterList_;
    }
    
    public void addCharacter(WOWCharacter character) {
        characterList_.add(character);
    }
    
    public int getTotalDKP() {
        int score = 0;
        Iterator<WOWCharacter> it = characterList_.iterator();
        WOWCharacter tmp;
        while (it.hasNext()) {
            tmp = it.next();
            score += tmp.getTotalDKP();
        }
        return score;
    }
    
}
