package Vista.VistaGrafica;

import Controlador.Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VMenuPrincipal extends JFrame {
    private JPanel panel;
    private JButton jugar;
    private JButton como_jugar;
    private JButton ranking;
    private JLabel opcion;
    private JPanel panel_botones;
    private Controlador controlador;
    private VistaGrafica vistaPrincipal;

    public VMenuPrincipal(Controlador controlador, VistaGrafica vistaPrincipal){
        inicializar_comp(controlador, vistaPrincipal);
    }
    private void inicializar_comp(Controlador controlador, VistaGrafica vistaPrincipal){
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        setResizable(false);//No permitir cambio de tamanio en la ventana
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 450, 109);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        panel=new JPanel();
        panel_botones=new JPanel(new FlowLayout());
        setContentPane(panel);
        opcion=new JLabel("Elija una opcion");
        opcion.setHorizontalAlignment(SwingConstants.CENTER);
        opcion.setVerticalAlignment(SwingConstants.CENTER);
        jugar=new JButton("Jugar");
        como_jugar=new JButton("Como jugar");
        ranking=new JButton("Ver ranking");
        panel_botones.add(jugar, SwingConstants.CENTER);
        panel_botones.add(como_jugar, SwingConstants.CENTER);
        panel_botones.add(ranking, SwingConstants.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(opcion, BorderLayout.CENTER);
        panel.add(panel_botones, BorderLayout.SOUTH);
        jugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_inicio();
            }
        });
        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    vistaPrincipal.mostrar_ranking();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        como_jugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaPrincipal.mostrar_como_jugar();
            }
        });
    }
}
