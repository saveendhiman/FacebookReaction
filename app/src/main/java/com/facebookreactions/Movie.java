package com.facebookreactions;

/**
 * Created by saveen_dhiman on 15-August-17.
 */
public class Movie {
    private String title, genre, year, liketype;
    private int like;

    public Movie() {
    }

    public Movie(String title, String genre, String year, String liketype, int like) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.liketype = liketype;
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLikeType() {
        return liketype;
    }

    public void setLikeType(String liketype) {
        this.liketype = liketype;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
