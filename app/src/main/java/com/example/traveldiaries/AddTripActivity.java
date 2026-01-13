package com.example.traveldiaries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddTripActivity extends AppCompatActivity {

    EditText titleEdt, descEdt;
    Button selectPhotosBtn, saveTripBtn;
    ArrayList<Uri> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        titleEdt = findViewById(R.id.tripTitle);
        descEdt = findViewById(R.id.tripDesc);
        selectPhotosBtn = findViewById(R.id.selectPhotosBtn);
        saveTripBtn = findViewById(R.id.saveTripBtn);

        // Open image picker
        selectPhotosBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            // REQUIRED FLAGS
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION |
                            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            );

            startActivityForResult(intent, 100);
        });

        // Save trip
        saveTripBtn.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.putExtra("title", titleEdt.getText().toString());
            intent.putExtra("description", descEdt.getText().toString());

            ArrayList<String> imageUris = new ArrayList<>();
            for (Uri uri : selectedImages) {
                imageUris.add(uri.toString());
            }

            intent.putStringArrayListExtra("images", imageUris);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();

                    // PERSIST PERMISSION (CRITICAL LINE)
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    selectedImages.add(uri);
                }

            } else if (data.getData() != null) {

                Uri uri = data.getData();

                // PERSIST PERMISSION
                getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                selectedImages.add(uri);
            }
        }
    }
}
