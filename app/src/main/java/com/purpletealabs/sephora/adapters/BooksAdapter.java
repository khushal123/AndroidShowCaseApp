package com.purpletealabs.sephora.adapters;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.purpletealabs.sephora.databinding.RowBookBinding;
import com.purpletealabs.sephora.viewmodels.BookViewModel;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private final ObservableArrayList<BookViewModel> mBooks;

    public BooksAdapter(ObservableArrayList<BookViewModel> books) {
        mBooks = books;
        mBooks.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<BookViewModel>>() {
            @Override
            public void onChanged(ObservableList<BookViewModel> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<BookViewModel> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<BookViewModel> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<BookViewModel> sender, int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<BookViewModel> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(RowBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setViewmodel(mBooks.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RowBookBinding binding;

        ViewHolder(RowBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
