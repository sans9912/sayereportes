package app.empresa.ferreland.interfaces;

import androidx.recyclerview.widget.RecyclerView;

public interface ICallBackItemTouch {
    void itemTouchOnMode(int oldPosition, int newPosition);
    void onSwiped(RecyclerView.ViewHolder viewHolder, int position);
}
