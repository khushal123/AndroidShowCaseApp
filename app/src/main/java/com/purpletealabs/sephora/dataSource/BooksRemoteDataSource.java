package com.purpletealabs.sephora.dataSource;

import android.support.annotation.NonNull;

import com.purpletealabs.sephora.apis.GoogleBooksServiceFactory;
import com.purpletealabs.sephora.apis.IBooksService;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;
import com.purpletealabs.sephora.utils.AppExecutors;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Remote data source to search books in
public class BooksRemoteDataSource implements BooksDataSource {

    private static volatile BooksRemoteDataSource INSTANCE;

    private final AppExecutors mAppExecutors;

    private final List<Call> mRetrofitCalls;

    private BooksRemoteDataSource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
        mRetrofitCalls = new ArrayList<>();
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
    public void searchBooks(String searchTerm, int page, @NonNull final SearchBooksCallback callback) {
        IBooksService service = GoogleBooksServiceFactory.newServiceInstance(mAppExecutors.networkIO());
        Call<SearchBooksResponseModel> call = service.serarchBooks(searchTerm, page * 40, 40);
        call.enqueue(new Callback<SearchBooksResponseModel>() {
            @Override
            public void onResponse(Call<SearchBooksResponseModel> call, final Response<SearchBooksResponseModel> response) {
                if (!call.isCanceled()) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        callback.onSearchBooksResult(response.body());
                    } else {
                        callback.onSearchBooksFailure();
                    }
                    mRetrofitCalls.remove(call);
                }
            }

            @Override
            public void onFailure(Call<SearchBooksResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    callback.onSearchBooksFailure();
                    mRetrofitCalls.remove(call);
                }
            }
        });
    }

    @Override
    public void cancelPendingExecutions() {
        for (Call call : mRetrofitCalls) {
            call.cancel();
        }
    }
}