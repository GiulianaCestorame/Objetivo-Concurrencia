# Carrera a campo traviesa (concurrencia en Java)

Simulación del enunciado visto en el curso: **C corredores**, un **puente colgante** que usa **una sola persona a la vez**, y **solo hilos que representan corredores**.

## Enunciado

Se simula una carrera a campo traviesa. En la mitad del recorrido hay un puente colgante que puede usar una única persona a la vez.

- Cuando los C corredores llegaron al punto de partida, comienza la carrera.
- Al llegar al puente, cada corredor espera su turno **en orden de llegada**, lo cruza (un par de minutos) y sigue hasta la meta.
- Cada corredor pasa **una sola vez** por el puente.
- Solo hay procesos/hilos de corredores (el puente es un monitor, no un hilo extra).

## Correspondencia con Ada

| Ada | Java |
| --- | --- |
| `Process Corredor[0..C-1]` | `Thread` + `Runnable` (`Corredor`) |
| `Monitor Puente` | clase `Puente` con `ReentrantLock` |
| `llegue` / `await(cola)` / `signal_all` | `llegue()` + `Condition largada` |
| `pasar` / `await(cola)` | `pasar()` + cola FIFO de `Condition` |
| `PasarPuente()` (fuera del monitor) | `pasarPuente()` con `Thread.sleep` |
| `siguiente` / `signal(cola)` | `siguiente()` despierta al primero de la cola |
| `join` implícito al terminar | `Thread.join()` en `Main` |

En Ada una sola `Cond cola` servía para la largada y para el puente. Acá van **separadas**, para que un `signal` del puente no despierte a alguien que todavía espera la largada (y al revés).

La cola de `Condition` garantiza el **orden de llegada al puente**, que un `wait`/`notify` único no asegura en Java.

## Cómo correrlo

Java 17+ y Maven:

```bash
mvn -q compile exec:java
```

Con otra cantidad de corredores (en PowerShell hay que entrecomillar el `-D`):

```bash
mvn -q compile exec:java "-Dexec.args=8"
```

