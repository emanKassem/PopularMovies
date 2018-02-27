package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LENOVO on 26/02/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersHolder>{
    ArrayList<Trailer> trailers;
    Context context;

    public interface OnItemClickListener {

        void onItemClick(Trailer trailer);
    }
    private final OnItemClickListener listener;

    public TrailersAdapter(Context context, ArrayList<Trailer> trailers, OnItemClickListener listener){
        this.context = context;
        this.trailers = trailers;
        this.listener = listener;
    }

    @Override
    public TrailersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false);
        TrailersHolder trailersHolder = new TrailersHolder(view);
        return trailersHolder;
    }

    @Override
    public void onBindViewHolder(TrailersHolder holder, int position) {
        holder.bind(trailers.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailersHolder extends RecyclerView.ViewHolder{

        TextView trailerTextView;
        public TrailersHolder(View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.textViewTrailer);
        }

        public void bind(final Trailer trailer, final OnItemClickListener listener) {
            trailerTextView.setText(trailer.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(trailer);
                }
            });
        }
    }
}
