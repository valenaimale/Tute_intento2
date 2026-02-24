package Cliente;

import Controlador.Controlador;
import Vista.IVista;
import Vista.VistaConsola.VistaConsola;
import Vista.VistaGrafica.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Cliente {
    public static void main(String[] args) throws RemoteException {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        Controlador controlador=new Controlador();
        JFrame elegir_vista= new JFrame("TUTE");
        JPanel jPanel=new JPanel(new BorderLayout());
        JPanel panel_botones=new JPanel(new FlowLayout());
        JButton boton_grafica=new JButton("Grafica");
        JButton boton_consola=new JButton("Consola");
        JLabel eleccion=new JLabel("Elija una vista para jugar");
        panel_botones.add(boton_consola, SwingConstants.CENTER);
        panel_botones.add(boton_grafica, SwingConstants.CENTER);
        jPanel.add(panel_botones, BorderLayout.SOUTH);
        jPanel.add(eleccion,BorderLayout.CENTER);
        eleccion.setHorizontalAlignment(SwingConstants.CENTER);
        eleccion.setVerticalAlignment(SwingConstants.CENTER);
        elegir_vista.setContentPane(jPanel);
        elegir_vista.setBounds(100,100, 400, 100);
        elegir_vista.setVisible(true);

        boton_grafica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IVista vista_grafica=new VistaGrafica(controlador);
                vista_grafica.iniciar();
                elegir_vista.setVisible(false);
                controlador.setVista(vista_grafica);
                ar.edu.unlu.rmimvc.cliente.Cliente c = new ar.edu.unlu.rmimvc.cliente.Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
                try {
                    c.iniciar(controlador);
                } catch (RemoteException f) {
                    // TODO Auto-generated catch block
                    f.printStackTrace();
                } catch (RMIMVCException f) {
                    // TODO Auto-generated catch block
                    f.printStackTrace();
                }
            }
        });
        boton_consola.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IVista vista_consola=new VistaConsola(controlador);
                vista_consola.iniciar();
                elegir_vista.setVisible(false);
                controlador.setVista(vista_consola);
                ar.edu.unlu.rmimvc.cliente.Cliente c = new ar.edu.unlu.rmimvc.cliente.Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
                try {
                    c.iniciar(controlador);
                } catch (RemoteException f) {
                    // TODO Auto-generated catch block
                    f.printStackTrace();
                } catch (RMIMVCException f) {
                    // TODO Auto-generated catch block
                    f.printStackTrace();
                }
            }
        });

    }

}
