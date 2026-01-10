package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Carta;
import Model.Jugador;

import java.util.ArrayList;

public class VistaPrincipal {
    private Controlador controlador;
    private Menu_principal menu_principal;
    private VentanaInicioJugador ventanaInicioJugador;
    private EsperandoJugadores esperando;
    private Cartas_en_mano mano;
    private Anuncios anuncios;

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
        anuncios.ofrecer_tute();
    }
    public void oferta_las_40(){
        anuncios.ofrecer_las_40();
    }
    public void oferta_las_20(){
        anuncios.ofrecer_las_20();
    }
    public void canta_tute(String nombre){
        anuncios.canto_tute(nombre);
    }
    public void canta_las_40(String nombre){
        anuncios.canto_las_40(nombre);
    }
    public void canta_las_20(String nombre){
        anuncios.canto_las_20(nombre);
    }
    public void puntajes(ArrayList<Jugador> jugadores, Jugador ganador){
        anuncios.mostrar_puntajes(jugadores,ganador);
    }
    public void gana_por_puntos(String nombre){
        anuncios.ganador_por_punts(nombre);
    }
    public void gana_ultimas_10(ArrayList<Jugador> jugadores, Jugador ganador){
        anuncios.mostrar_puntajes_ultimas_10(jugadores,ganador);
    }
    public void set_cartas_clicleables(ArrayList<Integer> id_cartas_posibles){
        System.out.println("VistaPrincipal.set_cartas_clicleables de: "+ controlador.getId_jugador());
        mano.cartas_clicleables(id_cartas_posibles);
        mano.setVisible(true);
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
