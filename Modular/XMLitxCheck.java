package EvntFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class XMLitxCheck {

    int totalTexts;
    private String totalDevITX;

    void itxFileCheck(String ITXFileName) throws FileNotFoundException, XMLStreamException {

        XMLInputFactory xif = XMLInputFactory.newInstance();

        XMLStreamReader sr = xif.createXMLStreamReader(new InputStreamReader(new FileInputStream(ITXFileName)));

        // a total of usefull texts
        while (sr.hasNext()) {
            sr.next();
            if (sr.getEventType() == XMLStreamReader.START_ELEMENT) {
                if ("description".equals(sr.getLocalName())) {
                    totalTexts++;
                }
            }
        }
        totalDevITX = "Файл текстов : " + ITXFileName + " содержит " + totalTexts + " текстовых описаний.";
        sr.close();
    }

    String getTextState() {
        if (totalTexts > 0) {
            return totalDevITX;
        } else {
            return null;
        }
    }

}
