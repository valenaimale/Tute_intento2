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
    private JPanel panel_botones;
    private JButton universal_si;
    private JButton universal_no;
    private JButton boton_ok;
    private JButton ver_ranking;
    private JButton volver_jugar;
    private JButton salir;
    private Runnable accion_boton;
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
        //texto.setVisible(true);
        universal_si = new JButton("SI");
        universal_no = new JButton("NO");
        volver_jugar = new JButton("Volver a jugar");
        salir = new JButton("Salir");
        ver_ranking = new JButton("Ver ranking");
        boton_ok = new JButton("OK");

        panel_botones.add(universal_no, SwingConstants.CENTER);
        panel_botones.add(universal_si, SwingConstants.CENTER);
        panel_botones.add(volver_jugar, SwingConstants.CENTER);
        panel_botones.add(salir, SwingConstants.CENTER);
        panel_botones.add(ver_ranking, SwingConstants.CENTER);

        panel_botones.add(boton_ok, SwingConstants.CENTER);
        panel_principal.add(panel_botones, BorderLayout.SOUTH);
        //panel_botones.setVisible(true);
        universal_no.setEnabled(false);//HAGO TODOS LOS BOTONES INVISIBLES
        universal_si.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        volver_jugar.setVisible(false);
        volver_jugar.setEnabled(false);
        salir.setVisible(false);
        salir.setEnabled(false);
        ver_ranking.setVisible(false);
        ver_ranking.setEnabled(false);
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
                universal_no.setVisible(false);
                universal_si.setVisible(false);
                universal_no.setEnabled(false);
                universal_si.setEnabled(false);
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
                volver_jugar.setVisible(false);
                volver_jugar.setEnabled(false);
                ver_ranking.setVisible(false);
                ver_ranking.setVisible(false);
                salir.setVisible(false);
                salir.setEnabled(false);
                vistaPrincipal.no_mostrar_mano();
                vistaPrincipal.mostrar_esperando();
                controlador.iniciar_player(vistaPrincipal.nombre_user());
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                volver_jugar.setVisible(false);
                volver_jugar.setVisible(false);
                salir.setVisible(false);
                salir.setEnabled(false);
                ver_ranking.setVisible(false);
                ver_ranking.setEnabled(false);
                try{
                    controlador.terminar();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ver_ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    vistaPrincipal.mostrar_ranking_al_finalizar();
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
                accion_boton.run();
            }
        });

    }
    public void oferta_canto(String cadena){
        texto.setText(cadena);
        System.out.println("Entro a oferta_canto. Texto: "+texto.getText());
        System.out.println("Texto visible: "+texto.isVisible());
        System.out.println("Panel principal visible: "+panel_principal.isVisible());
        System.out.println("Panel botones visible: "+panel_botones.isVisible());

        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        setVisible(true);
    }

    public void canto(String cadena){
        texto.setText(cadena);
        System.out.println("Entro a oferta_canto. Texto: "+texto.getText());
        System.out.println("Texto visible: "+texto.isVisible());
        System.out.println("Panel principal visible: "+panel_principal.isVisible());
        System.out.println("Panel botones visible: "+panel_botones.isVisible());
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        accion_boton = () -> {
            try {
                controlador.respuesta_anuncio();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
        setVisible(true);
    }
    public void dejar_de_verme(){
        boton_ok.setVisible(false);
        boton_ok.setEnabled(false);
        setVisible(false);
    }



    public void cartel_ganador(String cadena) {
        texto.setText(cadena);
        System.out.println("Entro a oferta_canto. Texto: "+texto.getText());
        System.out.println("Texto visible: "+texto.isVisible());
        System.out.println("Panel principal visible: "+panel_principal.isVisible());
        System.out.println("Panel botones visible: "+panel_botones.isVisible());
        volver_jugar.setEnabled(true);
        volver_jugar.setVisible(true);
        salir.setEnabled(true);
        salir.setVisible(true);
        ver_ranking.setEnabled(true);
        ver_ranking.setVisible(true);
        setVisible(true);
    }


    public void cartel_error(String cadena){
        texto.setText(cadena);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        accion_boton = () -> vistaPrincipal.mostrar_menu_principal();
        setVisible(true);
    }
}
