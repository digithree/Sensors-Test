package ie.simonkenny.sensorstest;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import icepick.Icepick;
import icepick.State;

public class LinearAccelerometerActivity extends AppCompatActivity {

    private static final int NUM_READINGS = 100;
    private static final int READING_DELAY_MS = 200;

    private LineChart mChart;

    @State
    ArrayList<Entry> values = new ArrayList<>();
    ArrayList<String> readingNames = new ArrayList<>();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    private Handler mTimerHandler;
    private Runnable mTimerEvent;

    @State
    float mAccelReading = 0.f;
    @State
    int mNextEntryPosition = -1;

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_linear_accelerometer);

        mChart = (LineChart) findViewById(R.id.accelerometer_line_chart);

        for (int i = 0 ; i < NUM_READINGS ; i++) {
            readingNames.add(String.format("%d", (i+1)));
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == mSensor) {
                    mAccelReading = event.values[0];
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // nothing
            }
        };

        if (mSensor == null) {
            Toast.makeText(getApplicationContext(), "Light sensor not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // register the Sensor listener
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
        // create the timer
        mTimerHandler = new Handler();
        mTimerEvent = new Runnable() {
            @Override
            public void run() {
                updateValues();
                updateChart();
                mTimerHandler.postDelayed(this, READING_DELAY_MS);
            }
        };
        mTimerHandler.post(mTimerEvent);
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop the timer
        mTimerHandler.removeCallbacks(mTimerEvent);
        // unregister the Sensor listener
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private void updateValues() {
        mNextEntryPosition++;
        if (mNextEntryPosition >= readingNames.size()) {
            mNextEntryPosition = mNextEntryPosition - 1;
            for (int i = 1 ; i < values.size() ; i++) {
                values.get(i).setXIndex(values.get(i).getXIndex() - 1);
            }
            values.remove(0);
        }
        values.add(new Entry(mAccelReading, mNextEntryPosition));
    }

    private void updateChart() {
        LineDataSet lineDataSet = new LineDataSet(values, "Acceleration in m/s^2");
        lineDataSet.setDrawCubic(true);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1.8f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        lineDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        lineDataSet.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });

        LineData data = new LineData(readingNames, lineDataSet);
        data.setDrawValues(false);

        mChart.setData(data);

        mChart.invalidate();
    }
}
