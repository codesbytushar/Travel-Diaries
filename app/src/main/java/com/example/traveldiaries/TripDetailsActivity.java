package com.example.traveldiaries;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.traveldiaries.adapter.ImageSliderAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle("TravelDiaries");
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView title = findViewById(R.id.tripTitleText);
        TextView desc = findViewById(R.id.tripDescText);
        ViewPager2 slider = findViewById(R.id.imageSlider);

        title.setText(getIntent().getStringExtra("title"));
        desc.setText(getIntent().getStringExtra("description"));

        ArrayList<String> images =
                getIntent().getStringArrayListExtra("images");

        slider.setAdapter(new ImageSliderAdapter(images));
    }
}
