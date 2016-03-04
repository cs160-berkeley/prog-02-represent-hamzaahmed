package com.example.hamza.prog02;

public class SimplePage {

    public String mName;
    public String mPosition;
    public int mIconId;
    public int mBackgroundId;

    public SimplePage(String title, String text, int iconId, int backgroundId) {
        this.mName = title;
        this.mPosition = text;
        this.mIconId = iconId;
        this.mBackgroundId = backgroundId;
    }
}