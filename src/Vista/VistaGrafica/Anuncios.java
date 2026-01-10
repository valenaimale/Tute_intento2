package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Anuncios extends JFrame{
    private JPanel panel_principal;//of_tute, of_las_40, of_las_20
    private JLabel texto;
    private JPanel panel_botones;//universal_si, universal_no
    private JButton universal_si;
    private JButton universal_no;
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;
    private Puntajes puntajes;
    private EstadoAnuncio estado=EstadoAnuncio.NADA;
    private Timer timer;


    public Anuncios(Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar(Controlador controlador, VistaPrincipal vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        puntajes=new Puntajes();
        puntajes.setVisible(false);
        panel_principal=new JPanel(new BorderLayout());
        panel_botones=new JPanel(new FlowLayout());
        texto=new JLabel();
        panel_principal.add(texto, BorderLayout.NORTH);
        texto.setVisible(true);
        universal_si=new JButton("SI");
        universal_no=new JButton("NO");
        panel_botones.add(universal_no,SwingConstants.CENTER);
        panel_botones.add(universal_si,SwingConstants.CENTER);
        panel_principal.add(panel_botones,BorderLayout.SOUTH);
        panel_botones.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        timer=new Timer(15000, e->{
            setVisible(false);
            texto.setVisible(false);
            panel_principal.setVisible(false);
            puntajes.setVisible(false);
        });

        setVisible(false);
        universal_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(estado==EstadoAnuncio.CANTO_TUTE){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                    try {
                        controlador.canta_tute();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_40){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                    try {
                        controlador.canta_las_40();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_20){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                    try {
                        controlador.canta_las_20();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        universal_no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(estado==EstadoAnuncio.CANTO_TUTE){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_40){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_20){
                    setVisible(false);
                    panel_principal.setVisible(false);
                    texto.setVisible(false);
                    panel_botones.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
            }
        });
    }


    public void ofrecer_tute(){
        texto.setText("TUTE");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_TUTE;
    }
    public void ofrecer_las_40(){
        texto.setText("LAS 40");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_40;

    }
    public void ofrecer_las_20(){
        texto.setText("LAS 20");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_20;
    }
    /*of_tute=new JLabel("TUTE");
        of_las_40=new JLabel("LAS 40");
        of_las_20=new JLabel("LAS 20");
        universal_si=new JButton("SI");
        universal_no=new JButton("NO");*/
    public void canto_tute(String nombre){
        texto.setText(nombre + "canto tute. Gano el juego!");
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
    public void canto_las_40(String nombre){
        texto.setText(nombre + "canto las 40. Suma 40 puntos!");
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
    public void canto_las_20(String nombre){
        texto.setText(nombre + "canto las 20. Suma 20 puntos!");
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
    public void mostrar_puntajes(ArrayList<Jugador> jugadores, Jugador ganador){
        puntajes.actualizar_puntaje(jugadores, ganador, "gano la baza. Puntajes:");
        puntajes.setVisible(true);
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
    public void ganador_por_punts(String nombre){
        texto.setText(nombre + " sumo 101 puntos o mas. Es el ganador del juego!");
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
    public void mostrar_puntajes_ultimas_10(ArrayList<Jugador> jugadores, Jugador ganador){
        puntajes.actualizar_puntaje(jugadores,ganador, " gano la ultima baza. Suma 10 puntos!. Puntajes: ");
        puntajes.setVisible(true);
        panel_principal.setVisible(true);
        setVisible(true);
        timer.start();
    }
}
