package EvntFileReader;

class TxtOfAddressBuild {

    static final private String[] ELEMENTYPES = {"датчик", // 0
        "вход", // 1
        "выход", // 2
        "внешний", // 3
        "принтер", // 4
        "пульт управления", // 5
        "аккумуляторы", // 6
        "сеть IntegralLAN", // 7
        "активная часть модуля", // 8
        "пассивная часть модуля", // 9
        "задержка тревоги", // 10
        "панель пожарной бригады", // 11
        "станция ПС Schrack", // 12
        "", // 13
        "индикатор", // 13
        "соединение LAN", // 14
        "система управления", // 15
        "шлейф", // 16
        "удаленный доступ", // 17
        "тех.обслуживание", // 18
        "направление пожаротушения", // 19
        "мастер станция", // 20
        "узел сети Seconet", // 21
        "сеть Seconet", // 22
        "контролируемый выход", // 23
        "внешний принтер", // 24
        "зона оповещения", // 25
        "внешняя система", // 26
        "панель", // 27
        "источник питания" // 28
};

    private int zoneAddress, deviceAddress, devID;
    private int[] adrAr = {0, 0, 30};
    private String TXTofAdress = "нет текста для устройства";

    private void TxtOfaddressBuild() {

        if ((65535 > deviceAddress) && (deviceAddress > 0)) {
            adrAr[1] = deviceAddress;
        } else {
            adrAr[1] = 0;
        }
//group address build      
        if ((0 < zoneAddress) && (zoneAddress < 255)) {
            adrAr[0] = zoneAddress;
        } else {
            adrAr[0] = 0;
        }
        if ((devID > 29) && (devID < 0)) {
            TXTofAdress = "недопустимый тип устройства";
        } else {
            adrAr[2] = devID;
        }

    }

    void setTXT0fAddress(int groupNumber, int elementNumber, int devType, String AddrText) {
        zoneAddress = groupNumber;
        deviceAddress = elementNumber;
        devID = devType;
        TXTofAdress = AddrText;

        TxtOfaddressBuild();
    }

    int[] getTXT0fAddress() {

        return adrAr;

    }

    String getTXT0fAddressLine() {

        return TXTofAdress;

    }

    String getFULLAddressLine() {// testing output

        if (deviceAddress == 0) {

            return "у устройства нет адреса";

        } else if (zoneAddress == 0) {

            return "тип устройства: " + ELEMENTYPES[devID] + ", адрес устройства: " + deviceAddress + ", "
                    + TXTofAdress;

        } else if ("недопустимый тип устройства".equals(TXTofAdress)) {

            return "тип устройства: " + deviceAddress + "/" + zoneAddress + ", " + "не существует";

        } else {
            
            return "тип устройства: " + ELEMENTYPES[devID] + 
                    ", адрес устройства: " + deviceAddress + ", гр. адрес: "
                    + zoneAddress + ", " + TXTofAdress;
        }

    }
}
