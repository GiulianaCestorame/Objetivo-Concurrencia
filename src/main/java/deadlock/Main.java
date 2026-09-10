package deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Deadlock: T1 toma A y espera B; T2 toma B y espera A.
 */
public final class Main {

    static final Lock a = new ReentrantLock();
    static final Lock b = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            a.lock();
            System.out.println("T1 tiene A, pide B");
            dormir();
            b.lock();
            System.out.println("T1 nunca imprime esto");
        }, "T1");

        Thread t2 = new Thread(() -> {
            b.lock();
            System.out.println("T2 tiene B, pide A");
            dormir();
            a.lock();
            System.out.println("T2 nunca imprime esto");
        }, "T2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Esto tampoco");
    }

    static void dormir() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
