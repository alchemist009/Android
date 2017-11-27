/**
 * Helper class to detect any shakes in the device using the Accelerometer
 * This class is used when a user shakes the phone to get a hint to the current question
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * version: 2.0: 11/26/2017
 */

package classes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    //The gForce that is necessary to register as shake
    //Must be greater than 1G (one earth gravity unit)
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener listener;
    private long shakeTimestamp;
    private int shakeCount;

    /**
     * This method sets the on shake listener for this class
     *
     * @param listener
     */
    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    /**
     * This is the interface which has the method 'onShake'
     * The above method is necessary to handle the events/actions that occur when the device is shaken
     *
     */
    public interface OnShakeListener {
        public void onShake(int count);
    }

    /**
     * onAccuracyChanged method is used when when accuracy of the sensor (Accelerometer) changes
     *
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    /**
     * onSensorChanged method is used when when the sensor (Accelerometer) values change
     * The new x, y, z values are used to determine if there is a shake in the device
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (listener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();

                // ignore shake events too close to each other (500ms)
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0;
                }

                shakeTimestamp = now;
                shakeCount++;

                listener.onShake(shakeCount);
            }
        }
    }
}