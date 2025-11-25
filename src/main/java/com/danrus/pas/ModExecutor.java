package com.danrus.pas;

import com.danrus.pas.config.PasConfig;

import java.util.*;
import java.util.concurrent.*;

public class ModExecutor {
    public static ExecutorService MAIN_EXECUTOR = Executors.newFixedThreadPool(PasConfig.getInstance().getDownloadThreads());
    public static ExecutorService DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(PasConfig.getInstance().getDownloadThreads());

    public static void reload() {
        int threadsCount = PasConfig.getInstance().getDownloadThreads();
        List<Runnable> runnables = MAIN_EXECUTOR.shutdownNow();
        MAIN_EXECUTOR = Executors.newFixedThreadPool(threadsCount);
        for (Runnable runnable : runnables) {
            MAIN_EXECUTOR.submit(runnable);
        }

        runnables = DOWNLOAD_EXECUTOR.shutdownNow();
        DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(threadsCount);
        for (Runnable runnable : runnables) {
            DOWNLOAD_EXECUTOR.submit(runnable);
        }
    }

    public static void stop() {
        MAIN_EXECUTOR.shutdown();
        DOWNLOAD_EXECUTOR.shutdown();
    }

    public static CompletableFuture<Void> execute(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, MAIN_EXECUTOR);
    }
}
