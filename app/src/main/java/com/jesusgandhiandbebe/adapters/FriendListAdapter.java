package com.jesusgandhiandbebe.adapters;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesusgandhiandbebe.R;

import java.util.List;

/**
 *
 * Created by rishant_dwivedi on 11/18/15.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder>{

    List<String> friendNames;

    public FriendListAdapter(List<String> friendNames) {
        this.friendNames = friendNames;
    }

    /**
     * Create a new view with attached data
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Bind name to position
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = friendNames.get(position);
        holder.friendName.setText(name);
    }

    /**
     * Get Count
     * @return
     */
    @Override
    public int getItemCount() {
        return friendNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatCheckBox checkBox;
        public TextView friendName;

        public ViewHolder(View itemView) {
            super(itemView);

            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.friend_checked);
            friendName = (TextView) itemView.findViewById(R.id.friend_name);
        }
    }
}
