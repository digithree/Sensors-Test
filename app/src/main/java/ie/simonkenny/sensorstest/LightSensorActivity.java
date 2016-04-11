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

public class LightSensorActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    private LineChart mChart;

    ArrayList<String> timePeriods = new ArrayList<>();
    ArrayList<Entry> values = new ArrayList<>();

    int currentTimePeriod = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_light_sensor);

        mChart = (LineChart) findViewById(R.id.light_sensor_line_chart);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (mSensor == null) {
            Toast.makeText(getApplicationContext(), "Light sensor not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor == mSensor) {
                    // get sensor data
                    int light = (int) event.values[0];
                    // add new time period and data at that period
                    timePeriods.add("" + currentTimePeriod);
                    values.add(new Entry(light, currentTimePeriod));
                    // move to next time period, ready for next reading
                    currentTimePeriod++;
                    // update chart
                    updateChart();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // nothing
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        // register the Sensor listener
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister the Sensor listener
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private void updateChart() {
        LineDataSet lineDataSet = new LineDataSet(values, "Light Readings");
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.BLUE);

        LineData data = new LineData(timePeriods, lineDataSet);
        data.setDrawValues(false);

        mChart.setData(data);
        mChart.invalidate();
    }
}
