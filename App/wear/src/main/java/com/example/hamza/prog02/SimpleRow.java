package com.example.hamza.prog02;

import java.util.ArrayList;

public class SimpleRow {

    ArrayList<CongressItem> mPagesRow = new ArrayList<CongressItem>();

    public void addPages(CongressItem page) {
        mPagesRow.add(page);
    }

    public CongressItem getPages(int index) {
        return mPagesRow.get(index);
    }

    public int size(){
        return mPagesRow.size();
    }
}