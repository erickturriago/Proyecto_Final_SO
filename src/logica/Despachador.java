package logica;

public class Despachador {
    private ListaRR listaColaRR = new ListaRR();
    private ListaFCFS listaColaFCFS = new ListaFCFS();
    private ListaSRTF listaColaSRTF = new ListaSRTF();
    private ListaBloqueados listaBloqueados = new ListaBloqueados();
    private Proceso ultimoProcesoInsertado;
    int contadorProcesos = 1;

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
        if(listaColaSRTF.getProcesoCabeza().getSiguiente() != listaColaSRTF.getProcesoCabeza()){
            procesoRetorno = listaColaSRTF.atender();
            return procesoRetorno;
        }
        return null;
    }

    public Proceso insertarProcesoAleatorio(){
        int numeroAleatorio = new java.util.Random().nextInt(3) + 1;

        Proceso procesoInsertado = new Proceso(this.contadorProcesos);

        switch (numeroAleatorio){
            case 1:
                listaColaRR.insertar(procesoInsertado);
                procesoInsertado.setNombreCola("RR");
                break;
            case 2:
                listaColaFCFS.insertar(procesoInsertado);
                procesoInsertado.setNombreCola("FCFS");
                break;
            case 3:
                listaColaSRTF.insertar(procesoInsertado);
                procesoInsertado.setNombreCola("SRTF");
                break;
            default:
                System.out.println("Numero no esperado.");
                break;
        }
        this.contadorProcesos++;
        return procesoInsertado;
    }

    public boolean insertarProcesoEspecifico(Proceso procesoInsertar){
        String nombreCola = procesoInsertar.getNombreCola();

        switch (nombreCola){
            case "RR":
                listaColaRR.insertar(procesoInsertar);
                break;
            case "FCFS":
                listaColaFCFS.insertar(procesoInsertar);
                break;
            case "SRTF":
                listaColaSRTF.insertar(procesoInsertar);
                break;
            default:
                System.out.println("Nombre no esperado.");
                break;
        }
        return true;
    }

    public void insertarColaRR(){
        Proceso procesoInsertado = new Proceso(this.contadorProcesos);
        listaColaRR.insertar(procesoInsertado);
        procesoInsertado.setNombreCola("RR");
    }

    public void insertarColaFCFS(){
        Proceso procesoInsertado = new Proceso(this.contadorProcesos);
        listaColaFCFS.insertar(procesoInsertado);
        procesoInsertado.setNombreCola("FCFS");
    }

    public void insertarColaSRTF(){
        Proceso procesoInsertado = new Proceso(this.contadorProcesos);
        listaColaSRTF.insertar(procesoInsertado);
        procesoInsertado.setNombreCola("SRTF");
    }

    public void actualizarEnvejecimiento(){
        listaColaSRTF.notificarEnvejecimientoObservadores();
        listaColaFCFS.notificarEnvejecimientoObservadores();
        this.organizarColas();
    }

    public void organizarColas(){
        Proceso procesoAuxiliar;

        //Validar envejecimiento cola SRTF (Cola 3)
        procesoAuxiliar = listaColaSRTF.getProcesoCabeza().getSiguiente();
        while (procesoAuxiliar != listaColaSRTF.getProcesoCabeza()) {

            boolean esEnvejecido = false;

            if(procesoAuxiliar.getTiempoEnvejecimiento() == 0){
                listaColaSRTF.atender(procesoAuxiliar);
                listaColaFCFS.insertar(procesoAuxiliar);
                procesoAuxiliar.setTiempoEnvejecimiento(10); // Se reinicia el tiempo de envejecimiento
                procesoAuxiliar.setNombreCola("FCFS"); //Se cambia el nombre de la cola a la que pertenece

                esEnvejecido=true;
            }
            if(esEnvejecido){
                procesoAuxiliar=listaColaSRTF.getProcesoCabeza().getSiguiente();
            }
            else{
                procesoAuxiliar = procesoAuxiliar.getSiguiente();
            }

        }


        //Validar envejecimiento cola FCFS
        procesoAuxiliar = listaColaFCFS.getProcesoCabeza().getSiguiente();
        while (procesoAuxiliar != listaColaFCFS.getProcesoCabeza()) {

            boolean esEnvejecido = false;

            if(procesoAuxiliar.getTiempoEnvejecimiento() == 0){
                listaColaFCFS.atender(procesoAuxiliar);
                listaColaRR.insertar(procesoAuxiliar);
                // Se inserta en la cola 2 y se quita de la 3
                procesoAuxiliar.setTiempoEnvejecimiento(10); // Se reinicia el tiempo de envejecimiento
                procesoAuxiliar.setNombreCola("RR"); //Se cambia el nombre de la cola a la que pertenece
                esEnvejecido=true;
            }
            if(esEnvejecido){
                procesoAuxiliar=listaColaFCFS.getProcesoCabeza().getSiguiente();
            }
            else{
                procesoAuxiliar = procesoAuxiliar.getSiguiente();
            }

        }
    }

    public boolean checkProcesoMayorPrioridad(Proceso proceso){
        String nombreCola = proceso.getNombreCola();

        if(nombreCola == "SRTF"){
            if(this.listaColaFCFS.getTamano() > 0){
                return true;
            }
        }
        else if(nombreCola=="FCFS"){
            if(this.listaColaRR.getTamano() > 0){
                return true;
            }
        }
        return false;
    }
    
    public void actualizarListaBloqueados() {
    	Proceso procesoAux =listaBloqueados.getProcesoCabeza().getSiguiente();
    	//System.out.println("tamano de la lista: " + listaBloqueados.getTamano());
    	while(procesoAux!=listaBloqueados.getProcesoCabeza() ) {
    		procesoAux.setTiempoBloqueo(procesoAux.getTiempoBloqueo()-1);
    		if(procesoAux.getTiempoBloqueo()<=0) {
    			listaBloqueados.atender();
    			insertarProcesoEspecifico((Proceso)procesoAux.clone());
    		}
    		procesoAux=procesoAux.getSiguiente();
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
    
    public ListaBloqueados getListaBloqueados() {
    	return listaBloqueados;
    }
}
