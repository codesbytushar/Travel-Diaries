package com.example.traveldiaries.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {

    private String title;
    private String description;
    private ArrayList<String> imagePaths;

    public Trip(String title, String description, ArrayList<String> imagePaths) {
        this.title = title;
        this.description = description;
        this.imagePaths = imagePaths;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }
}
