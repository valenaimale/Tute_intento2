package Model;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static Model.Eventos.*;

public class Juego extends ObservableRemoto implements IJuego {
    private ArrayList<Jugador> jugadores;
    private Mazo mazo;
    private Crupier crupier;
    private int siguiente_id=-1;
    private Jugador jugador_actual;
    private String palo_triunfo;

    public Juego(){
        inicializar();
    }
    private void inicializar(){
        jugadores=new ArrayList<>();
        mazo= new Mazo();
        crupier=new Crupier(mazo);

    }
    public void iniciar_jugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        notificarObservadores(JUGADOR_AGREGADO);
        if(jugadores.size()==4){
            notificarObservadores(COMENZAR_JUEGO);
        }
    }
    private void repartir() throws RemoteException {
        palo_triunfo=crupier.repartida(jugadores);
        notificarObservadores(CARTAS_REPARTIDAS);
    }

    public int siguienteId(){
        siguiente_id=siguiente_id+1;
        return siguiente_id;
    }
    public ArrayList<Jugador> getJugadores(){
        return jugadores;
    }
    public String getPalo_triunfo(){
        return palo_triunfo;
    }

}
