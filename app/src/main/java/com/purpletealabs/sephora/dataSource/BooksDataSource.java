package com.purpletealabs.sephora.dataSource;

import android.support.annotation.NonNull;

import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;

public interface BooksDataSource {
    interface SearchBooksCallback {
        void onSearchBooksResult(SearchBooksResponseModel result);

        void onSearchBooksFailure();
    }

    void searchBooks(String searchTerm, final int page, @NonNull SearchBooksCallback callback);

    void cancelPendingExecutions();
}