package EvntFileReader;

/*
 * Schrack group address build, any representation of the address:
 * [gr.nr.]/[device nr.] , 
 * GroupAddressBuild[]{[gr.nr],[device nr.]}
 * Gr. adress will build only for detector, its which have included to the Group
 */

class GroupAddressBuild {

    private int zoneAddress, deviceAddress;
    private final int[] adrAr = new int[2];
    private int grNr;
    private int devNr;

    private void addressBuild() {

        if ((65535 > devNr) && (devNr > 0)) {
            deviceAddress = devNr;
        } else {
            deviceAddress = 0;
        }
        // group address build
        if ((0 < grNr) && (grNr < 255)) {
            zoneAddress = grNr;
        } else {
            zoneAddress = 0;
        }

    }

    public void setAddress(int groupNumber, int elementNumber) {
        grNr = groupNumber;
        devNr = elementNumber;
        addressBuild();
    }

    public int[] getAddress() {

        if (deviceAddress == 0) {
            return adrAr; // Only zeros return
        } else {
            adrAr[0] = zoneAddress;
            adrAr[1] = deviceAddress;
            return adrAr;
        }
    }

    public String getAddressLine() {

        if (deviceAddress == 0) {
            return "- нет адреса";
        } else if (zoneAddress == 0) {
            return String.valueOf(deviceAddress);
        } else {
            return String.valueOf(deviceAddress) + "/" + String.valueOf(zoneAddress);
        }
    }
}
