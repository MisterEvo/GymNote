package com.sommerfeld.gymnote.util;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);

    void saveOrder();
}
