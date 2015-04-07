package io.smartsoft.booster.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import io.smartsoft.booster.R;
import java.util.ArrayList;
import java.util.List;

abstract public class ListAdapter<ViewHolderType extends RecyclerView.ViewHolder, ModelType>
        extends RecyclerView.Adapter<ViewHolderType> implements View.OnClickListener {

    protected final Activity mContext;
    protected final Picasso mPicasso;

    protected List<ModelType> mDataList = new ArrayList<>();
    protected OnRecyclerViewItemClickListener<ModelType> mItemClickListener;
    protected SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    protected boolean mSelectionEnabled = false;

    public ListAdapter(Activity context, Picasso picasso) {
        mContext = context;
        mPicasso = picasso;
    }

    public void add(ModelType dataItem, int pos) {
        mDataList.add(pos, dataItem);

        notifyItemInserted(pos);
    }

    public void update(ModelType dataItem) {
        int position = mDataList.indexOf(dataItem);

        if (position != -1) {
            mDataList.set(position, dataItem);

            notifyItemChanged(position);
        }
    }

    public void addAll(List<ModelType> dataList) {
        mDataList = dataList;

        notifyDataSetChanged();
    }

    public void setSelectionEnabled(boolean selectionEnabled) {
        mSelectionEnabled = selectionEnabled;
    }

    public void remove(ModelType dataItem) {
        int pos = mDataList.indexOf(dataItem);
        boolean removed = mDataList.remove(dataItem);

        if (removed) {
            notifyItemRemoved(pos);
        }
    }

    public void toggleSelection(int pos) {
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        } else {
            mSelectedItems.put(pos, true);
        }

        notifyItemChanged(pos);
    }

    public void setSelected(int pos) {
        mSelectedItems.put(pos, true);
        notifyItemChanged(pos);
    }

    public void clearSelection(int pos) {
        if (mSelectedItems.get(pos, false)) {
            mSelectedItems.delete(pos);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        if (mSelectedItems.size() > 0) {
            mSelectedItems.clear();
            notifyDataSetChanged();
        }
    }

    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    public ArrayList<ModelType> getSelectedItems() {
        ArrayList<ModelType> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(getItem(mSelectedItems.keyAt(i)));
        }
        return items;
    }

    public List<ModelType> getDataList() {
        return mDataList;
    }

    public ModelType getItem(int pos) {
        return mDataList.get(pos);
    }

    @Override
    abstract public ViewHolderType onCreateViewHolder(ViewGroup parent, int viewType);

    abstract public void bindView(ViewHolderType holder, ModelType model);

    @Override
    public void onBindViewHolder(final ViewHolderType holder, int position) {
        final ModelType model = mDataList.get(position);

        if (mSelectionEnabled) {
            //TODO: why activated background is not working ? WHY ?!?!
            if (mSelectedItems.get(position, false)) {
                holder.itemView.setBackground(new ColorDrawable(0x44ffffff));
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_activated);
            }

            holder.itemView.setActivated(mSelectedItems.get(position, false));
        }

        bindView(holder, model);

        holder.itemView.setTag(model);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            ModelType model = (ModelType) view.getTag();

            int modelPosition = mDataList.indexOf(model);

            if (mSelectionEnabled) {
                toggleSelection(modelPosition);
            }

            mItemClickListener.onItemClick(view, model, modelPosition);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<ModelType> listener) {
        this.mItemClickListener = listener;
    }
}