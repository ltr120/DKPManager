package model;

public class Boss {

    private String name_;
    private String raidName_;
    
    public Boss(String name, String raidName) {
        name_ = name;
        raidName_ = raidName;
    }
    
    public String getName() {
        return name_;
    }
    
    public String getRaid() {
        return raidName_;
    }
}
