package org.fruct.yar.bluetoothconnectivitydemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;


/**
* Server operating with UA-767BT
*/
public class AnDBloodPressureMonitor extends Thread
{
    /** MIDlet object.  */
    private BluetoothConnectivityDemo midlet;

    /** AND data packet length. */
    private static final int DATA_PACKET_LENGTH = 70;

    /** SPP service URL. */
    private static final String serviceURL
        = "btspp://localhost:3B9FA89520078C303355AAA694238F08;name=PWAccessP;encrypt=false";

    /** This interface defines the capabilities that a connection notifier must have. */
    private StreamConnectionNotifier server = null;

    /**
     * Constructor.
     * @param app Midlet which starts this server
     */
    public AnDBloodPressureMonitor(BluetoothConnectivityDemo app) {
        midlet = app;
    }

    /**
     * The thread entry point.
     */
    public void run() {
        LocalDevice localDevice = null;
        try {
            localDevice = LocalDevice.getLocalDevice();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (localDevice == null) return;

        try {
            localDevice.setDiscoverable(DiscoveryAgent.GIAC);
            server = (StreamConnectionNotifier) Connector.open(serviceURL,
                    Connector.READ_WRITE, false);
            while (true) {
                StreamConnection conn = server.acceptAndOpen();
                if (conn != null) {
                    receiveData(conn);
                    conn.close();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Close server.
     */
    public void closeServer() {
        if (server != null) {
            try {
                server.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Communicate with a remote device.
     * @param connection Stream of connection
     */
    public void receiveData(StreamConnection connection) {
        try {
            // Receive a data packet
            byte buffer[] = new byte[DATA_PACKET_LENGTH];
            byte data[] = new byte[DATA_PACKET_LENGTH];
            InputStream in = connection.openInputStream();
            OutputStream out = connection.openOutputStream();
            int received = 0;
            while (received < DATA_PACKET_LENGTH) {
                int length = in.read(buffer);
                if (length == -1) break;
                for (int i = 0; i < length; i++) {
                    data[received + i] = buffer[i];
                }
                received += length;
            }

            // Send the information from the package to the UI
            if(midlet != null) {
                AnDPackageParser andPackageParser = new AnDPackageParser();
                andPackageParser.processPacket(data);
                midlet.getMainForm().sendBPResult(andPackageParser.getSystolic(),
                        andPackageParser.getDiastolic(), andPackageParser.getPulseRate());
            }

            // Send an acknowledgement to the device
            sleep(350);
            byte cmd[] = new byte[4];
            cmd[0] = 'P';
            cmd[1] = 'W';
            cmd[2] = 'A';
            cmd[3] = '1';
            out.write(cmd);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}
