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
        estado_ui=Estado_UI.ESPERANDO_INGRESO_NOMBRE;
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
            if(estado_ui != Estado_UI.NORMAL){
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
        System.out.println("Eventos pendientes:\n");
        for(Eventos e: eventos_pendientes){
            System.out.println(e);
        }
        while(estado_ui==Estado_UI.NORMAL && !eventos_pendientes.isEmpty()){//ver ppor que entra al while si la cola de eventos pendientes esta vacia
            Eventos evento_a_procesar=eventos_pendientes.poll();
            procesar_evento(evento_a_procesar);
        }
    }
    private void procesar_evento(Eventos evento) throws RemoteException {
        switch (evento){
            case GANADOR_POR_PUNTOS:
                estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                vista.gana_por_puntos();
                break;
            case BAZA_TERMINADA:
                estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                vista.mostrar_puntajes();
                break;
            case ACTUALIZACION_TURNO, CARTAS_REPARTIDAS:
                if (juego.getJugador_actual().getId() == id_jugador) {
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
                    if(estado_ui==Estado_UI.ESPERANDO_INGRESO_NOMBRE || estado_ui==Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO){
                        juego.removerObservador(this);
                        vista.mostrar_mensaje_error();
                    }
                    else{
                        vista.no_mostrar_espera(juego.getJugadores().size(), id_jugador);
                    }
                    break;
                case JUGADOR_AGREGADO:
                    if(estado_ui==Estado_UI.NORMAL){
                        System.out.println("entro a JUGADOR_AGREGADO");
                        vista.limpiar_tablas();
                        for(Jugador j:juego.getJugadores()){
                            vista.aniadir_jugador_a_tablas(j.getId(),j.getPuntaje(), j.getNombre());
                        }
                    }
                    break;
                case CARTAS_REPARTIDAS:
                    vista.iniciar_valores_partida(juego.cartas_repartidas_al_jugador(id_jugador), juego.getPalo_triunfo());
                    vista.mostrar_turno(juego.getJugador_actual().getId());
                    if(estado_ui!=Estado_UI.NORMAL){//significa que aun se estan mostrando los puntajes
                        if (juego.getJugador_actual().getId() == id_jugador) {
                            System.out.println("CARTAS_REPARTIDAS encolado");
                            eventos_pendientes.add(Eventos.CARTAS_REPARTIDAS);
                        }
                    }
                    else{//significa que no se estan mostrando los puntajes
                        if(juego.getJugador_actual().getId() == id_jugador) {
                            System.out.println("CARTAS_REPARTIDAS ejecutado");
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                    }
                    break;
                case GANADOR_POR_TUTE:
                    System.out.println("entro a GANADOR_POR_TUTE");
                    estado_ui=Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                    vista.canta_tute();
                    break;
                case OFRECER_LAS_40:
                    if(juego.getGanador_parcial().getId()==id_jugador) {
                        System.out.println("entro a OFRECER_LAS_40");
                        vista.oferta_las_40();
                    }
                    break;
                case CANTA_LAS_40:
                    System.out.println("entro a CANTA_LAS_40");
                    estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                    vista.canta_las_40(juego.getGanador_parcial().getNombre());
                    break;
                case CANTA_LAS_20:
                    System.out.println("entro a CANTA_LAS_20");
                    estado_ui=Estado_UI.ESPERANDO_OK_CANTO;
                    vista.canta_las_20(juego.getGanador_parcial().getNombre());
                    break;
                case BAZA_TERMINADA:
                    vista.limpiar_cartas_mesa();
                    vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre());
                    if(estado_ui!=Estado_UI.NORMAL){//si entra aca es por que no se esta mostrando ningun anuncio
                        System.out.println("BAZA_TERMINADA encolado");
                        eventos_pendientes.add(Eventos.BAZA_TERMINADA);
                    }
                    else{
                        System.out.println("BAZA_TERMINADA ejecutado");
                        estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                        vista.mostrar_puntajes();
                    }
                    break;
                case OFRECER_TUTE:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        System.out.println("Entro a OFRECER_TUTE");
                        vista.oferta_tute();
                    }
                    break;
                case OFRECER_LAS_20:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        System.out.println("Entro a OFRECER_LAS_20");
                        vista.oferta_las_20();
                    }
                    break;
                case GANADOR_POR_PUNTOS:
                    vista.setear_ganador(juego.getGanador().getNombre());
                    if(estado_ui!=Estado_UI.NORMAL){
                        System.out.println("GANADOR_POR_PUNTOS encolado");
                        eventos_pendientes.add(Eventos.GANADOR_POR_PUNTOS);
                    }
                    else{
                        System.out.println("GANADOR_POR_PUNTOS ejecutado");
                        estado_ui = Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                        vista.gana_por_puntos();
                    }
                    break;
                case ACTUALIZACION_TURNO:
                    vista.mostrar_turno(juego.getJugador_actual().getId());
                    if(estado_ui!=Estado_UI.NORMAL){//si entra aca es porque se estan mostrand los puntajes o un anuncio
                        if(juego.getJugador_actual().getId() == id_jugador){
                            System.out.println("ACTUALIZACION_TURNO encolado");
                            eventos_pendientes.add(Eventos.ACTUALIZACION_TURNO);
                        }
                    }
                    else{//cambio de turno normal no hay que mostrar nada, se le setean las cartas clicleables al jugador actual
                        if (juego.getJugador_actual().getId() == id_jugador) {
                            System.out.println("ACTUALIZACION_TURNO ejecutado");
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
    public void respuesta_anuncio() throws RemoteException {
        while(!eventos_pendientes.isEmpty()){
            Eventos eventos=eventos_pendientes.poll();
            switch (eventos){
                case BAZA_TERMINADA :
                    estado_ui=Estado_UI.ESPERANDO_OK_PUNTAJES;
                    vista.mostrar_puntajes();
                    break;
            }
        }
    }

    public void respuesta_puntajes() throws RemoteException {
        if(!eventos_pendientes.isEmpty()){
             Eventos evento_a_procesar = eventos_pendientes.poll();
             switch (evento_a_procesar){
                case ACTUALIZACION_TURNO, CARTAS_REPARTIDAS:
                    estado_ui = Estado_UI.NORMAL;
                    vista.setCartas_clicleables(juego.cartas_posibles());
                    break;
                case GANADOR_POR_PUNTOS:
                    estado_ui = Estado_UI.ESPERANDO_RESPUESTA_TERMINO_JUEGO;
                    vista.gana_por_puntos();
                    break;
             }
        }
        else{
            estado_ui = Estado_UI.NORMAL;
        }
    }
    public void salir(){
        System.exit(0);
    }


    /*
    LA IDEA ES QUE CON LA COLA DE EVENTOS PENDIENTES, SE PUEDA LOGRAR QUE EL CONTROLADOR MUESTRE UN EVENTO UNO ATRAS DEL OTRO CUANDO
    CORRESPONDA SEGUN EL ESTADO EN EL QUE ESTA LA INTERFAZ GRAFICA!
    ESTO TE PERMITE QUE UNA VEZ QUE SE PRESIONA "OK" EN "Valen canto las 20. Suma 20 puntos!" EL ACTION LISTENER SEA:
    "controlador.procesar_eventos_pendientes()"
    */

}