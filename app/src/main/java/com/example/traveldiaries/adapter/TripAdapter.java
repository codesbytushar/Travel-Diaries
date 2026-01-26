package com.example.traveldiaries.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveldiaries.R;
import com.example.traveldiaries.model.Trip;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
        void onTripDelete(int position);
    }

    private final Context context;
    private final ArrayList<Trip> trips;
    private final OnTripClickListener listener;

    public TripAdapter(Context context,
                       ArrayList<Trip> trips,
                       OnTripClickListener listener) {
        this.context = context;
        this.trips = trips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.title.setText(trip.getTitle());

        if (trip.getImageUris() != null && !trip.getImageUris().isEmpty()) {
            holder.image.setImageURI(Uri.parse(trip.getImageUris().get(0)));
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        // 📂 OPEN TRIP
        holder.itemView.setOnClickListener(v ->
                listener.onTripClick(trip)
        );

        // 🗑️ DELETE TRIP
        holder.deleteBtn.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onTripDelete(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        ImageView image, deleteBtn;
        TextView title;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tripImage);
            title = itemView.findViewById(R.id.tripTitle);
            deleteBtn = itemView.findViewById(R.id.deleteTripBtn);
        }
    }
}
