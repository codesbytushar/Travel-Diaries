package com.example.traveldiaries;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveldiaries.adapter.TripAdapter;
import com.example.traveldiaries.model.Trip;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addTripFab;
    ArrayList<Trip> tripList = new ArrayList<>();
    TripAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.tripsRecyclerView);
        addTripFab = findViewById(R.id.addTripFab);

        loadTripsFromFile();

        adapter = new TripAdapter(this, tripList, position -> deleteTrip(position));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        addTripFab.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, AddTripActivity.class), 1
                )
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            String title = data.getStringExtra("title");
            String desc = data.getStringExtra("description");
            ArrayList<String> images = data.getStringArrayListExtra("images");

            tripList.add(new Trip(title, desc, images));
            adapter.notifyItemInserted(tripList.size() - 1);
            saveTripsToFile();
        }
    }

    private void saveTripsToFile() {
        try {
            JSONArray array = new JSONArray();
            for (Trip t : tripList) {
                JSONObject obj = new JSONObject();
                obj.put("title", t.getTitle());
                obj.put("description", t.getDescription());
                obj.put("images", new JSONArray(t.getImagePaths()));
                array.put(obj);
            }

            FileOutputStream fos = openFileOutput("trips.json", MODE_PRIVATE);
            fos.write(array.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTripsFromFile() {
        try {
            File file = new File(getFilesDir(), "trips.json");
            if (!file.exists()) return;

            FileInputStream fis = openFileInput("trips.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            JSONArray array = new JSONArray(new String(data));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                ArrayList<String> imgs = new ArrayList<>();
                JSONArray imgArr = obj.getJSONArray("images");
                for (int j = 0; j < imgArr.length(); j++) {
                    imgs.add(imgArr.getString(j));
                }

                tripList.add(
                        new Trip(
                                obj.getString("title"),
                                obj.getString("description"),
                                imgs
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteTrip(int position) {
        try {
            Trip trip = tripList.get(position);

            for (String path : trip.getImagePaths()) {
                File file = new File(path);
                if (file.exists()) file.delete();
            }

            tripList.remove(position);
            adapter.notifyItemRemoved(position);

            saveTripsToFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
