package com.keithsmyth.resistance.feature.game.presentation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<GameRoundViewModel> rounds;

    public RoundAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        rounds = new ArrayList<>();
    }

    public void setRounds(List<GameRoundViewModel> rounds) {
        this.rounds.clear();
        this.rounds.addAll(rounds);
        notifyDataSetChanged();
    }

    @Override
    public RoundAdapter.RoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_round, parent, false);
        return new RoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoundAdapter.RoundViewHolder holder, int position) {
        final GameRoundViewModel gameRoundViewModel = rounds.get(position);
        holder.roundText.setText(context.getString(R.string.game_round, gameRoundViewModel.roundNumber));
        holder.outcomeText.setText(context.getString(R.string.game_round_info, gameRoundViewModel.numberOfPlayers, gameRoundViewModel.numberOfFails));
        //if (gameRoundViewModel.status == GamePlayProvider.STATUS_COMPLETE) {
        // TODO: colour the background
        //}
    }

    @Override
    public int getItemCount() {
        return rounds.size();
    }

    public static class RoundViewHolder extends RecyclerView.ViewHolder {

        public final TextView outcomeText;
        public final TextView roundText;

        public RoundViewHolder(View itemView) {
            super(itemView);
            outcomeText = (TextView) itemView.findViewById(R.id.outcome_text);
            roundText = (TextView) itemView.findViewById(R.id.round_text);
        }
    }
}
