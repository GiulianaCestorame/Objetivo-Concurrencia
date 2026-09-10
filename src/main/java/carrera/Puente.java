package carrera;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public final class Puente {

    private final int totalCorredores;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition largada = lock.newCondition();
    private final Queue<Condition> colaPuente = new ArrayDeque<>();

    private int cant = 0;
    private boolean libre = true;

    public Puente(int totalCorredores) {
        this.totalCorredores = totalCorredores;
    }

    public void llegue(int id) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            cant++;
            log(id, "llegó a la partida (" + cant + "/" + totalCorredores + ")");
            if (cant != totalCorredores) {
                largada.await();
            } else {
                log(id, "es el último: ¡largada!");
                largada.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void pasar(int id) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (libre) {
                libre = false;
                log(id, "toma el puente (nadie esperaba)");
            } else {
                log(id, "espera su turno en el puente (cola=" + (colaPuente.size() + 1) + ")");
                Condition turno = lock.newCondition();
                colaPuente.add(turno);
                try {
                    turno.await();
                } catch (InterruptedException e) {
                    colaPuente.remove(turno);
                    throw e;
                }
                log(id, "le toca cruzar");
            }
        } finally {
            lock.unlock();
        }
    }

    public void siguiente(int id) {
        lock.lock();
        try {
            if (!colaPuente.isEmpty()) {
                log(id, "liberó el puente; pasa el siguiente");
                colaPuente.remove().signal();
            } else {
                libre = true;
                log(id, "liberó el puente; queda libre");
            }
        } finally {
            lock.unlock();
        }
    }

    private static void log(int id, String mensaje) {
        System.out.printf("[%s] Corredor %d %s%n", Reloj.ahora(), id, mensaje);
    }
}
