package Model;
import Serializador.AdministradorRanking;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import static Model.Eventos.*;
import Exception.*;
public class Juego extends ObservableRemoto implements Serializable, IJuego {
    private ArrayList<Jugador> jugadores;
    private Crupier crupier;
    private int siguiente_id=-1;
    private ResolutorJugada resolutorJugada;
    private Jugada jugada;
    private Boolean partida_iniciada=false;
    private ArrayList<Integer> jugadores_confirmados;
    private AdministradorRanking administradorRanking=new AdministradorRanking();
    private ArrayList<Jugador> ranking;

    public Juego(){
        inicializar();
    }
    private void inicializar() {
        jugadores = new ArrayList<>();
        jugadores_confirmados = new ArrayList<>();
        Mazo mazo_juego = new Mazo();
        resolutorJugada = new ResolutorJugada(mazo_juego);
        crupier = new Crupier(mazo_juego);
        jugada = new Jugada(jugadores);
        this.ranking = this.administradorRanking.cargarRanking();
    }

    @Override
    public int iniciarJugador(String nombre) throws RemoteException, NombreInvalido, PartidaIniciada {
        if(partida_iniciada){
            throw new PartidaIniciada("La partida ya esta comenzada, intentelo mas tarde");
        }
        if(nombre_existente(nombre)){
            throw new NombreInvalido("Este nombre ya esta en uso.");
        }
        Jugador jugador = new Jugador(nombre);
        jugador.setId(siguienteId());
        jugadores.add(jugador);
        if(jugadores.size() == 1){
            jugada.setMi_actual(jugador);
        }
        return jugador.getId();
    }
    public void jugador_agregado() throws RemoteException {
        notificarObservadores(JUGADOR_AGREGADO);
        if(jugadores.size()==4){
            partida_iniciada = true;
            notificarObservadores(COMENZAR_JUEGO);
            repartir();
        }
    }

    @Override
    public Object[][] getTablaRanking() throws RemoteException{
        Object[][] datos=new Object[this.ranking.size()][3];
        int i=0;
        for(Jugador j: this.ranking){
            datos[i][0] = j.getNombre();
            datos[i][1] = j.getPuntaje();
            datos[i][2] = j.getFecha_ranking();
            i++;
        }
        return datos;
    }



    @Override
    public int siguienteId() throws RemoteException {
        siguiente_id=siguiente_id+1;
        return siguiente_id;
    }

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return jugadores;
    }

    @Override
    public String getPalo_triunfo() throws RemoteException {
        return resolutorJugada.getPalo_del_triunfo();
    }

    @Override
    public void repartir() throws RemoteException {
        Carta carta_palo_triunfo=crupier.repartida(jugadores);
        jugada.setPalo_triunfo(carta_palo_triunfo.getPalo());
        resolutorJugada.setPalo_del_triunfo(carta_palo_triunfo.getPalo());
        notificarObservadores(CARTAS_REPARTIDAS);
    }

    public void tirada_de_carta(int id) throws RemoteException {
        for(Carta c:jugada.getMi_actual().getMazo_jugador()) {
            if (c.getId() == id) {
                jugada.recibo_carta(c);
                resolutorJugada.agregar_carta_mazo(c);
                notificarObservadores(CARTA_TIRADA);
                break;
            }
        }
        if(jugada.getQue_hago()==Estado_jugada.TERMINADA){
            resolutorJugada.setGanador_baza(jugada.getMi_ganador());
            resolutorJugada.actualizar_puntaje(jugada.getMis_cartas());
            resolutorJugada.chequeo_canto_ganador_baza();
            jugada.cerrar_jugada();
            termino_jugada();
        }
        else{
            jugada.actualizar_turno();
            notificarObservadores(ACTUALIZACION_TURNO);
        }
    }


    private void termino_jugada() throws RemoteException {
        switch (resolutorJugada.getEstado_ganador_baza()){
            case NADA:
                notificarObservadores(BAZA_TERMINADA);
                break;
            case TUTE:
                notificarObservadores(OFRECER_TUTE);
                break;
            case LAS_40:
                notificarObservadores(OFRECER_LAS_40);
                break;
            case LAS_20:
                notificarObservadores(OFRECER_LAS_20);
                break;
            case ULTIMAS_10:
                notificarObservadores(ULTIMAS_10);
                break;
        }
    }
    private void actualizar_ranking(){
        ranking.add(resolutorJugada.getGanador_final());
        administradorRanking.guardarRanking(ranking);
    }

    public void canto_positivo() throws RemoteException{
        switch (resolutorJugada.getEstado_ganador_baza()){
            case Estado_ganador_baza.LAS_20:
                resolutorJugada.aceptacion_canto();
                notificarObservadores(Eventos.CANTA_LAS_20);
                break;
            case Estado_ganador_baza.LAS_40:
                resolutorJugada.aceptacion_canto();
                notificarObservadores(Eventos.CANTA_LAS_40);
                break;
            case Estado_ganador_baza.TUTE:
                resolutorJugada.aceptacion_canto();
                notificarObservadores(Eventos.GANADOR_POR_TUTE);
                reiniciar_juego();
                break;
        }
    }
    public void canto_negativo() throws RemoteException {
        resolutorJugada.negacion_canto();
        notificarObservadores(Eventos.BAZA_TERMINADA);
    }
    public ArrayList<Integer> cartas_posibles(){
        ArrayList<Carta> posibles=jugada.determinar_cartas_disponibles();
        ArrayList<Integer> ids_posibles=new ArrayList<>();
        for(Carta c:jugada.getMi_actual().getMazo_jugador()){
            for(Carta pos:posibles){
                if(c==pos){
                    ids_posibles.add(c.getId());
                }
            }
        }
        return ids_posibles;
    }

    @Override
    public Jugador getGanador() throws RemoteException {
        return resolutorJugada.getGanador_final();
    }
    @Override
    public Jugador getGanador_parcial() throws RemoteException {
        return jugada.getMi_ganador();
    }

    @Override
    public Jugador getJugador_actual() throws RemoteException {
        return jugada.getMi_actual();
    }

    @Override
    public ArrayList<Carta> getCartas_jugadas_en_la_mano() throws RemoteException {
        return jugada.getMis_cartas();
    }

    public void reiniciar_juego(){
        jugadores.clear();
        siguiente_id=-1;
        partida_iniciada = false;
        Mazo mazo=new Mazo();
        crupier.setMazo1(mazo);
        jugada.reinicio();
        resolutorJugada.reiniciar(mazo);
    }
    public ArrayList<Integer> cartas_repartidas_al_jugador(int id_jugador) throws RemoteException {
        ArrayList<Integer> id_cartas = new ArrayList<>();
        for(Jugador j: jugadores){
            if(j.getId()==id_jugador){
                for (Carta c : j.getMazo_jugador()) {
                    id_cartas.add(c.getId());
                }
            }
        }
        return id_cartas;
    }
    private Boolean nombre_existente(String nombre){
        ArrayList<String> nombres = new ArrayList<>();
        Boolean rta=false;
        for(Jugador j:jugadores){
            nombres.add(j.getNombre());
        }
        if(nombres.contains(nombre)){
            rta=true;
        }
        return rta;
    }
    @Override
    public Boolean baza_comenzada(){
        if(jugada.getQue_hago()==Estado_jugada.JUGANDO){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void confirmacion_baza_terminada(int id_confirmado) throws RemoteException {
        //jugadores_confirmados.add(id_confirmado);
        if(id_confirmado==jugada.getMi_actual().getId() && jugada.getMis_cartas().isEmpty()){
            switch (resolutorJugada.getEstado_ganador_baza()){
                case LAS_20, LAS_40, NADA:
                    notificarObservadores(ACTUALIZACION_TURNO);
                    break;
                case ULTIMAS_10:
                    if(resolutorJugada.chequear_si_hay_ganador(jugadores)){
                        resolutorJugada.actualizar_ganador(jugadores);
                        notificarObservadores(GANADOR_POR_PUNTOS);
                        reiniciar_juego();
                    }
                    else{
                        repartir();
                    }
                    break;
            }
        }
        else{
            if(resolutorJugada.getEstado_ganador_baza()==Estado_ganador_baza.ULTIMAS_10 && resolutorJugada.chequear_si_hay_ganador(jugadores)){
                resolutorJugada.actualizar_ganador(jugadores);
                notificarObservadores(GANADOR_POR_PUNTOS);
                reiniciar_juego();
            }
        }
        /*/if(jugadores.size()==jugadores_confirmados.size()){
            jugadores_confirmados.clear();
        }*/
    }
    /*public void confirmacion_baza_terminada(int id_confirmado) throws RemoteException {
        jugadores_confirmados.add(id_confirmado);
        if(jugadores_confirmados.size()==jugadores.size()){
            jugadores_confirmados.clear();
            switch (resolutorJugada.getEstado_ganador_baza()){
                case LAS_20, LAS_40, NADA:
                    notificarObservadores(ACTUALIZACION_TURNO);
                    break;
                case ULTIMAS_10:
                    if(resolutorJugada.chequear_si_hay_ganador(jugadores)){
                        resolutorJugada.actualizar_ganador(jugadores);
                        notificarObservadores(GANADOR_POR_PUNTOS);
                        reiniciar_juego();
                    }
                    else{
                        repartir();
                    }
                    break;
            }
        }
    }*/
    //SI HAY CANTA_LAS_20 NO HAY BAZA_TERMINADA

}