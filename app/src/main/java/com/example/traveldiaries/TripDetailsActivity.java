package com.example.traveldiaries;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        ViewPager2 imageSlider = findViewById(R.id.imageSlider);
        TextView titleText = findViewById(R.id.tripTitleText);
        TextView descText = findViewById(R.id.tripDescText);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        // 🔙 Safe back
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        // 🔐 SAFE intent data
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        ArrayList<String> imageStrings =
                getIntent().getStringArrayListExtra("images");

        titleText.setText(title != null ? title : "");
        descText.setText(description != null ? description : "");

        ArrayList<Uri> imageUris = new ArrayList<>();
        if (imageStrings != null) {
            for (String s : imageStrings) {
                if (s != null) {
                    imageUris.add(Uri.parse(s));
                }
            }
        }

        // 🚫 Prevent ViewPager crash
        if (imageUris.isEmpty()) {
            imageSlider.setVisibility(View.GONE);
        } else {
            imageSlider.setAdapter(new ImageSliderAdapter(imageUris));
        }
    }
}
