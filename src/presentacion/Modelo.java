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

        // getVistaPrincipal().getPanelCola().setBackground(Color.red);
        //getVistaPrincipal().getPanelTabla().setBackground(Color.blue);
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
        int cantClientes = 14;
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
                    System.out.println("Atención iniciada");
                }
            }
        });
        hiloAtender.start();
    }

    public void actualizarSeccionCritica(Proceso proceso){
        if(proceso == null){
            getVistaPrincipal().getLabelSeccionCritica().setBackground(Color.green);
            getVistaPrincipal().getLabelSeccionCritica().setText("Sec. Critica Libre");
            getVistaPrincipal().getLabelSeccionCritica().setForeground(Color.black);
        }
        else{
            getVistaPrincipal().getLabelSeccionCritica().setBackground(Color.red);
            getVistaPrincipal().getLabelSeccionCritica().setText(proceso.getNombreProceso()+" en Sec. Critica");
            getVistaPrincipal().getLabelSeccionCritica().setForeground(Color.white);
        }

    }

    public void insertarTabla(Proceso procesoInsertar){
        Proceso procesoCopia = (Proceso) procesoInsertar.clone();
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

    public void insertarPorQuantum(Proceso proceso){

        Proceso clon = (Proceso) proceso.clone();

        clon.setEstado("Listo");
        //System.out.println("ID proceso clone: "+clon.getIdProceso());
        clon.setTiempoComienzo(0);
        clon.setTiempoFinal(0);
        clon.setTiempoRetorno(0);
        clon.setTiempoEspera(0);
        clon.setNombreProceso(proceso.getNombreProceso()+"*");
        clon.setQuantumAlcanzado(false);

        if(clon.getRafagaRestante()>0){
            //listaTurnos.insertar(clon);
            insertarTabla(clon);
        }



    }

    public void iniciarAtencionBloqueados(){
        hiloAtencionBloqueados = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo()){
                    System.out.println("Gestion bloqueados");
                }
            }
        });
        hiloAtencionBloqueados.start();
    }

    public void iniciarPintarCola(){
        hiloPintarCola = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo()){
                    try {
                        Thread.sleep(cicloReloj); // Agregar un retraso de 200 ms entre cada elemento

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pintarCola();
                }
            }
        });
        hiloPintarCola.start();
    }

    public void iniciarPintarTabla(){
        hiloPintarTabla = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo() ){
                    try {

                        Thread.sleep(cicloReloj); // Agregar un retraso de 200 ms entre cada elemento

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pintarTabla();
                }
            }
        });
        hiloPintarTabla.start();
    }
    public void iniciarDiagramaGantt(){
        hiloPintarDiagramaGantt = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isSistemaActivo() ){
                    try {

                        Thread.sleep(cicloReloj); // Agregar un retraso de 200 ms entre cada elemento

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pintarDiagramaGantt();
                }
            }
        });
        hiloPintarDiagramaGantt.start();
    }

    public void atenderCliente(){
        int cantidadAtender = Integer.parseInt(this.getVistaPrincipal().getTxtCantAtender().getText());
        for(int i=0;i<cantidadAtender;i++){
            //this.listaTurnos.atender();
        }
        this.pintarCola();
        this.pintarTabla();

    }

    public void pintarTabla(){

        //Pintar tabla cola RR
        ArrayList<Proceso> procesosRR = despachador.getListaColaRR().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola1().setRowCount(0);
        for(int i = 0; i< procesosRR.size(); i++){
            this.getVistaPrincipal().getModelTablaCola1().addRow(new Object[]{
                    procesosRR.get(i).getNombreProceso(),
                    procesosRR.get(i).getTiempoLlegada(),
                    procesosRR.get(i).getRafagaRestante()
            });
        }

        //Pintar tabla cola FCFS
        ArrayList<Proceso> procesosFCFS = despachador.getListaColaFCFS().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola2().setRowCount(0);
        for(int i = 0; i< procesosFCFS.size(); i++){
            this.getVistaPrincipal().getModelTablaCola2().addRow(new Object[]{
                    procesosFCFS.get(i).getNombreProceso(),
                    procesosFCFS.get(i).getTiempoLlegada(),
                    procesosFCFS.get(i).getRafagaRestante()
            });
        }

        //Pintar tabla cola SRTF
        ArrayList<Proceso> procesosSRTF = despachador.getListaColaSRTF().listarProcesos();
        this.getVistaPrincipal().getModelTablaCola3().setRowCount(0);
        for(int i = 0; i< procesosSRTF.size(); i++){
            this.getVistaPrincipal().getModelTablaCola3().addRow(new Object[]{
                    procesosSRTF.get(i).getNombreProceso(),
                    procesosSRTF.get(i).getTiempoLlegada(),
                    procesosSRTF.get(i).getRafagaRestante()
            });
        }
    }

    public void pintarCola(){
        System.out.println("Pintar cola");
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
            //System.out.println("proceso :" + procesosIngresados.get(i)[0] + " estado: " + procesosIngresados.get(i)[7]);
        }
        if(procesoActual!=null){
            for(int i=0; i<procesosTabla.size(); i++){
                System.out.println(procesosTabla.get(i).getNombreProceso() + " " + procesosTabla.get(i).getEstado());
                if(procesosTabla.get(i).getEstado()=="Listo"){
                    final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                    final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                    final Task t2 = new Task("ejecutando", new SimpleTimePeriod(0, 0));
                    t.addSubtask(t1);
                    t.addSubtask(t2);
                    s.add(t);
                }
                if(procesosTabla.get(i).getEstado()=="En ejecución"){
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
                    System.out.println("rafaga ejecutada parcial del proceso Bloqueado: "+procesosTabla.get(i).getTiempoComienzo() + procesosTabla.get(i).getRafagaEjecutadaParcial());
                    t.addSubtask(t1);
                    t.addSubtask(t2);
                    s.add(t);
                }
                if(procesosTabla.get(i).getEstado()=="Terminado" || procesosTabla.get(i).getEstado()=="Expulsado"){
                    final Task t = new Task(""+procesosTabla.get(i).getNombreProceso(), new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(), contadorReloj));
                    final Task t1 = new Task("esperando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoLlegada(),procesosTabla.get(i).getTiempoComienzo()));
                    final Task t2 = new Task("ejecutando", new SimpleTimePeriod(procesosTabla.get(i).getTiempoComienzo(), procesosTabla.get(i).getTiempoFinal()));
                    t.addSubtask(t1);
                    t.addSubtask(t2);
                    s.add(t);
                }
            }

            model.add(s);
        }
        //model.removeAll();
        return model;
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
