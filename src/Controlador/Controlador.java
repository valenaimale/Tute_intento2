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
        if(juego.getJugadores().size()>=4){
            terminar();
        }
        else{
            if(estado_ui == Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO){
                estado_ui = Estado_UI.NORMAL;
            }
            Jugador jugador = new Jugador(nombre);
            jugador.setId(juego.siguienteId());
            id_jugador = jugador.getId();
            juego.iniciar_jugador(jugador);
        }
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
    private void procesar_evento(Eventos evento) throws RemoteException {
        switch (evento){
            case GANADOR_POR_PUNTOS:
                estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                vista.gana_por_puntos();
                break;
            case MANO_TERMINADA:
                System.out.println(Eventos.MANO_TERMINADA);
                estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                vista.mostrar_puntajes();
                break;
            case ACTUALIZACION_TURNO:
                vista.mostrar_turno(juego.getJugador_actual().getId());
                if (juego.getJugador_actual().getId() == id_jugador) {
                    vista.setCartas_clicleables(juego.cartas_posibles());
                }
                break;
        }
    }

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
                    if(estado_ui==Estado_UI.NORMAL){
                        vista.limpiar_tablas();
                        for(Jugador j:juego.getJugadores()){
                            vista.aniadir_jugador_a_tablas(j.getId(),j.getPuntaje(), j.getNombre());
                        }
                    }
                    break;
                case CARTAS_REPARTIDAS:
                    vista.iniciar_valores_partida(cartas_repartidas_a_mi_jugador(), juego.getPalo_triunfo());
                    vista.mostrar_turno(juego.getJugador_actual().getId());
                    if (juego.getJugador_actual().getId() == id_jugador) {
                        vista.setCartas_clicleables(juego.cartas_posibles());
                    }
                    break;
                case GANADOR_POR_TUTE:
                    estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                    vista.canta_tute();
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
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                    }
                    break;
                case CARTA_TIRADA:
                    vista.agregar_carta_mano(juego.getCartas_jugadas_en_la_mano().getLast().getId(), juego.getJugador_actual().getId());
                    break;
                case ULTIMAS_10:
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

    /*
    LA IDEA ES QUE CON LA COLA DE EVENTOS PENDIENTES, SE PUEDA LOGRAR QUE EL CONTROLADOR MUESTRE UN EVENTO UNO ATRAS DEL OTRO CUANDO
    CORRESPONDA SEGUN EL ESTADO EN EL QUE ESTA LA INTERFAZ GRAFICA!
    ESTO TE PERMITE QUE UNA VEZ QUE SE PRESIONA "OK" EN "Valen canto las 20. Suma 20 puntos!" EL ACTION LISTENER SEA:
    "controlador.procesar_eventos_pendientes()"
    */

}