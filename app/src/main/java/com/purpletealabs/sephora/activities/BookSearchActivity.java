package com.purpletealabs.sephora.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.purpletealabs.sephora.R;
import com.purpletealabs.sephora.adapters.BooksAdapter;
import com.purpletealabs.sephora.databinding.ActivityBookSearchBinding;
import com.purpletealabs.sephora.viewmodels.BookSearchActivityViewModel;
import com.purpletealabs.sephora.viewmodels.EndlessRecyclerViewScrollListener;

public class BookSearchActivity extends AppCompatActivity {

    private BookSearchActivityViewModel mViewModel;

    public EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBookSearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_book_search);

        mViewModel = ViewModelProviders.of(this).get(BookSearchActivityViewModel.class);

        mViewModel.isSearchInProgress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (((ObservableBoolean) observable).get()) {
                    hideKeyboard();
                }
            }
        });

        binding.setViewmodel(mViewModel);

        initViews(binding);
    }

    private void initViews(ActivityBookSearchBinding binding) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.rvBooks.setLayoutManager(layoutManager);
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                mViewModel.loadMore(page);
            }
        };
        binding.rvBooks.addOnScrollListener(mScrollListener);
        binding.rvBooks.setAdapter(new BooksAdapter(mViewModel.mBooks));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setIconifiedByDefault(true);
        if (!TextUtils.isEmpty(mViewModel.mSearchTerm)) {
            searchView.setIconified(false);
            searchView.setQuery(mViewModel.mSearchTerm, false);
            searchView.clearFocus();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.searchFor(query, false);
                mScrollListener.resetState();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mViewModel.searchFor(query, true);
                mScrollListener.resetState();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        mViewModel.destroy();
        super.onDestroy();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

}
