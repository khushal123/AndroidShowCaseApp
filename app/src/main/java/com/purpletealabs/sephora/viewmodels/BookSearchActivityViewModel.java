package com.purpletealabs.sephora.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.AsyncTask;
import android.os.Handler;

import com.purpletealabs.sephora.dtos.Book;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;
import com.purpletealabs.sephora.tasks.SearchBooksTask;

import java.util.ArrayList;
import java.util.List;

public class BookSearchActivityViewModel extends ViewModel implements SearchBooksTask.Callback {
    public final ObservableArrayList<BookViewModel> mBooks = new ObservableArrayList<>();

    public final ObservableBoolean isSearchInProgress = new ObservableBoolean(false);

    public final ObservableBoolean isSearchResultEmpty = new ObservableBoolean(true);

    public final ObservableBoolean isScreenInInitialState = new ObservableBoolean(true);

    private Handler handler = new Handler();

    private Runnable mSearchStarter = new Runnable() {
        @Override
        public void run() {
            searchBooks();
        }
    };

    private String mSearchTerm;

    private SearchBooksTask mSearchTask;

    private int mTotal;

    public void searchFor(String query, boolean delayed) {
        mSearchTerm = query;
        mBooks.clear();
        isScreenInInitialState.set(true);
        isSearchResultEmpty.set(mBooks.isEmpty());
        handler.removeCallbacks(mSearchStarter);
        if (!mSearchTerm.isEmpty()) {
            handler.postDelayed(mSearchStarter, delayed ? 1000L : 10L);
        }
    }

    private void searchBooks() {
        isScreenInInitialState.set(false);
        isSearchInProgress.set(true);
        mSearchTask = new SearchBooksTask(mSearchTerm, 0, this);
        mSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void loadMore(int page) {
        if (mBooks.size() < mTotal) {
            if (mSearchTask != null) {
                mSearchTask.cancel(true);
            }
            mSearchTask = new SearchBooksTask(mSearchTerm, page, this);
            mSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onSearchResult(SearchBooksResponseModel serarchResult) {
        isSearchInProgress.set(false);
        if (serarchResult != null) {
            mTotal = serarchResult.getTotalItems();
            List<Book> bookList = serarchResult.getBooks();
            List<BookViewModel> books = new ArrayList<>();
            for (Book b : bookList) {
                books.add(new BookViewModel(b));
            }
            mBooks.addAll(books);
            isSearchResultEmpty.set(mBooks.isEmpty());
        }
    }
}
