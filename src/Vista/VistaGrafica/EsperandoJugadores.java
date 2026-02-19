package Vista.VistaGrafica;

import Model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EsperandoJugadores extends JFrame{
    private DefaultTableModel modelo_tabla;
    private JTable tabla_jugadores;
    //JPanel panel;
    private JScrollPane panel;

    public EsperandoJugadores(){
        inicializar_componentes();
    }
    private void inicializar_componentes(){
        setTitle("Esperando a que haya 4 jugadores...");
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        modelo_tabla=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo_tabla.addColumn("NOMBRE");
        modelo_tabla.addColumn("ID");
        tabla_jugadores=new JTable();
        tabla_jugadores.setModel(modelo_tabla);
        panel=new JScrollPane(tabla_jugadores);
        setContentPane(panel);
    }
    public void aniadir_jugador(int id, int puntaje, String nombre){
        modelo_tabla.addRow(new Object[]{
                nombre,
                id,
                puntaje
        });
    }
    public void borrar_jugadores(){
        modelo_tabla.setRowCount(0);
    }
}
