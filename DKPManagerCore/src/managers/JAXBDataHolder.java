package managers;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import model.WOWCharacter;

@XmlRootElement(namespace = "managers", name = "DKPData")
@XmlAccessorType(XmlAccessType.FIELD)
public class JAXBDataHolder {

    public List<WOWCharacter> wowCharacters;

    public JAXBDataHolder(List<WOWCharacter> list) {
        wowCharacters = list;
    }
    
    /*
    * The following code shouldn't be used by model. Only JAXB should use it.
    */
    public JAXBDataHolder() {
        
    }
    
    public void setWowCharacterList(List<WOWCharacter> list) {
        wowCharacters = list;
    }
    
    public List<WOWCharacter> getWowCharacters() {
        return wowCharacters;
    }
}
