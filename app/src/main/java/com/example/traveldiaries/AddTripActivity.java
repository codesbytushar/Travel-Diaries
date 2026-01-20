package com.example.traveldiaries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveldiaries.adapter.SelectedImageAdapter;

import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity {

    EditText titleEdt, descEdt;
    Button selectPhotosBtn, saveTripBtn;
    RecyclerView selectedImagesRecycler;

    ArrayList<Uri> selectedImages = new ArrayList<>();
    SelectedImageAdapter imageAdapter;

    // ✅ CORRECT TYPE
    ActivityResultLauncher<PickVisualMediaRequest> pickImagesLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        titleEdt = findViewById(R.id.tripTitle);
        descEdt = findViewById(R.id.tripDesc);
        selectPhotosBtn = findViewById(R.id.selectPhotosBtn);
        saveTripBtn = findViewById(R.id.saveTripBtn);
        selectedImagesRecycler = findViewById(R.id.selectedImagesRecycler);

        // RecyclerView setup
        imageAdapter = new SelectedImageAdapter(selectedImages);
        selectedImagesRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        selectedImagesRecycler.setAdapter(imageAdapter);

        // ✅ REGISTER PHOTO PICKER (CORRECT)
        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(),
                uris -> {
                    if (uris != null && !uris.isEmpty()) {
                        selectedImages.addAll(uris);
                        imageAdapter.notifyDataSetChanged();
                    }
                }
        );

        // 📸 Open image picker
        selectPhotosBtn.setOnClickListener(v -> {
            pickImagesLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
        });

        // 💾 Save trip
        saveTripBtn.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("title", titleEdt.getText().toString().trim());
            result.putExtra("description", descEdt.getText().toString().trim());

            ArrayList<String> imageStrings = new ArrayList<>();
            for (Uri uri : selectedImages) {
                imageStrings.add(uri.toString());
            }

            result.putStringArrayListExtra("images", imageStrings);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
