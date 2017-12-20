package com.purpletealabs.sephora.tasks;


import android.os.AsyncTask;

import com.purpletealabs.sephora.apis.GoogleBooksServiceFactory;
import com.purpletealabs.sephora.apis.IBooksService;
import com.purpletealabs.sephora.dtos.SearchBooksResponseModel;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Response;

public class SearchBooksTask extends AsyncTask<Void, Void, SearchBooksResponseModel> {

    private final String searchTerm;

    private final Integer page;

    private final Callback listener;

    public SearchBooksTask(String searchTerm, Integer page, Callback listener) {
        this.searchTerm = searchTerm;
        this.page = page;
        this.listener = listener;
    }

    @Override
    protected SearchBooksResponseModel doInBackground(Void... voids) {
        try {
            IBooksService service = GoogleBooksServiceFactory.newServiceInstance();
            Response<SearchBooksResponseModel> response = service.serarchBooks(searchTerm, page * 40, 40).execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(SearchBooksResponseModel responseModel) {
        super.onPostExecute(responseModel);
        listener.onSearchResult(responseModel);
    }

    public interface Callback {
        void onSearchResult(SearchBooksResponseModel serarchResult);
    }
}
