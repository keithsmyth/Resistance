package com.keithsmyth.resistance.admin.gameslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.admin.R;

import java.util.ArrayList;
import java.util.List;

public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.GamesListViewHolder> {

    private final List<Integer> gameNumbers;
    private final Listener listener;

    public GamesListAdapter(Listener listener) {
        this.listener = listener;
        gameNumbers = new ArrayList<>();
    }

    @Override
    public GamesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_games_list, parent, false);
        return new GamesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GamesListViewHolder holder, int position) {
        final Integer gameNumber = gameNumbers.get(position);
        holder.gameNumberText.setText(String.valueOf(gameNumber));
        holder.gameNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGameNumberClick(gameNumber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameNumbers.size();
    }

    public void setGameNumbers(List<Integer> gameNumbers) {
        this.gameNumbers.clear();
        this.gameNumbers.addAll(gameNumbers);
        notifyDataSetChanged();
    }

    public void addGameNumber(int gameNumber) {
        gameNumbers.add(0, gameNumber);
        notifyItemInserted(0);
    }

    public void removeGameNumber(int gameNumber) {
        final int index = gameNumbers.indexOf(gameNumber);
        gameNumbers.remove(index);
        notifyItemRemoved(index);
    }

    interface Listener {

        void onGameNumberClick(int gameNumber);
    }

    public static class GamesListViewHolder extends RecyclerView.ViewHolder {

        private final TextView gameNumberText;

        public GamesListViewHolder(View itemView) {
            super(itemView);
            gameNumberText = (TextView) itemView.findViewById(R.id.game_number_text);
        }
    }
}
