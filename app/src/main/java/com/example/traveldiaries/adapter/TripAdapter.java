package com.example.traveldiaries.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveldiaries.R;
import com.example.traveldiaries.TripDetailsActivity;
import com.example.traveldiaries.model.Trip;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Trip> trips;

    public TripAdapter(Context context, ArrayList<Trip> trips) {
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        // Set title
        holder.title.setText(trip.getTitle());

        // ✅ Safe image loading (NO crash)
        if (trip.getImages() != null && !trip.getImages().isEmpty()) {
            holder.image.setImageURI(trip.getImages().get(0));
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        // Card click → open TripDetailsActivity
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, TripDetailsActivity.class);

            intent.putExtra("title", trip.getTitle());
            intent.putExtra("description", trip.getDescription());

            ArrayList<String> imageStrings = new ArrayList<>();

            if (trip.getImages() != null && !trip.getImages().isEmpty()) {
                for (Uri uri : trip.getImages()) {
                    imageStrings.add(uri.toString());
                }
            }

            intent.putStringArrayListExtra("images", imageStrings);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // ---------------- ViewHolder ----------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tripImage);
            title = itemView.findViewById(R.id.tripTitle);
        }
    }
}
