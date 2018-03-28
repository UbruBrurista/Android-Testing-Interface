package com.example.zwoo18.newandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.things.pio.UartDevice;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.android.things.pio.PeripheralManager;
/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class HomeActivity extends Activity {

    private PeripheralManager mPeripheralManager;
    private UartDevice mArduino;
    private byte[] START_BYTE = {1, '\n'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPeripheralManager = PeripheralManager.getInstance();

        /* See available devices */
        /*List<String> deviceList = mPeripheralManager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            System.out.println("No device");
        } else {
            for (String s : deviceList) {
                System.out.println(s);
            }
        }*/


        try {
            mArduino = mPeripheralManager.openUartDevice("UART0");
            System.out.println("connected to " + mArduino.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("wrong name!!!");
        }
        //set baudrate to 9600, or whatever allowed value as set on Arduino
        try {
            mArduino.setBaudrate(9600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //submit repeatable operation of sending data

        findViewById(R.id.arduino_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    conversateWithArduino();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void conversateWithArduino() throws IOException {
        try {
            mArduino.write(START_BYTE, START_BYTE.length);
            System.out.println("writing to arduino: " + Arrays.toString(START_BYTE));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAILED to write....");
        }
    }
}