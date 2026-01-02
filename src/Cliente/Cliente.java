package Cliente;

import Controlador.Controlador;
import Vista.VistaGrafica.VistaPrincipal;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;

import javax.swing.*;
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
        VistaPrincipal vistaPrincipal=new VistaPrincipal(controlador);
        ar.edu.unlu.rmimvc.cliente.Cliente c = new ar.edu.unlu.rmimvc.cliente.Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        vistaPrincipal.iniciar();
        try {
            c.iniciar(controlador);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
