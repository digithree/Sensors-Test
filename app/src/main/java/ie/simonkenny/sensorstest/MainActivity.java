package ie.simonkenny.sensorstest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuItems = new ArrayList<>();
        menuItems.add(getString(R.string.activity_title_step_counter));
        menuItems.add(getString(R.string.activity_title_significant_motion_sensor));
        menuItems.add(getString(R.string.activity_title_random_radar));
        menuItems.add(getString(R.string.activity_title_light_sensor));
        menuItems.add(getString(R.string.activity_title_accelerometer));

        ListView listView = (ListView) findViewById(R.id.menu_list_view);
        if (listView != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        Intent intent = new Intent(getApplicationContext(), StepCounterActivity.class);
                        startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(getApplicationContext(), SignificantMotionActivity.class);
                        startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent(getApplicationContext(), RandomRadarActivity.class);
                        startActivity(intent);
                    } else if (position == 3) {
                        Intent intent = new Intent(getApplicationContext(), LightSensorActivity.class);
                        startActivity(intent);
                    } else if (position == 4) {
                        Intent intent = new Intent(getApplicationContext(), LinearAccelerometerActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }


}
