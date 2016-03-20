package com.keithsmyth.resistance.feature.lobby.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.data.model.PlayerDataModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final List<PlayerDataModel> items;

    public PlayerAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        holder.nameText.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<PlayerDataModel> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(PlayerDataModel item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(PlayerDataModel item) {
        final int position = items.indexOf(item);
        if (position >= 0) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameText;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.name_text);
        }
    }
}
