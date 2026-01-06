package Model;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IJuego extends IObservableRemoto {
    public void iniciar_jugador(Jugador jugador) throws RemoteException;
    public int siguienteId() throws RemoteException;
    public ArrayList<Jugador> getJugadores() throws RemoteException;
    public String getPalo_triunfo() throws RemoteException;
    public void repartir() throws RemoteException;
    public void tirada_de_carta(Carta c) throws RemoteException;
    public void canto_tute() throws RemoteException;
    public void canto_las_40() throws RemoteException;
    public void canto_las_20() throws RemoteException;

}
