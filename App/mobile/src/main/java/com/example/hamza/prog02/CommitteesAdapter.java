package com.example.hamza.prog02;

/**
 * Created by Hamza on 3/6/16.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class CommitteesAdapter extends RecyclerView.Adapter<CommitteesAdapter.CustomViewHolder> {
    private List<String> committeesList;
    private Context mContext;

    public CommitteesAdapter(Context context, List<String> committeesList) {
        this.committeesList = committeesList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_committee, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final String committeeName = committeesList.get(i);

        final CustomViewHolder viewHolder = customViewHolder;

        //Setting text view title
        viewHolder.name.setText(Html.fromHtml(committeeName));
    }

    @Override
    public int getItemCount() {
        return (null != committeesList ? committeesList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;

        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.CommitteesItemText);
        }
    }
}