package app.empresa.ferreland.CallBacks;

public class TouchHelperCallbackCategory  {

//    ICallBackItemTouch ICallBackItemTouch;
//
//    public TouchHelperCallbackCategory(ICallBackItemTouch ICallBackItemTouch) {
//        this.ICallBackItemTouch = ICallBackItemTouch;
//    }
//
//    @Override
//    public boolean isLongPressDragEnabled() {
//        return true;
//    }
//
//    @Override
//    public boolean isItemViewSwipeEnabled() {
//        return true;
//    }
//
//    @Override
//    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//        return makeMovementFlags(dragFlags, swipeFlags);
//    }
//
//    @Override
//    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//        ICallBackItemTouch.itemTouchOnMode(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        return true;
//    }
//
//    @Override
//    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        ICallBackItemTouch.onSwiped(viewHolder, viewHolder.getAdapterPosition());
//    }
//
//    @Override
//    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        } else {
//            final View foregroundView = ((AdapterUsuario.ViewHolder) viewHolder).rlB;
//            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
//        }
//    }
//
//    @Override
//    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (actionState != ItemTouchHelper.ACTION_STATE_DRAG) {
//            final View foregroundView = ((AdapterUsuario.ViewHolder) viewHolder).rlF;
//            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
//        }
//    }
//
//    @Override
//    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//        final View foregroundView = ((AdapterUsuario.ViewHolder) viewHolder).rlF;
//        foregroundView.setBackgroundColor(ContextCompat.getColor(((
//                AdapterUsuario.ViewHolder) viewHolder).rlF.getContext(), R.color.white));
//        getDefaultUIUtil().clearView(foregroundView);
//    }
//
//    @Override
//    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//        if (viewHolder != null) {
//            final View foregroundView = ((AdapterUsuario.ViewHolder) viewHolder).rlB;
//            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
//                foregroundView.setBackgroundColor(Color.LTGRAY);
//            }
//            getDefaultUIUtil().onSelected(foregroundView);
//        }
//    }
//
//    @Override
//    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
//        return super.convertToAbsoluteDirection(flags, layoutDirection);
//    }
}
