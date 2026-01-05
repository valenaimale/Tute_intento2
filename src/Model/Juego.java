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
            repartir();
        }
    }
    public void repartir() throws RemoteException {
        palo_triunfo=crupier.repartida(jugadores);
        notificarObservadores(CARTAS_REPARTIDAS);
    }

    public int siguienteId(){
        siguiente_id=siguiente_id+1;
        return siguiente_id;
    }
    public ArrayList<Jugador> getJugadores() throws RemoteException{
        return jugadores;
    }
    public String getPalo_triunfo(){
        return palo_triunfo;
    }
    /*public void tirar_carta(Carta c) throws RemoteException {
        if(validar_carta(c)){
            jugador_actual.recibir_carta(c);
            actualizar_turno();
            notificarObservadores(Eventos.ACTUALIZACION_TURNO);
        }
    }
    private void actualizar_turno(){
        int i=jugadores.indexOf(jugador_actual);
    }
    private Boolean validar_carta(Carta c){
    }
    */
    //falta comprobar si la mano se termino y, en ese caso, determinar ganador donde el ganador es el nuevo jugador_actual.

}
