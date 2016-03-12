package com.example.hamza.prog02;

import android.graphics.Bitmap;

/**
 * Created by Hamza on 3/6/16.
 */
public class CongressItem {
    private String firstName;
    private String lastName;
    private String title;
    private String bioguideId;
    private String party;


    public CongressItem(String firstName, String lastName, String title, String bioguideId, String party) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.bioguideId = bioguideId;
        this.party = party;
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

    public String getBioguideId() {
        return this.bioguideId;
    }

    public String getParty() {
        return this.party;
    }
}