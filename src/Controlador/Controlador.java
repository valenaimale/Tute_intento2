package Controlador;

import Model.Carta;
import Model.Eventos;
import Model.IJuego;
import Model.Jugador;
import Vista.VistaGrafica.VistaPrincipal;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    VistaPrincipal vistaPrincipal;
    IJuego juego;
    int id_jugador;
    public Controlador(){
    }

    public void setVistaPrincipal(VistaPrincipal vistaPrincipal){
        this.vistaPrincipal=vistaPrincipal;
    }
    public void iniciar_player(String nombre) throws RemoteException {
        Jugador jugador = new Jugador(nombre);
        jugador.setId(juego.siguienteId());
        id_jugador=jugador.getId();
        juego.iniciar_jugador(jugador);
    }
    public void tira_carta(int id_carta) throws RemoteException {
        juego.tirada_de_carta(id_carta);
    }
    public void canta_tute() throws RemoteException {
        juego.canto_tute();
    }
    public void canta_las_40() throws RemoteException {
        juego.canto_las_40();
    }
    public void canta_las_20() throws RemoteException {
        juego.canto_las_20();
    }
    public int getId_jugador(){
        return id_jugador;
    }
    /*
    public void agregar_carta_mano(int id_carta, int id_actual)
    public void set_cartas_clicleables(ArrayList<Integer> id_cartas_posibles)
    public void mostrar_mano(ArrayList<Integer> id_cartas_jugador, String palo_triunfo, int nombre_carta_de_triunfo)
    */

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.juego= (IJuego) t;
    }
    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        try{
            Eventos evento = (Eventos) o;
            switch (evento){
                case COMENZAR_JUEGO:
                    vistaPrincipal.iniciar_posiciones_mano(juego.getJugadores().size(),id_jugador);
                    vistaPrincipal.no_mostrar_espera();
                    break;
                case JUGADOR_AGREGADO:
                    vistaPrincipal.agregar_jugador_a_la_espera(juego.getJugadores());
                    break;
                case CARTAS_REPARTIDAS:
                    ArrayList<Integer> id_cartas=new ArrayList<>();
                    for(Carta c:juego.getJugadores().get(id_jugador).getMazo_jugador()){
                        id_cartas.add(c.getId());
                    }
                    vistaPrincipal.mostrar_mano(id_cartas, juego.getPalo_triunfo().getPalo(),juego.getPalo_triunfo().getId());
                    if(juego.getJugador_actual().getId()==id_jugador){
                        vistaPrincipal.hacer_todas_clicleables();
                    }
                    break;
                case OFRECER_TUTE:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        System.out.println("CONTROLADOR. OFRECER_TUTE");
                        SwingUtilities.invokeLater(() -> {
                            vistaPrincipal.oferta_tute();
                        });
                    }
                    break;
                case OFRECER_LAS_40:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        System.out.println("CONTROLADOR. OFRECER_LAS_40");
                        SwingUtilities.invokeLater(() -> {
                            vistaPrincipal.oferta_las_40();
                        });
                    }
                    break;
                case OFRECER_LAS_20:
                    if(juego.getGanador_parcial().getId()==id_jugador){
                        System.out.println("CONTROLADOR. OFRECER_LAS_20");
                        SwingUtilities.invokeLater(() -> {
                            vistaPrincipal.oferta_las_20();
                        });
                    }
                    break;
                case GANADOR_POR_TUTE:
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vistaPrincipal.canta_tute(juego.getGanador_parcial().getNombre());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case CANTA_LAS_40:
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vistaPrincipal.canta_las_40(juego.getGanador_parcial().getNombre());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case CANTA_LAS_20:
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vistaPrincipal.canta_las_20(juego.getGanador_parcial().getNombre());
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case MANO_TERMINADA:
                    System.out.println("CONTROLADOR. MANO_TERMINADA");
                    vistaPrincipal.limpiar_cartas_mesa();
                    vistaPrincipal.actualizar_puntajes(juego.getJugadores(),juego.getGanador_parcial());
                    break;
                case ULTIMAS_10:
                    vistaPrincipal.gana_ultimas_10(juego.getJugadores(),juego.getGanador_parcial());
                    break;
                case GANADOR_POR_PUNTOS:
                    vistaPrincipal.gana_por_puntos(juego.getGanador_parcial().getNombre());
                    break;
                case ACTUALIZACION_TURNO:
                    if(juego.getJugador_actual().getId()==id_jugador){
                        System.out.println("CONTROLADOR.ACTUALIZACION_TURNO turno del jugador: " + juego.getJugador_actual().getNombre());
                        SwingUtilities.invokeLater(() -> {
                            try {
                                vistaPrincipal.set_cartas_clicleables(juego.cartas_posibles());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    break;
                case CARTA_TIRADA:
                    System.out.println("Controlador.CARTA_TIRADA. Id jugador: " + id_jugador);
                    vistaPrincipal.agregar_carta_mano(juego.getCartas_jugadas_en_la_mano().getLast().getId(),juego.getJugador_actual().getId());
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public IJuego getJuego(){
        return juego;
    }
}
