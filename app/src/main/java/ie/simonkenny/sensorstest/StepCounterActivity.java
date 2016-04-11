package ie.simonkenny.sensorstest;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import icepick.Icepick;
import icepick.State;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    TextView mNumStepsTextView;
    TextView mInitialNumStepsTextView;
    int mNumSteps = 0;
    @State
    int mInitialNumSteps = 0;

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_step_counter);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Toast.makeText(getApplicationContext(), "KitKat or newer OS required", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mSensor == null) {
            Toast.makeText(getApplicationContext(), "Step Counter sensor not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mNumStepsTextView = (TextView) findViewById(R.id.num_steps_text_view);
        mInitialNumStepsTextView = (TextView) findViewById(R.id.initial_num_steps_text_view);
        updateTextView();
    }

    private void updateTextView() {
        if (mNumStepsTextView != null) {
            mNumStepsTextView.setText(String.format("Steps: %s", (mNumSteps - mInitialNumSteps)));
        }
        if (mInitialNumStepsTextView != null) {
            mInitialNumStepsTextView.setText(String.format("Initial: %s", mInitialNumSteps));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mNumSteps = (int) event.values[0];
        if (mInitialNumSteps == 0) {
            mInitialNumSteps = mNumSteps;
        }
        updateTextView();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing needs to be done here
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
