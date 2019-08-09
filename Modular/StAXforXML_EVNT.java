package EvntFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class StAXforXML_EVNT {

    private String evntDate;// line includes an event date and time
    private String user;// Schrack user, excl. level 0
    private int command;// A command did send by the user
    private int idTypeName; // Schrack original id of device
    private int messageNumber; // an evnt file attribute
    private int seconetAddress; // seconet net Address
    private int functionType; // opeation with device
    private int subType; // subtype of device
    private int elementNumber; // the log number
    private int zoneAddress; // a detector address in the group
    private int stateOfDevice;// state of device
    private int subStateofDevice;// substate of device
    private int cmdZoneStartAddr = 255;// a start command range of group/s
    private int cmdDeviceStartAddr;// a start command range of device/s
    private int cmdZoneEndAddr = 255;// an end command range of groups
    private int cmdDeviceEndAddr = 65280;// an end command range of devices
    private int addInfo = 0;// start/end of state
    private int totalTXT;// total lines of a xml text file
    private int transmitter;// get a type of command transmitter
    private boolean hasText = false;// this will be set if an ITX file has loaded previously
    private String evntMessCreated; // a dbg frame text info
    private String[] evntMessages;// output messages
    private String[] evntTXTMessages, evntUTF16TXTMessages;// output messages had made with customer texts
    private GroupAddressBuild[] DevAddr; // an address builder
    private CreateOneEvntMessage[] OneMsg;// a message builder
    private TxtOfAddressBuild[] msgTexts;// texts of messages

    void StAXforEVNT(String EVNTFile, int totalLines, Boolean outSet[])
            throws FileNotFoundException, XMLStreamException, IOException {

        int linesInEvnt = totalLines + 5; // it's a magic number linesInEvnt, a real range is 1-3

        // A message Objects create
        // A group address object create
        OneMsg = new CreateOneEvntMessage[linesInEvnt];
        DevAddr = new GroupAddressBuild[linesInEvnt];

        for (int k = 0; k < linesInEvnt; k++) {
            OneMsg[k] = new CreateOneEvntMessage();
            DevAddr[k] = new GroupAddressBuild();
        }

        XMLInputFactory xif = XMLInputFactory.newInstance();

        XMLStreamReader streamReader = xif.createXMLStreamReader(new InputStreamReader(new FileInputStream(EVNTFile)));

        if (streamReader.hasNext()) {
            streamReader.next(); // A first line canceled
        }
        int i = 0; // nr. message

        while (streamReader.hasNext()) {

            streamReader.next();

            if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {

                // get an element address for all types
                try {
                    elementNumber = Integer.parseInt(streamReader.getAttributeValue(7));
                } catch (IllegalArgumentException e) {
                    elementNumber = 65535;
                }

                // get a gr. address for the message
                if (streamReader.getAttributeValue(0).equals("2")) {
                    try {
                        zoneAddress = Integer.parseInt(streamReader.getAttributeValue(9));
                    } catch (IllegalArgumentException e) {
                        zoneAddress = 255;
                    }
                }

                // get additional information of a current message
                if (streamReader.getAttributeValue(0).equals("2")) {
                    try {
                        addInfo = Integer.parseInt(streamReader.getAttributeValue(8));
                    } catch (IllegalArgumentException e) {
                        addInfo = 0;
                    }
                }

                // date get the date for different messages and commands
                if (streamReader.getAttributeValue(0).equals("2")) {
                    try {
                        evntDate = streamReader.getAttributeValue(13);
                    } catch (IllegalArgumentException e) {
                        evntDate = "даты и времени записи нет";
                    }
                } // a command type
                else if (streamReader.getAttributeValue(0).equals("3")) {
                    try {
                        evntDate = streamReader.getAttributeValue(11);
                    } catch (IllegalArgumentException e) {
                        evntDate = "даты и времени записи нет";
                    }
                }

                if (streamReader.getAttributeValue(0).equals("3")) {
                    try {
                        user = streamReader.getAttributeValue(15);
                    } catch (IllegalArgumentException e) {
                        user = "пользователь неизвестен";
                    }
                }

                // get a type of command transmitter
                if (streamReader.getAttributeValue(0).equals("3")) {
                    try {
                        transmitter = Integer.parseInt(streamReader.getAttributeValue(14));
                    } catch (IllegalArgumentException e) {
                        transmitter = 30;
                    }
                }

                // get the command id number
                if (streamReader.getAttributeValue(0).equals("3")) {
                    try {
                        command = Integer.parseInt(streamReader.getAttributeValue(2));
                    } catch (IllegalArgumentException e) {
                        command = 41;
                    }
                }

                // get command's address(es)
                if ((streamReader.getAttributeValue(0).equals("3"))
                        && (streamReader.getAttributeValue(6).equals("1"))) {
                    try {
                        cmdDeviceStartAddr = Integer.parseInt(streamReader.getAttributeValue(7));
                    } catch (IllegalArgumentException e) {
                    }
                    try {
                        cmdDeviceEndAddr = Integer.parseInt(streamReader.getAttributeValue(8));
                    } catch (IllegalArgumentException e) {
                    }
                    try {
                        cmdZoneStartAddr = Integer.parseInt(streamReader.getAttributeValue(9));
                    } catch (IllegalArgumentException e) {
                    }
                    try {
                        cmdZoneEndAddr = Integer.parseInt(streamReader.getAttributeValue(10));
                    } catch (IllegalArgumentException e) {
                    }

                } else if ((streamReader.getAttributeValue(0).equals("3"))
                        && (streamReader.getAttributeValue(6).equals("0"))) {
                    try {
                        cmdDeviceStartAddr = Integer.parseInt(streamReader.getAttributeValue(7));
                    } catch (IllegalArgumentException e) {
                    }
                    try {
                        cmdZoneStartAddr = Integer.parseInt(streamReader.getAttributeValue(9));
                    } catch (IllegalArgumentException e) {
                    }
                    cmdDeviceEndAddr = 65280;
                    cmdZoneEndAddr = 255;
                }

                try {
                    idTypeName = Integer.parseInt(streamReader.getAttributeValue(4));
                } catch (IllegalArgumentException e) {
                    idTypeName = 255;
                }

                try {
                    messageNumber = Integer.parseInt(streamReader.getAttributeValue(1));
                } catch (IllegalArgumentException e) {
                    messageNumber = 65535;
                }

                try {
                    seconetAddress = Integer.parseInt(streamReader.getAttributeValue(3));
                } catch (IllegalArgumentException e) {
                    seconetAddress = 65535;
                }

                try {
                    functionType = Integer.parseInt(streamReader.getAttributeValue(5));
                } catch (IllegalArgumentException e) {
                    functionType = 14;
                }

                try {
                    subType = Integer.parseInt(streamReader.getAttributeValue(6));
                } catch (IllegalArgumentException e) {
                    subType = 16;
                }

                try {
                    stateOfDevice = Integer.parseInt(streamReader.getAttributeValue(10));
                } catch (IllegalArgumentException e) {
                    // stateOfDevice=29;
                }

                try {
                    subStateofDevice = Integer.parseInt(streamReader.getAttributeValue(11));
                } catch (IllegalArgumentException e) {
                    // subStateofDevice=14;
                }

                // an device address build
                DevAddr[i].setAddress(zoneAddress, elementNumber);

                // a message build
                OneMsg[i].OneMessageWriter(messageNumber, DevAddr[i].getAddressLine(), seconetAddress,
                        streamReader.getLocalName(), idTypeName, functionType, subType, stateOfDevice, subStateofDevice,
                        evntDate, user, command, cmdZoneStartAddr, cmdDeviceStartAddr, cmdZoneEndAddr, cmdDeviceEndAddr,
                        addInfo, transmitter, outSet);

                i++;

            }
        }

        /*
		 * the OUT Text logfile create
         */
        if (!hasText) {
//if texts has not loaded by the user
            evntMessages = new String[linesInEvnt];
            String outEventFile = EVNTFile.replaceAll(".b5evtl", ".csv");
            outEventFile = outEventFile.replaceAll(".b5evtb", ".csv");
            File logFile = new File(outEventFile); // an OUTPUT Log File set

            try (FileOutputStream fwLOG = new FileOutputStream(logFile.getAbsolutePath())) {
                OutputStreamWriter outLOG = new OutputStreamWriter(fwLOG, "UTF-8");
                BufferedWriter bwLOG = new BufferedWriter(outLOG);

                String OneMessageTemp;

                for (int j = 0; j < linesInEvnt; j++) {

                    if (OneMsg[j].getEvntMessage() != null) { // Message write without magic number miracle
                        OneMessageTemp = OneMsg[j].getEvntMessage();
                        bwLOG.append(OneMessageTemp);
                        bwLOG.append("\n");
                        evntMessages[j] = OneMessageTemp;
                    }
                }

                bwLOG.flush();
                bwLOG.close();

                evntMessCreated = "Cоздан файл протокола: " + logFile.getAbsolutePath();

            } catch (FileNotFoundException e) {

                System.out.println("Невозможно создать файл протокола для записи.");
                evntMessCreated = "Невозможно создать файл протокола для записи.";
            }

        } else {
//device texts had loaded 
            evntTXTMessages = new String[linesInEvnt];
            evntUTF16TXTMessages = new String[linesInEvnt];

            String outTXTEventFIle = EVNTFile.replaceAll(".b5evtl", "TXT.csv");
            outTXTEventFIle = outTXTEventFIle.replaceAll(".b5evtb", "TXT.csv");

            String outUTF16TXTEventFIle = EVNTFile.replaceAll(".b5evtl", "UTF16TXT.csv");
            outUTF16TXTEventFIle = outUTF16TXTEventFIle.replaceAll(".b5evtb", "UTF16TXT.csv");

            File logFileTXT = new File(outTXTEventFIle); // an OUTPUT Log File set
            File logUTF16FileTXT = new File(outUTF16TXTEventFIle);

//an output file set for test
            try (FileOutputStream fwTXTLOG = new FileOutputStream(logFileTXT.getAbsolutePath())) {
                OutputStreamWriter outTXTLOG = new OutputStreamWriter(fwTXTLOG, "UTF-8");
                BufferedWriter bwTXTLOG = new BufferedWriter(outTXTLOG);

                FileOutputStream fwUTF16TXTLOG = new FileOutputStream(logUTF16FileTXT.getAbsolutePath());
                OutputStreamWriter outUTF16TXTLOG = new OutputStreamWriter(fwUTF16TXTLOG, "UTF-16");
                BufferedWriter bwUTF16TXTLOG = new BufferedWriter(outUTF16TXTLOG);

                String OneMessageTXTTemp;
                String OneMessageUTF16TXTTemp;

                for (int j = 0; j < linesInEvnt; j++) {
//Message make without magic number miracle 
                    if (OneMsg[j].getEvntMessage() != null) {

                        OneMessageTXTTemp = OneMsg[j].getEvntMessage();
                        OneMessageUTF16TXTTemp = OneMsg[j].getEvntMessage();

                        int[] tempDevAdr = DevAddr[j].getAddress();
                        int devMsg = tempDevAdr[1];
                        int grpMsg = tempDevAdr[0];
                        int devId = OneMsg[j].getDeviceType();

                        for (int h = 0; h < totalTXT; h++) {

                            int[] tempMsgKey = msgTexts[h].getTXT0fAddress();
                            int tmpKeyGrp = tempMsgKey[0];
                            int tmpKeyDev = tempMsgKey[1];
                            int tmpKeyId = tempMsgKey[2];

                            if ((devId == tmpKeyId) && (devMsg == tmpKeyDev) && (grpMsg == tmpKeyGrp)) {
                                OneMessageTXTTemp += "; ";
                                OneMessageTXTTemp += msgTexts[h].getTXT0fAddressLine();
                                OneMessageUTF16TXTTemp += "; ";
                                OneMessageUTF16TXTTemp += msgTexts[h].getTXT0fAddressLine();
                            }

                        }

                        bwTXTLOG.append(OneMessageTXTTemp);
                        bwTXTLOG.append("\n");
                        evntTXTMessages[j] = OneMessageTXTTemp;

                        bwUTF16TXTLOG.append(OneMessageTXTTemp);
                        bwUTF16TXTLOG.append("\n");
                        evntUTF16TXTMessages[j] = OneMessageUTF16TXTTemp;

                    }

                }

                bwTXTLOG.flush();
                bwTXTLOG.close();

                bwUTF16TXTLOG.flush();
                bwUTF16TXTLOG.close();

                evntMessCreated = "Cозданы файлы протокола станции ПС Schrack с текстами заказчика: " + outTXTEventFIle
                        + ", " + outUTF16TXTEventFIle + ".";

            } catch (FileNotFoundException e) {

                System.out.println("Невозможно создать файл протокола с текстами заказчика для записи.");
                evntMessCreated = "Невозможно создать файл протокола с текстами заказчика для записи.";
            }
        }

        streamReader.close();
    }

    List<String> getAllMessages() {
        if (hasText) {
            return Arrays.asList(evntTXTMessages);
        } else {
            return Arrays.asList(evntMessages);
        }
    }

    void setITXhasLoaded(boolean hasITXFile, int totalTexts, TxtOfAddressBuild[] customerTexts) {

        hasText = hasITXFile;
        totalTXT = totalTexts;
        msgTexts = customerTexts;

    }

    String getMessageState() {
        return this.evntMessCreated;
    }
}
