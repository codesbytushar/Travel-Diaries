package com.example.traveldiaries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.traveldiaries.model.Trip;

import java.io.Serializable;
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

        selectPhotosBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 100);
        });

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

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    selectedImages.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                selectedImages.add(data.getData());
            }
        }
    }
}
