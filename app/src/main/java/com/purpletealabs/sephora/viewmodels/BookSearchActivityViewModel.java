package com.purpletealabs.sephora.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Handler;

import com.purpletealabs.sephora.dataSource.BooksDataSource;
import com.purpletealabs.sephora.dataSource.BooksRemoteDataSource;
import com.purpletealabs.sephora.dataSource.BooksRepository;
import com.purpletealabs.sephora.dtos.Book;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;
import com.purpletealabs.sephora.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class BookSearchActivityViewModel extends ViewModel implements BooksDataSource.SearchBooksCallback {
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

    private int mTotal;

    public void searchFor(String query, boolean delayed) {
        mSearchTerm = query;
        mBooks.clear();
        isScreenInInitialState.set(true);
        isSearchResultEmpty.set(mBooks.isEmpty());
        handler.removeCallbacks(mSearchStarter);
        BooksRepository br = BooksRepository.getInstance(BooksRemoteDataSource.getInstance(new AppExecutors()));
        br.cancelPendingExecutions();
        if (!mSearchTerm.isEmpty()) {
            handler.postDelayed(mSearchStarter, delayed ? 1000L : 10L);
        }
    }

    private void searchBooks() {
        isScreenInInitialState.set(false);
        isSearchInProgress.set(true);
        BooksRepository br = BooksRepository.getInstance(BooksRemoteDataSource.getInstance(new AppExecutors()));
        br.cancelPendingExecutions();
        br.searchBooks(mSearchTerm, 0, this);
    }

    public void loadMore(int page) {
        if (mBooks.size() < mTotal) {
            BooksRepository br = BooksRepository.getInstance(BooksRemoteDataSource.getInstance(new AppExecutors()));
            br.cancelPendingExecutions();
            br.searchBooks(mSearchTerm, page, this);
        }
    }

    @Override
    public void onSearchBooksResult(SearchBooksResponseModel serarchResult) {
        isSearchInProgress.set(false);
        mTotal = serarchResult.getTotalItems();
        List<BookViewModel> bookViewModels = new ArrayList<>();
        List<Book> bookList = serarchResult.getBooks();
        if (bookList != null) {
            for (Book b : bookList) {
                bookViewModels.add(new BookViewModel(b));
            }
        }
        mBooks.addAll(bookViewModels);
        isSearchResultEmpty.set(mBooks.isEmpty());
    }

    @Override
    public void onSearchBooksFailure() {
        isSearchInProgress.set(false);
    }

    public void destroy() {
        BooksRepository br = BooksRepository.getInstance(BooksRemoteDataSource.getInstance(new AppExecutors()));
        br.cancelPendingExecutions();
    }
}
