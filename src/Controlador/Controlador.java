package Controlador;

import Model.*;
import Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.*;
import Exception.*;

public class Controlador implements IControladorRemoto {
    IVista vista;
    IJuego juego;
    int id_jugador;
    Boolean jugando=false;

    public Controlador() {
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciar_player(String nombre){
        try {
            id_jugador=juego.iniciarJugador(nombre);
            jugando = true;
            juego.jugador_agregado();
        } catch (RemoteException e) {
            manejar_error_de_conexion(e);
        } catch (ExceptionJuego e) {
            vista.mostrar_mensaje_error(e.getMessage());
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

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.juego = (IJuego) t;
    }
    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if(jugando){
            try {
                Eventos evento = (Eventos) o;
                switch (evento) {
                    case COMENZAR_JUEGO:
                        vista.iniciar_posiciones_mano(juego.getJugadores().size(), id_jugador);
                        vista.no_mostrar_espera();
                        break;
                    case JUGADOR_AGREGADO:
                        vista.limpiar_tablas();
                        for(Jugador j:juego.getJugadores()){
                            vista.aniadir_jugador_a_tablas(j.getId(),j.getPuntaje(), j.getNombre());
                        }
                        vista.mostrar_esperando();
                        break;
                    case CARTAS_REPARTIDAS:
                        vista.no_mostrar_espera_confirmaciones();
                        vista.iniciar_valores_partida(juego.cartas_repartidas_al_jugador(id_jugador), juego.getPalo_triunfo());
                        vista.mostrar_turno(juego.getJugador_actual().getNombre());
                        if(juego.getJugador_actual().getId() == id_jugador) {
                            vista.limpiar_cartas_mesa();
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                        break;
                    case GANADOR_POR_TUTE:
                        vista.no_mostrar_espera_confirmaciones();
                        vista.limpiar_cartas_mesa();
                        vista.limpiar_partida();
                        jugando=false;
                        vista.cartel_ganador(juego.getGanador().getNombre()+ " canto Tute. Gano el juego!");
                        break;
                    case OFRECER_LAS_40:
                        if(juego.getGanador_parcial().getId()==id_jugador) {
                            vista.oferta_canto("LAS 40");
                        }
                        break;
                    case CANTA_LAS_40:
                        System.out.println("Entro a canta las 40");
                        vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre());
                        vista.canto(juego.getGanador_parcial().getNombre()+ " gano la baza y canto las 40. Suma 40 puntos!");
                        break;
                    case CANTA_LAS_20:
                        System.out.println("Entro a canta las 20");
                        vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre());
                        vista.canto(juego.getGanador_parcial().getNombre()+ " gano la baza y canto las 20. Suma 20 puntos!");
                        break;
                    case BAZA_TERMINADA:
                        System.out.println("Entro a baza_terminada");
                        vista.limpiar_cartas_mesa();
                        vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre());
                        vista.mostrar_puntajes();
                        break;
                    case OFRECER_TUTE:
                        if(juego.getGanador_parcial().getId()==id_jugador){
                            vista.oferta_canto("TUTE");
                        }
                        break;
                    case OFRECER_LAS_20:
                        if(juego.getGanador_parcial().getId()==id_jugador){
                            vista.oferta_canto("LAS 20");
                        }
                        break;
                    case GANADOR_POR_PUNTOS:
                        vista.no_mostrar_espera_confirmaciones();
                        vista.limpiar_partida();
                        jugando=false;
                        vista.cartel_ganador(juego.getGanador().getNombre()+" sumo 101 puntos o mas. Gano el juego!");
                        break;
                    case ACTUALIZACION_TURNO:
                        vista.no_mostrar_espera_confirmaciones();
                        vista.mostrar_turno(juego.getJugador_actual().getNombre());
                        if(juego.getJugador_actual().getId() == id_jugador){//caso de que sea el jugador actual (se hace la notificacion en juego.tirada_de_carta(id))
                            vista.setCartas_clicleables(juego.cartas_posibles());
                        }
                        break;
                    case CARTA_TIRADA:
                        vista.agregar_carta_mano(juego.getCartas_jugadas_en_la_mano().getLast().getId(), juego.getJugador_actual().getId());
                        break;
                    case ULTIMAS_10:
                        vista.actualizar_puntaje(juego.getGanador_parcial().getId(),juego.getGanador_parcial().getPuntaje(),juego.getGanador_parcial().getNombre());
                        vista.canto(juego.getGanador_parcial().getNombre()+" gano la ultima baza. Suma 10 puntos!");
                        break;
                }
            } catch (RemoteException e) {
                manejar_error_de_conexion(e);
            }
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
        vista.limpiar_cartas_mesa();
        vista.mostrar_puntajes();
    }
    public void respuesta_puntaje() throws RemoteException {
        vista.esperar_confirmacion("Esperando que todos los jugadores esten listos...");
        System.out.println("Entro a respuesta_puntaje, se hace juego.confirmacion_baza_terminada("+id_jugador+")");
        juego.confirmacion_baza_terminada(id_jugador);
    }
    public void respuesta_puntaje_por_inactividad() throws RemoteException{
        System.out.println("Entro a respuesta_puntaje_por_inactividad, se hace juego.confirmacion_baza_terminada("+id_jugador+")");
        juego.confirmacion_baza_terminada(id_jugador);
    }
    private void manejar_error_de_conexion(RemoteException e) {
        System.err.println("Error de conexion: " + e.getMessage());
    }

}