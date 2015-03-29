package managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.Constants;
import model.DKPEvent;
import model.WOWCharacter;

public class JAXBManager extends AbstractManager {

    File file_;

    public void setFile(File f) {
        file_ = f;
    }

    public boolean hasFile() {
        return !(file_ == null);
    }

    @Override
    public void saveData(List<WOWCharacter> characters) {
        JAXBDataHolder dataHolder = new JAXBDataHolder(characters);
        try {
            JAXBContext context = JAXBContext.newInstance(JAXBDataHolder.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            System.out.println("Done marshalling. File: "
                    + file_.getAbsolutePath());
            if (file_ != null) {
                System.out.println(file_.getAbsolutePath());
                m.marshal(dataHolder, file_);
            } else {
                // TODO should throw exception
                System.out.println("File is null. cannot save to file.");
            }
        } catch (JAXBException e) {
            // TODO Need to design proper loging and exception handling here!
            e.printStackTrace(System.out);
        }
    }

    @Override
    public List<WOWCharacter> loadData() {
        if (!hasFile()) {
            // TODO Should throw exception
            return null;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(JAXBDataHolder.class);
            Unmarshaller um = context.createUnmarshaller();
            JAXBDataHolder data = (JAXBDataHolder) um.unmarshal(new FileReader(
                    file_));
            if (data != null) {
                provisionData(data);
                return data.getWowCharacters();
            } else {
                // TODO throw exception
                return null;
            }
        } catch (JAXBException e) {
            // TODO Need to design proper loging and exception handling here!
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            // TODO Need to design proper loging and exception handling here!
            e.printStackTrace();
            return null;
        }
    }

    private void provisionData(JAXBDataHolder data) {
        List<WOWCharacter> cList = data.getWowCharacters();
        for (WOWCharacter c : cList) {
            List<DKPEvent> eList = c.getEventList();
            for (DKPEvent e : eList) {
                e.setCharacter(c);
                if (e.getBossName() != null) {
                    e.setBoss(Constants.bossMap.get(e.getBossName()));
                }
            }
        }
    }

}
