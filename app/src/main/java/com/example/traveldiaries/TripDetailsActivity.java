package com.example.traveldiaries;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.traveldiaries.adapter.ImageSliderAdapter;

import java.util.ArrayList;
import com.google.android.material.appbar.MaterialToolbar;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        ViewPager2 imageSlider = findViewById(R.id.imageSlider);
        TextView titleText = findViewById(R.id.tripTitleText);
        TextView descText = findViewById(R.id.tripDescText);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        ArrayList<String> imageStrings =
                getIntent().getStringArrayListExtra("images");

        ArrayList<Uri> imageUris = new ArrayList<>();
        if (imageStrings != null) {
            for (String s : imageStrings) {
                imageUris.add(Uri.parse(s));
            }
        }

        titleText.setText(title);
        descText.setText(description);

        ImageSliderAdapter adapter = new ImageSliderAdapter(imageUris);
        imageSlider.setAdapter(adapter);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

    }
}
