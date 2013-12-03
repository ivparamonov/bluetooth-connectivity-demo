package org.fruct.yar.bluetoothconnectivitydemo;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;



/**
* Client to work with Nonin 4100 Oximeter
*/
public class PulseOximeterClient extends Thread
{
    /** Oximeret MAC. */
    private static final String OximeterMAC = "00A0960E620B";

    /** MIDlet object.  */
    private static BluetoothConnectivityDemo midlet;

    /** Flag to abort the client's operation. */
    private boolean abortOperation = false;

    /**
     * Constructor.
     * @param app Midlet which starts this server
     */
    public PulseOximeterClient(BluetoothConnectivityDemo app) {
        midlet = app;
    }

    /**
     * Override run method.
     */
    public void run() {
        try {
            StreamConnection conn = (StreamConnection) Connector.open("btspp://" + OximeterMAC + ":1");
            if(conn != null) {
                receiveData(conn);
                conn.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Abort the client's operation.
     */
    public void abortOperation() {
        abortOperation = true;
    }

    /**
     * Communicate with a remote device.
     * @param connection Stream of connection
     */
    public void receiveData(StreamConnection connection) {
        try {
            // Set transmission mode
            OutputStream out = connection.openOutputStream();
            byte cmd[] = new byte[2];
            cmd[0] = 'D';
            cmd[1] = '1';
            out.write(cmd);
            out.flush();

            // Read the data in a loop
            InputStream in = connection.openInputStream();
            byte data[] = new byte[3];
            int received = 0;
            in.read();
            while (!abortOperation) {
                int value = in.read();
                if(value == -1) break;
                data[received++] = (byte) value;
                if(received == 3) {
                    int HR = data[1] & 0x7F + (data[0] & 0x03) * 0x80;
                    int SPO2 = data[2] & 0x7F;
                    if(midlet != null && (HR != 0x1FF)) {
                        midlet.getMainForm().sendPOResult(HR, SPO2);
                    }
                    received = 0;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
