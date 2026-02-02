package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Carta;
import Model.Jugador;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaPrincipal {
    private Controlador controlador;
    private Menu_principal menu_principal;
    private VentanaInicioJugador ventanaInicioJugador;
    private EsperandoJugadores esperando;
    private Cartas_en_mano mano;
    private Anuncios anuncios;
    private Puntajes puntajes;

    public VistaPrincipal(Controlador controlador){
        this.controlador=controlador;
        controlador.setVistaPrincipal(this);
    }
    public void iniciar(){
        ventanaInicioJugador=new VentanaInicioJugador(this, controlador);
        menu_principal=new Menu_principal(controlador, this);
        esperando= new EsperandoJugadores();
        mano = new Cartas_en_mano(controlador, this);
        anuncios=new Anuncios(controlador,this);
        puntajes=new Puntajes(this);
        mostrar_menu_principal();
    }
    public void mostrar_menu_principal(){
        menu_principal.setVisible(true);
    }
    public void mostrar_inicio(){
        ventanaInicioJugador.setVisible(true);
    }
    public void mostrar_esperando(){
        esperando.setVisible(true);
        mano.setTitle(ventanaInicioJugador.getNombreUsuario());
    }
    public void no_mostrar_espera(){
        esperando.setVisible(false);
    }
    public void agregar_jugador_a_la_espera(ArrayList<Jugador> jugadores){
        esperando.agregar(jugadores);
    }
    public void mostrar_mano(ArrayList<Integer> id_cartas_jugador, String palo_triunfo, int nombre_carta_de_triunfo){
        mano.iniciar_cartas_jugador(id_cartas_jugador);
        mano.iniciar_palo_triunfo(palo_triunfo, ventanaInicioJugador.getNombreUsuario());
        mano.mostrar_carta_palo_triunfo(nombre_carta_de_triunfo);
        mano.setVisible(true);
    }
    public void oferta_tute(){
        System.out.println("VISTA PRINCIPAL. OFERTA_TUTE");
        anuncios.toFront();
        anuncios.requestFocus();
        mano.setVisible(false);
        anuncios.ofrecer_tute();
    }
    public void oferta_las_40(){
        System.out.println("VISTA PRINCIPAL. OFERTA_LAS_40");
        mano.setVisible(false);
        anuncios.toFront();
        anuncios.requestFocus();
        anuncios.ofrecer_las_40();
    }
    public void oferta_las_20(){
        System.out.println("VISTA PRINCIPAL. OFERTA_LAS_20");
        mano.setVisible(false);
        anuncios.toFront();
        anuncios.requestFocus();
        anuncios.ofrecer_las_20();
    }
    public void canta_tute(String nombre){
        System.out.println("VISTA PRINCIPAL. CANTA_TUTE");
        mano.setVisible(false);
        anuncios.toFront();
        anuncios.requestFocus();
        anuncios.canto_tute(nombre);
    }
    public void canta_las_40(String nombre){
        System.out.println("VISTA PRINCIPAL. CANTA_LAS_40");
        mano.setVisible(false);
        anuncios.toFront();
        anuncios.requestFocus();
        anuncios.canto_las_40(nombre);
    }
    public void canta_las_20(String nombre){
        System.out.println("VISTA PRINCIPAL. CANTA_LAS_20");
        mano.setVisible(false);
        anuncios.toFront();
        anuncios.requestFocus();
        anuncios.canto_las_20(nombre);
    }
    public void actualizar_puntajes(ArrayList<Jugador> jugadores, Jugador ganador){
        mano.setVisible(false);
        puntajes.actualizar_puntaje(jugadores, ganador, " gano la baza. Puntajes:");
        puntajes.setVisible(true);
    }
    public void mostrar_mano_visible(){
        mano.setVisible(true);
    }
    public void gana_por_puntos(String nombre){
        anuncios.ganador_por_punts(nombre);
    }
    public void gana_ultimas_10(ArrayList<Jugador> jugadores, Jugador ganador){
        puntajes.actualizar_puntaje(jugadores,ganador, " gano la ultima baza. Suma 10 puntos!. Puntajes: ");
        puntajes.setVisible(true);
    }
    public void set_cartas_clicleables(ArrayList<Integer> id_cartas_posibles) throws RemoteException {
        System.out.println("VistaPrincipal.set_cartas_clicleables de: "+ controlador.getJuego().getJugador_actual().getNombre());
        mano.cartas_clicleables(id_cartas_posibles);

    }
    public void limpiar_cartas_mesa(){
        mano.reiniciar_cartas_mano();
    }
    public void agregar_carta_mano(int id_carta, int id_actual){
        System.out.println("Vista Principal. agregar_carta_mano");
        mano.iniciar_cartas_mano(id_carta,id_actual);
    }
    public void hacer_todas_clicleables(){
        mano.todas_cartas_clicleables();
    }
    public void iniciar_posiciones_mano(int cantidad, int id_jugador){
        mano.iniciar_posiciones(cantidad, id_jugador);
    }
    public void no_mostrar_mano(){
        mano.setVisible(false);
    }

    /*
    cartas_clicleables()
    iniciar_cartas_mano(int id_carta,int id_jugador)
    iniciar_cartas_jugador(ArrayList<Integer> id_cartas)
    */




}
