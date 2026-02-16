package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Carta;
import Model.Jugador;
import Vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaPrincipal implements IVista {
    private Controlador controlador;
    private Menu_principal menu_principal;
    private VentanaInicioJugador ventanaInicioJugador;
    private EsperandoJugadores esperando;
    private Cartas_en_mano mano;
    //private Anuncios anuncios;
    //private Puntajes puntajes;

    public VistaPrincipal(Controlador controlador){
        this.controlador=controlador;
        controlador.setVistaPrincipal(this);
    }
    public void iniciar(){
        ventanaInicioJugador=new VentanaInicioJugador(this, controlador);
        menu_principal=new Menu_principal(controlador, this);
        esperando= new EsperandoJugadores();
        mano = new Cartas_en_mano(controlador, this);
        //anuncios=new Anuncios(controlador,this); //IMPLEMENTACION SIN JDIALOG
        //puntajes=new Puntajes(this, controlador);//IMPLEMENTACION SIN JDIALOG
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

    public void mostrar_mano(ArrayList<Integer> id_cartas_jugador, String palo_triunfo, int nombre_carta_de_triunfo){
        mano.iniciar_cartas_jugador(id_cartas_jugador);
        mano.iniciar_palo_triunfo(palo_triunfo, ventanaInicioJugador.getNombreUsuario());
    }

    public void set_cartas_clicleables(ArrayList<Integer> id_cartas_posibles) throws RemoteException {
        mano.cartas_clicleables(id_cartas_posibles);
    }
    public void limpiar_cartas_mesa(){
        mano.reiniciar_cartas_mano();
    }
    public void agregar_carta_mano(int id_carta, int id_actual){
        System.out.println("Vista Principal. agregar_carta_mano");
        mano.iniciar_cartas_mano(id_carta,id_actual);
    }

    public void iniciar_posiciones_mano(int cantidad, int id_jugador){
        mano.iniciar_posiciones(cantidad, id_jugador);
    }

    public void mostrar_mano_visible(){
        mano.setVisible(true);
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //IMPLEMENTACION JDIALOG ANUNCIOS

    public void oferta_tute(){
        mano.ofrecer_tute();
    }
    public void oferta_las_40(){
        mano.ofrecer_las_40();
    }
    public void oferta_las_20(){
        mano.ofrecer_las_20();
    }

    public void canta_tute(String nombre){
        mano.canta_tute(nombre);
    }
    public void canta_las_40(String nombre){
        mano.canta_las_40(nombre);
    }
    public void canta_las_20(String nombre){
        mano.canta_las_20(nombre);
    }

    public void gana_por_puntos(String nombre){
        mano.ganador_por_punts(nombre);
    }
    public void gana_ultimas_10(String nombre){
        mano.gana_ultimas_10(nombre);
    }
    public void deshabilitar_botones_cartas(){
        mano.deshabilitar_botones();
    }
    //IMPLEMENTACION JDIALOG PUNTAJES
    public void aniadir_jugador_a_tablas(int id, int puntaje, String nombre){
        mano.aniadir_jugador(id,  puntaje, nombre);
        esperando.aniadir_jugador(id,  puntaje, nombre);
    }
    public void actualizar_puntaje(int id, int puntaje, String nombre){
        System.out.println("actualizar_puntaje. Vista principal");
        mano.actualizar_puntaje_ganador(id, puntaje, nombre);
    }


}
