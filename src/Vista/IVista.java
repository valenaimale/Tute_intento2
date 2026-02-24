package Vista;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    public void mostrar_inicio();
    public void aniadir_jugador_a_tablas(int id, int puntaje, String nombre);
    public void iniciar();
    public void no_mostrar_espera(int cantidad_jugadores, int id_jugador);
    public void iniciar_valores_partida(ArrayList<Integer> ids_cartas, String palo_triunfo);
    public void limpiar_tablas();
    public void setCartas_clicleables(ArrayList<Integer> ids_posibles) throws RemoteException;
    public void agregar_carta_mano(int id_carta, int id_jugador);
    public void oferta_las_40();
    public void oferta_las_20();
    public void oferta_tute();

    public void canta_las_40(String nombre) throws RemoteException;
    public void canta_las_20(String nombre) throws RemoteException;
    public void canta_tute() throws RemoteException;

    public void gana_ultimas_10(String nombre) throws RemoteException;

    public void gana_por_puntos() throws RemoteException;

    public void limpiar_cartas_mesa();

    public void actualizar_puntaje(int id, int puntaje, String nombre);

    public void mostrar_puntajes() throws RemoteException;
    public void cierre_juego(String nombre_ganador);
    public void mostrar_turno(int id);
    public void setear_ganador(String nombre);
}
