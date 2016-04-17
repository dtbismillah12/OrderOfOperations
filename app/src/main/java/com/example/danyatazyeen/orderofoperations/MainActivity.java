package com.example.danyatazyeen.orderofoperations;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity implements SensorEventListener{

    //view of game
    MissionView gameView;
    private FrameLayout frameLayout;
    private BounceSurfaceView bounceSurfaceView;

    // http://developer.android.com/guide/topics/sensors/sensors_position.html (background on android sensors)
    // http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125 (background on accelerometer sensor)
    // http://stackoverflow.com/questions/6479637/android-accelerometer-moving-ball (kinematic equations for ball movement)
    // http://stackoverflow.com/questions/24595913/move-an-object-using-accelerometer (minus instead of plus for x direc.)
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        gameView = new MissionView(this, size.x, size.y);
        setContentView(R.layout.content_main);

        ImageView background = (ImageView) findViewById(R.id.spaceView);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        bounceSurfaceView = new BounceSurfaceView(this, null);
        frameLayout.addView(bounceSurfaceView);



        // Initialize the manager which allows access to all sensors
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Specify the accelerometer sensor from the sensor manager
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Register the listener to the accelerometer sensor
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method called when change in reading of the sensor
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xAccel = event.values[0];
            float yAccel = event.values[1];
            AnimationArena.updateVelo((int) xAccel, (int) yAccel);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

}


