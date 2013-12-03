package org.fruct.yar.bluetoothconnectivitydemo;

import javax.microedition.midlet.MIDlet;


import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.events.ActionEvent;

/**
 * Bluetooth connectivity demo.
 */
public class BluetoothConnectivityDemo extends MIDlet  {

    /** MIDlet object. */
    private static BluetoothConnectivityDemo midlet;

    /** Main form. */
    private MainForm mainForm;

    /** AND blood pressure monitore. */
    private AnDBloodPressureMonitor bpMonitor;

    /** Pulse oximereter client. */
    private PulseOximeterClient poClient;

    /**
     * Create commands.
     */
    private void createCommands() {
        Command exitCommand = new Command("Exit") {
            public void actionPerformed(ActionEvent event) {
                destroyApp(true);
                notifyDestroyed();
            }
        };
        mainForm.addCommand(exitCommand);
        mainForm.setBackCommand(exitCommand);

        Command startBPCommand = new Command("Receive blood pressure") {
            public void actionPerformed(ActionEvent event) {
                if (poClient != null) poClient.abortOperation();
                if (bpMonitor != null) bpMonitor.closeServer();
                bpMonitor = new AnDBloodPressureMonitor(midlet);
                bpMonitor.start();
            }
        };
        mainForm.addCommand(startBPCommand);

        Command startPOCommand = new Command("Receive heart rate/oxygenation") {
            public void actionPerformed(ActionEvent event) {
                if (poClient != null) poClient.abortOperation();
                if (bpMonitor != null) bpMonitor.closeServer();
                poClient = new PulseOximeterClient(midlet);
                poClient.start();
            }
        };
        mainForm.addCommand(startPOCommand);
    }

    /**
     * Override startApp method.
     */
    protected void startApp() {
        Display.init(this);
        midlet = this;
        mainForm = new MainForm(getAppProperty("MIDlet-Name"));
        createCommands();
        mainForm.show();
    }

    /**
     * Get main form.
     * @return Main form
     */
    public MainForm getMainForm() {
        return mainForm;
    }

    /**
     * Override destroyApp method.
     * @param unconditional If false the MIDlet may throw MIDletStateChangeException
     *  to indicate it does not want to be destroyed at this time
     */
    protected void destroyApp(boolean unconditional) {
    }

    /**
     * Override pauseApp method.
     */
    protected void pauseApp() {
    }

    /** Get MIDlet object.
     * @return MIDlet object.
     */
    public static BluetoothConnectivityDemo getMIDletObject() {
        return BluetoothConnectivityDemo.midlet;
    }
}
