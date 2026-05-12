package Model;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;
import Exception.*;

public interface IJuego extends IObservableRemoto {
    public int iniciarJugador(String nombre) throws RemoteException, NombreInvalido, PartidaIniciada;
    public int siguienteId() throws RemoteException;
    public ArrayList<Jugador> getJugadores() throws RemoteException;
    public String getPalo_triunfo() throws RemoteException;
    public void repartir() throws RemoteException;
    public void tirada_de_carta(int indice) throws RemoteException;
    public Jugador getGanador_parcial() throws RemoteException;
    public Jugador getJugador_actual() throws RemoteException;
    public ArrayList<Carta> getCartas_jugadas_en_la_mano() throws RemoteException;
    public ArrayList<Integer> cartas_posibles() throws RemoteException;
    public Jugador getGanador() throws RemoteException;
    public void canto_positivo() throws RemoteException;
    public void canto_negativo() throws RemoteException;
    public Object[][] getTablaRanking() throws RemoteException;
    public ArrayList<Integer> cartas_repartidas_al_jugador(int id) throws RemoteException;
    public void confirmacion_baza_terminada(int id_confirmado) throws RemoteException;
    public void jugador_agregado() throws RemoteException;
    public Boolean baza_comenzada() throws RemoteException;
    public Boolean exists_in_jugadores_confirmados(int id_jugador) throws RemoteException;
}
