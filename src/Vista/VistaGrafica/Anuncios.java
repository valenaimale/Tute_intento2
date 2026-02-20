package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*public class Anuncios extends JFrame{
    private JPanel panel_principal;//of_tute, of_las_40, of_las_20
    private JLabel texto;
    private JPanel panel_botones;//universal_si, universal_no
    private JButton universal_si;
    private JButton universal_no;
    private JButton boton_ok;
    private JButton finalizado_si;//boton unicamente para el cartel de volver a jugar (distintos action listeners)
    private JButton finalizado_no;//boton unicamente para el cartel de volver a jugar (distintos action listeners)
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;



    public Anuncios(Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar(Controlador controlador, VistaPrincipal vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        setBounds(100, 100, 247, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        setResizable(false);
        panel_principal=new JPanel(new BorderLayout());
        panel_botones=new JPanel(new FlowLayout());
        texto=new JLabel();
        panel_principal.add(texto, BorderLayout.CENTER);
        texto.setVisible(true);
        universal_si=new JButton("SI");
        universal_no=new JButton("NO");
        finalizado_si=new JButton("SI");
        finalizado_no=new JButton("NO");
        boton_ok=new JButton("OK");

        panel_botones.add(universal_no,SwingConstants.CENTER);
        panel_botones.add(universal_si,SwingConstants.CENTER);
        panel_botones.add(finalizado_no,SwingConstants.CENTER);
        panel_botones.add(finalizado_si,SwingConstants.CENTER);

        panel_botones.add(boton_ok,SwingConstants.CENTER);
        panel_principal.add(panel_botones,BorderLayout.SOUTH);
        panel_botones.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        finalizado_no.setEnabled(false);
        finalizado_si.setEnabled(false);
        finalizado_no.setVisible(false);
        finalizado_si.setVisible(false);
        boton_ok.setEnabled(false);
        setContentPane(panel_principal);
        setVisible(false);
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        texto.setVerticalAlignment(SwingConstants.CENTER);
        panel_principal.setBackground(Color.BLUE);
        universal_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                universal_no.setVisible(false);
                universal_si.setVisible(false);
                universal_no.setEnabled(false);
                universal_si.setEnabled(false);
                //este hay que ver porque tal vez no haya que mostrar la mano sino la ultimas_10, hay que ponerle estados a los anuncios
                try {
                    controlador.eleccion_si();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        universal_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                universal_no.setVisible(false);
                universal_si.setVisible(false);
                universal_no.setEnabled(false);
                universal_si.setEnabled(false);
                try {
                    controlador.eleccion_no();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        finalizado_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                finalizado_no.setVisible(false);
                finalizado_si.setVisible(false);
                finalizado_no.setEnabled(false);
                finalizado_si.setEnabled(false);
                /*try {
                    controlador.termino_de_partida_si();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }*/
        /*    }
        });
        finalizado_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                finalizado_no.setVisible(false);
                finalizado_si.setVisible(false);
                finalizado_no.setEnabled(false);
                finalizado_si.setEnabled(false);
                /*try {
                    //controlador.termino_de_partida_no();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }*/
         /*   }
        });
        boton_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                boton_ok.setEnabled(false);
                boton_ok.setVisible(false);
                texto.setVisible(false);
                panel_principal.setVisible(false);
                try {
                    controlador.procesar_eventos_pendientes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public void ofrecer_tute(){
        texto.setText("TUTE");
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);


    }
    public void ofrecer_las_40(){
        texto.setText("LAS 40");
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);

        System.out.println("Anuncios. Ofrecer_las_40");

    }
    public void ofrecer_las_20(){
        texto.setText("LAS 20");
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);

    }
    public void ofrecer_termino_juego(String nombre){
        texto.setText(nombre + " es el ganador. ¿Volver a jugar?");
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        finalizado_no.setVisible(true);
        finalizado_si.setVisible(true);
        finalizado_no.setEnabled(true);
        finalizado_si.setEnabled(true);
    }

    public void canto_tute(String nombre){
        texto.setText(nombre + " canto tute. Gano el juego!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }
    public void canto_las_40(String nombre){
        texto.setText(nombre + " canto las 40. Suma 40 puntos!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }
    public void canto_las_20(String nombre){
        texto.setText(nombre + " canto las 20. Suma 20 puntos!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ganador_por_punts(String nombre){
        texto.setText(nombre + " sumo 101 puntos o mas. Es el ganador del juego!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ultimas_10(String nombre){
        texto.setText(nombre + " gano la ultima baza. Suma 10 puntos!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }


}*/
public class Anuncios extends JDialog {
    private JPanel panel_principal;//of_tute, of_las_40, of_las_20
    private JLabel texto;
    private JPanel panel_botones;//universal_si, universal_no
    private JButton universal_si;
    private JButton universal_no;
    private JButton boton_ok;
    private JButton boton_ok_palo_triunfo;
    private JButton finalizado_si;//boton unicamente para el cartel de volver a jugar (distintos action listeners)
    private JButton finalizado_no;//boton unicamente para el cartel de volver a jugar (distintos action listeners)
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;
    private Timer timer;
    private String palo_triunfo;


    public Anuncios(JFrame v_padre, Controlador controlador, VistaPrincipal vistaPrincipal) {
        super(v_padre, false);
        inicializar(controlador, vistaPrincipal);
    }

    private void inicializar(Controlador controlador, VistaPrincipal vistaPrincipal) {
        this.controlador = controlador;
        this.vistaPrincipal = vistaPrincipal;
        setBounds(100, 100, 500, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        setResizable(false);
        panel_principal = new JPanel(new BorderLayout());
        panel_botones = new JPanel(new FlowLayout());
        texto = new JLabel();
        panel_principal.add(texto, BorderLayout.CENTER);
        texto.setVisible(true);
        universal_si = new JButton("SI");
        universal_no = new JButton("NO");
        finalizado_si = new JButton("SI");
        finalizado_no = new JButton("NO");
        boton_ok = new JButton("OK");
        boton_ok_palo_triunfo=new JButton("Volver");
        timer=new Timer(15000, e -> {
            controlador.terminar();
        });
        panel_botones.add(boton_ok_palo_triunfo, SwingConstants.CENTER);
        panel_botones.add(universal_no, SwingConstants.CENTER);
        panel_botones.add(universal_si, SwingConstants.CENTER);
        panel_botones.add(finalizado_no, SwingConstants.CENTER);
        panel_botones.add(finalizado_si, SwingConstants.CENTER);

        panel_botones.add(boton_ok, SwingConstants.CENTER);
        panel_principal.add(panel_botones, BorderLayout.SOUTH);
        panel_botones.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        finalizado_no.setEnabled(false);
        finalizado_si.setEnabled(false);
        finalizado_no.setVisible(false);
        finalizado_si.setVisible(false);
        boton_ok.setVisible(false);
        boton_ok.setEnabled(false);
        setContentPane(panel_principal);
        setVisible(false);
        texto.setHorizontalAlignment(SwingConstants.CENTER);
        texto.setVerticalAlignment(SwingConstants.CENTER);
        panel_principal.setBackground(Color.BLUE);
        universal_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                universal_no.setVisible(false);
                universal_si.setVisible(false);
                universal_no.setEnabled(false);
                universal_si.setEnabled(false);
                //este hay que ver porque tal vez no haya que mostrar la mano sino la ultimas_10, hay que ponerle estados a los anuncios
                try {
                    controlador.eleccion_si();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        universal_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                universal_no.setVisible(false);
                universal_si.setVisible(false);
                universal_no.setEnabled(false);
                universal_si.setEnabled(false);
                try {
                    controlador.eleccion_no();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        finalizado_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                finalizado_no.setVisible(false);
                finalizado_si.setVisible(false);
                finalizado_no.setEnabled(false);
                finalizado_si.setEnabled(false);
                /*try {
                    controlador.termino_de_partida_si();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }*/
            }
        });
        finalizado_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                finalizado_no.setVisible(false);
                finalizado_si.setVisible(false);
                finalizado_no.setEnabled(false);
                finalizado_si.setEnabled(false);
                /*try {
                    //controlador.termino_de_partida_no();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }*/
            }
        });
        boton_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                boton_ok.setEnabled(false);
                boton_ok.setVisible(false);
                texto.setVisible(false);
                panel_principal.setVisible(false);
                try {
                    controlador.procesar_eventos_pendientes();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        boton_ok_palo_triunfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                boton_ok_palo_triunfo.setEnabled(false);
                boton_ok_palo_triunfo.setVisible(false);
                texto.setVisible(false);
                panel_principal.setVisible(false);
            }
        });
    }


    public void ofrecer_tute() {
        setVisible(false);
        texto.setText("TUTE");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);


    }

    public void ofrecer_las_40() {
        setVisible(false);
        texto.setText("LAS 40");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);

        System.out.println("Anuncios. Ofrecer_las_40");
    }

    public void ofrecer_las_20() {
        setVisible(false);
        texto.setText("LAS 20");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);

    }

    public void ofrecer_termino_juego(String nombre) {
        setVisible(false);
        texto.setText(nombre + " es el ganador. ¿Volver a jugar?");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        setVisible(true);
        texto.setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        boton_ok.setVisible(false);
        finalizado_no.setVisible(true);
        finalizado_si.setVisible(true);
        finalizado_no.setEnabled(true);
        finalizado_si.setEnabled(true);
    }

    public void canto_tute(String nombre) {
        setVisible(false);
        texto.setText(nombre + " canto tute. Gano el juego!");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void canto_las_40(String nombre) {
        setVisible(false);
        texto.setText(nombre + " canto las 40. Suma 40 puntos!");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void canto_las_20(String nombre) {
        setVisible(false);
        texto.setText(nombre + " canto las 20. Suma 20 puntos!");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ganador_por_punts(String nombre) {
        setVisible(false);
        texto.setText(nombre + " sumo 101 puntos o mas. Es el ganador del juego!");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ultimas_10(String nombre) {
        setVisible(false);
        texto.setText(nombre + " gano la ultima baza. Suma 10 puntos!");
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }
    public void terminar(String nombre_ganador) {
        setVisible(false);
        texto.setText("El juego termino! El ganador es "+nombre_ganador);
        boton_ok_palo_triunfo.setVisible(false);
        boton_ok_palo_triunfo.setEnabled(false);

        texto.setVisible(true);
        timer.start();
        setVisible(true);
    }
    public void set_palo_triunfo(String palo_triunfo1){
        palo_triunfo=palo_triunfo1;
    }
    public void mostrar_palo_triunfo(){
        texto.setText("El palo del triunfo es: "+palo_triunfo);
        texto.setVisible(true);
        boton_ok_palo_triunfo.setEnabled(true);
        boton_ok_palo_triunfo.setVisible(true);
        boton_ok.setVisible(false);
        boton_ok.setEnabled(false);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        setVisible(true);
    }

}
