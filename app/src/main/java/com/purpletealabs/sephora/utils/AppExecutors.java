package com.purpletealabs.sephora.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private final ExecutorService networkIO;

    public AppExecutors() {
        networkIO = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public ExecutorService networkIO() {
        return networkIO;
    }
}
