package com.keithsmyth.resistance.presentation;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int from, int to);

    void onItemDismiss(int position);
}
