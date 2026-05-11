package Vista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    public void aniadir_jugador_a_tablas(int id, int puntaje, String nombre);
    public void iniciar();
    public void no_mostrar_espera(int cantidad_jugadores, int id_jugador);
    public void iniciar_valores_partida(ArrayList<Integer> ids_cartas, String palo_triunfo);
    public void limpiar_tablas();
    public void setCartas_clicleables(ArrayList<Integer> ids_posibles) throws RemoteException;
    public void agregar_carta_mano(int id_carta, int id_jugador);
    public void limpiar_cartas_mesa();
    public void actualizar_puntaje(int id, int puntaje, String nombre);
    public void mostrar_puntajes() throws RemoteException;
    public void mostrar_turno(int id);
    public void mostrar_mensaje_error(String error);
    public void canto(String s) throws RemoteException;
    public void cartel_ganador(String s);
    public void oferta_canto(String s);
    public void mostrar_esperando();
    public void limpiar_partida();
}
