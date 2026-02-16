package Vista.VistaConsola;

import Controlador.Controlador;
import Vista.IVista;
import Vista.VistaGrafica.VistaPrincipal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VistaConsola implements IVista {
    JTextField texto_entrada;
    Controlador controlador;
    JTextArea texto_salida;
    EstadoConsola estado;

    public VistaConsola (Controlador controlador){
        inicializar(controlador);
    }
    private void inicializar(Controlador controlador){
        this.controlador=controlador;
        texto_entrada=new JTextField();
        texto_salida=new JTextArea();
        texto_salida.setEditable(false);
        texto_entrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    procesar_entrada(texto_entrada.getText());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void procesar_entrada(String cadena) throws RemoteException {
        switch (estado){
            case EstadoConsola.MENU_PRINCIPAL:
                procesar_inicio(cadena);
                break;
            case EstadoConsola.MOSTRAR_INICIO:
                texto_entrada.setText("");
                controlador.iniciar_player(cadena);
                break;
        }
    }
    private void procesar_inicio(String cadena){
        switch (cadena){
            case "1":
                mostrar_inicio();
                break;
            case "2":
                mostrar_como_jugar();
                break;
            default:
                println("Opcion no valida!");
                mostrar_menu_principal();
                break;
        }
    }
    public void print(String cadena){
        texto_salida.append(cadena);
    }
    public void println(String cadena){
        print(cadena + "\n");
    }
    public void iniciar(){
        estado=EstadoConsola.MENU_PRINCIPAL;
        mostrar_menu_principal();

    }
    public void mostrar_menu_principal(){
        println("Seleccione una de estas opciones ingresando el numero junto a las mismas y luego presionando enter:");
        println("1 - Jugar");
        println("2 - Como jugar");
    }
    private void mostrar_como_jugar(){

    }
    public void mostrar_inicio(){
        texto_entrada.setText("");
        estado=EstadoConsola.MOSTRAR_INICIO;
        print("Ingrese su nombre para comenzar a jugar: ");
    }



}
