package presentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorVentanaPrincipal implements ActionListener {

    private VentanaPrincipal ventana;
    private Modelo modelo;

    public ControladorVentanaPrincipal(VentanaPrincipal ventana) {
        this.ventana = ventana;
        this.modelo = ventana.getModelo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boton;
        if(e.getSource() instanceof JButton){
            boton = (JButton) e.getSource();
            if(boton.getText().equals("AGREGAR")){
                ventana.getModelo().agregarProceso();
            }
            if(boton.getText().equals("INICIAR") || boton.getText().equals("DETENER")){
                ventana.getModelo().setSistemaActivo(!ventana.getModelo().isSistemaActivo());
                ventana.getModelo().iniciarAtencion();
                //ventana.getModelo().iniciarPintarCola();
                //ventana.getModelo().iniciarPintarTabla();
                //ventana.getModelo().iniciarDiagramaGantt();
                //ventana.getModelo().iniciarAtencionBloqueados();

                if(boton.getText().equals("INICIAR")){
                    boton.setText("DETENER");
                }
                else{
                    boton.setText("INICIAR");
                }
            }
        }
        return;
    }
}
