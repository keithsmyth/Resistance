package com.keithsmyth.resistance.feature.lobby.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private final LobbyPresenter lobbyPresenter;
    private final List<CharacterViewModel> items;

    public CharacterAdapter(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
        items = new ArrayList<>();
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        final CharacterViewModel characterViewModel = items.get(position);
        holder.nameText.setText(characterViewModel.name);
        holder.selectedCheckBox.setChecked(lobbyPresenter.isCharacterSelected(characterViewModel));
        holder.selectedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lobbyPresenter.selectCharacter(characterViewModel, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<CharacterViewModel> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameText;
        public final CheckBox selectedCheckBox;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.name_text);
            selectedCheckBox = (CheckBox) itemView.findViewById(R.id.selected_check_box);
        }
    }
}
