package com.purpletealabs.sephora.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.purpletealabs.sephora.R;
import com.purpletealabs.sephora.dataSource.BooksDataSource;
import com.purpletealabs.sephora.dataSource.BooksRemoteDataSource;
import com.purpletealabs.sephora.dataSource.BooksRepository;
import com.purpletealabs.sephora.dtos.Book;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;
import com.purpletealabs.sephora.utils.AppExecutors;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BookSearchActivityViewModel extends AndroidViewModel implements BooksDataSource.SearchBooksCallback {

    public final ObservableArrayList<BookViewModel> mBooks = new ObservableArrayList<>();

    // These observable fields will update Views automatically
    public final ObservableBoolean isSearchInProgress = new ObservableBoolean(false);

    public final ObservableBoolean isSearchResultEmpty = new ObservableBoolean(true);

    public final ObservableField<String> emptyViewText = new ObservableField<>();

    private final WeakReference<Context> mContext;

    private Handler handler = new Handler();

    private Runnable mSearchStarter = new Runnable() {
        @Override
        public void run() {
            searchBooks();
        }
    };

    public String mSearchTerm;

    private int mTotal;

    public BookSearchActivityViewModel(@NonNull Application application) {
        super(application);
        mContext = new WeakReference<Context>(application.getApplicationContext());
        emptyViewText.set(mContext.get().getString(R.string.text_search_books));
    }

    public void searchFor(String query, boolean delayed) {
        mSearchTerm = query;
        handler.removeCallbacks(mSearchStarter);
        BooksRepository br = BooksRepository.getInstance(BooksRemoteDataSource.getInstance(new AppExecutors()));
        br.cancelPendingExecutions();
        if (!mSearchTerm.isEmpty()) {
            //Trigger search if user has not typed anything in last one second or user has pressed search in keyboard
            handler.postDelayed(mSearchStarter, delayed ? 1000L : 10L);
        }
    }

    private void searchBooks() {
        //Clear existing search result before starting new search
        mBooks.clear();
        isSearchResultEmpty.set(mBooks.isEmpty());
        emptyViewText.set(mContext.get().getString(R.string.text_search_books));
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
        emptyViewText.set(mContext.get().getString(R.string.text_empty_search_result, mSearchTerm));
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
