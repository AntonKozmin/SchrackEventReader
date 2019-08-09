package EvntFileReader;

/*
 *  create oneline's message, collect data for address, search key builde
 *  data prepare for the key's  builder
 */
class CreateOneEvntMessage {

    private static final CommonSchrackData SCHRACKDATA = new CommonSchrackData();
    private StringBuilder msg; // a message line build
    private String eventMsg; // a returning value to main program
    private int deviceType = 30; // for text message key

//A method handle the RAW message file, then  it will have translated 
    void OneMessageWriter(int messageNumber, String deviceAddress, int seconetAdr, String infoType,
            int elementType, int functionType, int subType, int stateDev, int subStateDev, String evntDate,
            String userSchrack, int command, int cmdZoneStartAddr, int cmdDeviceStartAddr, int cmdZoneEndAddr,
            int cmdDeviceEndAddr, int addInfo, int transmitter, Boolean outputSets[]) {
        msg = new StringBuilder();
//Date-Time prepare   
        msg.append(evntDate.replaceAll(".000", "; "));

//A global chose of message type
        switch (infoType.trim().toLowerCase())// an element name(message type) in the evnt. file
        {
            case "elementstate":
                infoType = "сообщение устройства";
                break;
            case "_command_":
                infoType = "команда системы";
                break;
            case "time":
                infoType = "установка времени";
                break;
            case "outputcontrol":
                infoType = "контр. выхода";
                break;
            case "bindingresult":
                infoType = "результат адресации";
                break;
            case "globalevent":
                infoType = "глобальное событие";
                break;
            case "statequery":
                infoType = "осуществляется запрос";
                break;
            case "statequeryend":
                infoType = "окончание запроса";
                break;
            case "acknowledge":
                infoType = "подтвреждение";
                break;
            case "printlist":
                infoType = "печать списка";
                break;
            case "analogvalue":
                infoType = "аналоговое значение";
                break;
            case "command":
                infoType = "команда";
                break;
            case "informationquery":
                infoType = "информационный запрос";
                break;
            case "information":
                infoType = "информация";
                break;
            case "externaldetector":
                infoType = "внешний датчик";
                break;
            case "maintenancemessage":
                infoType = "сообщение ТО";
                break;
            case "outputfreeze":
                infoType = "блокировка выходов";
                break;
            case "networkevent":
                infoType = "сетевое событие";
                break;
            case "statequerystart":
                infoType = "старт запроса";
                break;
            case "manufacturertest":
                infoType = "тест производителя";
                break;
            case "operatinghours":
                infoType = "рабоч. часы";
                break;
            case "eepdata":
                infoType = "данные ЭСППЗУ";
                break;
            case "textinfo":
                infoType = "текстовое сообщ.";
                break;

            default:
                infoType = "сообщение неизвестно";
        }

        if (!outputSets[1]) {
            msg.append(infoType);
            msg.append("; ");
        }

        if (!outputSets[0]) {

            if (seconetAdr > 65534) {

            } else if (seconetAdr == 65534) {

                msg.append("локальная станция");
                msg.append("; ");

            } else {

                msg.append("узел secoNet ");
                msg.append(seconetAdr);
                msg.append("; ");

            }
        }

        if ("сообщение устройства".equals(infoType)) {

            if (!outputSets[2]) {

                msg.append("адрес устройства ");

            }

            msg.append(deviceAddress);
            msg.append("; ");

// Device type handles
            deviceType = elementType;
            msg.append(SCHRACKDATA.getElementType(elementType));
            msg.append("; ");

// A subtypes set
            if (!outputSets[3]) {

                String tempDevType = SCHRACKDATA.getSubType(elementType, subType);

                if (tempDevType != null) {

                    msg.append(tempDevType);
                    msg.append("; ");

                } else {

                    msg.append("; ");
                }
            }

            if (!outputSets[4]) {

                String funcType = SCHRACKDATA.getFunctionType(functionType);

                msg.append(funcType);
                msg.append("; ");
            }

            if ((stateDev < 0) || (stateDev > 28)) {

                msg.append("неизв. сост-е устр-ва; ");

            } else {

                msg.append(SCHRACKDATA.getState(stateDev));
                msg.append("; ");
            }

            if (!outputSets[5]) { // the device state with substate set

                String tempDevState = SCHRACKDATA.getDevState(stateDev, subStateDev);

                if (tempDevState != null) {

                    msg.append(tempDevState);
                    msg.append("; ");

                } else {

                    msg.append("; ");
                }
            }

//addititional information is adding of the device state
            String strAddInfo = SCHRACKDATA.getAddInfo(addInfo);

            if (strAddInfo != null) {

                msg.append(strAddInfo);
                msg.append("; ");

            } else {

                msg.append("; ");
            }

        } else if ("команда".equals(infoType)) {

            if (userSchrack == null) {

            } else if ("".equals(userSchrack)) {

                msg.append("команда отправлена без ввода пароля; ");

            } else {

                msg.append("пользователь: ");
                msg.append(userSchrack);
                msg.append("; ");
            }

//the command type
            if ((command < 0) || (command > 40)) {

                msg.append("неизвестная команда; ");

            } else {

                msg.append(SCHRACKDATA.getCommand(command));
                msg.append("; ");
            }

//zone address(adr.ranges) handle & build     
            if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr < 65280) && (cmdDeviceStartAddr != 65535)
                    && (cmdZoneStartAddr < 255) && (cmdZoneEndAddr < 255) && (cmdZoneStartAddr > 0)
                    && (cmdZoneEndAddr > 0)) {
                msg.append("адреса : ").append(cmdZoneStartAddr).append("/").append(cmdDeviceStartAddr).append("-")
                        .append(cmdZoneEndAddr).append("/").append(cmdDeviceEndAddr).append("; ");
            } else if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr < 65280) && (cmdDeviceStartAddr != 65535)
                    && (cmdZoneStartAddr < 255) && (cmdZoneEndAddr == 255) && (cmdZoneStartAddr > 0)) {
                msg.append("адреса : ").append(cmdZoneStartAddr).append("/").append(cmdDeviceStartAddr).append("-")
                        .append(cmdZoneStartAddr).append("/").append(cmdDeviceEndAddr).append("; ");
            } else if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr < 65280) && (cmdDeviceStartAddr != 65535)
                    && (cmdZoneEndAddr < 255) && (cmdZoneStartAddr == 255) && (cmdZoneEndAddr > 0)) {
                msg.append("адреса : ").append(cmdZoneEndAddr).append("/").append(cmdDeviceStartAddr).append("-")
                        .append(cmdZoneEndAddr).append("/").append(cmdDeviceEndAddr).append("; ");
            } else if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr < 65280) && (cmdDeviceStartAddr != 65535)
                    && (cmdZoneEndAddr == 255) && (cmdZoneStartAddr == 255)) {
                msg.append("адреса : ").append(cmdDeviceStartAddr).append("-").append(cmdDeviceEndAddr).append("; ");
            } else if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr == 65280) && (cmdDeviceStartAddr < 65535)
                    && (cmdZoneEndAddr == 255) && (cmdZoneStartAddr == 255)) {
                msg.append("адрес : ").append(cmdDeviceStartAddr).append("; ");
            } else if ((cmdDeviceStartAddr > 0) && (cmdDeviceEndAddr == 65280) && (cmdDeviceStartAddr < 65535)
                    && (cmdZoneEndAddr == 255) && (cmdZoneStartAddr < 255)) {
                msg.append("адрес : ").append(cmdZoneStartAddr).append("/").append(cmdDeviceStartAddr).append("; ");
            } else {
            }

// A type of command transmitter  
            if (transmitter == 11) {

                msg.append("команда отправлена станцией для ПЦН; ");

            } else if ((transmitter >= 0) && (transmitter < 11)) {

                msg.append("команда отправлена c устройства:;");
                msg.append(SCHRACKDATA.getElementType(transmitter));
                msg.append("; ");

            } else if ((transmitter > 11) && (transmitter < 30)) {

                msg.append("команда отправлена c устройства:;");
                msg.append(SCHRACKDATA.getElementType(transmitter));
                msg.append("; ");

            } else {

                msg.append("команда отправлена c неизвестного устройства; ");
            }
        }

        /*
		 * if(messageNumber>65534) {
		 * 
		 * msg.append("недействительный номер сообщения; ");
		 * 
		 * } else {
		 * 
		 * msg.append("номер записи "); msg.append(messageNumber); msg.append("; "); }
         */
        if (outputSets[1] && ("команда".equals(infoType))) {

            eventMsg = null;

        } else {

            eventMsg = msg.toString();
        }
    }

    String getEvntMessage() {
        return eventMsg;
    }

    int getDeviceType() {
        return deviceType;
    }

}
