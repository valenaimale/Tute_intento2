package Vista.VistaGrafica;

import Model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Puntajes extends JFrame {
    JTable tabla_puntajes;
    JScrollPane panel;
    DefaultTableModel modelo_de_tabla;

    public Puntajes(){
        inicializar_componentes();
    }
    private void inicializar_componentes() {
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        modelo_de_tabla = new DefaultTableModel();
        modelo_de_tabla.addColumn("NOMBRE");
        modelo_de_tabla.addColumn("ID");
        modelo_de_tabla.addColumn("PUNTAJE");
        tabla_puntajes = new JTable();
        tabla_puntajes.setModel(modelo_de_tabla);
        panel = new JScrollPane(tabla_puntajes);
        setContentPane(panel);
    }
    public void actualizar_puntaje(ArrayList<Jugador> jugadores, Jugador ganador, String titulo){
        setTitle(ganador.getNombre() + "gano la baza. Puntajes:");
        modelo_de_tabla.setRowCount(0);
        for(Jugador jugador:jugadores){
            modelo_de_tabla.addRow(new Object[]{
                    jugador.getNombre(),
                    jugador.getId(),
                    jugador.getPuntaje()
            });
        }
    }
}
