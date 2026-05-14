package Vista.VistaGrafica;

import Controlador.Controlador;
import Vista.TimerUnico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;



public class DPuntajes extends JDialog {
    private JTable tabla_puntajes;
    private JScrollPane panel;
    private JPanel panel_principal;
    private DefaultTableModel modelo_de_tabla;
    private Runnable accion_boton;
    private VistaGrafica vistaPrincipal;
    private JButton boton_ok;
    private JLabel ganador_baza;
    private Controlador controlador;
    private TimerUnico timer;

    public DPuntajes(JFrame ventana_partida, VistaGrafica vistaPrincipal, Controlador controlador){
        super(ventana_partida, false);
        inicializar_componentes(vistaPrincipal, controlador);
    }
    private void inicializar_componentes(VistaGrafica vistaPrincipal, Controlador controlador) {
        this.vistaPrincipal=vistaPrincipal;
        this.controlador=controlador;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        setSize(500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        this.timer=new TimerUnico(10000);
        panel_principal=new JPanel(new BorderLayout());
        boton_ok=new JButton("OK");
        ganador_baza = new JLabel();
        ganador_baza.setVisible(false);
        ganador_baza.setFont(new Font("Arial", Font.BOLD, 14));
        ganador_baza.setHorizontalAlignment(SwingConstants.CENTER);
        boton_ok.setEnabled(true);
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
        panel_principal.add(ganador_baza, BorderLayout.NORTH);
        panel_principal.add( panel, BorderLayout.CENTER);
        panel_principal.add(boton_ok, BorderLayout.SOUTH);
        setContentPane(panel_principal);
        setTitle("PUNTAJES");
        boton_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                accion_boton.run();
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
        ganador_baza.setText("El ganador de la baza es "+nombre+". Suma "+puntaje+"! Puntajes totales:");
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
        ganador_baza.setVisible(true);
        accion_boton= () -> {
            ganador_baza.setVisible(false);
            try {
                timer.interrumpir();
                controlador.respuesta_puntaje();
                setVisible(false);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
        timer.iniciar(()->{
            ganador_baza.setVisible(false);
            try {
                controlador.respuesta_puntaje_por_inactividad();
                setVisible(false);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("timer iniciado. Accion:"+accion_boton);
        setVisible(true);
    }
    public void mostrarme_partida_en_curso(){
        if(!isVisible()){
            accion_boton = () ->{};
            setVisible(true);
        }
    }
    public void modificar_accion(){
        ganador_baza.setVisible(false);
        accion_boton = () -> {};
        setVisible(false);
    }
}
