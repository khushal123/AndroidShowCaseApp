package com.purpletealabs.sephora.apis;

import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IBooksService {
    @GET("volumes")
    Call<SearchBooksResponseModel> serarchBooks(
            @Query("q") String queryText,
            @Query("startIndex") int startIndex,
            @Query("maxResults") int maxResults
    );
}