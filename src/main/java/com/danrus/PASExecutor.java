package com.danrus;


import java.util.*;
import java.util.concurrent.*;

public class PASExecutor {

    public static ExecutorService MAIN_EXECUTOR = Executors.newFixedThreadPool(3);
    public static ExecutorService DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(3);

    public static void reload() {
        int threadsCount = 3;
        List<Runnable> runnables = MAIN_EXECUTOR.shutdownNow();
        MAIN_EXECUTOR = Executors.newFixedThreadPool(threadsCount);
        for (Runnable runnable : runnables) {
            MAIN_EXECUTOR.submit(runnable);
        }
    }

    public static void stop() {
        MAIN_EXECUTOR.shutdown();
    }

    public static CompletableFuture<Void> execute(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, MAIN_EXECUTOR);
    }
}
