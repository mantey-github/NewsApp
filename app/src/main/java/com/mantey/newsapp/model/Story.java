package com.mantey.newsapp.model;

public class Story {

    private String mSection;
    private String mTitle;
    private String datePublished;
    private String mAuthor;
    private String mUrl;

    public Story(String section, String title, String date_published, String author, String url) {
        mSection = section;
        mTitle = title;
        datePublished = date_published;
        mAuthor = author;
        mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

}
