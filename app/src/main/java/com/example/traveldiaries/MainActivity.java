package com.example.traveldiaries;
import android.net.Uri;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveldiaries.adapter.TripAdapter;
import com.example.traveldiaries.model.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addTripFab;
    ArrayList<Trip> tripList;
    TripAdapter adapter;

    public static final int ADD_TRIP_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.tripsRecyclerView);
        addTripFab = findViewById(R.id.addTripFab);

        tripList = new ArrayList<>();
        adapter = new TripAdapter(this, tripList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        addTripFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            startActivityForResult(intent, ADD_TRIP_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TRIP_REQUEST && resultCode == RESULT_OK && data != null) {

            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");

            ArrayList<String> imageStrings =
                    data.getStringArrayListExtra("images");

            ArrayList<Uri> imageUris = new ArrayList<>();
            if (imageStrings != null) {
                for (String s : imageStrings) {
                    imageUris.add(Uri.parse(s));
                }
            }

            Trip trip = new Trip(title, description, imageUris);
            tripList.add(trip);
            adapter.notifyItemInserted(tripList.size() - 1);
        }
    }
}
