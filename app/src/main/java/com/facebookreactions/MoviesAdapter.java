package com.facebookreactions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    HomeFeedClickListener mListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre, tvlikeview;
        LinearLayout btLike,btComment,btShare;
        ImageView ivliketype;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            tvlikeview = (TextView) view.findViewById(R.id.tvlikeview);
            btLike = (LinearLayout) view.findViewById(R.id.btLike);
            btComment = (LinearLayout) view.findViewById(R.id.btComment);
            btShare = (LinearLayout) view.findViewById(R.id.btShare);

            ivliketype = (ImageView) view.findViewById(R.id.ivliketype);
        }
    }

    /**
     * click listeners
     */
    interface HomeFeedClickListener {

        void onReactionClicked(int position, String liketype, int like);

        void onReactionLongPressClicked(View view,int position, int like);
    }

    public MoviesAdapter(List<Movie> moviesList, HomeFeedClickListener mHomeFeedListener) {
        this.moviesList = moviesList;
        mListener = mHomeFeedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());

        if (movie.getLikeType().equalsIgnoreCase("like")){
            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.like);
                holder.tvlikeview.setText("Like");
            }
        }else if (movie.getLikeType().equalsIgnoreCase("love")){

            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.love);
                holder.tvlikeview.setText("Love");
            }

        }else if (movie.getLikeType().equalsIgnoreCase("haha")){

            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.haha);
                holder.tvlikeview.setText("Haha");
            }

        }else if (movie.getLikeType().equalsIgnoreCase("wow")){

            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.wow);
                holder.tvlikeview.setText("Wow");
            }
        }else if (movie.getLikeType().equalsIgnoreCase("sad")){
            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.sad);
                holder.tvlikeview.setText("Sad");
            }
        }else if (movie.getLikeType().equalsIgnoreCase("angry")){

            if (movie.getLike() == 0){
                holder.ivliketype.setImageResource(R.drawable.ic_like);
                holder.tvlikeview.setText("Like");
            }else{
                holder.ivliketype.setImageResource(R.drawable.angry);
                holder.tvlikeview.setText("Angry");
            }
        }

        //add reaction listener

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (movie.getLike() == 1){
                    mListener.onReactionClicked(holder.getAdapterPosition(), "like", 0);
                }else{
                    mListener.onReactionClicked(holder.getAdapterPosition(), "like", 1);
                }

            }
        });


        holder.btLike.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mListener.onReactionLongPressClicked(holder.btLike, holder.getAdapterPosition(), 1);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
