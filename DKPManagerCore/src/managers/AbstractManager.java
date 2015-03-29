package managers;

import java.util.List;

import model.WOWCharacter;

public abstract class AbstractManager {

    public abstract void saveData(List<WOWCharacter> characters);
    
    public abstract List<WOWCharacter> loadData();
}
