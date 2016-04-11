package ie.simonkenny.sensorstest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import java.util.ArrayList;

public class RandomRadarActivity extends AppCompatActivity {

    private RadarChart mChart;
    ArrayList<String> sensorNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_radar);

        setupChart();

        updateChartWithValues();

        Button button = (Button) findViewById(R.id.update_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateChartWithValues();
                }
            });
        }
    }

    private void setupChart() {
        mChart = (RadarChart) findViewById(R.id.chart1);

        if (mChart == null) {
            return;
        }
        YAxis yAxis = mChart.getYAxis();
        yAxis.setAxisMinValue(0.f);
        yAxis.setAxisMaxValue(10.f);

        sensorNames = new ArrayList<>();
        sensorNames.add("Sensor 1");
        sensorNames.add("Sensor 2");
        sensorNames.add("Sensor 3");
        sensorNames.add("Sensor 4");
        sensorNames.add("Sensor 5");
        sensorNames.add("Sensor 6");
    }

    private void updateChartWithValues() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0 ; i < 6 ; i++) {
            values.add(new Entry(2.f + ((float) Math.random() * 8.f), 0));
        }

        RadarDataSet radarDataSet = new RadarDataSet(values, "Values");

        RadarData data = new RadarData(sensorNames, radarDataSet);

        mChart.setData(data);

        mChart.invalidate();
    }
}
