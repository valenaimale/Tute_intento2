package Controlador;

import Model.*;
import Vista.IVista;
import Vista.VistaGrafica.VistaPrincipal;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.*;

public class Controlador implements IControladorRemoto {
    IVista vista;
    IJuego juego;
    int id_jugador;
    Queue<Eventos> eventos_pendientes;
    Estado_UI estado_ui;

    public Controlador() {
        estado_ui=Estado_UI.NORMAL;
        eventos_pendientes=new ArrayDeque<>();
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciar_player(String nombre) throws RemoteException {
        Jugador jugador = new Jugador(nombre);
        jugador.setId(juego.siguienteId());
        id_jugador = jugador.getId();
        juego.iniciar_jugador(jugador);
    }

    public void tira_carta(int id_carta) throws RemoteException {
        juego.tirada_de_carta(id_carta);
    }

    public void eleccion_si() throws RemoteException {
        juego.canto_positivo();
    }

    public void eleccion_no() throws RemoteException {
        juego.canto_negativo();
    }

    public void procesar_eventos_pendientes() throws RemoteException {
        estado_ui=Estado_UI.NORMAL;
        for(Eventos e: eventos_pendientes){
            System.out.println(e);
        }
        while(estado_ui==Estado_UI.NORMAL && !eventos_pendientes.isEmpty()){//ver ppor que entra al while si la cola de eventos pendientes esta vacia
            Eventos evento_a_procesar=eventos_pendientes.poll();
            System.out.println(evento_a_procesar);
            procesar_evento(evento_a_procesar);
        }
    }
    //se me ocurre que para las cartas en la vista de consola puede haber un hashMap con
    private ArrayList<Integer> cartas_repartidas_a_mi_jugador() throws RemoteException {
        ArrayList<Integer> id_cartas = new ArrayList<>();
        for(Jugador j: juego.getJugadores()){
            if(j.getId()==id_jugador){
                for (Carta c : j.getMazo_jugador()) {
                    id_cartas.add(c.getId());
                }
            }
        }
        return id_cartas;
    }

    private void procesar_evento(Eventos evento) throws RemoteException {
        switch (evento){
            case ULTIMAS_10:
                estado_ui=Estado_UI.ESPERANDO_OK_ULTIMAS_10;
                vista.gana_ultimas_10(juego.getGanador_parcial().getNombre());
                break;
            case GANADOR_POR_PUNTOS:
                estado_ui=Estado_UI.ESPERANDO_OK_GANADOR;
                vista.gana_por_puntos(juego.getGanador().getNombre());
                break;
            case MANO_TERMINADA:
                System.out.println(Eventos.MANO_TERMINADA);
                estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                vista.mostrar_puntajes();
                break;
            case TERMINO_JUEGO:
                estado_ui=Estado_UI.ESPERANDO_RTA_TERMINO_JUEGO;
                System.out.println("controlador termino_juego");
                vista.cierre_juego(juego.getGanador().getNombre());
                break;
            case GANADOR_POR_TUTE:
                estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                vista.canta_tute(juego.getGanador().getNombre());
                break;
            case CARTAS_REPARTIDAS:
                vista.iniciar_valores_partida(cartas_repartidas_a_mi_jugador(), juego.getPalo_triunfo().getPalo());
                vista.mostrar_turno(juego.getJugador_actual().getId());
                if (juego.getJugador_actual().getId() == id_jugador) {
                    vista.setCartas_clicleables(juego.cartas_posibles());
                }
                break;
            case ACTUALIZACION_TURNO:
                vista.mostrar_turno(juego.getJugador_actual().getId());
                if (juego.getJugador_actual().getId() == id_jugador) {
                    System.out.println(Eventos.ACTUALIZACION_TURNO);
                    vista.setCartas_clicleables(juego.cartas_posibles());
                }
                break;

        }

    }
    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.juego = (IJuego) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        try {
            Eventos evento = (Eventos) o;
            switch (evento) {
                case COMENZAR_JUEGO:
                    vista.no_mostrar_espera(juego.getJugadores().size(), id_jugador);
                    break;
                case JUGADOR_AGREGADO:
                    vista.limpiar_tablas();
                    for(Jugador j:juego.getJugadores()){
                        vista.aniadir_jugador_a_tablas(j.getId(),j.getPuntaje(), j.getNombre());
                    }
                    break;
                case CARTAS_REPARTIDAS:
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.CARTAS_REPARTIDAS);
                    }
                    else{
                        vista.iniciar_valores_partida(cartas_repartidas_a_mi_jugador(), juego.getPalo_triunfo().getPalo());
                        vista.mostrar_turno(juego.getJugador_actual().getId());
                        if (juego.getJugador_actual().getId() == id_jugador) {
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                    }
                    break;
                case GANADOR_POR_TUTE:
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.GANADOR_POR_TUTE);
                    }
                    else{
                        try {
                            estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                            vista.canta_tute(juego.getGanador().getNombre());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case OFRECER_LAS_40:
                    if(juego.getGanador_parcial().getId()==id_jugador) {
                        vista.oferta_las_40();
                    }
                    break;
                case CANTA_LAS_40:
                    try {
                        estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                        vista.canta_las_40(juego.getGanador_parcial().getNombre());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case CANTA_LAS_20:
                    try {
                        estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                        vista.canta_las_20(juego.getGanador_parcial().getNombre());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case MANO_TERMINADA:
                    vista.limpiar_cartas_mesa();
                    vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre() );
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.MANO_TERMINADA);
                    }
                    else{
                        System.out.println(Eventos.MANO_TERMINADA);
                        estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                        vista.mostrar_puntajes();
                    }
                    break;
                case OFRECER_TUTE:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        vista.oferta_tute();
                    }
                    break;
                case OFRECER_LAS_20:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        vista.oferta_las_20();
                    }
                    break;
                case GANADOR_POR_PUNTOS:
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.GANADOR_POR_PUNTOS);
                    }
                    else{
                        vista.gana_por_puntos(juego.getGanador().getNombre());
                    }
                    break;
                case ACTUALIZACION_TURNO:
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.ACTUALIZACION_TURNO);
                    }
                    else{
                        vista.mostrar_turno(juego.getJugador_actual().getId());
                        if (juego.getJugador_actual().getId() == id_jugador) {
                            System.out.println("actualizacion de turno desde el controlador");
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                    }
                    break;
                case CARTA_TIRADA:
                    vista.agregar_carta_mano(juego.getCartas_jugadas_en_la_mano().getLast().getId(), juego.getJugador_actual().getId());
                    break;
                case ULTIMAS_10:
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.ULTIMAS_10);
                    }
                    else{
                        estado_ui=Estado_UI.ESPERANDO_OK_ULTIMAS_10;
                        vista.gana_ultimas_10(juego.getGanador_parcial().getNombre());
                    }
                    break;
                case TERMINO_JUEGO:
                    if(estado_ui!=Estado_UI.NORMAL){
                        System.out.println("controlador termino_juego con estado_ui==normal");
                        eventos_pendientes.add(Eventos.TERMINO_JUEGO);
                    }
                    else{
                        System.out.println("controlador termino_juego");
                        vista.cierre_juego(juego.getGanador().getNombre());
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void terminar(){
        System.exit(0);
    }

    //vistaPrincipal.actualizar_puntajes(juego.getJugadores(), juego.getGanador_parcial());
    //CONCEPTO CLAVE-> O HACES TODO INVOKE LATER O TODO SIN INVOKE LATER, SINO HAY BUGS
    //LA VENTANA DE LA MANO SE TIENE QUE HACER VISIBLE UNICAMENTE DESPUES DE PRESIONAR "OK" EN LOS PUNTAJES.
    /*
    COMENZAR_JUEGO,//COMIENZA EL JUEGO, EL PRIMER JUGADOR TIENE QUE TIRAR UNA CARTA
    JUGADOR_AGREGADO,//SE AGREGA UN JUGADOR AL ARRAYLIST DE JUGADORES
    CARTAS_REPARTIDAS,//SE AGREGAN LAS CARTAS AL MAZO DE CADA JUGADOR
    ACTUALIZACION_TURNO,//SE ACTUALIZA EL JUGADOR_ACTUAL
    MANO_TERMINADA,//SE LIMPIAN LAS CARTAS DE LA MESA (CARTAS_JUGADAS_EN_LA_MANO.ISEMPTY())
    GANADOR_POR_PUNTOS,//SE ESTABLECE EL GANADOR_FINAL
    ULTIMAS_10,//SE LE SUMAN 10 PUNTOS AL JUGADOR_ACTUAL Y CAMBIA EL ESTADO DE ESTADO_CANTO
    ULTIMAS_10_GANADOR,//SE LE SUMAN 10 PUNTOS AL JUGADOR_ACTUAL, CAMBIA EL ESTADO DE ESTADO_CANTO Y SE ESTABLECE UN GANADOR_FINAL
    CARTA_TIRADA,//SE SUMA UNA CARTA AL ARRAYLIST DE CARTAS_JUGADAS_EN_LA_MANO
    OFRECER_LAS_20,//CAMBIA EL ESTADO DE ESTADO_CANTO
    CANTA_LAS_20,//SE SUMAN 20 PUNTOS AL JUGADOR ACTUAL
    CANTA_LAS_20_ULTIMAS_10_GANADOR,//SE SUMAN 20 PUNTOS AL JUGADOR ACTUAL Y 10 MAS POR GANAR LA ULTIMA BAZA Y HAY SE ESTABLECE UN GANADOR_FINAL
    CANTA_LAS_20_ULTIMAS_10,//SE SUMAN 20 PUNTOS AL JUGADOR ACTUAL Y 10 MAS POR GANAR LA ULTIMA BAZA
    OFRECER_LAS_40,//CAMBIA EL ESTADO DE ESTADO_CANTO
    CANTA_LAS_40,//SE SUMAN 40 PUNTOS AL JUGADOR ACTUAL
    CANTA_LAS_40_ULTIMAS_10_GANADOR,//SE SUMAN 40 PUNTOS AL JUGADOR ACTUAL Y 10 MAS POR GANAR LA ULTIMA BAZA Y HAY SE ESTABLECE UN GANADOR_FINAL
    CANTA_LAS_40_ULTIMAS_10,//SE SUMAN 40 PUNTOS AL JUGADOR ACTUAL Y 10 MAS POR GANAR LA ULTIMA BAZA
    OFRECER_TUTE,//CAMBIA EL ESTADO DE ESTADO_CANTO
    GANADOR_POR_TUTE,//SE ESTABLECE UN GANADOR_FINAL POR TUTE
    GANADOR_POR_TUTE_ULTIMAS_10,//SE SUMAN 10 POR GANAR LA ULTIMA BAZA Y SE ESTABLECE UN GANADOR_FINAL POR TUTE
     */

    /*
    LA IDEA ES QUE CON LA COLA DE EVENTOS PENDIENTES, SE PUEDA LOGRAR QUE EL CONTROLADOR MUESTRE UN EVENTO UNO ATRAS DEL OTRO CUANDO
    CORRESPONDA SEGUN EL ESTADO EN EL QUE ESTA LA INTERFAZ GRAFICA!
    ESTO TE PERMITE QUE UNA VEZ QUE SE PRESIONA "OK" EN "Valen canto las 20. Suma 20 puntos!" EL ACTION LISTENER SEA:
    "controlador.procesar_eventos_pendientes()"
    VAMOS VAMOS VAMOS A CORONAR!
    SOMOS DE BOCA MUCHACHOS BUENOS EL QUE NO ES CHORRO ES CRIMINAL
    EL MAS COBARDE MATO A SU MADRE Y EL MAS VALIENTE PARA QUE VAMOS A HABLAR

     */

}