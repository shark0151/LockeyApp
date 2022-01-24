package com.example.lockey;


import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class RecyclerViewSimpleAdapter<T> extends RecyclerView.Adapter<RecyclerViewSimpleAdapter<T>.MyViewHolder> {

    private static final String LOG_TAG = "devicelist";
    private final List<T> data;
    private OnItemClickListener<T> onItemClickListener;
    private final int viewId = View.generateViewId();
    private final int deviceid = View.generateViewId();
    private final int status = View.generateViewId();
    private final int time = View.generateViewId();

    public RecyclerViewSimpleAdapter(List<T> data) {
        this.data = data;
        Log.d(LOG_TAG, data.toString());

    }

    @NonNull
    @Override
    public RecyclerViewSimpleAdapter<T>.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = makeView(parent.getContext());
        Log.d(LOG_TAG, v.toString());
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    private View makeView(Context context) {
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //main container
        LinearLayout layoutmegamain = new LinearLayout(context);

        LinearLayout layout = new LinearLayout(context);
        layoutmegamain.setPadding(10,10,10,10);
        layout.setPadding(10,10,10,10);
        layoutmegamain.setLayoutParams(params);
        layout.setBackground(ContextCompat.getDrawable(context,R.drawable.rounded_corners));
        layoutmegamain.setBackgroundColor(ContextCompat.getColor(context,R.color.backgroundColor));
        layout.setClipToOutline(true);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        //Comment itself
        LinearLayout device = new LinearLayout(context);
        device.setOrientation(LinearLayout.VERTICAL);
        device.setLayoutParams(params);

        TextView username = new TextView(context);
        username.setTextSize(16);
        username.setId(deviceid);
        username.setLayoutParams(params);
        username.setTextColor(ContextCompat.getColor(context,R.color.userTextColor));

        TextView userComment = new TextView(context);
        userComment.setId(status);
        userComment.setLayoutParams(params);
        userComment.setTextColor(ContextCompat.getColor(context,R.color.PostColor));

        TextView commentComments = new TextView(context);
        commentComments.setId(time);
        commentComments.setLayoutParams(params);
        commentComments.setTextColor(ContextCompat.getColor(context,R.color.CommentColor));
        commentComments.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        commentComments.setTextSize(12);

        device.addView(username);
        device.addView(userComment);
        device.addView(commentComments);

        layout.addView(device);
        layout.setId(viewId);
        layoutmegamain.addView(layout);

        // Log.d("banana", textView.toString());
        return layoutmegamain;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device dataItem = (Device) data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        //holder.view.setText(dataItem.getUser());
        String displayText;
        ((TextView)holder.itemView.findViewById(deviceid)).setText(dataItem.getId());
        if (dataItem.getIsLocked())
            displayText="Locked";
        else
            displayText="Unlocked";
        ((TextView)holder.itemView.findViewById(status)).setText(displayText);
        ((TextView)holder.itemView.findViewById(time)).setText("Time: " +dataItem.getTime());
        Log.d(LOG_TAG, "onBindViewHolder called " + position);
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    public Device getItem(int position)
    {
        return (Device) data.get(position);
    }

    void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final LinearLayout view;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            view = itemView.findViewById(viewId);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition(), data.get(getAdapterPosition()));

            }
        }
    }
}

