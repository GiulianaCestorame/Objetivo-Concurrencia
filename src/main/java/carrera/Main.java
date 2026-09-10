package carrera;

/**
 * Enunciado : Se debe simular una carrera a campo traviesa con C corredores donde en la mitad del recorrido
 * hay un puente colgante que puede ser usado por una única persona a la vez. Cuando los C corredores han llegado
 * al punto de partida comienza la carrera. Cuando un corredor llega al puente espera su turno (respetando el orden de llegada al mismo)
 * y lo cruza (suponga que tarde un par de minutos en cruzarlo) ;
 * y luego continua su carrera hasta llegar a la meta. Nota: cada corredor pasa solo una vez por el puente. Solo se pueden usar procesos que representen a los corredores.
 *
 * Resolucion en ADA:
 *
 * Process Corredor[0..C-1]{
 * puente.llegue()
 * //llegaron a la partida
 *  puente.pasar
 * PasarPuente()
 * puente.siguiente()
 * }
 * Monitor Puente{
 * int cant=0, esperando=0, Cond cola; boolean libre=true
 * procedure llegue(){
 *    cant++
 *    if(cant!= C){
 *          await(cola)
 *    }
 * else{ signal_all(cola)}
 * }
 * }
 *
 * procedure pasar(){
 *   if(libre){
 *        libre = false
 * }
 * else{
 *      esperando ++
 *      await(cola)
 * }
 * }
 *
 * procedure siguiente(){
 *    if(esperando >0){
 *        esperando - -
 *         signal(cola)
 *     }
 * else{ libre = true}
 * }
 *
 * }
 */
public final class Main {

    private static final int CORREDORES_POR_DEFECTO = 6;

    public static void main(String[] args) throws InterruptedException {
        int c = CORREDORES_POR_DEFECTO;
        if (args.length > 0) {
            c = Integer.parseInt(args[0]);
        }


        Puente puente = new Puente(c);
        Thread[] corredores = new Thread[c];

        for (int i = 0; i < c; i++) {
            corredores[i] = new Thread(new Corredor(i, puente), "Corredor-" + i);
            corredores[i].start();
        }

        for (Thread corredor : corredores) {
            corredor.join();
        }

        System.out.println("Todos los corredores llegaron a la meta.");
    }
}
