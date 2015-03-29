package model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6036957449579580186L;

    @XmlElement(name = "name")
    protected String name_;
    @XmlElement(name = "id")
    protected int id_;
    @XmlTransient
    protected int startingPrice_;

    public Item(int id, String name) {
        startingPrice_ = Constants.REG_STARTING_PRICE;
        id_ = id;
        name_ = name;
    }

    // JAXB uses this method as well
    public String getName() {
        return name_;
    }
    
    /*
     * The following code shouldn't be used by model. Only JAXB should use it.
     */
    public Item() {
        
    }
    
    public void setName(String name) {
        name_ = name;
    }
    
    public void setId(int id) {
        id_ = id;
    }
}
