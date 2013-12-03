package org.fruct.yar.bluetoothconnectivitydemo;

/**
 * Class used to get information about blood pressure.
 */
public class AnDPackageParser {

    /** Buffer for header. **/
    private byte[] dataBuffer;

    /** Buffer for data. **/
    private byte[] headerBuffer;

    /** Pulse rate. **/
    private int pulseRate;

    /** Mean arterial pressure. **/
    private int meanArterialPressure;

    /** Systolic pressure. **/

    private int systolic;
    /** Diastolic pressure. **/

    private int diastolic;

    /**
     * Receive byte array with data.
     * @param data byte array
     */
    public void processPacket(byte[] data) {
        headerBuffer = new byte[60];
        System.arraycopy(data, 0, headerBuffer, 0, 60);
        dataBuffer = new byte[10];
        System.arraycopy(data, 60, dataBuffer, 0, 10);
        extractData();
    }

    /**
     * Get systolic value.
     * @return systolic value
     */
    public int getSystolic() {
        return systolic;
    }

    /**
     * Get diastolic value.
     * @return diastolic value
     */
    public int getDiastolic() {
        return diastolic;
    }

    /**
     * Get pulse rate.
     * @return pulse rate
     */
    public int getPulseRate() {
        return pulseRate;
    }

    /**
     * Mean arterial pressure.
     * @return value mean arterial pressure.
     */
    public int getMAP() {
        return meanArterialPressure;
    }

    /**
     * Extract all information from received packet.
     */
    private void extractData() {
        diastolic = extractBloodPressureData(4, 5);
        systolic = diastolic + extractBloodPressureData(2, 3);
        pulseRate = extractBloodPressureData(6, 7);
        meanArterialPressure = extractBloodPressureData(8, 9);
    }

    /**
     * Extract concrete information blood pressure data section.
     * @param index1 first byte index
     * @param index2 second byte index
     * @return value of blood pressure parameter
     */
    private int extractBloodPressureData(int index1, int index2) {
        char[] charBuff = {(char) dataBuffer[index1], (char) dataBuffer[index2]};
        return Integer.parseInt(String.valueOf(charBuff), 16);
    }
}
