package com.example.traveldiaries.model;

import java.util.ArrayList;

public class Trip {

    private final String title;
    private final String description;
    private final ArrayList<String> imageUris;

    public Trip(String title, String description, ArrayList<String> imageUris) {
        this.title = title;
        this.description = description;
        this.imageUris = imageUris;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImageUris() {
        return imageUris;
    }
}
