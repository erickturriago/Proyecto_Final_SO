package logica;

public class ListaSRTF extends Lista{


  @Override
  public Proceso insertar() {
    Proceso procesoNuevo = new Proceso(this.contador);

    this.ultimoAgregado = procesoNuevo;
    this.contador++;
    int rafagaRestanteNuevoProceso = procesoNuevo.getRafagaRestante();
    Proceso procesoActual = this.getProcesoCabeza().getSiguiente();
    Proceso procesoAnterior = this.getProcesoCabeza();

    // Si la cola está vacía y sólo está el cajero.
    if (this.getProcesoCabeza().getSiguiente() == this.getProcesoCabeza()) {
      this.getProcesoCabeza().setSiguiente(procesoNuevo);
      procesoNuevo.setSiguiente(this.getProcesoCabeza());
      return procesoNuevo;
    }

    while (procesoActual != this.getProcesoCabeza()) {

      int rafagaRestanteProcesoActual = procesoActual.getRafagaRestante();

      if(rafagaRestanteNuevoProceso >= rafagaRestanteProcesoActual){
        procesoAnterior=procesoActual;
        procesoActual = procesoActual.getSiguiente();
      }
      else{
        procesoAnterior.setSiguiente(procesoNuevo);
        procesoNuevo.setSiguiente(procesoActual);
        return procesoNuevo;
      }
    }
    procesoAnterior.setSiguiente(procesoNuevo);
    procesoNuevo.setSiguiente(this.getProcesoCabeza());

    return procesoNuevo;
  }

  public Proceso insertar(Proceso procesoNuevo) {
    ultimoAgregado = procesoNuevo;
    this.contador++;
    int prioridadNuevoProceso = procesoNuevo.getRafagaRestante();
    Proceso procesoActual = this.getProcesoCabeza().getSiguiente();
    Proceso procesoAnterior = this.getProcesoCabeza();

    // Si la cola está vacía y sólo está el cajero.
    if (this.getProcesoCabeza().getSiguiente() == this.getProcesoCabeza()) {
      this.getProcesoCabeza().setSiguiente(procesoNuevo);
      procesoNuevo.setSiguiente(this.getProcesoCabeza());
      return procesoNuevo;
    }

    while (procesoActual != this.getProcesoCabeza()) {

      int prioridadProcesoActual = procesoActual.getRafagaRestante();

      if(prioridadNuevoProceso >= prioridadProcesoActual){
        procesoAnterior=procesoActual;
        procesoActual = procesoActual.getSiguiente();
      }
      else{
        procesoAnterior.setSiguiente(procesoNuevo);
        procesoNuevo.setSiguiente(procesoActual);
        return procesoNuevo;
      }
    }
    procesoAnterior.setSiguiente(procesoNuevo);
    procesoNuevo.setSiguiente(this.getProcesoCabeza());

    return procesoNuevo;
  }

}