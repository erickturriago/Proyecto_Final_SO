package logica;

public class ListaBloqueados extends Lista{

	@Override
	public Proceso insertar() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	public void insertar(Proceso proceso) {
		proceso.setNombreProceso(proceso.getNombreProceso()+"'");
		Proceso procesoAux = this.getProcesoCabeza();
		Proceso procesoAnt = null;
		boolean flag = false;
		while(procesoAux.getSiguiente()!=this.getProcesoCabeza()) {
			if(procesoAux.getTiempoBloqueo()<proceso.getTiempoBloqueo()) {
				procesoAnt = procesoAux;
				procesoAux = procesoAux.getSiguiente();
			}else {
				procesoAnt.setSiguiente(proceso);
				proceso.setSiguiente(procesoAux);
				flag = true;
			}
		}
		if(!flag) {
			proceso.setSiguiente(this.getProcesoCabeza());
			procesoAux.setSiguiente(proceso);
		}
		this.tamano++;
		
	}

}
