package com.keithsmyth.resistance.feature.lobby.presentation;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.data.model.PlayerDataModel;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.presentation.ItemTouchHelperAdapter;
import com.keithsmyth.resistance.presentation.ItemTouchViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>
    implements ItemTouchHelperAdapter {

    private final List<PlayerDataModel> items;
    private PlayerActionListener playerActionListener;

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

    @Override
    public boolean onItemMove(int from, int to) {
        Collections.swap(items, from, to);
        notifyItemMoved(from, to);
        if (playerActionListener != null) {
            playerActionListener.onMovePlayer(from, to);
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        if (playerActionListener != null) {
            playerActionListener.onRemovePlayer(position);
        }
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

    public void setPlayerActionListener(PlayerActionListener playerActionListener) {
        this.playerActionListener = playerActionListener;
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder
        implements ItemTouchViewHolder {

        public final TextView nameText;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.name_text);
        }

        @Override
        public void onItemDragStart() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemDragEnd() {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public interface PlayerActionListener {

        void onMovePlayer(int from, int to);

        void onRemovePlayer(int position);
    }
}
