package Vista.VistaGrafica;

import Controlador.Controlador;
import Vista.TimerUnico;
import Vista.VistaGrafica.Utilidad.MapeoCartasGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class VPartida extends JFrame{
    private HashMap<JButton ,Integer> mapeo_botones;//a partir de un boton accedo al indice de la carta del boton tirado (va a servir
    // para cuando un jugador tira una carta poder decirle al controlador que indice de carta fue tirado)
    private Controlador controlador;
    private JLabel fondo;//contenedor principal de la ventana, tiene la imagen del fondo de la mesa
    private ArrayList<JLabel> cartas_mano;//son los labels de las cartas tiradas en una baza, se van a ir agregando JLabels cada vez que
    //un jugador tire una carta. Cuando termina la baza, se vacia el ArrayList y las cartas tiradas en la baza dejan de verse
    private MapeoCartasGrafica mapeoCartas;//Objeto que permite, a partir de un indice de carta, obtener la imagen correspondiente a
    // ese indice de carta
    private JPanel panel_cartas_jug;//flowlayout de las cartas del jugador de esta vista, se va a incluir dentro del panel
    // "panel_cartas_jug_y_botones"
    private JPanel panel_cartas_jug_y_botones;//va a tener todo lo que es la parte sur del panel principal, va a tener las cartas del jugador
    //de la vista ("panel_cartas_jug") y algunos labels y botones (ver_puntaje, palo_triunfo, turno_actual)
    private JButton ver_puntaje;//permite ver el puntaje actual
    private JLabel palo_triunfo;//etiqueta del palo del triunfo
    private JLabel turno_actual;//etiqueta del turno actual
    private JPanel panel_botones_puntaje_y_triunfo;//box layout que contiene los 3 objetos de arriba y van a estar al este de "panel_cartas_jug_y_botones"
    private int centro;//ubicaciones de cada jugador segun ID
    private int este;//ubicaciones de cada jugador segun ID
    private int norte;//ubicaciones de cada jugador segun ID
    private int oeste;//ubicaciones de cada jugador segun ID
    private DAnuncios anuncios;//JDialog de anuncios
    private DPuntajes puntajes;//JDialog de puntajes
    //los JDialogs son ventanas que aparecen dentro de esta ventana, es decir, toman el foco al aparecer teniendo de fondo este frame



    public VPartida(Controlador controlador, VistaGrafica vistaPrincipal){
        inicializar(controlador,vistaPrincipal);
    }
    private void inicializar (Controlador controlador, VistaGrafica vistaPrincipal){
        this.mapeoCartas=new MapeoCartasGrafica();
        this.mapeo_botones=new HashMap<>();
        this.ver_puntaje=new JButton("Ver puntajes");
        this.palo_triunfo=new JLabel();
        this.turno_actual = new JLabel("Turno de: ");
        this.controlador=controlador;
        anuncios=new DAnuncios(this,controlador,vistaPrincipal);
        puntajes=new DPuntajes(this,vistaPrincipal, controlador);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//que pasa al cerrar la ventana
        setBounds(50, 50, 1400, 1200);//posicion x (horizontal)=100, posicion y (vertical)=100, ancho=247 , largo=109
        setLocationRelativeTo(null);//centra la ventana en la pantalla
        ImageIcon fondo_i = new ImageIcon("src/Imagenes_cartas/MESA.png");//se obtiene la imagen del fondo
        this.fondo= new JLabel(fondo_i);//se crea un JLabel con la imagen del fondo
        fondo.setLayout(new BorderLayout());
        this.cartas_mano= new ArrayList<>();
        this.panel_cartas_jug=new JPanel(new FlowLayout());
        this.panel_cartas_jug_y_botones=new JPanel(new BorderLayout());
        this.panel_botones_puntaje_y_triunfo=new JPanel();
        panel_botones_puntaje_y_triunfo.setLayout(new BoxLayout(panel_botones_puntaje_y_triunfo,BoxLayout.Y_AXIS));
        panel_botones_puntaje_y_triunfo.add(ver_puntaje);
        panel_botones_puntaje_y_triunfo.add(Box.createVerticalStrut(5));
        panel_botones_puntaje_y_triunfo.add(palo_triunfo);
        panel_botones_puntaje_y_triunfo.add(turno_actual);
        panel_cartas_jug_y_botones.add(panel_cartas_jug,BorderLayout.CENTER);
        panel_cartas_jug_y_botones.add(panel_botones_puntaje_y_triunfo,BorderLayout.EAST);
        setContentPane(fondo);
        fondo.add(panel_cartas_jug_y_botones,BorderLayout.SOUTH);
        //fondo.add(panel_cartas_jug,BorderLayout.SOUTH);
        ver_puntaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!puntajes.isVisible()){
                    puntajes.mostrarme_partida_en_curso();//un metodo distinto que haga que se muestre un boton de OK distinto en puntajes
                                                          //con el objetivo de que el boton de OK no haga un controlador.procesar_eventos_pendientes()
                }
            }
        });
    }
    public void iniciar_palo_triunfo(String palo_triunfo1, String nombre_user){
        setTitle("Vista de: "+ nombre_user);
        palo_triunfo.setText("El palo del triunfo es: "+palo_triunfo1);
        System.out.println("Palo del triunfo: "+palo_triunfo1);
    }
    public void limpiar_tab(){
        puntajes.limpiar_puntajes();
    }

    public void iniciar_cartas_jugador(ArrayList<Integer> id_cartas){//cambiar de id carta (del controlador)
        System.out.println("Mis cartas:\n");
        for(Integer i:id_cartas){
            ImageIcon original=mapeoCartas.obtener_carta(i);//obtengo la imagen original para esa carta
            Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);//creo la imagen con los tamanios adecuados
            ImageIcon carta_modificada=new ImageIcon(imagen);//creo el ImageIcon con el tamanio de carta defintiivo
            JButton boton_carta=new JButton(carta_modificada);//creo un boton con el ImageIcon
            boton_carta.setBorderPainted(false);//oculta el borde del boton
            boton_carta.setContentAreaFilled(false);//elimina el fondo del boton haciendolo transparente
            boton_carta.setFocusPainted(false);//elimina el resaltado cuando el boton tiene el foco
            boton_carta.setEnabled(false);//deshabilita el boton
            mapeo_botones.put(boton_carta, i);
            panel_cartas_jug.add(boton_carta);
            boton_carta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        boton_carta.setVisible(false);//deja de hacer visible la carta tirada
                        controlador.tira_carta(mapeo_botones.get(boton_carta));//invoca el metodo para tirar la carta del controlador
                        //pasandole el indice de la carta tirada gracias al HashMap mapeo_botones con la clave es el boton y el valor el
                        //indice de la carta del boton
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
    public void iniciar_cartas_mano(int id_carta,int id_jugador){
        ImageIcon original=mapeoCartas.obtener_carta(id_carta);
        Image imagen=original.getImage().getScaledInstance(80,140,Image.SCALE_SMOOTH);
        ImageIcon carta_modificada=new ImageIcon(imagen);
        JLabel carta_mano=new JLabel(carta_modificada);
        cartas_mano.add(carta_mano);//se aniade la carta tirada a un ArrayList para vaciarlo y que se limpie la mesa cuando termina la baza
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

    //este metodo se encarga de que
    public void iniciar_posiciones(int cantidad_jug, int id){//se recibe la cantidad de jugadores y el id del jugador de esta vista
        centro=id;//suponga que id=3 y cantidad_jug=4, el jugador de esta vista siempre va en el centro
        id++;//id=4
        este=id%cantidad_jug;//4/4->resto 0. Es decir el jugador con id=0 va a estar a la derecha de este jugador (id=3)
        id++;//id=5
        norte=id%cantidad_jug;//5/4->resto 1. Es decir el jugador con id=1 va a estar en frente de este jugador (id=3) y a la derecha del jugador con id=0
        id++;//6
        oeste=id%cantidad_jug;//6/4->resto 2. Es decir el jugador con id=2 va a estar a la izquierda de este jugador (id=3) y a la derecha del jugador con id=1
    }//quedaria 3->0->1->2->3->0->1->2..... (orden correcto)
    public void reiniciar_cartas_mano(){
        for(JLabel j:cartas_mano){
            j.setVisible(false);
        }
        cartas_mano.clear();
        System.out.println("Cartas_en_mano.reiniciar_cartas_mano. Cartas limpiadas de la mesa");
    }


    //El objetivo de este metodo es hacer clicleables los botones de las cartas disponibles para tirar, recibiendo los indices posibles por parametro
    public void cartas_clicleables(ArrayList<Integer> cartas_posibles ) throws RemoteException {
        System.out.println("Cartas clicleables:\n");
        for(JButton b:mapeo_botones.keySet()){//se evalua cada boton del HashMap de botones del jugador
            int id=mapeo_botones.get(b);//extrae cada indice a partir de la clave (el boton)
            for(int j:cartas_posibles){//recorre todos los indices de cartas disponibles para tirar
                if(j==id){//si el ID de la carta del boton es igual al ID de los indices de cartas disponibles para tirar
                    b.setEnabled(true);//habilita ese boton
                    System.out.println(mapeoCartas.obtener_carta(id));
                }
            }
        }
    }
    public void mostrar_turno(String nombre_jug){
        this.turno_actual.setText("Turno de: " + nombre_jug);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //implementacion JDialog anuncios
    public void canto(String cadena){
        anuncios.canto(cadena);
    }
    public void cartel_ganador(String cadena){
        anuncios.cartel_ganador(cadena);
    }
    public void oferta_canto(String cadena){
        anuncios.oferta_canto(cadena);
    }


    //implementacion JDialog puntajes
    public void aniadir_jugador(int id,  int puntaje,String nombre){
        puntajes.aniadir_jugador(id, puntaje, nombre);
    }
    public void actualizar_puntaje_ganador(int id, int puntaje, String nombre){
        puntajes.actualizar_puntaje_ganador(id, puntaje, nombre);
    }
    public void mostrar_los_puntajes(){
        puntajes.mostrarme();
    }
    public void limpiar_partida(){
        if(puntajes.isVisible()){
            puntajes.modificar_accion();
        }
        if(anuncios.isVisible()){//nunca van a estar visibles los anuncios en esta intancia
            anuncios.dejar_de_verme();
        }

        for(JButton j:mapeo_botones.keySet()){
            j.setVisible(false);
        }//dejo de hacer visibles los botones ya que al ganar con tute, el jugador se va a quedar con cartas sin tirar
        //caso distinto a ganar por puntos donde si o si el jugador ganador no va a tener ninguna carta.
        mapeo_botones.clear();
    }
    public void mostrar_mensaje_error(String cadena_error){
        setVisible(false);
        anuncios.cartel_error(cadena_error);
    }
    public void esperar_confirmacion(String cadena){
        anuncios.esperar_confirmaciones(cadena);
    }
    public void no_mostrar_espera_confirmaciones(){
        anuncios.setVisible(false);
    }
}