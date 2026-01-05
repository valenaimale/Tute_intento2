package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VentanaInicioJugador extends JFrame {
    private JPanel contenedor;//lo que permite organizar los componentes (boton y nombre del usuario en la ventana)
    private JTextField nombre_usuario;//donde se va a escribir el nombre del usuaerio
    private JButton boton_confirmar;//boton de confirmacion de nombre
    private JButton volver;
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;



    public VentanaInicioJugador(VistaPrincipal vistaPrincipal, Controlador controlador){
        inicializar_comp(vistaPrincipal, controlador);
    }
    private void inicializar_comp(VistaPrincipal vistaPrincipal, Controlador controlador){
        this.vistaPrincipal=vistaPrincipal;
        this.controlador=controlador;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 247, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        contenedor=new JPanel();
        contenedor.setBorder(new EmptyBorder(5, 5, 5, 5)); //tamanio de los bordes
        setContentPane(contenedor);//define el panel principal
        BorderLayout layout = new BorderLayout();
        contenedor.setLayout(layout);//define la distribucion
        JLabel lblUsuario = new JLabel("Usuario");//es un cartel o etiqueta no editable, solo muestra
        contenedor.add(lblUsuario, BorderLayout.WEST);//aniade el componente de JLabel con una restriccion
        nombre_usuario = new JTextField();//campo de texto de una sola linea
        contenedor.add(nombre_usuario,BorderLayout.CENTER);//aniade el componente JTextField con una restriccion
        nombre_usuario.setColumns(10);//tamanio del JTextField
        boton_confirmar = new JButton("Confirmar");
        contenedor.add(boton_confirmar, BorderLayout.EAST);//aniade el JBoton junto con la restriccion
        volver=new JButton("Volver");
        contenedor.add(volver, BorderLayout.SOUTH);
        SwingUtilities.getRootPane(boton_confirmar).setDefaultButton(boton_confirmar);//lo que quiere decir es que si el usuario presiona "enter" el boton por defecto que se va a presionar es el btnIniciar
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_menu_principal();
            }
        });
        boton_confirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_esperando();
                try {
                    controlador.iniciar_player(nombre_usuario.getText());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public String getNombreUsuario(){
        return this.nombre_usuario.getText();
    }
}
