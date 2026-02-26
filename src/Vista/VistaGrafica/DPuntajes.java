package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


/*public class Puntajes extends JFrame {
    private JTable tabla_puntajes;
    private JScrollPane panel;
    private JPanel panel_principal;
    private DefaultTableModel modelo_de_tabla;

    private VistaPrincipal vistaPrincipal;
    private JButton boton_ok;
    private Controlador controlador;

    public Puntajes(VistaPrincipal vistaPrincipal, Controlador controlador){
        inicializar_componentes(vistaPrincipal, controlador);
    }
    private void inicializar_componentes(VistaPrincipal vistaPrincipal, Controlador controlador) {
        this.vistaPrincipal=vistaPrincipal;
        this.controlador=controlador;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel_principal=new JPanel(new BorderLayout());
        boton_ok=new JButton("Ok");
        boton_ok.setEnabled(false);
        boton_ok.setVisible(true);
        modelo_de_tabla=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
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
                try {
                    controlador.procesar_eventos_pendientes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                vistaPrincipal.mostrar_mano_visible();
            }
        });
    }

    public void aniadir_jugador(int id, int puntaje, String nombre){
        modelo_de_tabla.addRow(new Object[]{
                nombre,
                id,
                puntaje
        });
        boton_ok.setEnabled(true);
    }
    public void actualizar_puntaje_ganador(int id, int puntaje, String nombre){
        System.out.println("actualizar_puntaje_ganador. Puntajes");
        setVisible(true);
        setTitle(nombre + " gano la baza. Puntajes:");
        modelo_de_tabla.setValueAt(puntaje, id, 2);
    }
}*/
public class DPuntajes extends JDialog {
    private JTable tabla_puntajes;
    private JScrollPane panel;
    private JPanel panel_principal;
    private DefaultTableModel modelo_de_tabla;

    private VistaGrafica vistaPrincipal;
    private JButton boton_ok;
    private JButton boton_ok_partida_en_curso;
    private JPanel panel_botones_ok;
    private Controlador controlador;

    public DPuntajes(JFrame cartas_en_mano, VistaGrafica vistaPrincipal, Controlador controlador){
        super(cartas_en_mano, false);
        inicializar_componentes(vistaPrincipal, controlador);
    }
    private void inicializar_componentes(VistaGrafica vistaPrincipal, Controlador controlador) {
        this.vistaPrincipal=vistaPrincipal;
        this.controlador=controlador;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        setSize(500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel_principal=new JPanel(new BorderLayout());
        boton_ok=new JButton("OK");
        boton_ok_partida_en_curso=new JButton("OK");
        boton_ok.setEnabled(false);
        boton_ok.setVisible(false);
        boton_ok_partida_en_curso.setEnabled(false);
        boton_ok_partida_en_curso.setVisible(false);
        modelo_de_tabla=new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo_de_tabla.addColumn("NOMBRE");
        modelo_de_tabla.addColumn("ID");
        modelo_de_tabla.addColumn("PUNTAJE");
        panel_botones_ok=new JPanel(new FlowLayout());
        tabla_puntajes = new JTable();
        tabla_puntajes.setModel(modelo_de_tabla);
        panel = new JScrollPane(tabla_puntajes);
        panel_botones_ok.add(boton_ok);
        panel_botones_ok.add(boton_ok_partida_en_curso);
        panel_principal.add( panel, BorderLayout.CENTER);
        panel_principal.add(panel_botones_ok, BorderLayout.SOUTH);
        setContentPane(panel_principal);
        boton_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boton_ok.setVisible(false);
                boton_ok.setEnabled(false);
                setVisible(false);
                try {
                    controlador.procesar_eventos_pendientes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                //vistaPrincipal.mostrar_mano_visible();
            }
        });
        boton_ok_partida_en_curso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boton_ok_partida_en_curso.setVisible(false);
                boton_ok_partida_en_curso.setEnabled(false);
                setVisible(false);
            }
        });
    }

    public void aniadir_jugador(int id, int puntaje, String nombre){
        modelo_de_tabla.addRow(new Object[]{
                nombre,
                id,
                puntaje
        });

    }
    public void actualizar_puntaje_ganador(int id, int puntaje, String nombre){
        System.out.println("actualizar_puntaje_ganador. Puntajes");
        setTitle(nombre + " gano la baza. Puntajes:");
        modelo_de_tabla.setValueAt(puntaje, id, 2);
        if(isVisible()){
            setVisible(false);
            setVisible(true);
        }
    }
    public void limpiar_puntajes(){
        modelo_de_tabla.setRowCount(0);
    }
    public void mostrarme(){
        setVisible(false);
        boton_ok_partida_en_curso.setVisible(false);
        boton_ok_partida_en_curso.setEnabled(false);
        boton_ok.setEnabled(true);
        boton_ok.setVisible(true);
        setVisible(true);
    }
    public void mostrarme_partida_en_curso(){
        if(!boton_ok.isVisible()){
            boton_ok_partida_en_curso.setVisible(true);
            boton_ok_partida_en_curso.setEnabled(true);
            setVisible(true);
        }

    }
}
