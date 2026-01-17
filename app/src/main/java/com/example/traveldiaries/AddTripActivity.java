package com.example.traveldiaries;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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

        selectPhotosBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, 100);
        });

        saveTripBtn.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("title", titleEdt.getText().toString());
            result.putExtra("description", descEdt.getText().toString());

            ArrayList<String> images = new ArrayList<>();
            for (Uri uri : selectedImages) {
                images.add(uri.toString());
            }

            result.putStringArrayListExtra("images", images);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    selectedImages.add(uri);
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                selectedImages.add(uri);
            }

            imageAdapter.notifyDataSetChanged();
        }
    }
}
