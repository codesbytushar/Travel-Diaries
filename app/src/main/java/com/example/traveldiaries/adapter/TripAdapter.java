package com.example.traveldiaries.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveldiaries.R;
import com.example.traveldiaries.TripDetailsActivity;
import com.example.traveldiaries.model.Trip;

import java.io.File;
import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    Context context;
    ArrayList<Trip> trips;
    DeleteListener deleteListener;

    // 🔴 Interface for delete callback
    public interface DeleteListener {
        void onDelete(int position);
    }

    public TripAdapter(Context context, ArrayList<Trip> trips, DeleteListener listener) {
        this.context = context;
        this.trips = trips;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_trip, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.title.setText(trip.getTitle());

        if (!trip.getImagePaths().isEmpty()) {
            Glide.with(context)
                    .load(new File(trip.getImagePaths().get(0)))
                    .into(holder.image);
        }

        // Open details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TripDetailsActivity.class);
            intent.putExtra("title", trip.getTitle());
            intent.putExtra("description", trip.getDescription());
            intent.putStringArrayListExtra("images", trip.getImagePaths());
            context.startActivity(intent);
        });

        // DELETE CLICK
        holder.deleteBtn.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, deleteBtn;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tripImage);
            title = itemView.findViewById(R.id.tripTitle);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
