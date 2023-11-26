import logica.Lista;
import presentacion.Modelo;
import presentacion.VentanaPrincipal;

class Main {
  private Modelo miApp;
  public Main() {
    miApp = new Modelo();
    miApp.iniciar();
  }

  public static void main(String[] args) {
    new Main();
  }
}