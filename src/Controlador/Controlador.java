package Controlador;

import Model.Carta;
import Model.Eventos;
import Model.IJuego;
import Model.Jugador;
import Vista.VistaGrafica.VistaPrincipal;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

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
    public void tira_carta(Carta c) throws RemoteException {
        juego.tirada_de_carta(c);
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
                    vistaPrincipal.no_mostrar_espera();
                case JUGADOR_AGREGADO:
                    vistaPrincipal.agregar_jugador_a_la_espera(juego.getJugadores());
                case CARTAS_REPARTIDAS:
                    ArrayList<Jugador> jugadores=juego.getJugadores();
                    for(Jugador j : jugadores){
                        if(j.getId()==id_jugador){
                            vistaPrincipal.mostrar_mano(j.getMazo_jugador(), juego.getPalo_triunfo());
                        }
                    }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
