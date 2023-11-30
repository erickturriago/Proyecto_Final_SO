package presentacion;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class VentanaPrincipal extends ApplicationFrame {

    private Modelo modelo;
    private ControladorVentanaPrincipal control;

    ChartPanel chartPanel;
    private JPanel panelCola;
    private JPanel panelTabla;
    private JPanel diagramaGantt;
    private JTextField txtCantClientes;
    private JTextField txtCantAtender;
    private JButton btnAgregarProceso;
    private JButton btnAtender;
    private JButton btnIniciarDetener;

    private JButton insertCola1,insertCola2,insertCola3;
    DefaultTableModel modelTablaTiempos,modelTablaBloqueado,modelTablaCola1,modelTablaCola2,modelTablaCola3;
    JTable tablaCola1;
    JTable tablaCola2;
    JTable tablaCola3;
    JTable tablaTiempos;
    JTable tablaBloqueados;
    Font fontButton;
    Font fontLabel;
    JScrollPane scrollPaneTiempos;
    JLabel labelCajero;
    JLabel labelAccion;
    JLabel labelSeccionCritica;
    JLabel labelSemaforo;
    JLabel labelCola1,labelCola2,labelCola3,labelColaTiempos,labelColaBloqueados;


    private JLabel labelContadorCiclo;
    public VentanaPrincipal(Modelo modelo) {
        super("Sistema Cola Cajero");
        this.modelo=modelo;
        // Establecer el título de la ventana
        this.setLayout(null);
        inicializarComponentes();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        capturarEventos();
    }

    public Modelo getModelo() {
        return modelo;
    }

    public ControladorVentanaPrincipal getControl() {
        if(control == null){
            control = new ControladorVentanaPrincipal(this);
        }
        return control;
    }

    public void inicializarComponentes(){



        this.fontButton = new Font("Roboto", Font.BOLD, 15);
        this.fontLabel = new Font("Roboto", Font.BOLD, 25);

        //Botones

        this.btnAtender = new JButton("ATENDER");
        this.btnAtender.setBounds(440,750,120,40);
        this.btnAtender.setFont(fontButton);

        this.btnAgregarProceso = new JButton("AGREGAR");
        this.btnAgregarProceso.setBounds(900,415,120,40);
        this.btnAgregarProceso.setFont(fontButton);

        this.btnIniciarDetener = new JButton("INICIAR");
        this.btnIniciarDetener.setBounds(900,465,120,40);
        this.btnIniciarDetener.setFont(fontButton);


        this.insertCola1 = new JButton("insertCola1");
        this.insertCola1.setBounds(50,580,120,40);
        this.insertCola1.setFont(fontButton);

        this.insertCola2 = new JButton("insertCola2");
        this.insertCola2.setBounds(528,580,120,40);
        this.insertCola2.setFont(fontButton);

        this.insertCola3 = new JButton("insertCola3");
        this.insertCola3.setBounds(996,580,120,40);
        this.insertCola3.setFont(fontButton);

        //TextFields

        this.txtCantAtender = new JTextField();
        this.txtCantAtender.setBounds(370,750,50,40);
        this.txtCantAtender.setHorizontalAlignment(JTextField.CENTER);
        this.txtCantAtender.setText("1");
        this.txtCantAtender.setFont(this.fontButton);

        this.txtCantClientes = new JTextField();
        this.txtCantClientes.setBounds(370,800,50,40);
        this.txtCantClientes.setHorizontalAlignment(JTextField.CENTER);
        this.txtCantClientes.setText("1");
        this.txtCantClientes.setFont(this.fontButton);

        //Label
        this.labelSemaforo = new JLabel();
        this.labelSemaforo.setBounds(910,140,150,200);
        this.labelSemaforo.setOpaque(true);
        this.labelSemaforo.setIcon(new ImageIcon(getClass().getResource("/imagenes/semaforo_verde.png")));

        //Label
        this.labelCajero = new JLabel();
        this.labelCajero.setBounds(150,530,80,80);
        this.labelCajero.setOpaque(true);
        this.labelCajero.setIcon(new ImageIcon(getClass().getResource("/imagenes/atm.png")));

        this.labelAccion = new JLabel();
        this.labelAccion.setBounds(460,900,120,40);
        this.labelAccion.setFont(this.fontLabel);
        this.labelAccion.setHorizontalTextPosition(SwingConstants.CENTER);
        this.labelAccion.setText("Tiempo");

        this.labelContadorCiclo = new JLabel();
        this.labelContadorCiclo.setBounds(490, 940, 120, 40);
        this.getLabelContadorCiclo().setFont(this.fontLabel);
        this.labelContadorCiclo.setHorizontalTextPosition(SwingConstants.CENTER);

        this.labelSeccionCritica = new JLabel("Sec. Critica Libre");
        this.labelSeccionCritica.setHorizontalAlignment(SwingConstants.CENTER);
        this.labelSeccionCritica.setVerticalAlignment(SwingConstants.CENTER);
        this.labelSeccionCritica.setBounds(440,730,120,40);
        this.labelSeccionCritica.setOpaque(true);
        this.labelSeccionCritica.setBackground(Color.green);

        this.labelCola1 = new JLabel();
        this.labelCola1.setBounds(210,590,160,40);
        this.labelCola1.setFont(this.fontLabel);
        this.labelCola1.setHorizontalTextPosition(SwingConstants.CENTER);
        this.labelCola1.setText("1 - RR");

        this.labelCola2 = new JLabel();
        this.labelCola2.setBounds(688,590,160,40);
        this.labelCola2.setFont(this.fontLabel);
        this.labelCola2.setHorizontalTextPosition(SwingConstants.CENTER);
        this.labelCola2.setText("2 - FCFS");

        this.labelCola3 = new JLabel();
        this.labelCola3.setBounds(1156,590,160,40);
        this.labelCola3.setFont(this.fontLabel);
        this.labelCola3.setHorizontalTextPosition(SwingConstants.CENTER);
        this.labelCola3.setText("3 - SRTF");

        this.labelColaBloqueados = new JLabel();
        this.labelColaBloqueados.setBounds(1574,590,250,40);
        this.labelColaBloqueados.setFont(this.fontLabel);
        this.labelColaBloqueados.setHorizontalTextPosition(SwingConstants.CENTER);
        this.labelColaBloqueados.setText("Cola Bloqueados");


        //Diagrama de Gant

        JFreeChart chart = ChartFactory.createGanttChart(
                "Diagrama de Gantt", // chart title
                "Procesos", // domain axis label
                "Tiempo", // range axis label
                null, // data
                false, // include legend
                true, // tooltips
                false // urls
        );

        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.setBounds(1050, 60, 800, 450);

        //Tablas

        //Formato de celda con texto centrado
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        //Tabla de procesos principal
        modelTablaTiempos = new DefaultTableModel();

        modelTablaTiempos.addColumn("Proceso");
        modelTablaTiempos.addColumn("T.Llegada");
        modelTablaTiempos.addColumn("Rafaga");
        modelTablaTiempos.addColumn("T.Comienzo");
        modelTablaTiempos.addColumn("T.Final");
        modelTablaTiempos.addColumn("T.Retorno");
        modelTablaTiempos.addColumn("T.Espera");
        modelTablaTiempos.addColumn("Estado");
        modelTablaTiempos.addColumn("C. Origen");

        tablaTiempos = new JTable(modelTablaTiempos);
        tablaTiempos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaTiempos.setBackground(Color.white);

        TableColumnModel columnModelTiempos = tablaTiempos.getColumnModel();

        for (int i = 0; i < tablaTiempos.getColumnCount(); i++) {
            columnModelTiempos.getColumn(i).setPreferredWidth(87);
            tablaTiempos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPaneTiempos = new JScrollPane(tablaTiempos);
        scrollPaneTiempos.setBounds(50,60,800,450);

        //Tablas colas y bloqueo
        modelTablaCola1 = new DefaultTableModel();
        modelTablaCola2 = new DefaultTableModel();
        modelTablaCola3 = new DefaultTableModel();
        modelTablaBloqueado = new DefaultTableModel();

        modelTablaCola1.addColumn("Proceso");
        modelTablaCola1.addColumn("T.Llegada");
        modelTablaCola1.addColumn("Rafaga");
        modelTablaCola1.addColumn("Envejecimiento");

        modelTablaCola2.addColumn("Proceso");
        modelTablaCola2.addColumn("T.Llegada");
        modelTablaCola2.addColumn("Rafaga");
        modelTablaCola2.addColumn("Envejecimiento");

        modelTablaCola3.addColumn("Proceso");
        modelTablaCola3.addColumn("T.Llegada");
        modelTablaCola3.addColumn("Rafaga");
        modelTablaCola3.addColumn("Envejecimiento");

        modelTablaBloqueado.addColumn("Proceso");
        modelTablaBloqueado.addColumn("T. Bloqueo");
        modelTablaBloqueado.addColumn("Rafaga");
        modelTablaBloqueado.addColumn("Envejecimiento");

        tablaCola1 = new JTable(modelTablaCola1);
        tablaCola2 = new JTable(modelTablaCola2);
        tablaCola3 = new JTable(modelTablaCola3);
        tablaBloqueados = new JTable(modelTablaBloqueado);

        tablaCola1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaCola2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaCola3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaBloqueados.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        tablaCola1.setBackground(Color.white);
        tablaCola2.setBackground(Color.white);
        tablaCola3.setBackground(Color.white);
        tablaBloqueados.setBackground(Color.white);

        TableColumnModel columnModelCola1 = tablaCola1.getColumnModel();
        TableColumnModel columnModelCola2 = tablaCola2.getColumnModel();
        TableColumnModel columnModelCola3 = tablaCola3.getColumnModel();
        TableColumnModel columnModelBloqueados = tablaBloqueados.getColumnModel();

        for (int i = 0; i < tablaCola1.getColumnCount(); i++) {
            columnModelCola1.getColumn(i).setPreferredWidth(98);
            columnModelCola2.getColumn(i).setPreferredWidth(98);
            columnModelCola3.getColumn(i).setPreferredWidth(98);
            columnModelBloqueados.getColumn(i).setPreferredWidth(98);
            tablaCola1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            tablaCola2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            tablaCola3.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            tablaBloqueados.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPaneCola1 = new JScrollPane(tablaCola1);
        scrollPaneCola1.setBounds(50,630,400,370);

        JScrollPane scrollPaneCola2 = new JScrollPane(tablaCola2);
        scrollPaneCola2.setBounds(528,630,400,370);

        JScrollPane scrollPaneCola3 = new JScrollPane(tablaCola3);
        scrollPaneCola3.setBounds(996,630,400,370);

        JScrollPane scrollPaneBloqueados = new JScrollPane(tablaBloqueados);
        scrollPaneBloqueados.setBounds(1464,630,400,370);


        // Agregar elementos a los paneles
        this.add(chartPanel);
        this.add(scrollPaneCola1);
        this.add(scrollPaneCola2);
        this.add(scrollPaneCola3);
        this.add(scrollPaneBloqueados);

        this.add(this.scrollPaneTiempos);
        //this.add(this.labelCajero);

        this.add(this.btnAgregarProceso);
        this.add(this.btnIniciarDetener);
        this.add(this.labelSemaforo);
        this.add(this.labelCola1);
        this.add(this.labelCola2);
        this.add(this.labelCola3);
        this.add(this.labelColaBloqueados);
        this.add(this.insertCola1);
        this.add(this.insertCola2);
        this.add(this.insertCola3);
        //this.panelTabla.add(this.scrollPaneBloqueados);
        //this.panelTabla.add(this.btnAtender);
        //this.panelTabla.add(this.btnAgregarClientes);
        //this.panelTabla.add(this.btnIniciarDetener);
        //this.panelTabla.add(this.txtCantClientes);
       // this.panelTabla.add(this.txtCantAtender);

        //this.panelCola.add(this.labelCajero);
        //this.panelTabla.add(this.labelAccion);
        //this.panelTabla.add(this.labelContadorCiclo);
        //this.panelTabla.add(this.labelSeccionCritica);

        //this.add(panelCola);
        //this.add(panelTabla);
        //this.add(diagramaGantt);

        this.repaint();
    }

    public void actualizarSemaforo(String color){
        if(color == "Verde"){
            this.labelSemaforo.setIcon(new ImageIcon(getClass().getResource("/imagenes/semaforo_verde.png")));
        }
        if(color == "Rojo"){
            this.labelSemaforo.setIcon(new ImageIcon(getClass().getResource("/imagenes/semaforo_rojo.png")));
        }
    }

    public DefaultTableModel getModelTablaTiempos() {
        return modelTablaTiempos;
    }

    public DefaultTableModel getModelTablaBloqueado() {
        return modelTablaBloqueado;
    }

    public JPanel getPanelCola() {
        return panelCola;
    }

    public JPanel getPanelTabla() {
        return panelTabla;
    }

    public JPanel getDiagramaGantt() { return diagramaGantt; }

    public JTextField getTxtCantClientes() {
        return txtCantClientes;
    }

    public JTextField getTxtCantAtender() {
        return txtCantAtender;
    }

    public JLabel getLabelAccion() {
        return labelAccion;
    }

    public JButton getBtnIniciarDetener() {
        return btnIniciarDetener;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    public JLabel getLabelSeccionCritica() {
        return labelSeccionCritica;
    }

    private void capturarEventos() {
        this.btnAtender.addActionListener(getControl());
        this.btnAgregarProceso.addActionListener(getControl());
        this.btnIniciarDetener.addActionListener(getControl());
        this.insertCola1.addActionListener(getControl());
        this.insertCola2.addActionListener(getControl());
        this.insertCola3.addActionListener(getControl());
    }

    public JLabel getLabelContadorCiclo() {
        return labelContadorCiclo;
    }

    public DefaultTableModel getModelTablaCola1() {
        return modelTablaCola1;
    }

    public DefaultTableModel getModelTablaCola2() {
        return modelTablaCola2;
    }

    public DefaultTableModel getModelTablaCola3() {
        return modelTablaCola3;
    }
}
