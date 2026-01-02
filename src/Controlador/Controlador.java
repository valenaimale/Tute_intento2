package Controlador;

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
                    juego.repartir();
                case JUGADOR_AGREGADO:
                    vistaPrincipal.agregar_jugador_a_la_espera(juego.getJugadores());
                case CARTAS_REPARTIDAS:
                    ArrayList<Jugador> jugadores=juego.getJugadores();
                    for(Jugador j : jugadores){
                        if(j.getId()==id_jugador){
                            vistaPrincipal.mostrar_cartas(j.getMazo_jugador(), juego.getPalo_triunfo());
                        }
                    }


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
