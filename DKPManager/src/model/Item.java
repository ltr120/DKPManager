package model;

import java.io.Serializable;

public class Item implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6036957449579580186L;
    
    protected String name_;
    protected int id_;
    protected int startingPrice_;
    
    public Item(int id, String name) {
        startingPrice_ = Constants.REG_STARTING_PRICE;
        id_ = id;
        name_ = name;
    }
}
