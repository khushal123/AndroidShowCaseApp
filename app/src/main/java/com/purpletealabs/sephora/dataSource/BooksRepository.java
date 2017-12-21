package com.purpletealabs.sephora.dataSource;

import android.support.annotation.NonNull;

import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;

public class BooksRepository implements BooksDataSource {
    private volatile static BooksRepository INSTANCE = null;

    private final BooksDataSource mDataSource;

    private BooksRepository(BooksDataSource dataSource) {
        mDataSource = dataSource;
    }

    public static BooksRepository getInstance(BooksDataSource dataSource) {
        if (INSTANCE == null) {
            synchronized (BooksRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BooksRepository(dataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void searchBooks(String searchTerm, int page, @NonNull final SearchBooksCallback callback) {
        mDataSource.searchBooks(searchTerm, page, new SearchBooksCallback() {
            @Override
            public void onSearchBooksResult(SearchBooksResponseModel result) {
                callback.onSearchBooksResult(result);
            }

            @Override
            public void onSearchBooksFailure() {
                callback.onSearchBooksFailure();
            }
        });
    }
}