package Vista.VistaGrafica;

import Model.Jugador;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Puntajes extends JFrame {
    JTable tabla_puntajes;
    JScrollPane panel;
    JPanel panel_principal;
    DefaultTableModel modelo_de_tabla;
    VistaPrincipal vistaPrincipal;
    JButton boton_ok;

    public Puntajes(VistaPrincipal vistaPrincipal){
        inicializar_componentes(vistaPrincipal);
    }
    private void inicializar_componentes(VistaPrincipal vistaPrincipal) {
        this.vistaPrincipal=vistaPrincipal;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel_principal=new JPanel(new BorderLayout());
        boton_ok=new JButton("Ok");
        boton_ok.setEnabled(false);
        boton_ok.setVisible(true);
        modelo_de_tabla = new DefaultTableModel();
        modelo_de_tabla.addColumn("NOMBRE");
        modelo_de_tabla.addColumn("ID");
        modelo_de_tabla.addColumn("PUNTAJE");
        tabla_puntajes = new JTable();
        tabla_puntajes.setModel(modelo_de_tabla);
        panel = new JScrollPane(tabla_puntajes);
        panel_principal.add( panel, BorderLayout.CENTER);
        panel_principal.add(boton_ok, BorderLayout.SOUTH);
        setContentPane(panel_principal);
        boton_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_mano_visible();
            }
        });

    }
    public void actualizar_puntaje(ArrayList<Jugador> jugadores, Jugador ganador, String titulo){
        setTitle(ganador.getNombre() + " gano la baza. Puntajes:");
        modelo_de_tabla.setRowCount(0);
        for(Jugador jugador:jugadores){
            modelo_de_tabla.addRow(new Object[]{
                    jugador.getNombre(),
                    jugador.getId(),
                    jugador.getPuntaje()
            });
        }
        boton_ok.setEnabled(true);
    }
}
