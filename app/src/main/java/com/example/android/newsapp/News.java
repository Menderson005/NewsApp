package com.example.android.newsapp;

public class News {

    private String mAuthor;

    /** Title of the Article */
    private String mTitle;

    /** Section of Article */
    private String mSection;

    /** Date of Article */
    private String mDate;

    /** Website URL of Article */
    private String mUrl;

    public News(String webSectionName, String webPublicationDate, String webTitle, String webUrl, String byLine){
        mAuthor = byLine;
        mTitle = webTitle;
        mSection = webSectionName;
        mDate = webPublicationDate;
        mUrl = webUrl;
    }



    public String getAuthor(){return mAuthor;}

    /** Returns the title */
    public String getTitle(){return mTitle;}

    /** Returns the section */
    public String getSection(){return mSection;}

    /** Returns the date */
    public String getDate(){return mDate;}

    /** Returns URL */
    public String getUrl() {return mUrl;}
}
