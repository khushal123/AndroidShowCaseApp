package com.purpletealabs.sephora.apis;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleBooksServiceFactory {
    private static final String API_BASE_URL = "https://www.googleapis.com/books/v1/";
    private static final long CONNECTION_TIMEOUT = 10L;
    private static final long READ_TIMEOUT = 60L;
    private static final long WRITE_TIMEOUT_SECONDS = 60L;
    private static final String API_KEY = "AIzaSyDEv3i28ipjy9uWAwjGlVyNnsv__vxt784";
    private static final String PARAM_KEY = "key";

    public static IBooksService newServiceInstance(ExecutorService executorService) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(getOkHttpClient(executorService))
                .build();
        return retrofit.create(IBooksService.class);
    }

    private static OkHttpClient getOkHttpClient(ExecutorService executorService) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Dispatcher dispatcher = new Dispatcher(executorService);
        httpClientBuilder.dispatcher(dispatcher);
        httpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(getQueyParamsInterceptor());
        return httpClientBuilder.build();
    }

    private static Interceptor getQueyParamsInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl oldUrl = request.url();
                HttpUrl.Builder newUrlBuilder = oldUrl.newBuilder();
                newUrlBuilder.addQueryParameter(PARAM_KEY, API_KEY);
                Request newRequest = request.newBuilder().url(newUrlBuilder.build()).build();
                return chain.proceed(newRequest);
            }
        };
    }
}