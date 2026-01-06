package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Carta;
import Vista.VistaGrafica.Utilidad.MapeoCartas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Cartas_en_mano extends JFrame{
    ArrayList<JButton> cartas_jugador;
    Controlador controlador;
    VistaPrincipal vistaPrincipal;
    JLabel fondo;
    ArrayList<JLabel> cartas_mano;
    JLabel palo_triunfo_texto;
    JLabel palo_triunfo_carta;
    MapeoCartas mapeoCartas;
    JButton boton_presionado; //esto puede ir si se rquiere la confirmacion del modelo para ver si la carta tirada es valida o no!
                              //desde el modelo lo unico que tengo es la carta no el boton presionado. Si no tengo el ultimo boton presionado
                              // no se a que boton corresponde la carta


    public Cartas_en_mano (Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar (Controlador controlador, VistaPrincipal vistaPrincipal){
        this.mapeoCartas=new MapeoCartas();
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        ImageIcon fondo_i = new ImageIcon("src/Imagenes_cartas/MESA.png");
        this.fondo= new JLabel(fondo_i);
        fondo.setLayout(new BorderLayout());
        this.cartas_jugador= new ArrayList<>();
        this.cartas_mano= new ArrayList<>();
        setContentPane(fondo);


    }
    public void iniciar_palo_triunfo(String palo_triunfo){
        palo_triunfo_texto=new JLabel(palo_triunfo);
        palo_triunfo_carta=new JLabel(mapeoCartas.obtener_carta(palo_triunfo));
        fondo.add(palo_triunfo_carta,BorderLayout.NORTH);
        fondo.add(palo_triunfo_texto,BorderLayout.CENTER);
    }
    public void iniciar_cartas_jugador(ArrayList<Carta> cartas_jugador1){
        for(Carta c:cartas_jugador1){
            ImageIcon original=mapeoCartas.obtener_carta(c.getNombre());
            Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);
            ImageIcon carta_modificada=new ImageIcon(imagen);
            JButton boton_carta=new JButton(carta_modificada);
            boton_carta.setBorderPainted(false);
            boton_carta.setContentAreaFilled(false);
            boton_carta.setFocusPainted(false);
            boton_carta.setEnabled(false);
            cartas_jugador.add(boton_carta);
            fondo.add(boton_carta,BorderLayout.SOUTH);
            cartas_jugador.getLast().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        controlador.tira_carta(c);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
    public void iniciar_cartas_mano(Carta carta){
        ImageIcon original=mapeoCartas.obtener_carta(carta.getNombre());
        Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);
        ImageIcon carta_modificada=new ImageIcon(imagen);
        JLabel carta_mano=new JLabel(carta_modificada);
        cartas_mano.add(carta_mano);
        fondo.add(carta_mano);
    }
    public void reiniciar_cartas_mano(){
        for(JLabel l:cartas_mano){
            cartas_mano.remove(l);
            fondo.remove(l);
        }
    }

    /*
    Cuando es el turno de un jugador, primero el controlador le pregunta al modelo que cartas puede tirar
    y cuales no. A partir de eso, el controlador habilita solo los botones de las cartas que puede tirar.
    Luego, de que el jugador tira, el juego valida internamente si la carta es valida para tirarse o no y
    actualiza el turno. Es decir, los botones de las cartas de cada jugador estan SIEMPRE deshabilitados
    salvo cuando el jugador esta por tirar, habilitando solo los botones de las cartas permitidos.
    */
    //un boton se puede hacer visible o no





}
