package carrera;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

final class Reloj {

    static final long MINUTOS_DE_CRUCE = 2_000L;

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Reloj() {
    }

    static String ahora() {
        return LocalTime.now().format(HORA);
    }

    static void dormir(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    static void dormirAleatorio(int minMillis, int maxMillis) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(minMillis, maxMillis + 1));
    }
}
