package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VInicioJugador extends JFrame {
    private JPanel contenedor;
    private JTextField nombre_usuario;
    private JButton boton_confirmar;
    private JButton volver;
    private Controlador controlador;
    private VistaGrafica vistaPrincipal;



    public VInicioJugador(VistaGrafica vistaPrincipal, Controlador controlador){
        inicializar_comp(vistaPrincipal, controlador);
    }
    private void inicializar_comp(VistaGrafica vistaPrincipal, Controlador controlador){
        this.vistaPrincipal=vistaPrincipal;
        this.controlador=controlador;
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 247, 109);
        setLocationRelativeTo(null);
        contenedor=new JPanel();
        contenedor.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contenedor);
        BorderLayout layout = new BorderLayout();
        contenedor.setLayout(layout);
        JLabel lblUsuario = new JLabel("Usuario");
        contenedor.add(lblUsuario, BorderLayout.WEST);
        nombre_usuario = new JTextField();
        contenedor.add(nombre_usuario,BorderLayout.CENTER);
        nombre_usuario.setColumns(10);
        boton_confirmar = new JButton("Confirmar");
        contenedor.add(boton_confirmar, BorderLayout.EAST);
        volver=new JButton("Volver");
        contenedor.add(volver, BorderLayout.SOUTH);
        SwingUtilities.getRootPane(boton_confirmar).setDefaultButton(boton_confirmar);
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
