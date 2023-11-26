package logica;

import java.util.ArrayList;
import java.util.List;

abstract class Lista{
  private Proceso procesoCabeza;
  int tamano = 0;
  Proceso ultimoAgregado;

  int contador = 1;
  private List<IOProceso> procesosObservadores = new ArrayList<>();

  public Lista() {
    this.procesoCabeza = new Proceso(tamano);
    this.procesoCabeza.setSiguiente(procesoCabeza);
  }

  public abstract Proceso insertar();

  public Proceso atender(){
    Proceso procesoAtendido = procesoCabeza.getSiguiente();
    procesoCabeza.setSiguiente(procesoAtendido.getSiguiente());
    this.tamano--;

    return procesoAtendido;
  }

  public void atender(Proceso procesoRemover){
    Proceso procesoAuxiliar = procesoCabeza;
    while (procesoAuxiliar.getSiguiente() != procesoCabeza) {
      if(procesoAuxiliar.getSiguiente()==procesoRemover){
        procesoAuxiliar.setSiguiente(procesoRemover.getSiguiente());
        tamano--;
        return;
      }
      procesoAuxiliar = procesoAuxiliar.getSiguiente();
    }
  }

  public Proceso getUltimoEnLista(){
    Proceso proceso = this.procesoCabeza.getSiguiente();
    if(proceso!=this.procesoCabeza){
      while(proceso.getSiguiente()!=this.procesoCabeza){
        proceso=proceso.getSiguiente();
      }
      return proceso;
    }
    return null;
  }

  public ArrayList<Proceso> listarProcesos(){
    ArrayList<Proceso> procesos = new ArrayList<Proceso>();
    Proceso procesoAuxiliar = procesoCabeza;
    while (procesoAuxiliar.getSiguiente() != procesoCabeza) {
      procesos.add(procesoAuxiliar.getSiguiente());
      procesoAuxiliar = procesoAuxiliar.getSiguiente();
    }
    return procesos;
  }

  public void imprimirLista(){
    String lista = "";
    Proceso procesoAuxiliar = procesoCabeza;
    while (procesoAuxiliar.getSiguiente() != procesoCabeza) {
      lista += " "+procesoAuxiliar.getSiguiente().getIdProceso();
      procesoAuxiliar = procesoAuxiliar.getSiguiente();
    }

    System.out.println(lista);
  }

  public Proceso getProcesoCabeza(){
    return this.procesoCabeza;
  }

  public Proceso getUltimoAgregado() {
    return ultimoAgregado;
  }


  public void agregarObservador(IOProceso observador) {
    procesosObservadores.add(observador);
  }

  public void eliminarObservador(IOProceso observador) {
    procesosObservadores.remove(observador);
  }

  public void notificarEnvejecimientoObservadores() {
    for (IOProceso observador : procesosObservadores) {
      observador.actualizarEnvejecimiento();
    }
  }


}