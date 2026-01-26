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

    private RecyclerView recyclerView;
    private FloatingActionButton addTripFab;

    private final ArrayList<Trip> tripList = new ArrayList<>();
    private TripAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // Views
        recyclerView = findViewById(R.id.tripsRecyclerView);
        addTripFab = findViewById(R.id.addTripFab);

        // Load saved trips
        loadTripsFromFile();

        // RecyclerView + Adapter
        adapter = new TripAdapter(this, tripList, new TripAdapter.OnTripClickListener() {
            @Override
            public void onTripClick(Trip trip) {
                Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);
                intent.putExtra("title", trip.getTitle());
                intent.putExtra("description", trip.getDescription());
                intent.putStringArrayListExtra("images", trip.getImageUris());
                startActivity(intent);
            }

            @Override
            public void onTripDelete(int position) {
                deleteTrip(position);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Add new trip
        addTripFab.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, AddTripActivity.class),
                        1
                )
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            ArrayList<String> images = data.getStringArrayListExtra("images");

            tripList.add(new Trip(title, description, images));
            adapter.notifyItemInserted(tripList.size() - 1);

            saveTripsToFile();
        }
    }

    // 💾 Save trips to internal storage (JSON)
    private void saveTripsToFile() {
        try {
            JSONArray array = new JSONArray();

            for (Trip t : tripList) {
                JSONObject obj = new JSONObject();
                obj.put("title", t.getTitle());
                obj.put("description", t.getDescription());
                obj.put("images", new JSONArray(t.getImageUris()));
                array.put(obj);
            }

            FileOutputStream fos = openFileOutput("trips.json", MODE_PRIVATE);
            fos.write(array.toString().getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 📂 Load trips from internal storage
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

                ArrayList<String> images = new ArrayList<>();
                JSONArray imgArr = obj.getJSONArray("images");
                for (int j = 0; j < imgArr.length(); j++) {
                    images.add(imgArr.getString(j));
                }

                tripList.add(
                        new Trip(
                                obj.getString("title"),
                                obj.getString("description"),
                                images
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🗑️ Delete trip (SAFE for any position)
    private void deleteTrip(int position) {
        if (position < 0 || position >= tripList.size()) return;

        tripList.remove(position);
        adapter.notifyItemRemoved(position);

        saveTripsToFile();
    }
}
