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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity {

    EditText titleEdt, descEdt;
    Button selectPhotosBtn, saveTripBtn;
    RecyclerView selectedImagesRecycler;

    ArrayList<Uri> selectedImages = new ArrayList<>();
    SelectedImageAdapter imageAdapter;

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

        imageAdapter = new SelectedImageAdapter(selectedImages);
        selectedImagesRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        selectedImagesRecycler.setAdapter(imageAdapter);

        pickImagesLauncher = registerForActivityResult(
                new ActivityResultContracts.PickMultipleVisualMedia(),
                uris -> {
                    if (uris != null) {
                        selectedImages.addAll(uris);
                        imageAdapter.notifyDataSetChanged();
                    }
                }
        );

        selectPhotosBtn.setOnClickListener(v ->
                pickImagesLauncher.launch(
                        new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build()
                )
        );

        saveTripBtn.setOnClickListener(v -> {
            ArrayList<String> savedPaths = new ArrayList<>();

            for (Uri uri : selectedImages) {
                String path = saveImageToInternalStorage(uri);
                if (path != null) savedPaths.add(path);
            }

            Intent result = new Intent();
            result.putExtra("title", titleEdt.getText().toString());
            result.putExtra("description", descEdt.getText().toString());
            result.putStringArrayListExtra("images", savedPaths);

            setResult(RESULT_OK, result);
            finish();
        });
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File imagesDir = new File(getFilesDir(), "images");
            if (!imagesDir.exists()) imagesDir.mkdir();

            File imageFile = new File(imagesDir,
                    "trip_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            inputStream.close();
            return imageFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
