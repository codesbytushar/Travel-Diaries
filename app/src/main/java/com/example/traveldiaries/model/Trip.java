package com.example.traveldiaries.model;

import android.net.Uri;
import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    private final String title;
    private final String description;
    private final ArrayList<Uri> images;

    public Trip(String title, String description, ArrayList<Uri> images) {
        this.title = title;
        this.description = description;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }
}
