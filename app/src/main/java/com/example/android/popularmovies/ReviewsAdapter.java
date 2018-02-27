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

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder>{
    ArrayList<Review> reviews;
    Context context;
    public ReviewsAdapter(Context context, ArrayList<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }
    @Override
    public ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row,parent, false);
        ReviewsHolder reviewsHolder = new ReviewsHolder(row);
        return reviewsHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewAuthor.setText(review.getAuthor());
        holder.reviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewsHolder extends RecyclerView.ViewHolder{

        TextView reviewContent;
        TextView reviewAuthor;
        public ReviewsHolder(View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.textViewReviewContent);
            reviewAuthor = itemView.findViewById(R.id.textViewReviewAuther);
        }
    }
}
