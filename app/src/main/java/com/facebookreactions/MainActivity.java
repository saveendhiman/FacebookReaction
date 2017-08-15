package com.facebookreactions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebookreactions.reaction.ReactionView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  MoviesAdapter.HomeFeedClickListener, ReactionView.SelectedReaction {
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private PopupWindow popupWindow;
    private int position = -1;
    private int like = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList, this);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    private void prepareMovieData() {
        Movie movie = new Movie("Mad Max: Fury Road", "Action & Adventure", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("Inside Out", "Animation, Kids & Family", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("Star Wars: Episode VII - The Force Awakens", "Action", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("Shaun the Sheep", "Animation", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("The Martian", "Science Fiction & Fantasy", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("Mission: Impossible Rogue Nation", "Action", "2017", "", 0);
        movieList.add(movie);

        movie = new Movie("Up", "Animation", "2009", "", 0);
        movieList.add(movie);

        movie = new Movie("Star Trek", "Science Fiction", "2009","", 0);
        movieList.add(movie);

        movie = new Movie("The LEGO Movie", "Animation", "2014", "", 0);
        movieList.add(movie);

        movie = new Movie("Iron Man", "Action & Adventure", "2008", "", 0);
        movieList.add(movie);

        movie = new Movie("Aliens", "Science Fiction", "1986", "", 0);
        movieList.add(movie);

        movie = new Movie("Chicken Run", "Animation", "2000", "", 0);
        movieList.add(movie);

        movie = new Movie("Back to the Future", "Science Fiction", "1985", "", 0);
        movieList.add(movie);

        movie = new Movie("Raiders of the Lost Ark", "Action & Adventure", "1981", "", 0);
        movieList.add(movie);

        movie = new Movie("Goldfinger", "Action & Adventure", "1965", "", 0);
        movieList.add(movie);

        movie = new Movie("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014", "", 0);
        movieList.add(movie);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReactionClicked(int position, String liketype, int like) {

        if (like == 0){
            movieList.get(position).setLikeType(liketype);
            movieList.get(position).setLike(0);
        }else{
            movieList.get(position).setLikeType(liketype);
            movieList.get(position).setLike(1);
        }

        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onReactionLongPressClicked(View view, int position, int like) {

        this.position = position;
        this.like = like;

        showReactionPopup(view);

    }

    private void showReactionPopup(View anchorView) {

        ReactionView layout = new ReactionView(MainActivity.this, this);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(layout, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {

            popupWindow.showAsDropDown(anchorView, 0, -(anchorView.getHeight() + 200) );

        }
    }

    @Override
    public void onSelectReaction(String selectedReaction) {

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        if (like == 0){
            movieList.get(position).setLikeType(selectedReaction);
            movieList.get(position).setLike(0);
        }else{
            movieList.get(position).setLikeType(selectedReaction);
            movieList.get(position).setLike(1);
        }
        mAdapter.notifyItemChanged(position);

        position = -1;
        like = -1;
    }
}
