package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu_principal extends JFrame {
    JPanel panel;
    JButton jugar;
    JButton como_jugar;
    JLabel opcion;
    Controlador controlador;
    VistaPrincipal vistaPrincipal;

    public Menu_principal(Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar_comp(controlador, vistaPrincipal);
    }
    private void inicializar_comp(Controlador controlador, VistaPrincipal vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 247, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel=new JPanel();
        setContentPane(panel);
        opcion=new JLabel("Elija una opcion");
        jugar=new JButton("Jugar");
        como_jugar=new JButton("Como jugar");
        panel.setLayout(new BorderLayout());
        panel.add(opcion, BorderLayout.NORTH);
        panel.add(jugar, BorderLayout.WEST);
        panel.add(como_jugar, BorderLayout.EAST);
        jugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_inicio();
            }
        });
    }
}
