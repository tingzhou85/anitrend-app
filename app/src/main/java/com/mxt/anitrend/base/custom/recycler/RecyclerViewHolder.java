package com.mxt.anitrend.base.custom.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mxt.anitrend.util.ActionModeHelper;

import butterknife.ButterKnife;

/**
 * Created by max on 2017/06/09.
 * Recycler view holder implementation
 */

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ActionModeHelper<T> callback;

    /**
     * Default constructor which includes binding with butter knife
     */
    public RecyclerViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    /**
     * Load image, text, buttons, etc. in this method from the given parameter
     * <br/>
     *
     * @param model Is the model at the current adapter position
     */
    public abstract void onBindViewHolder(T model);

    /**
     * If any image views are used within the view holder, clear any pending async img requests
     * by using Glide.clear(ImageView) or Glide.with(context).clear(view) if using Glide v4.0
     * <br/>
     * @see com.bumptech.glide.Glide
     */
    public abstract void onViewRecycled();


    /**
     * Handle any onclick events from our views
     * <br/>
     *
     * @param v the view that has been clicked
     * @see View.OnClickListener
     */
    @Override
    public abstract void onClick(View v);

    public @NonNull Context getContext() {
        return itemView.getContext().getApplicationContext();
    }

    /**
     * Applying selection styling on the desired item
     * @param model the current model item
     */
    void onBindSelectionState(T model) {
        if(callback != null)
            callback.setBackgroundColor(this, callback.isSelected(model));
    }

    /**
     * Checks if the the current adapter position is not below 0
     */
    protected boolean isValidIndex() {
        return getAdapterPosition() > -1;
    }

    protected boolean isClickable (T clicked) {
        return isValidIndex() && (callback == null || !callback.onItemClick(this, clicked));
    }

    protected boolean isLongClickable (T clicked) {
        return isValidIndex() && (callback == null || !callback.onItemLongClick(this, clicked));
    }

    void setActionMode(ActionModeHelper<T> actionModeHelper) {
        callback = actionModeHelper;
    }
}
