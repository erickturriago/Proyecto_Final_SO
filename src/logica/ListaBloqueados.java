package logica;

public class ListaBloqueados extends Lista{

	@Override
	public Proceso insertar() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	public void insertar(Proceso procesoNuevo) {
		tamano++;
		Proceso procesoAuxiliar = this.getProcesoCabeza();
		while (procesoAuxiliar.getSiguiente() != this.getProcesoCabeza()) {
			procesoAuxiliar = procesoAuxiliar.getSiguiente();
		}
		procesoAuxiliar.setSiguiente(procesoNuevo);
		procesoNuevo.setSiguiente(this.getProcesoCabeza());

		this.agregarObservador(procesoNuevo);

	}

}
