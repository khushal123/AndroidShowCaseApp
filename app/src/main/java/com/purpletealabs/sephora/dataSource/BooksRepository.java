package com.purpletealabs.sephora.dataSource;

import android.support.annotation.NonNull;

import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;

public class BooksRepository implements BooksDataSource {
    private volatile static BooksRepository INSTANCE = null;

    //Remote data source to search books in
    private final BooksDataSource mRemoteDataSource;

    //Here we can have multiple different data sources
    private BooksRepository(BooksDataSource dataSource) {
        mRemoteDataSource = dataSource;
    }

    public static BooksRepository getInstance(BooksDataSource remoteDataSource) {
        if (INSTANCE == null) {
            synchronized (BooksRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BooksRepository(remoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void searchBooks(String searchTerm, int page, @NonNull final SearchBooksCallback callback) {
        mRemoteDataSource.searchBooks(searchTerm, page, new SearchBooksCallback() {
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

    @Override
    public void cancelPendingExecutions() {
        mRemoteDataSource.cancelPendingExecutions();
    }
}