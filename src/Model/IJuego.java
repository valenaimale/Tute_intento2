package Model;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IJuego extends IObservableRemoto {
    public void iniciar_jugador(Jugador jugador) throws RemoteException;
    public int siguienteId() throws RemoteException;
    public ArrayList<Jugador> getJugadores() throws RemoteException;
    public Carta getPalo_triunfo() throws RemoteException;
    public void repartir() throws RemoteException;
    public void tirada_de_carta(int indice) throws RemoteException;
    public Jugador getGanador_parcial() throws RemoteException;
    public Jugador getJugador_actual() throws RemoteException;
    public ArrayList<Carta> getCartas_jugadas_en_la_mano() throws RemoteException;
    public ArrayList<Integer> cartas_posibles() throws RemoteException;
    public void comprobar_cantos() throws RemoteException;
    public Jugador getGanador() throws RemoteException;
    public void canto_positivo() throws RemoteException;
    public void canto_negativo() throws RemoteException;

}
