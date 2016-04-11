package ie.simonkenny.sensorstest;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import icepick.Icepick;
import icepick.State;

public class SignificantMotionActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TriggerEventListener mTriggerEventListener;

    @State
    ArrayList<String> motionEvents = new ArrayList<>();

    ArrayAdapter<String> mAdapter;

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_significant_motion);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        if (mSensor == null) {
            Toast.makeText(getApplicationContext(), "Significant Motion sensor not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Toast.makeText(getApplicationContext(), "Jelly Bean or newer OS required", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mTriggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                motionEvents.add(String.format("Event at %s", SimpleDateFormat.getDateTimeInstance().format(new Date())));
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
                mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);
            }
        };

        mSensorManager.requestTriggerSensor(mTriggerEventListener, mSensor);

        ListView listView = (ListView) findViewById(R.id.significant_motion_list_view);
        if (listView != null) {
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, motionEvents);
            listView.setAdapter(mAdapter);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.cancelTriggerSensor(mTriggerEventListener, mSensor);
    }
}
