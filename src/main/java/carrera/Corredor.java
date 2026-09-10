package carrera;

public final class Corredor implements Runnable {

    private final int id;
    private final Puente puente;

    public Corredor(int id, Puente puente) {
        this.id = id;
        this.puente = puente;
    }

    @Override
    public void run() {
        try {
            acercarseALaPartida();
            puente.llegue(id);

            primeraMitad();

            puente.pasar(id);
            pasarPuente();
            puente.siguiente(id);

            segundaMitad();
            System.out.printf("[%s] Corredor %d llegó a la meta%n", Reloj.ahora(), id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("[%s] Corredor %d fue interrumpido%n", Reloj.ahora(), id);
        }
    }

    private void acercarseALaPartida() throws InterruptedException {
        Reloj.dormirAleatorio(200, 800);
        System.out.printf("[%s] Corredor %d se acerca a la partida%n", Reloj.ahora(), id);
    }

    private void primeraMitad() throws InterruptedException {
        System.out.printf("[%s] Corredor %d corre hacia el puente%n", Reloj.ahora(), id);
        Reloj.dormirAleatorio(400, 1800);
        System.out.printf("[%s] Corredor %d llegó al puente%n", Reloj.ahora(), id);
    }

    private void pasarPuente() throws InterruptedException {
        System.out.printf("[%s] Corredor %d está cruzando el puente...%n", Reloj.ahora(), id);
        Reloj.dormir(Reloj.MINUTOS_DE_CRUCE);
        System.out.printf("[%s] Corredor %d terminó de cruzar%n", Reloj.ahora(), id);
    }

    private void segundaMitad() throws InterruptedException {
        System.out.printf("[%s] Corredor %d corre hacia la meta%n", Reloj.ahora(), id);
        Reloj.dormirAleatorio(400, 1800);
    }
}
