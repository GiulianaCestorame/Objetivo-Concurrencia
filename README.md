# Objetivo concurrencia 

Ejercicios de concurrencia en Java 17: **monitor**, **semáforos** y un **deadlock** a propósito.

```bash
mvn -q compile
```

---

## 1. Carrera a campo traviesa (monitor)

C corredores. En la mitad hay un puente que usa **una persona a la vez**. Arrancan cuando todos llegaron a la partida. En el puente esperan **en orden de llegada**, cruzan y siguen a la meta. Solo hay hilos de corredores; el puente es un monitor (`ReentrantLock` + `Condition`).

| Teórico | Java |
| --- | --- |
| `Process Corredor` | `Thread` + `Corredor` |
| `Monitor Puente` | clase `Puente` |
| `llegue` / `signal_all` | barrera de largada |
| `pasar` / `siguiente` | cola FIFO de `Condition` |


```bash
java -cp target/classes carrera.Main 6
```

---

## 2. Máquina expendedora (semáforos)

U usuarios y un repositor. Capacidad de latas: constante `CAPACIDAD` en el código (ahora 10). Turno **FIFO**. Si al usarla no hay latas, avisa al repositor, espera la recarga, saca una y se va. Mientras se recarga, otros pueden **encolarse** (el mutex solo cubre la fila, no la recarga).

| Teórico | Java (`Semaphore`) |
| --- | --- |
| `P` / `V` | `acquire()` / `release()` |
| `mutex` | exclusión de `cola` y `libre` |
| `esperando[id]` | un semáforo por usuario (turno) |
| `repositor` / `esperandoBotellas` | aviso y espera de recarga |

Paquete `expendedora`. El argumento es **cantidad de usuarios**, no de latas.

```bash
java -cp target/classes expendedora.Main 15
```

---

## 3. Deadlock

Dos hilos, dos locks. T1 toma A y pide B; T2 toma B y pide A. Quedan esperándose para siempre.

```bash
java -cp target/classes deadlock.Main
```

Va a colgarse: cortar con **Ctrl+C**.
