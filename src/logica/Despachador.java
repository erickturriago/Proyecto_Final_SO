package logica;

public class Despachador {
    private ListaRR listaColaRR = new ListaRR();
    private ListaFCFS listaColaFCFS = new ListaFCFS();
    private ListaSRTF listaColaSRTF = new ListaSRTF();

    public Proceso getProceso(){
        Proceso procesoRetorno;

        if(listaColaRR.getProcesoCabeza().getSiguiente() != listaColaRR.getProcesoCabeza()){
            procesoRetorno = listaColaRR.atender();
            return procesoRetorno;
        }

        if(listaColaFCFS.getProcesoCabeza().getSiguiente() != listaColaFCFS.getProcesoCabeza()){
            procesoRetorno = listaColaFCFS.atender();
            return procesoRetorno;
        }

        if(listaColaFCFS.getProcesoCabeza().getSiguiente() != listaColaFCFS.getProcesoCabeza()){
            procesoRetorno = listaColaFCFS.atender();
            return procesoRetorno;
        }

        return null;
    }

    public Proceso insertarProcesoAleatorio(){
        int numeroAleatorio = new java.util.Random().nextInt(3) + 1;

        Proceso procesoInsertado = null;

        switch (numeroAleatorio){
            case 1:
                procesoInsertado = listaColaRR.insertar();
                break;
            case 2:
                procesoInsertado = listaColaFCFS.insertar();
                break;
            case 3:
                procesoInsertado = listaColaSRTF.insertar();
                break;
            default:
                System.out.println("Numero no esperado.");
                break;
        }

        return procesoInsertado;

    }

    public void actualizarEnvejecimiento(){
        listaColaSRTF.notificarEnvejecimientoObservadores();
        listaColaFCFS.notificarEnvejecimientoObservadores();
        this.organizarColas();
    }

    public void organizarColas(){
        Proceso procesoAuxiliar;

        //Validar envejecimiento cola SRTF (Cola 3)
        procesoAuxiliar = listaColaSRTF.getProcesoCabeza();
        while (procesoAuxiliar.getSiguiente() != listaColaSRTF.getProcesoCabeza()) {

            if(procesoAuxiliar.getTiempoEnvejecimiento() == 0){
                listaColaSRTF.atender(procesoAuxiliar); // Se quita el proceso de la cola 3
                listaColaFCFS.insertar(procesoAuxiliar); // Se inserta en la cola 2
                procesoAuxiliar.setTiempoEnvejecimiento(10); // Se reinicia el tiempo de envejecimiento
                procesoAuxiliar.setNombreCola("FCFS"); //Se cambia el nombre de la cola a la que pertenece
            }
            procesoAuxiliar = procesoAuxiliar.getSiguiente();
        }


        //Validar envejecimiento cola FCFS
        procesoAuxiliar = listaColaFCFS.getProcesoCabeza();
        while (procesoAuxiliar.getSiguiente() != listaColaFCFS.getProcesoCabeza()) {

            if(procesoAuxiliar.getTiempoEnvejecimiento() == 0){
                listaColaFCFS.atender(procesoAuxiliar); // Se quita el proceso de la cola 2
                listaColaRR.insertar(procesoAuxiliar); // Se inserta en la cola 1
                procesoAuxiliar.setTiempoEnvejecimiento(10); // Se reinicia el tiempo de envejecimiento
                procesoAuxiliar.setNombreCola("RR"); //Se cambia el nombre de la cola a la que pertenece
            }
            procesoAuxiliar = procesoAuxiliar.getSiguiente();
        }
    }

    public ListaRR getListaColaRR() {
        return listaColaRR;
    }

    public ListaFCFS getListaColaFCFS() {
        return listaColaFCFS;
    }

    public ListaSRTF getListaColaSRTF() {
        return listaColaSRTF;
    }
}
