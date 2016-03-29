package com.keithsmyth.resistance.admin.gamedetail;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.admin.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final List<PlayerViewModel> players;

    public PlayerAdapter() {
        players = new ArrayList<>();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        final PlayerViewModel playerViewModel = players.get(position);
        final StringBuilder builder = new StringBuilder();
        builder.append("name: ");
        builder.append(playerViewModel.name);
        builder.append("\n");
        if (!TextUtils.isEmpty(playerViewModel.characterName)) {
            builder.append("char: ");
            builder.append(playerViewModel.characterName);
            builder.append("\n");
        }
        builder.append("id: ");
        builder.append(playerViewModel.id);
        holder.playerText.setText(builder.toString());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<PlayerViewModel> players) {
        this.players.clear();
        this.players.addAll(players);
        notifyDataSetChanged();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        private final TextView playerText;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            playerText = (TextView) itemView.findViewById(R.id.player_text);
        }
    }
}
