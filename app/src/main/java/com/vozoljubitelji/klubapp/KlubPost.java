package com.vozoljubitelji.klubapp;

import java.lang.String;
import java.sql.Array;
import java.util.List;

/**
 * Created by macosx on 9/26/17.
 */

public class KlubPost {

    private String mCategory;

    private String mDate;

    private String mTitle;

    private String mText;

    private String mAuthor;

    private String mReferenceId;

    private String mMainImageUrl;

    private String mUrl;

    //private String mBalance;

    //private String mIncome;

    //private String mYear;

    private List mImagesUrl;

    public KlubPost (String category, String date, String title, String text, String author, String referenceid, String mainImageUrl, String url, List<String> imagesUrl) {

        mCategory = category;
        mDate = date;
        mTitle = title;
        mText = text;
        mAuthor = author;
        mReferenceId = referenceid;
        mMainImageUrl = mainImageUrl;
        mUrl = url;
        //mBalance = balance;
        //mIncome = income;
        //mYear = year;
        mImagesUrl = imagesUrl;
    }

    public String getmCategory() {return mCategory;}

    public String getmDate() {return mDate;}

    public String getmTitle() {return mTitle;}

    public String getmText() {return mText;}

    public String getmAuthor() {return mAuthor;}

    public String getmReferenceId() {return mReferenceId;}

    public String getmMainImageUrl() {return mMainImageUrl;}

    public String getmUrl() {return mUrl;}

    //public String getBalance() {return mBalance;}

    //public String getmIncome() {return mIncome;}

    //public String getmYear() {return mYear;}

    public List getmImagesUrl() {return mImagesUrl;
    }
}
