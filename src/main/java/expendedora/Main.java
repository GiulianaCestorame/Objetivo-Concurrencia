package expendedora;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implemente una solución para el siguiente problema. Se debe simular el uso de una máquina expendedora de gaseosas
 * con capacidad para 100 latas por parte de U usuarios. Además, existe un repositor
 * encargado de reponer las latas de la máquina. Los usuarios usan la máquina según
 * el orden de llegada. Cuando les toca usarla, sacan una lata y luego se retiran. En el caso de que la máquina se quede sin latas,
 * entonces le debe avisar al repositor para que cargue nuevamente la máquina en forma completa. Luego de la recarga,
 * saca una botella y se retira. Nota: maximizar la concurrencia; mientras se reponen las latas se debe permitir que otros
 * usuarios puedan agregarse a la fila.
 *
 *
 * Bool libre=true; Sem mutex=1;repositor=0; botellas =100;
 * Process usuario[id : 1..U]{
 *    Mutex(p)
 *    If(!libre){
 *        Cola.push(id)
 *       V(mutex) ;
 *       P(esperando[id]) ;
 *  }
 *   Else{
 *    Libre = false;
 *   V(mutex) ;
 * }
 *
 * //aca exclusion mutua
 *   If(botellas ==0){
 *      V(repositor); //despierto repo
 *      P(esperandoBotellas) ;
 *  }
 *   Botellas -- ;
 *
 * P(mutex) ;
 *  If(!cola.IsEmpy){
 *        V(esperando[cola.pop()]) ; //despierto al sig
 *  }
 *  Else{
 *      Libre = true ;
 *  }
 * V(mutex) ;
 *  //se va
 * }
 * Process Repositor::{
 *  while(true){
 *    P(repositor) ;
 *    Botellas = 100;
 *    v(esperandoBotellas);
 * }
 * }
 */
public final class Main {

    static final int CAPACIDAD = 10;

    static final Semaphore mutex = new Semaphore(1);
    static final Semaphore repositor = new Semaphore(0);
    static final Semaphore esperandoBotellas = new Semaphore(0);
    static Semaphore[] esperando;

    static final Queue<Integer> cola = new ArrayDeque<>();
    static boolean libre = true;
    static int botellas = CAPACIDAD;

    public static void main(String[] args) throws InterruptedException {
        int u = args.length > 0 ? Integer.parseInt(args[0]) : 15;
        esperando = new Semaphore[u];
        for (int i = 0; i < u; i++) {
            esperando[i] = new Semaphore(0);
        }

        Thread repo = new Thread(Main::procesoRepositor, "Repositor");
        repo.setDaemon(true);
        repo.start();

        Thread[] usuarios = new Thread[u];
        for (int i = 0; i < u; i++) {
            int id = i;
            usuarios[i] = new Thread(() -> procesoUsuario(id), "Usuario-" + id);
            usuarios[i].start();
        }
        for (Thread t : usuarios) {
            t.join();
        }
    }

    static void procesoUsuario(int id) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 400));

            mutex.acquire();
            if (!libre) {
                cola.add(id);
                mutex.release();
                esperando[id].acquire();
            } else {
                libre = false;
                mutex.release();
            }

            if (botellas == 0) {
                System.out.println("Usuario " + id + " avisa al repositor (máquina vacía)");
                repositor.release();
                esperandoBotellas.acquire();
            }
            botellas--;
            System.out.println("Usuario " + id + " saca una lata (quedan " + botellas + ")");

            mutex.acquire();
            if (!cola.isEmpty()) {
                esperando[cola.remove()].release();
            } else {
                libre = true;
            }
            mutex.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void procesoRepositor() {
        try {
            while (true) {
                repositor.acquire();
                System.out.println("Repositor recargando...");
                Thread.sleep(300);
                botellas = CAPACIDAD;
                System.out.println("Repositor recargó " + CAPACIDAD + " latas");
                esperandoBotellas.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
