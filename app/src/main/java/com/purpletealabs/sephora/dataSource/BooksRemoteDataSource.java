package com.purpletealabs.sephora.dataSource;

import android.support.annotation.NonNull;

import com.purpletealabs.sephora.apis.GoogleBooksServiceFactory;
import com.purpletealabs.sephora.apis.IBooksService;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;
import com.purpletealabs.sephora.utils.AppExecutors;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksRemoteDataSource implements BooksDataSource {

    private static volatile BooksRemoteDataSource INSTANCE;

    private final AppExecutors mAppExecutors;

    private BooksRemoteDataSource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    public static BooksRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (BooksRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BooksRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void searchBooks(final String searchTerm, final int page, @NonNull final SearchBooksCallback callback) {
        Runnable searcher = new Runnable() {
            @Override
            public void run() {
                IBooksService service = GoogleBooksServiceFactory.newServiceInstance();
                service.serarchBooks(searchTerm, page * 40, 40)
                        .enqueue(new Callback<SearchBooksResponseModel>() {
                            @Override
                            public void onResponse(Call<SearchBooksResponseModel> call, final Response<SearchBooksResponseModel> response) {
                                mAppExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.code() == HttpURLConnection.HTTP_OK) {
                                            callback.onSearchBooksResult(response.body());
                                        } else {
                                            callback.onSearchBooksFailure();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<SearchBooksResponseModel> call, Throwable t) {
                                mAppExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSearchBooksFailure();
                                    }
                                });
                            }
                        });
            }
        };
        mAppExecutors.networkIO().execute(searcher);
    }
}