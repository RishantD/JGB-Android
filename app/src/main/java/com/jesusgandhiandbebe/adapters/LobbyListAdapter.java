package com.jesusgandhiandbebe.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesusgandhiandbebe.CameraActivity;
import com.jesusgandhiandbebe.Constants;
import com.jesusgandhiandbebe.R;
import com.jesusgandhiandbebe.models.Lobby;

import java.util.List;

/**
 *
 */
public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.ViewHolder> {

    private List<Lobby> lobbies;

    public LobbyListAdapter(List<Lobby> data) {
        this.lobbies = data;
    }

    /**
     * Inflate the view by adding the attached data view
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby_list, parent, false);
        return new ViewHolder(v, new ViewHolder.CardClicked() {
            @Override
            public void onLobbyClicked(View view) {
                TextView tv = (TextView) view.findViewById(R.id.lobby_id);
                String lobbyID = tv.getText().toString();
                parent.getContext().startActivity(new Intent(parent.getContext(), CameraActivity.class)
                        .putExtra(Constants.LOBBY_ID_KEY, lobbyID));
            }
        });
    }

    /**
     * Bind the data for the lobby to the view by setting appropriate text
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lobby lobby = lobbies.get(position);
        holder.lobbyTV.setText(lobby.name);
        holder.lobbyIDTV.setText(lobby._id);
    }

    /**
     * Get lobby items total count
     * @return
     */
    @Override
    public int getItemCount() {
        return lobbies.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView lobbyTV;
        public CardClicked listener;
        public TextView lobbyIDTV;

        public ViewHolder(View itemView, CardClicked listener) {
            super(itemView);

            lobbyTV = (TextView) itemView.findViewById(R.id.lobby_name);
            lobbyIDTV = (TextView) itemView.findViewById(R.id.lobby_id);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onLobbyClicked(v);
        }

        public interface CardClicked {
            void onLobbyClicked(View view);
        }
    }
}
