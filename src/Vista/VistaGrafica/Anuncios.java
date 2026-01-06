package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class Anuncios extends JFrame{
    JPanel panel_pregunta;//of_tute, of_las_40, of_las_20
    JLabel of_tute;
    JLabel of_las_40;
    JLabel of_las_20;
    JLabel ganador_por_tute;
    JLabel canta_las_40;
    JLabel canta_las_20;
    JLabel ganador_por_puntos;
    JPanel panel_botones;//universal_si, universal_no
    JButton universal_si;
    JButton universal_no;
    Controlador controlador;
    VistaPrincipal vistaPrincipal;
    EstadoAnuncio estado=EstadoAnuncio.NADA;

    public Anuncios(Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar(Controlador controlador, VistaPrincipal vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        panel_pregunta=new JPanel(new BorderLayout());
        panel_botones=new JPanel(new FlowLayout());
        of_tute=new JLabel("TUTE");
        of_las_40=new JLabel("LAS 40");
        of_las_20=new JLabel("LAS 20");
        universal_si=new JButton("SI");
        universal_no=new JButton("NO");
        panel_pregunta.add(of_tute, BorderLayout.CENTER);
        panel_pregunta.add(of_las_40, BorderLayout.CENTER);
        panel_pregunta.add(of_las_20, BorderLayout.CENTER);
        panel_botones.add(universal_no,SwingConstants.CENTER);
        panel_botones.add(universal_si,SwingConstants.CENTER);
        panel_pregunta.add(panel_botones,BorderLayout.SOUTH);
        of_tute.setVisible(false);
        of_las_40.setVisible(false);
        of_las_20.setVisible(false);
        universal_no.setEnabled(false);
        universal_si.setEnabled(false);
        universal_no.setVisible(false);
        universal_si.setVisible(false);
        setVisible(false);
        universal_si.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(estado==EstadoAnuncio.CANTO_TUTE){
                    setVisible(false);
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
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
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
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
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
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
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_40){
                    setVisible(false);
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
                else if(estado==EstadoAnuncio.CANTO_LAS_20){
                    setVisible(false);
                    of_tute.setVisible(false);
                    universal_no.setVisible(false);
                    universal_no.setEnabled(false);
                    universal_si.setVisible(false);
                    universal_si.setEnabled(false);
                    estado=EstadoAnuncio.NADA;
                }
            }
        });


    }


    public void ofrecer_tute(){
        setVisible(true);
        of_tute.setVisible(true);
        universal_no.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setVisible(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_TUTE;
    }
    public void ofrecer_las_40(){
        setVisible(true);
        of_las_40.setVisible(true);
        universal_no.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setVisible(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_40;

    }
    public void ofrecer_las_20(){
        setVisible(true);
        of_las_20.setVisible(true);
        universal_no.setVisible(true);
        universal_no.setEnabled(true);
        universal_si.setVisible(true);
        universal_si.setEnabled(true);
        estado=EstadoAnuncio.CANTO_LAS_20;
    }
    public void canto_tute(){

    }
    public void canto_las_40(){

    }
    public void canto_las_20(){

    }
}
