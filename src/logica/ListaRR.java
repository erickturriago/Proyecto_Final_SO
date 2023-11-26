package logica;

public class ListaRR extends Lista{
  @Override
  public Proceso insertar() {
    tamano++;
    Proceso procesoNuevo = new Proceso(++contador);
    Proceso procesoAuxiliar = this.getProcesoCabeza();
    while (procesoAuxiliar.getSiguiente() != this.getProcesoCabeza()) {
      procesoAuxiliar = procesoAuxiliar.getSiguiente();
    }
    procesoAuxiliar.setSiguiente(procesoNuevo);
    procesoNuevo.setSiguiente(this.getProcesoCabeza());
    this.ultimoAgregado = procesoNuevo;

    return procesoNuevo;

  }

  public Proceso insertar(Proceso procesoNuevo) {
    tamano++;
    Proceso procesoAuxiliar = this.getProcesoCabeza();
    while (procesoAuxiliar.getSiguiente() != this.getProcesoCabeza()) {
      procesoAuxiliar = procesoAuxiliar.getSiguiente();
    }
    procesoAuxiliar.setSiguiente(procesoNuevo);
    procesoNuevo.setSiguiente(this.getProcesoCabeza());
    ultimoAgregado=procesoNuevo;

    return procesoNuevo;
  }
}