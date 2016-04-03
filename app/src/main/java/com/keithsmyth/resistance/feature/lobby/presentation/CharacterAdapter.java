package com.keithsmyth.resistance.feature.lobby.presentation;

import android.content.Context;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private final Context context;
    private final LobbyPresenter lobbyPresenter;
    private final List<Item> items;

    public CharacterAdapter(Context context, LobbyPresenter lobbyPresenter) {
        this.context = context;
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
        final Item item = items.get(position);
        if (item.isHeader) {
            bindHeader(holder, item.header);
        } else {
            bindCharacter(holder, item.characterViewModel);
        }
    }

    private void bindHeader(CharacterViewHolder holder, String header) {
        holder.nameText.setText(header);
        holder.selectedCheckBox.setVisibility(View.GONE);
    }

    private void bindCharacter(CharacterViewHolder holder, final CharacterViewModel characterViewModel) {
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

        sortListByTeam(items);

        this.items.add(new Item(context.getString(R.string.good)));
        boolean badHeaderAdded = false;
        for (CharacterViewModel characterViewModel : items) {
            if (!badHeaderAdded && characterViewModel.isBad) {
                this.items.add(new Item(context.getString(R.string.bad)));
                badHeaderAdded = true;
            }
            this.items.add(new Item(characterViewModel));
        }

        notifyDataSetChanged();
    }

    private void sortListByTeam(List<CharacterViewModel> items) {
        Collections.sort(items, new Comparator<CharacterViewModel>() {
            @Override
            public int compare(CharacterViewModel lhs, CharacterViewModel rhs) {
                if (lhs.isBad == rhs.isBad) {
                    return 0;
                }
                return lhs.isBad ? 1 : -1;
            }
        });
    }

    private static class Item {

        public final boolean isHeader;
        public final String header;
        public final CharacterViewModel characterViewModel;

        public Item(String header) {
            this.header = header;
            isHeader = true;
            characterViewModel = null;
        }

        public Item(CharacterViewModel characterViewModel) {
            this.characterViewModel = characterViewModel;
            isHeader = false;
            header = null;
        }
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
