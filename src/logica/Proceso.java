package logica;

public class Proceso implements Cloneable,IOProceso{
  private Proceso siguiente;
  private String nombreProceso;
  private String nombreCola;
  private int idProceso,tiempoLlegada,rafaga,tiempoComienzo,tiempoFinal,tiempoRetorno,tiempoEspera;
  private String estado;
  private int rafagaRestante;
  private int tiempoBloqueo;
  private int tiempoBloqueoTabla = 0;
  private int rafagaEjecutadaTotal = 0;
  private int rafagaEjecutadaParcial = 0;
  private int tiempoEnvejecimiento = 10;
  private boolean quantumAlcanzado = false;

  public Proceso(int id){
    this.idProceso = id;
    this.nombreProceso = "P" + id;
    this.rafaga= (int)(Math.random()*10+1);
    this.rafagaRestante=this.rafaga;
    this.tiempoLlegada = (int)(Math.random()*10+1);
    this.estado= "Listo";
    this.tiempoBloqueo=0;
    this.tiempoEnvejecimiento = 10;
  }

  public int getIdNodo() {
    return idProceso;
  }

  public void setIdProceso(int idProceso) {
    this.idProceso = idProceso;
  }

  public Proceso getSiguiente() {
    return siguiente;
  }

  public void setSiguiente(Proceso siguiente) {
    this.siguiente = siguiente;
  }

  public String getNombreProceso() {
    return nombreProceso;
  }

  public void setNombreProceso(String nombreProceso) {
    this.nombreProceso = nombreProceso;
  }

  public int getIdProceso() {
    return idProceso;
  }

  public int getTiempoLlegada() {
    return tiempoLlegada;
  }

  public void setTiempoLlegada(int tiempoLlegada) {
    this.tiempoLlegada = tiempoLlegada;
  }

  public int getRafaga() {
    return rafaga;
  }

  public void setRafaga(int rafaga) {
    this.rafaga = rafaga;
  }

  public int getTiempoComienzo() {
    return tiempoComienzo;
  }

  public void setTiempoComienzo(int tiempoComienzo) {
    this.tiempoComienzo = tiempoComienzo;
  }

  public int getTiempoFinal() {
    return tiempoFinal;
  }

  public void setTiempoFinal(int tiempoFinal) {
    this.tiempoFinal = tiempoFinal;
  }

  public int getTiempoRetorno() {
    return tiempoRetorno;
  }

  public void setTiempoRetorno(int tiempoRetorno) {
    this.tiempoRetorno = tiempoRetorno;
  }

  public int getTiempoEspera() {
    return tiempoEspera;
  }

  public void setTiempoEspera(int tiempoEspera) {
    this.tiempoEspera = tiempoEspera;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public int getRafagaRestante() {
    return rafagaRestante;
  }

  public void setRafagaRestante(int rafagaRestante) {
    this.rafagaRestante = rafagaRestante;
  }

  public int getTiempoBloqueo() {
    return tiempoBloqueo;
  }

  public void setTiempoBloqueo(int tiempoBloqueo) {
    this.tiempoBloqueo = tiempoBloqueo;
  }

  public int getTiempoBloqueoTabla() {
    return tiempoBloqueoTabla;
  }

  public void setTiempoBloqueoTabla(int tiempoBloqueoTabla) {
    this.tiempoBloqueoTabla = tiempoBloqueoTabla;
  }

  public int getRafagaEjecutadaTotal() {
    return rafagaEjecutadaTotal;
  }

  public void setRafagaEjecutadaTotal(int rafagaEjecutadaTotal) {
    this.rafagaEjecutadaTotal = rafagaEjecutadaTotal;
  }

  public int getRafagaEjecutadaParcial() {
    return rafagaEjecutadaParcial;
  }

  public void setRafagaEjecutadaParcial(int rafagaEjecutadaParcial) {
    this.rafagaEjecutadaParcial = rafagaEjecutadaParcial;
  }

  public boolean isQuantumAlcanzado() {
    return quantumAlcanzado;
  }

  public void setQuantumAlcanzado(boolean quantumAlcanzado) {
    this.quantumAlcanzado = quantumAlcanzado;
  }

  public String getNombreCola() {
    return nombreCola;
  }

  public int getTiempoEnvejecimiento() {
    return tiempoEnvejecimiento;
  }

  public void setNombreCola(String nombreCola) {
    this.nombreCola = nombreCola;
  }

  public void setTiempoEnvejecimiento(int tiempoEnvejecimiento) {
    this.tiempoEnvejecimiento = tiempoEnvejecimiento;
  }

  @Override
  public Object clone() {
    try {
      Proceso cloned = (Proceso) super.clone();
      // Clonar las propiedades específicas de la clase
      cloned.nombreProceso = this.nombreProceso;
      cloned.idProceso = this.idProceso;
      cloned.tiempoLlegada = this.tiempoLlegada;
      cloned.rafaga = this.rafaga;
      cloned.tiempoComienzo = this.tiempoComienzo;
      cloned.tiempoFinal = this.tiempoFinal;
      cloned.tiempoRetorno = this.tiempoRetorno;
      cloned.tiempoEspera = this.tiempoEspera;
      cloned.estado = this.estado;
      cloned.rafagaRestante = this.rafagaRestante;
      cloned.tiempoBloqueo = this.tiempoBloqueo;
      cloned.tiempoBloqueoTabla = this.tiempoBloqueoTabla;
      cloned.rafagaEjecutadaTotal = this.rafagaEjecutadaTotal;
      cloned.rafagaEjecutadaParcial = this.rafagaEjecutadaParcial;
      // Devuelve la copia del objeto
      return cloned;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(e); // Nunca debería ocurrir
    }
  }


  @Override
  public void actualizarEnvejecimiento() {
    this.tiempoEnvejecimiento--;
    //System.out.println("Disminuyendo proceso "+nombreProceso+" cola: "+nombreCola);
    //System.out.println("Proceso: "+this.nombreProceso +" Envejecimiento: "+this.tiempoEnvejecimiento);
  }
}