package com.example.hamza.prog02;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;

    public SampleGridPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<>();

        SimpleRow row1 = new SimpleRow();
        row1.addPages(new SimplePage("Barbara Boxer", "Senator", R.drawable.democrat, R.drawable.barbara));
        row1.addPages(new SimplePage("Diane Feinstein", "Senator", R.drawable.democrat, R.drawable.democrat));
        row1.addPages(new SimplePage("Barbara Lee", "House Rep.", R.drawable.democrat, R.drawable.barbara));
        SimpleRow row2 = new SimpleRow();
        row2.addPages(new SimplePage("Vote View","County", R.drawable.democrat, R.drawable.barbara));
        mPages.add(row1);
        mPages.add(row2);
    }

    @Override
    public Fragment getFragment(int row, int col) {
        SimplePage page = (mPages.get(row)).getPages(col);
        if (page.mName == "Vote View") {
            VoteFragment fragment = VoteFragment.newInstance(page.mName, page.mPosition, page.mIconId);
            return fragment;
        }
        CustomCardFragment fragment = CustomCardFragment.newInstance(page.mName, page.mPosition, page.mIconId);
        return fragment;
    }

    @Override
    public Drawable getBackgroundForPage(int row, int col) {
        SimplePage page = (mPages.get(row)).getPages(col);
        Drawable d = mContext.getResources().getDrawable(page.mBackgroundId, mContext.getTheme());
        return d;
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }
}