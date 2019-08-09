package EvntFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class StAXforXML_ITX {

    private TxtOfAddressBuild[] textsOfAddresses;

    void StAXforXML_ITXtext(String ITXFileName, int totalTexts) throws FileNotFoundException, XMLStreamException {

        XMLInputFactory xif = XMLInputFactory.newInstance();

        XMLStreamReader streamReader = xif.createXMLStreamReader(
                new InputStreamReader(new FileInputStream(ITXFileName), Charset.forName("UTF8")));

//An array for texts create        
        textsOfAddresses = new TxtOfAddressBuild[totalTexts];
//Objects create
        for (int ti = 0; ti < totalTexts; ti++) {
            textsOfAddresses[ti] = new TxtOfAddressBuild();
        }

        int i = 0;
        int GroupAddress = 0;
        int DeviceAddress = 0;
        int DeviceType = 30;
        String elText = "нет текста для устройства";

        while (streamReader.hasNext()) {

            streamReader.next();
            boolean hasAddr = false, hasGr = false, hasTxt = false;

            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {

                switch (streamReader.getLocalName()) {

                    case "element":
                        hasAddr = true;
//An element type 
                        DeviceType = Integer.parseInt(streamReader.getAttributeValue(1));
                        if (DeviceType >= 13) {
                            DeviceType += 1;
                        }

//An element address
                        DeviceAddress = Integer.parseInt(streamReader.getAttributeValue(0));
                        break;

                    case "sub-element":
                        hasGr = true;
//A gr. address
                        GroupAddress = Integer.parseInt(streamReader.getAttributeValue(0));
                        break;
//An element text                       
                    case "description":
                        hasTxt = true;
                        elText = streamReader.getElementText();
                        break;

                    default:
                        break;

                }

                if (hasAddr && !hasGr) {
                    GroupAddress = 0;
                }

                if (hasTxt) {
                    textsOfAddresses[i].setTXT0fAddress(GroupAddress, DeviceAddress, DeviceType, elText);
                    i++;
                }
            }

        }

        streamReader.close();
    }

    TxtOfAddressBuild[] getCutomerTexts() {

        return textsOfAddresses;

    }

}

/*
 * static final private String[] ELEMENTYPES = { "датчик", //0 "вход", //1
 * "выход", //2 "внешний", //3 "принтер", //4 "пульт управления",//5
 * "аккумуляторы", //6 "сеть IntegralLAN",//7 "активная часть модуля",//8
 * "пассивная часть модуля",//9 "задержка тревоги", //10
 * "панель пожарной бригады",//11 "станция ПС Schrack", //12 "",//13
 * "индикатор", //13 "соединение LAN",//14 "система управления",//15 "шлейф",
 * //16 "удаленный доступ",//17 "тех.обслуживание", //18
 * "направление пожаротушения", //19 "мастер станция", //20 "узел сети Seconet",
 * //21 "сеть Seconet", //22 "контролируемый выход",//23 "внешний принтер", //24
 * "зона оповещения", //25 "внешняя система", //26 "панель", //27
 * "источник питания" //28 };
 */
