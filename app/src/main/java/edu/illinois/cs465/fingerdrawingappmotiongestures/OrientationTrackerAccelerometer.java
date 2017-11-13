package edu.illinois.cs465.fingerdrawingappmotiongestures;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


public class OrientationTrackerAccelerometer implements SensorEventListener {
    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mAccelSensor;
    private int yaw, pitch, roll;


    public OrientationTrackerAccelerometer(Context context) {
        mContext = context;
        mSensorManager = null;
        mAccelSensor = null;
        yaw = 0;
        pitch = 0;
        roll = 0;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    protected void registerSensorListeners() {
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    protected void unRegisterSensorListeners() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        double Ax = event.values[0];
        double Ay = event.values[1];
        double Az = event.values[2];
        double Ax2 = Ax * Ax;
        double Ay2 = Ay * Ay;
        double Az2 = Az * Az;

        // Implements equation from course notes. Also converts to degrees and rounds
        pitch = Math.round((int)Math.toDegrees(Math.atan2(Ay, Math.sqrt(Ax2 + Az2))));

        // Will swap the sign of roll to match the android framework
        roll = -Math.round((int)Math.toDegrees(Math.atan2(Ax, Az)));

        yaw = Math.round((int)Math.toDegrees(Math.atan2(Ay, Ax)));

    }

    public int getYaw() {
        return yaw;
    }

    public int getPitch() {
        return pitch;
    }

    public int getRoll() {
        return roll;
    }



    // Must implement for interface but not needed
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

}
