package Vista.VistaGrafica;
import Controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class DAnuncios extends JDialog {
    private JPanel panel_principal;
    private JLabel texto;
    private String nombre_ganador;
    private JPanel panel_botones;
    private JButton universal_si;
    private JButton universal_no;
    private JButton boton_ok;
    private JButton volver_jugar;
    private JButton salir;
    private Controlador controlador;
    private VistaGrafica vistaPrincipal;


    public DAnuncios(JFrame v_padre, Controlador controlador, VistaGrafica vistaPrincipal) {
        super(v_padre, false);
        inicializar(controlador, vistaPrincipal);
    }

    private void inicializar(Controlador controlador, VistaGrafica vistaPrincipal) {
        this.controlador = controlador;
        this.vistaPrincipal = vistaPrincipal;
        setBounds(100, 100, 500, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        setResizable(false);
        panel_principal = new JPanel(new BorderLayout());
        panel_botones = new JPanel(new FlowLayout());
        texto = new JLabel();
        panel_principal.add(texto, BorderLayout.CENTER);
        texto.setVisible(true);
        universal_si = new JButton("SI");
        universal_no = new JButton("NO");
        volver_jugar = new JButton("Volver a jugar");
        salir = new JButton("Salir");
        boton_ok = new JButton("OK");

        panel_botones.add(universal_no, SwingConstants.CENTER);
        panel_botones.add(universal_si, SwingConstants.CENTER);
        panel_botones.add(volver_jugar, SwingConstants.CENTER);
        panel_botones.add(salir, SwingConstants.CENTER);

        panel_botones.add(boton_ok, SwingConstants.CENTER);
        panel_principal.add(panel_botones, BorderLayout.SOUTH);
        panel_botones.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        volver_jugar.setVisible(false);
        volver_jugar.setEnabled(false);
        salir.setVisible(false);
        salir.setEnabled(false);
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
        volver_jugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                volver_jugar.setVisible(false);
                volver_jugar.setEnabled(false);
                salir.setVisible(false);
                salir.setEnabled(false);
                try {
                    vistaPrincipal.no_mostrar_mano();
                    vistaPrincipal.mostrar_esperando();
                    controlador.iniciar_player(vistaPrincipal.nombre_user());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                panel_principal.setVisible(false);
                texto.setVisible(false);
                panel_botones.setVisible(false);
                volver_jugar.setVisible(false);
                volver_jugar.setVisible(false);
                salir.setVisible(false);
                salir.setEnabled(false);
                try{
                    controlador.terminar();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
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

    }


    public void ofrecer_tute() {
        setVisible(false);
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

    public void ofrecer_las_40() {
        setVisible(false);
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
    }

    public void ofrecer_las_20() {
        setVisible(false);
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



    public void canto_tute() {
        setVisible(false);
        texto.setText(nombre_ganador + " canto tute. Gano el juego!");
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
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ganador_por_punts() {
        setVisible(false);
        texto.setText(nombre_ganador + " sumo 101 puntos o mas. Es el ganador del juego!");
        texto.setVisible(true);
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        volver_jugar.setEnabled(true);
        volver_jugar.setVisible(true);
        salir.setEnabled(true);
        salir.setVisible(true);
        setVisible(true);
    }

    public void ultimas_10(String nombre) {
        setVisible(false);
        texto.setText(nombre + " gano la ultima baza. Suma 10 puntos!");
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
        volver_jugar.setVisible(true);
        volver_jugar.setEnabled(true);
        salir.setVisible(true);
        salir.setEnabled(true);
        panel_principal.setVisible(true);
        texto.setVisible(true);
        setVisible(true);
    }
    public void setGanador(String nombre_ganador){
        this.nombre_ganador=nombre_ganador;
    }
}
