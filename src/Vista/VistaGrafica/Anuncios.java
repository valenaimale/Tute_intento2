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
    private JButton boton_ok;
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;
    private EstadoAnuncio estado=EstadoAnuncio.NADA;



    public Anuncios(Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar(Controlador controlador, VistaPrincipal vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel_principal=new JPanel(new BorderLayout());
        panel_botones=new JPanel(new FlowLayout());
        texto=new JLabel();
        panel_principal.add(texto, BorderLayout.CENTER);
        texto.setVisible(true);
        universal_si=new JButton("SI");
        universal_no=new JButton("NO");
        boton_ok=new JButton("Ok");
        panel_botones.add(universal_no,SwingConstants.CENTER);
        panel_botones.add(universal_si,SwingConstants.CENTER);
        panel_botones.add(boton_ok,SwingConstants.CENTER);
        panel_principal.add(panel_botones,BorderLayout.SOUTH);
        panel_botones.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        boton_ok.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        setContentPane(panel_principal);
        setVisible(false);
        universal_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (estado) {
                    case EstadoAnuncio.CANTO_TUTE:
                        setVisible(false);
                        panel_principal.setVisible(false);
                        texto.setVisible(false);
                        panel_botones.setVisible(false);
                        universal_no.setVisible(false);
                        universal_si.setVisible(false);
                        universal_no.setEnabled(false);
                        universal_si.setEnabled(false);
                        estado = EstadoAnuncio.NADA;
                        vistaPrincipal.mostrar_mano_visible();
                        try {
                            controlador.canta_tute();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case EstadoAnuncio.CANTO_LAS_40:
                        setVisible(false);
                        panel_principal.setVisible(false);
                        texto.setVisible(false);
                        panel_botones.setVisible(false);
                        universal_no.setVisible(false);
                        universal_si.setVisible(false);
                        universal_no.setEnabled(false);
                        universal_si.setEnabled(false);
                        estado = EstadoAnuncio.NADA;
                        vistaPrincipal.mostrar_mano_visible();

                        try {
                            controlador.canta_las_40();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case EstadoAnuncio.CANTO_LAS_20:
                        setVisible(false);
                        panel_principal.setVisible(false);
                        texto.setVisible(false);
                        panel_botones.setVisible(false);
                        universal_no.setVisible(false);
                        universal_si.setVisible(false);
                        universal_no.setEnabled(false);
                        universal_si.setEnabled(false);
                        estado = EstadoAnuncio.NADA;
                        vistaPrincipal.mostrar_mano_visible();

                        try {
                            controlador.canta_las_20();
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
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
                estado=EstadoAnuncio.NADA;
                vistaPrincipal.mostrar_mano_visible();
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
                vistaPrincipal.mostrar_mano_visible();
            }
        });
    }


    public void ofrecer_tute(){
        texto.setText("TUTE");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_TUTE;

    }
    public void ofrecer_las_40(){
        texto.setText("LAS 40");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_40;
        System.out.println("Anuncios. Ofrecer_las_40");

    }
    public void ofrecer_las_20(){
        texto.setText("LAS 20");
        setVisible(true);
        panel_principal.setVisible(true);
        panel_botones.setVisible(true);
        universal_no.setVisible(true);
        universal_si.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_20;
        System.out.println("Anuncios. Ofrecer_las_20");
    }

    public void canto_tute(String nombre){
        texto.setText(nombre + " canto tute. Gano el juego!");
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }
    public void canto_las_40(String nombre){
        texto.setText(nombre + " canto las 40. Suma 40 puntos!");
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }
    public void canto_las_20(String nombre){
        texto.setText(nombre + " canto las 20. Suma 20 puntos!");
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

    public void ganador_por_punts(String nombre){
        texto.setText(nombre + " sumo 101 puntos o mas. Es el ganador del juego!");
        panel_botones.setVisible(true);
        panel_principal.setVisible(true);
        boton_ok.setVisible(true);
        boton_ok.setEnabled(true);
        setVisible(true);
    }

}
