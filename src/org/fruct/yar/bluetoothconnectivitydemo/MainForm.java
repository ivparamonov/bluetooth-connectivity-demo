package org.fruct.yar.bluetoothconnectivitydemo;

import com.nokia.lwuit.components.FormItem;
import com.sun.lwuit.Form;

/** Main form. */
public class MainForm extends Form {

    /**
     * Constructor.
     * @param title The Form's title
     */
    public MainForm(String title) {
        super(title);
    }

    /**
     * Add message.
     * @param msg Message
     */
    public void addMessage(String msg) {
        FormItem item = new FormItem(msg, false);
        addComponent(item);
    }

    /**
     * Add blood pressure results
     * @param systolic Systolic value
     * @param diastolic Diastolic value
     * @param heartRate Heart rate value
     */
    public void sendBPResult(int systolic, int diastolic, int heartRate) {
        String result = "SYS: " + systolic + ";  DIA: "
                + diastolic + ";  HR: " + heartRate;
        FormItem item = new FormItem(result, false);
        addComponent(item);
    }

    /**
     * Add pulse oximeter results
     * @param heartRate Heart rate value
     * @param oxygenation Blood oxygenation
     */
    public void sendPOResult(int heartRate, int oxygenation) {
        String result = "HR: " + heartRate + ";  SPO2: " + oxygenation;
        FormItem item = new FormItem(result, false);
        addComponent(item);
    }
}
