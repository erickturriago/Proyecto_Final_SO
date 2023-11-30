package presentacion;

import logica.Despachador;
import logica.Proceso;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import java.awt.*;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Modelo implements Cloneable{

    int contadorReloj =0;
    int cicloReloj = 1000;
    private Despachador despachador = new Despachador();
    private ArrayList<Proceso> procesosTabla = new ArrayList<>();
    private VentanaPrincipal ventanaPrincipal;
    private Proceso procesoActual;
    Thread hiloAtender;
    Thread hiloPintarTabla;
    Thread hiloPintarCola;
    Thread hiloPintarDiagramaGantt;
    Thread hiloAtencionBloqueados;
    boolean sistemaActivo = false;

    int turno = 0;

    public TaskSeriesCollection model;

    public void iniciar(){
        getVistaPrincipal().setSize(1920, 1080);
        getVistaPrincipal().setVisible(true);
        getVistaPrincipal().setLocationRelativeTo(null);

        this.pintarDiagramaGantt();
        this.insertarClientesIniciales();
    }

    public void agregarProceso(){
        Proceso procesoInsertado = despachador.insertarProcesoAleatorio();
        procesoInsertado.setTiempoLlegada(this.contadorReloj);
        pintarTabla();
    }
    public void insertarClientesIniciales(){
        //int cantClientes = (int)(Math.random()*10+1);
        int cantClientes = 0;
        for(int i=0;i<cantClientes;i++){
            this.agregarProceso();
            //insertarTabla(listaTurnos.getUltimoAgregado());
        }
    }

    public void iniciarAtencion(){
        hiloAtender = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo()){
                    Proceso procesoActual = despachador.getProceso();
                    //System.out.println("sistema activo");
                    if(procesoActual != null){

                        System.out.println("-------------------------------------------------------------------------------------------------------------");

                        String nombreProceso = procesoActual.getNombreProceso();
                        String nombreCola = procesoActual.getNombreCola();
                        System.out.println("Atendiendo proceso: "+ nombreProceso+" Cola: "+ nombreCola+ " Rafaga restante: "+procesoActual.getRafagaRestante());
                        getVistaPrincipal().actualizarSemaforo("Rojo");

                        String nombreColaProceso = procesoActual.getNombreCola();
                        int rafagaAEjecutar = procesoActual.getRafagaRestante();

                        procesoActual.setRafagaEjecutadaParcial(0);
                        procesoActual.setRafagaRestante(procesoActual.getRafaga() - procesoActual.getRafagaEjecutadaTotal());
                        procesoActual.setTiempoComienzo(contadorReloj);

                        insertarTabla(procesoActual);
                        //pintarTabla();

                        for(int i=0;i<rafagaAEjecutar;i++){

                            try {
                                Thread.sleep(cicloReloj);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            contadorReloj++;

                            System.out.println("Ciclo "+(i+1));
                            System.out.println("Reloj: "+contadorReloj);

                            ventanaPrincipal.getLabelContadorCiclo().setText(""+contadorReloj);
                            procesoActual.setEstado("En ejecucion");
                            actualizarTabla(procesoActual);
                            procesoActual.setRafagaEjecutadaTotal(procesoActual.getRafagaEjecutadaTotal()+1);
                            procesoActual.setRafagaEjecutadaParcial(procesoActual.getRafagaEjecutadaParcial()+1);
                            procesoActual.setRafagaRestante(procesoActual.getRafaga() - procesoActual.getRafagaEjecutadaTotal());



                            //Comprobacion algoritmo RR (Cuantum)
                            if(nombreColaProceso.equals("RR")){
                                //System.out.println(procesoActual.getNombreProceso() + " expulsado ");
                                if ( procesoActual.getRafagaEjecutadaParcial() == 4 && procesoActual.getRafagaRestante()>0){
                                    procesoActual.setQuantumAlcanzado(true);
                                    System.out.println("Proceso expulsado por Quantum - Rafaga restante: "+procesoActual.getRafagaRestante() + " Rafaga parciaL "+procesoActual.getRafagaEjecutadaParcial());
                                    System.out.println("Rafaga: "+procesoActual.getRafaga() + " RafagaTotal: "+procesoActual.getRafagaEjecutadaTotal());
                                    procesoActual.setEstado("Expulsado R");
                                    break;
                                }
                            }

                            int randomBloqueo = (int)(Math.random()*100+1);
                            //System.out.println("For ciclo: "+(i+1));
                            if(randomBloqueo % 27  == 0 && procesoActual.getRafagaRestante()>0){
                                int tiempoBloqueo = (int)(Math.random()*8+4);
                                procesoActual.setTiempoBloqueo(tiempoBloqueo);
                                procesoActual.setEstado("Bloqueado");
                                System.out.println("Proceso bloqueado");
                                break;
                            }


                            //Comprobar si llegó un proceso de mayor prioridad
                            if(despachador.checkProcesoMayorPrioridad(procesoActual)){
                                System.out.println("Llegó proceso con mayor prioridad");
                                procesoActual.setEstado("Expulsado P");
                                break;
                            }

                            //System.out.println("Ciclo: "+(i+1));
                            despachador.actualizarEnvejecimiento();
                            //despachador.actualizarListaBloqueados();
                            pintarDiagramaGantt();
                            pintarTabla();

                        }
                        //Calculos de tiempos
                        procesoActual.setTiempoFinal(procesoActual.getRafagaEjecutadaParcial() + procesoActual.getTiempoComienzo());
                        procesoActual.setTiempoRetorno(procesoActual.getTiempoFinal() - procesoActual.getTiempoLlegada());
                        procesoActual.setTiempoEspera(procesoActual.getTiempoRetorno() - procesoActual.getRafagaEjecutadaTotal());

                        procesoActual.setRafagaEjecutadaParcial(0);

                        if(procesoActual.getEstado() == "Bloqueado"){
                            System.out.println("Enviando a la cola de bloqueados");
                            despachador.getListaBloqueados().insertar(procesoActual);
                            despachador.getListaBloqueados().imprimirLista();
                            //despachador.getListaBloqueados().insertar((Proceso) procesoActual.clone());
                        }
                        else if(procesoActual.getEstado() == "Expulsado P" || procesoActual.getEstado() == "Expulsado R"){
                            System.out.println("Retornando a la cola de origen (Expulsado)");
                            retornarProcesoCola(procesoActual);
                        }
                        else{
                            System.out.println("Proceso terminó sin problema");
                            procesoActual.setEstado("Terminado");
                        }

                        actualizarTabla(procesoActual);
                    }
                    else{
                        System.out.println("No hay procesos por atender");
                        getVistaPrincipal().actualizarSemaforo("Verde");

                        try{
                            Thread.sleep(cicloReloj);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        contadorReloj++;
                        //despachador.actualizarListaBloqueados();
                        pintarDiagramaGantt();
                        pintarTabla();
                    }
                }
            }
        });
        hiloAtender.start();
    }

    public void retornarProcesoCola(Proceso procesoRetorno){

        Proceso procesoInsertar = (Proceso) procesoRetorno.clone();

        procesoInsertar.setNombreProceso(procesoInsertar.getNombreProceso()+"*");
        //procesoInsertar.setRafaga(procesoRetorno.getRafagaRestante());
        procesoInsertar.setTiempoLlegada(procesoRetorno.getTiempoLlegada());
        procesoInsertar.setNombreCola(procesoRetorno.getNombreCola());
        procesoInsertar.setEstado("Listo");

        procesoInsertar.setTiempoEspera(0);
        procesoInsertar.setTiempoRetorno(0);
        procesoInsertar.setTiempoComienzo(0);
        procesoInsertar.setTiempoFinal(0);

        despachador.insertarProcesoEspecifico(procesoInsertar);
    }

    public void insertarTabla(Proceso procesoInsertar){
        Proceso procesoCopia = (Proceso) procesoInsertar.clone();
        procesoCopia.setEstado("Listo");
        procesoCopia.setTiempoEspera(0);
        procesoCopia.setTiempoRetorno(0);
        procesoCopia.setTiempoComienzo(0);
        procesoCopia.setTiempoFinal(0);

        this.procesosTabla.add(procesoCopia);
    }

    public void actualizarTabla(Proceso procesoActualizar){

        for(int i=0;i<procesosTabla.size();i++){
            Proceso proceso = procesosTabla.get(i);

            if(proceso.getNombreProceso() == procesoActualizar.getNombreProceso()){
                proceso.setTiempoComienzo(procesoActualizar.getTiempoComienzo());
                proceso.setTiempoFinal(procesoActualizar.getTiempoFinal());
                proceso.setTiempoRetorno(procesoActualizar.getTiempoRetorno());
                proceso.setTiempoEspera(procesoActualizar.getTiempoEspera());
                proceso.setEstado(procesoActualizar.getEstado());
                return;
            }
        }
    }

    public void iniciarAtencionBloqueados(){
        hiloAtencionBloqueados = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo()){
                    try {
                        Thread.sleep(cicloReloj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Proceso> procesosBloqueados = despachador.getListaBloqueados().listarProcesos();
                    for(int i = 0; i< procesosBloqueados.size(); i++){
                        Proceso proceso = procesosBloqueados.get(i);
                        if(proceso.getTiempoBloqueo()>1){
                            proceso.setTiempoBloqueo(proceso.getTiempoBloqueo()-1);
                        }
                        else{
                            Proceso clon = (Proceso) proceso.clone();
                            clon.setEstado("Listo");
                            //System.out.println("ID proceso clone: "+clon.getIdProceso());
                            clon.setTiempoComienzo(0);
                            clon.setTiempoFinal(0);
                            clon.setTiempoRetorno(0);
                            clon.setTiempoEspera(0);
                            clon.setNombreProceso(proceso.getNombreProceso()+"'");
                            clon.setTiempoLlegada(proceso.getTiempoLlegada());

                            despachador.getListaBloqueados().atender(proceso);
                            despachador.insertarProcesoEspecifico(clon);
                            pintarTabla();

                            //insertarTabla(clon);
                        }
                    }
                }
            }
        });
        hiloAtencionBloqueados.start();
    }

    public void pintarTabla(){

        //Pintar tabla cola RR
        ArrayList<Proceso> procesosRR = despachador.getListaColaRR().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola1().setRowCount(0);
        for(int i = 0; i< procesosRR.size(); i++){
            this.getVistaPrincipal().getModelTablaCola1().addRow(new Object[]{
                    procesosRR.get(i).getNombreProceso(),
                    procesosRR.get(i).getTiempoLlegada(),
                    procesosRR.get(i).getRafagaRestante(),
                    procesosRR.get(i).getTiempoEnvejecimiento()
            });
        }

        //Pintar tabla cola FCFS
        ArrayList<Proceso> procesosFCFS = despachador.getListaColaFCFS().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola2().setRowCount(0);
        for(int i = 0; i< procesosFCFS.size(); i++){
            this.getVistaPrincipal().getModelTablaCola2().addRow(new Object[]{
                    procesosFCFS.get(i).getNombreProceso(),
                    procesosFCFS.get(i).getTiempoLlegada(),
                    procesosFCFS.get(i).getRafagaRestante(),
                    procesosFCFS.get(i).getTiempoEnvejecimiento()
            });
        }

        //Pintar tabla cola SRTF
        ArrayList<Proceso> procesosSRTF = despachador.getListaColaSRTF().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola3().setRowCount(0);
        for(int i = 0; i< procesosSRTF.size(); i++){
            this.getVistaPrincipal().getModelTablaCola3().addRow(new Object[]{
                    procesosSRTF.get(i).getNombreProceso(),
                    procesosSRTF.get(i).getTiempoLlegada(),
                    procesosSRTF.get(i).getRafagaRestante(),
                    procesosSRTF.get(i).getTiempoEnvejecimiento()
            });
        }
        
        ArrayList<Proceso> procesosBloqueados = despachador.getListaBloqueados().listarProcesos();
        this.getVistaPrincipal().getModelTablaBloqueado().setRowCount(0);
        for(int i = 0; i< procesosBloqueados.size(); i++) {
        	this.getVistaPrincipal().getModelTablaBloqueado().addRow(new Object[] {
        			procesosBloqueados.get(i).getNombreProceso(),
        			procesosBloqueados.get(i).getTiempoBloqueo(),
        			procesosBloqueados.get(i).getRafagaRestante(),
        			procesosBloqueados.get(i).getTiempoEnvejecimiento()
        	});
        }

        this.getVistaPrincipal().getModelTablaTiempos().setRowCount(0);
        for(int i = 0; i< procesosTabla.size(); i++){
            this.getVistaPrincipal().getModelTablaTiempos().addRow(new Object[]{
                    procesosTabla.get(i).getNombreProceso(),
                    procesosTabla.get(i).getTiempoLlegada(),
                    procesosTabla.get(i).getRafagaRestante(),
                    procesosTabla.get(i).getTiempoComienzo(),
                    procesosTabla.get(i).getTiempoFinal(),
                    procesosTabla.get(i).getTiempoRetorno(),
                    procesosTabla.get(i).getTiempoEspera(),
                    procesosTabla.get(i).getEstado(),
                    procesosTabla.get(i).getNombreCola()
            });
        }
    }

    public void pintarDiagramaGantt(){
        final IntervalCategoryDataset dataset = createSampleDataset();
        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
                "Diagrama de Gantt", // chart title
                "procesos", // domain axis label
                "tiempo", // range axis label
                dataset, // data
                false, // include legend
                true, // tooltips
                false // urls
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        DateAxis range = (DateAxis) plot.getRangeAxis();
        range.setDateFormatOverride(new SimpleDateFormat("S"));
        range.setMaximumDate(new Date(100 + (contadorReloj/100)*100));

        // insertando diagrama de gantt al panel
        getVistaPrincipal().getChartPanel().setChart(chart);

        //GanttRenderer personnalisÃ©..
        MyRenderer renderer = new MyRenderer(model);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        getVistaPrincipal().getChartPanel().repaint();
    }
    private IntervalCategoryDataset createSampleDataset() {
        model = new TaskSeriesCollection();
        final TaskSeries s = new TaskSeries("");
        //System.out.println("tamano :"+s.getTasks().size());
        for(int i=0; i<procesosTabla.size(); i++){
            //System.out.println("proceso :" + procesosTabla.get(i).getNombreProceso() + " estado: " + procesosTabla.get(i).getEstado());
        }
        for(int i=0; i<procesosTabla.size(); i++){
            //System.out.println(procesosTabla.get(i).getNombreProceso() + " " + procesosTabla.get(i).getEstado());
            if(procesosTabla.get(i).getEstado()=="Listo"){
                final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                final Task t2 = new Task("ejecutando", new SimpleTimePeriod(0, 0));
                t.addSubtask(t1);
                t.addSubtask(t2);
                s.add(t);
            }
            if(procesosTabla.get(i).getEstado()=="En ejecucion"){
                //System.out.println("Pintando proceso en ejecucion");
                final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), procesosTabla.get(i).getTiempoComienzo()));
                final Task t2 = new Task("ejecutando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoComienzo(), contadorReloj));
                t.addSubtask(t1);
                t.addSubtask(t2);
                s.add(t);
            }
            if(procesosTabla.get(i).getEstado()=="Bloqueado"){
                //System.out.println("Pintando proceso en ejecucion");
                final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), procesosTabla.get(i).getTiempoFinal()));
                final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), procesosTabla.get(i).getTiempoComienzo()));
                final Task t2 = new Task("ejecutando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoComienzo(), procesosTabla.get(i).getTiempoFinal()));
                //System.out.println("rafaga ejecutada parcial del proceso Bloqueado: "+procesosTabla.get(i).getTiempoComienzo() + procesosTabla.get(i).getRafagaEjecutadaParcial());
                t.addSubtask(t1);
                t.addSubtask(t2);
                s.add(t);
            }
            if(procesosTabla.get(i).getEstado()=="Terminado" || procesosTabla.get(i).getEstado()=="Expulsado R" || procesosTabla.get(i).getEstado()=="Expulsado P"){
                final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(),procesosTabla.get(i).getTiempoComienzo()));
                final Task t2 = new Task("ejecutando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoComienzo(), procesosTabla.get(i).getTiempoFinal()));
                t.addSubtask(t1);
                t.addSubtask(t2);
                s.add(t);
            }
        }

        model.add(s);
    
        //model.removeAll();
        return model;
    }

    public void insertCola1(){
        despachador.insertarColaRR(this.contadorReloj);
        pintarTabla();
    }
    public void insertCola2(){
        despachador.insertarColaFCFS(this.contadorReloj);
        pintarTabla();
    }
    public void insertCola3(){
        despachador.insertarColaSRTF(this.contadorReloj);
        pintarTabla();
    }

    public boolean isSistemaActivo() {
        return sistemaActivo;
    }

    public void setSistemaActivo(boolean sistemaActivo) {
        this.sistemaActivo = sistemaActivo;
    }


    public void resetHilos(){
        this.hiloPintarTabla=null;
        this.hiloPintarCola=null;
        this.hiloAtender=null;
        this.hiloAtencionBloqueados=null;


        this.hiloPintarDiagramaGantt=null;
    }

    public VentanaPrincipal getVistaPrincipal() {
        if(ventanaPrincipal == null){
            ventanaPrincipal = new VentanaPrincipal(this);
        }
        return ventanaPrincipal;
    }

}
