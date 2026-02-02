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
import java.util.HashMap;

public class Cartas_en_mano extends JFrame{
    private ArrayList<JButton> cartas_jugador;
    private HashMap<JButton ,Integer> mapeo_botones;
    private Controlador controlador;
    private VistaPrincipal vistaPrincipal;
    private JLabel fondo;
    private ArrayList<JLabel> cartas_mano;
    private JLabel palo_triunfo_carta;
    private MapeoCartas mapeoCartas;
    private JPanel panel_cartas_jug;
    private int centro;
    private int este;
    private int norte;
    private int oeste;

    private JButton boton_presionado; //esto puede ir si se rquiere la confirmacion del modelo para ver si la carta tirada es valida o no!

    //desde el modelo lo unico que tengo es la carta no el boton presionado. Si no tengo el ultimo boton presionado
                              // no se a que boton corresponde la carta


    public Cartas_en_mano (Controlador controlador, VistaPrincipal vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar (Controlador controlador, VistaPrincipal vistaPrincipal){
        this.mapeoCartas=new MapeoCartas();
        this.mapeo_botones=new HashMap<>();
        this.controlador=controlador;
        this.vistaPrincipal=vistaPrincipal;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(100, 100, 500, 500);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);
        ImageIcon fondo_i = new ImageIcon("src/Imagenes_cartas/MESA.png");
        this.fondo= new JLabel(fondo_i);
        fondo.setLayout(new BorderLayout());
        this.cartas_jugador= new ArrayList<>();
        this.cartas_mano= new ArrayList<>();
        this.panel_cartas_jug=new JPanel(new FlowLayout());
        setContentPane(fondo);
        fondo.add(panel_cartas_jug,BorderLayout.SOUTH);
    }
    public void iniciar_palo_triunfo(String palo_triunfo, String nombre_user){
        setTitle("El palo del triunfo es: " + palo_triunfo+ ". Vista de: "+ nombre_user);
    }
    public void mostrar_carta_palo_triunfo(int id_carta_triunfo){

    }
    public void iniciar_cartas_jugador(ArrayList<Integer> id_cartas){//cambiar de id carta (del controlador)
        for(Integer i:id_cartas){
            ImageIcon original=mapeoCartas.obtener_carta(i);
            Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);
            ImageIcon carta_modificada=new ImageIcon(imagen);
            JButton boton_carta=new JButton(carta_modificada);
            boton_carta.setBorderPainted(false);
            boton_carta.setContentAreaFilled(false);
            boton_carta.setFocusPainted(false);
            boton_carta.setEnabled(false);
            mapeo_botones.put(boton_carta, i);
            panel_cartas_jug.add(boton_carta);
            boton_carta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        boton_carta.setVisible(false);
                        controlador.tira_carta(mapeo_botones.get(boton_carta));
                        mapeo_botones.remove(boton_carta);
                        for(JButton b:mapeo_botones.keySet()){
                            b.setEnabled(false);
                        }
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
    public void iniciar_cartas_mano(int id_carta,int id_jugador){//cambiar a id de carta
        ImageIcon original=mapeoCartas.obtener_carta(id_carta);
        Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);
        ImageIcon carta_modificada=new ImageIcon(imagen);
        JLabel carta_mano=new JLabel(carta_modificada);
        cartas_mano.add(carta_mano);
        if(id_jugador==centro){
            carta_mano.setVisible(true);
            fondo.add(carta_mano, BorderLayout.CENTER);
            fondo.revalidate();
        }
        else if(id_jugador==este){
            carta_mano.setVisible(true);
            fondo.add(carta_mano,BorderLayout.EAST);
            fondo.revalidate();
        }
        else if(id_jugador==norte){
            carta_mano.setVisible(true);
            fondo.add(carta_mano,BorderLayout.NORTH);
            fondo.revalidate();

        }
        else if(id_jugador==oeste){
            carta_mano.setVisible(true);
            fondo.add(carta_mano,BorderLayout.WEST);
            fondo.revalidate();

        }
    }
    public void iniciar_posiciones(int cantidad_jug, int id){
        centro=id;
        id++;
        este=id%cantidad_jug;
        id++;
        norte=id%cantidad_jug;
        id++;
        oeste=id%cantidad_jug;
    }
    public void reiniciar_cartas_mano(){
        for(JLabel j:cartas_mano){
            j.setVisible(false);
        }
        cartas_mano.clear();
        System.out.println("Cartas_en_mano.reiniciar_cartas_mano. Cartas limpiadas de la mesa");
    }
    public void cartas_clicleables(ArrayList<Integer> cartas_posibles ) throws RemoteException {//cambiar a id de carta
        for(JButton b:mapeo_botones.keySet()){
            int id=mapeo_botones.get(b);
            for(int j:cartas_posibles){
                if(j==id){
                    System.out.println("Carta con id: "+id+" clicleable para: "+ controlador.getJuego().getJugador_actual().getNombre()+". Id asociado al boton: "+mapeo_botones.get(b));
                    boolean rta=SwingUtilities.isEventDispatchThread();
                    if(rta==true){
                        System.out.println("true");
                    }
                    else{
                        System.out.println("false");
                    }
                    b.setEnabled(true);
                }
            }
        }


    }
    public void todas_cartas_clicleables(){//en caso de que el jugador sea el primero en tirar, puede tirar cualquier carta
        for(JButton b:mapeo_botones.keySet()){
            b.setEnabled(true);
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
