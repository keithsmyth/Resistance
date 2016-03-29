package com.keithsmyth.resistance.admin.gameslist;

import java.util.List;

public interface GamesListView {

    void setItems(List<Integer> items);

    void addItem(int item);

    void removeItem(int item);

    void openGameDetailView();
}
