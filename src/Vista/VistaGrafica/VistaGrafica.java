package Vista.VistaGrafica;

import Controlador.Controlador;
import Vista.IVista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class VistaGrafica implements IVista {
    private Controlador controlador;
    private VMenuPrincipal ventana_menu_principal;
    private VInicioJugador ventana_inicio_jugador;
    private VEsperandoJugadores ventana_esperando;
    private VPartida ventana_partida;
    private VRanking ventana_ranking;
    private VComoJugar vComoJugar;


    public VistaGrafica(Controlador controlador){
        this.controlador=controlador;

    }
    @Override
    public void iniciar(){
        ventana_inicio_jugador =new VInicioJugador(this, controlador);
        ventana_menu_principal =new VMenuPrincipal(controlador, this);
        ventana_esperando = new VEsperandoJugadores();
        ventana_partida = new VPartida(controlador, this);
        ventana_ranking = new VRanking(this);
        vComoJugar = new VComoJugar(this);
        mostrar_menu_principal();
    }
    public void mostrar_menu_principal(){
        ventana_menu_principal.setVisible(true);
    }
    public void mostrar_inicio(){
        ventana_inicio_jugador.setVisible(true);
    }
    public void mostrar_como_jugar(){
        vComoJugar.setVisible(true);
    }
    public void no_mostrar_mano(){
        ventana_partida.setVisible(false);
    }
    @Override
    public void mostrar_esperando(){
        ventana_esperando.setVisible(true);
        ventana_partida.setTitle(ventana_inicio_jugador.getNombreUsuario());
    }
    public void no_mostrar_espera(int cantidad, int id_jugador){
        iniciar_posiciones_mano(cantidad, id_jugador);
        ventana_esperando.setVisible(false);
    }
    @Override
    public void iniciar_valores_partida(ArrayList<Integer> id_cartas_jugador, String palo_triunfo){
        ventana_partida.iniciar_cartas_jugador(id_cartas_jugador);
        ventana_partida.iniciar_palo_triunfo(palo_triunfo, ventana_inicio_jugador.getNombreUsuario());
        ventana_partida.setVisible(true);
        System.out.println("Ventana partida visible:"+ventana_partida.isVisible());
    }

    @Override
    public void limpiar_tablas() {
        ventana_partida.limpiar_tab();
        ventana_esperando.borrar_jugadores();
    }

    @Override
    public void setCartas_clicleables(ArrayList<Integer> ids_posibles) throws RemoteException {
        ventana_partida.cartas_clicleables(ids_posibles);
    }

    public void limpiar_cartas_mesa(){
        ventana_partida.reiniciar_cartas_mano();
    }
    public void agregar_carta_mano(int id_carta, int id_actual){
        System.out.println("Vista Principal. agregar_carta_mano");
        ventana_partida.iniciar_cartas_mano(id_carta,id_actual);
    }

    private void iniciar_posiciones_mano(int cantidad, int id_jugador){
        ventana_partida.iniciar_posiciones(cantidad, id_jugador);
    }
    public void mostrar_mano_visible(){
        ventana_partida.setVisible(true);
    }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //IMPLEMENTACION JDIALOG ANUNCIOS

    public void oferta_canto(String cadena){
        ventana_partida.oferta_canto(cadena);
    }
    public void canto(String cadena){
        ventana_partida.canto(cadena);
    }
    public void cartel_ganador(String cadena){
        ventana_partida.cartel_ganador(cadena);
    }




    //IMPLEMENTACION JDIALOG PUNTAJES


    public void aniadir_jugador_a_tablas(int id, int puntaje, String nombre){
        ventana_partida.aniadir_jugador(id,  puntaje, nombre);
        ventana_esperando.aniadir_jugador(id,  puntaje, nombre);
    }
    public void actualizar_puntaje(int id, int puntaje, String nombre){
        System.out.println("actualizar_puntaje. Vista principal");
        ventana_partida.actualizar_puntaje_ganador(id, puntaje, nombre);
    }

    @Override
    public void mostrar_puntajes() {
        ventana_partida.mostrar_los_puntajes();
    }
    @Override
    public void limpiar_partida(){
        ventana_partida.limpiar_partida();
    }
    @Override
    public void mostrar_turno(int id){
    }

    @Override
    public void mostrar_mensaje_error(String error) {
        ventana_partida.mostrar_mensaje_error(error);
    }

    public void mostrar_ranking_al_comienzo() throws RemoteException {
        ventana_ranking.cargarDatos(controlador.getTablaRanking());
        ventana_ranking.mostrar_boton_volver_comienzo();
        ventana_ranking.setVisible(true);
    }
    public void mostrar_ranking_al_finalizar() throws RemoteException {
        ventana_partida.setVisible(false);
        ventana_ranking.cargarDatos(controlador.getTablaRanking());
        ventana_ranking.mostrar_boton_volver_final();
        ventana_ranking.setVisible(true);
    }
    public String nombre_user(){
        return ventana_inicio_jugador.getNombreUsuario();
    }




}
