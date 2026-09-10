package com.objetivo.concurrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Lanza 3 hilos con ExecutorService. Cada uno espera 3 segundos y imprime
 * "Hello World". El hilo principal espera a que todos terminen.
 */
public class HelloWorldConcurrente {

    private static final int NUM_HILOS = 3;
    private static final int DELAY_SEGUNDOS = 3;

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_HILOS);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int i = 1; i <= NUM_HILOS; i++) {
                final int hilo = i;
                Future<?> future = executor.submit(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(DELAY_SEGUNDOS);
                        System.out.println("Hello World (hilo " + hilo + ")");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Hilo " + hilo + " interrumpido", e);
                    }
                });
                futures.add(future);
            }

            for (Future<?> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }

        System.out.println("Programa Finalizado");
    }
}
