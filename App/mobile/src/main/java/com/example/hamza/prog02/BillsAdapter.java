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

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.CustomViewHolder> {
    private List<String> billList;
    private List<String> datesList;
    private Context mContext;

    public BillsAdapter(Context context, List<String> billList, List<String> datesList) {
        this.billList = billList;
        this.datesList = datesList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_bill, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final String billName = billList.get(i);
        final String billDate = datesList.get(i);

        final CustomViewHolder viewHolder = customViewHolder;

        //Setting text view title
        viewHolder.name.setText(Html.fromHtml(billName));
        viewHolder.date.setText(Html.fromHtml(billDate));
    }

    @Override
    public int getItemCount() {
        return (null != billList ? billList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView date;

        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.billName);
            this.date = (TextView) view.findViewById(R.id.billDate);
        }
    }
}