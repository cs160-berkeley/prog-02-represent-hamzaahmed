package com.example.hamza.prog02;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Hamza on 3/6/16.
 */
public class CongressItem {
    private String firstName;
    private String lastName;
    private String title;
    private Bitmap thumbnail;
    private String party;
    private String email;
    private String website;
    private String twitter;


    public CongressItem(String firstName, String lastName, String title, Bitmap thumbnail, String party, String email, String website, String twitter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.thumbnail = thumbnail;
        this.party = party;
        this.email = email;
        this.website = website;
        this.twitter = twitter;
    }

    public String getTitle() {
        if (title.equalsIgnoreCase("sen")) {
            return "Senator";
        } else if (title.equalsIgnoreCase("rep")) {
            return "Representative";
        }  else if (title.equalsIgnoreCase("del")) {
            return "Delegate";
        } else if (title.equalsIgnoreCase("com")) {
            return "Commissioner";
        }else {
            return title;
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public Bitmap getThumbnail() {
        return this.thumbnail;
    }

//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }

    public String getParty() {
        return this.party;
    }

    public String getEmail() {
        return this.email;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getTwitter() {
        return this.twitter;
    }
}