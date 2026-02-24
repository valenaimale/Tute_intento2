package Model;
import Serializador.AdministradorRanking;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import static Model.Eventos.*;
public class Juego extends ObservableRemoto implements Serializable, IJuego {
    private ArrayList<Jugador> jugadores;
    private Crupier crupier;
    private int siguiente_id=-1;
    private ResolutorJugada resolutorJugada;
    private Jugada jugada;
    private AdministradorRanking administradorRanking=new AdministradorRanking();
    private ArrayList<Jugador> ranking;

    public Juego(){
        inicializar();
    }
    private void inicializar() {
        jugadores = new ArrayList<>();
        Mazo mazo_juego = new Mazo();
        resolutorJugada = new ResolutorJugada(mazo_juego);
        crupier = new Crupier(mazo_juego);
        jugada = new Jugada(jugadores);
        this.ranking = this.administradorRanking.cargarRanking();
    }
    public void iniciar_jugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        notificarObservadores(JUGADOR_AGREGADO);
        switch (jugadores.size()){
            case 1:
                jugada.setMi_actual(jugador);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                notificarObservadores(COMENZAR_JUEGO);
                repartir();
                break;
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
        System.out.println("Palo del triunfo real real: "+carta_palo_triunfo.getPalo());
        jugada.setPalo_triunfo(carta_palo_triunfo.getPalo());
        resolutorJugada.setPalo_del_triunfo(carta_palo_triunfo.getPalo());
        notificarObservadores(CARTAS_REPARTIDAS);
    }

    public void tirada_de_carta(int id) throws RemoteException {
        for(Carta c:jugada.getMi_actual().getMazo_jugador()) {
            if (c.getId() == id) {
                jugada.recibo_carta(c);
                resolutorJugada.getMazo().getMazo().add(c);
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
//DESPUES DE MOSTRAR CANTA_LAS_../ULTIMAS_10 -> SE MUESTRAN PUNTAJES SI O SI
//DESPUES DE MOSTRAR PUNTAJES-> PUEDE SER ACTUALIZACION DE TURNO OOOO QUE TERMINO EL JUEGO POR PUNTOS
//LO QUE SE PUEDE HACER ES DESPUES DEL OK DE ANUNCIOS-> ACTUALIZAR TURNO

    private void termino_jugada() throws RemoteException {
        switch (resolutorJugada.getEstado_ganador_baza()){
            case NADA:
                notificarObservadores(MANO_TERMINADA);
                notificarObservadores(ACTUALIZACION_TURNO);
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
                if(resolutorJugada.chequear_si_hay_ganador(jugadores)){
                    resolutorJugada.actualizar_ganador(jugadores);
                    actualizar_ranking();
                    notificarObservadores(Eventos.ULTIMAS_10);//setText_ultimas_10(juego.getGanador_baza()) y despues mostrar ->JLabel ganador_baza
                    notificarObservadores(Eventos.MANO_TERMINADA);
                    notificarObservadores(Eventos.GANADOR_POR_PUNTOS);//setText_ganador_por_puntos(juego.getGanador_final()) y despues mostrar -> JLabel ganador_final
                    reiniciar_juego();
                }
                else{
                    notificarObservadores(ULTIMAS_10);
                    notificarObservadores(MANO_TERMINADA);
                    repartir();
                }
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
                System.out.println("CANTA_LAS_20");
                notificarObservadores(Eventos.CANTA_LAS_20);//controlador muestra que un jugador canto las 20 y muestra puntajes
                notificarObservadores(Eventos.MANO_TERMINADA);//AGREGADOS MIYI
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            case Estado_ganador_baza.LAS_40://controlador muestra que un jugador canto las 40 y muestra puntajes
                resolutorJugada.aceptacion_canto();
                System.out.println("CANTA_LAS_40");
                notificarObservadores(Eventos.CANTA_LAS_40);
                notificarObservadores(Eventos.MANO_TERMINADA);//AGREGADOS MIYI
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            case Estado_ganador_baza.TUTE://controlador muestra que el jugador actual gano por tute
                notificarObservadores(Eventos.GANADOR_POR_TUTE);
                reiniciar_juego();
                //notificarObservadores(Eventos.TERMINO_JUEGO);
                break;
        }
    }
    public void canto_negativo() throws RemoteException {//el usuario no quiere cantar tute ni las 40 ni las 20
        notificarObservadores(Eventos.MANO_TERMINADA);
        notificarObservadores(Eventos.ACTUALIZACION_TURNO);
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

        Mazo mazo=new Mazo();//en caso de que el ganador sea por tute y el mazo no este completo
        crupier.setMazo1(mazo);
        jugada.reinicio();
        resolutorJugada.reiniciar(mazo);
    }

}