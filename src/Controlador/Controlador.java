package Controlador;

import Model.*;
import Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

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
        if(estado_ui == Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO){
            estado_ui = Estado_UI.NORMAL;
        }
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
        System.out.println("Eventos pendientes a procesar:");
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
            /*case ULTIMAS_10:
                estado_ui=Estado_UI.ESPERANDO_OK_ULTIMAS_10;
                vista.gana_ultimas_10(juego.getGanador_parcial().getNombre());
                break;*/
            case GANADOR_POR_PUNTOS:
                estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                vista.gana_por_puntos();
                break;
            case MANO_TERMINADA:
                System.out.println(Eventos.MANO_TERMINADA);
                estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                vista.mostrar_puntajes();
                break;
            /*case GANADOR_POR_TUTE:
                estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                vista.canta_tute();
                break;*/
            /*case CARTAS_REPARTIDAS:
                vista.iniciar_valores_partida(cartas_repartidas_a_mi_jugador(), juego.getPalo_triunfo());
                vista.mostrar_turno(juego.getJugador_actual().getId());
                if (juego.getJugador_actual().getId() == id_jugador) {
                    vista.setCartas_clicleables(juego.cartas_posibles());
                }
                break;*/
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
/*
notificarObservadores(Eventos.ULTIMAS_10);
notificarObservadores(Eventos.GANADOR_POR_PUNTOS);
notificarObservadores(Eventos.MANO_TERMINADA);
notificarObservadores(Eventos.TERMINO_JUEGO);


1) JUGADOR AGREGADO -> ESTADO_UI = NORMAL
2) COMENZAR_JUEGO -> ESTADO_UI = NORMAL
3) CARTAS_REPARTIDAS -> ESTADO_UI = NORMAL , SE HACE CONTROLADOR.TIRA_CARTA(INT ID)
4) CARTA_TIRADA -> ESTADO_UI = NORMAL, SE ANIADE LA CARTA TIRADA A LA MESA
5.A) SI NO TERMINO LA JUGADA SE HACE ACTUALIZACION_TURNO -> ESTADO_UI = NORMAL, SE HACEN LAS CARTAS CLICLEABLES PARA EL JUGADOR DEL TURNO
6.A) CARTA_TIRADA DE VUELTA -> ESTADO_UI = NORMAL
5.B) SI TERMINO LA JUGADA Y HAY ALGO PARA CANTAR SE HACE OFRECER_CANTO -> ESTADO_UI = NORMAL, SE LE OFRECE AL JUGADOR
6.B.1) SI DICE QUE SI EL JUGADOR SE HACE CANTA_CANTO -> ESTADO_UI = ESPERANDO_OK_CANTO, UNA VEZ APRETADO OK-> 7.B.1
7.B.1) SE HACE PROCESAR_EVENTOS_PENDIENTES DESDE LA VISTA, EN ESTE CASO SE PROCESA MANO_TERMINADA -> ESTADO_UI = ESPERANDO_OK_PUNTAJES

*/
    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        try {
            Eventos evento = (Eventos) o;
            switch (evento) {
                case COMENZAR_JUEGO:
                    vista.no_mostrar_espera(juego.getJugadores().size(), id_jugador);
                    System.out.println("Comenzar_juego. Controlador");
                    break;
                case JUGADOR_AGREGADO:
                    if(estado_ui==Estado_UI.NORMAL){
                        vista.limpiar_tablas();
                        for(Jugador j:juego.getJugadores()){
                            vista.aniadir_jugador_a_tablas(j.getId(),j.getPuntaje(), j.getNombre());
                        }
                    }
                    break;
                case CARTAS_REPARTIDAS:
                    /*if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.CARTAS_REPARTIDAS);
                    }*/
                    //else{
                    System.out.println("Cartas_repartidas. Controlador");
                    vista.iniciar_valores_partida(cartas_repartidas_a_mi_jugador(), juego.getPalo_triunfo());
                    vista.mostrar_turno(juego.getJugador_actual().getId());
                    if (juego.getJugador_actual().getId() == id_jugador) {
                        System.out.println("Cartas_repartidas en el controlador. Soy el jugador actual! Mi ID:"+id_jugador);
                        vista.setCartas_clicleables(juego.cartas_posibles());
                    }
                    //}
                    break;
                case GANADOR_POR_TUTE:
                    vista.setear_ganador(juego.getGanador().getNombre());
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.GANADOR_POR_TUTE);
                    }
                    else{
                        estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                        vista.canta_tute();
                    }
                    break;
                case OFRECER_LAS_40:
                    if(juego.getGanador_parcial().getId()==id_jugador) {
                        vista.oferta_las_40();
                    }
                    break;
                case CANTA_LAS_40:
                    estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                    vista.canta_las_40(juego.getGanador_parcial().getNombre());
                    break;
                case CANTA_LAS_20:
                    estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                    vista.canta_las_20(juego.getGanador_parcial().getNombre());
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
                    vista.setear_ganador(juego.getGanador().getNombre());
                    if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.GANADOR_POR_PUNTOS);
                    }
                    else{
                        estado_ui = Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                        vista.gana_por_puntos();
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
                    System.out.println("Jugador actual: "+juego.getJugador_actual().getNombre()+". Tiro la carta con id="+juego.getCartas_jugadas_en_la_mano().getLast().getId());
                    break;
                case ULTIMAS_10:
                    /*if(estado_ui!=Estado_UI.NORMAL){
                        eventos_pendientes.add(Eventos.ULTIMAS_10); -> no hay chance que haya un evento pendiente antes de cantar las ultimas_10
                    }*/
                    //else{
                    estado_ui=Estado_UI.ESPERANDO_OK_ULTIMAS_10;
                    vista.gana_ultimas_10(juego.getGanador_parcial().getNombre());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object[][] getTablaRanking() throws RemoteException {
        return juego.getTablaRanking();
    }
    public void terminar() throws RemoteException {
        juego.removerObservador(this);
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
    */

}