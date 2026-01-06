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
    private ArrayList<Carta> cartas_jugadas_en_la_mano;
    private Regla reglas;
    private Jugador ganador_parcial;

    public Juego(){
        inicializar();
    }
    private void inicializar(){
        jugadores=new ArrayList<>();
        mazo= new Mazo();
        crupier=new Crupier(mazo);
        cartas_jugadas_en_la_mano=new ArrayList<>();
        reglas=new Regla();
    }
    public void iniciar_jugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        notificarObservadores(JUGADOR_AGREGADO);
        if(jugadores.size()==4){
            reglas.setJugadores(jugadores);
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
    public void tirada_de_carta(Carta c) throws RemoteException {
        if(validar_carta(c)){
            jugador_actual.tirar_carta(c);
            cartas_jugadas_en_la_mano.add(c);
            if(reglas.determinar_ganador_parcial(c,cartas_jugadas_en_la_mano)){
                ganador_parcial=jugador_actual;
            }
            comprobar_termino_mano();
            actualizar_turno();
            notificarObservadores(Eventos.ACTUALIZACION_TURNO);
        }
        else{
            notificarObservadores(Eventos.CARTA_ERRONEA);//PUEDE NO IR, EL JUGADOR YA TENDRIA LOS BOTONES DE LAS CARTAS QUE NO PUEDE TIRAR DESACTIVADOS
        }
    }
    private void comprobar_termino_mano() throws RemoteException {
        if(cartas_jugadas_en_la_mano.size()==4){
            jugador_actual=ganador_parcial;
            ganador_parcial.setBazasGanadas(cartas_jugadas_en_la_mano);
            actualizar_puntaje();
            comprobar_cantos();
            notificarObservadores(Eventos.MANO_TERMINADA);//ACA SE DEBERIA MOSTRAR UN ANUNCIO DE QUE LA MANO TERMINO Y QUE
                                                          //EL GANADOR DE LA MISMA FUE TAL
            comprobar_termino_partida();
        }
    }
    private void actualizar_puntaje(){
        reglas.determinar_tantos(ganador_parcial.getBazasGanadas());
    }
    private void comprobar_cantos() throws RemoteException {
        if(reglas.determinar_si_puede_cantar_tute(ganador_parcial.getMazo_jugador())==true){
            notificarObservadores(Eventos.OFRECER_TUTE);
        }
        else if(reglas.determinar_si_puede_cantar_las40(ganador_parcial.getMazo_jugador())==true){
            notificarObservadores(Eventos.OFRECER_LAS_40);
        }
        else if(reglas.determinar_si_puede_cantar_las20(ganador_parcial.getMazo_jugador())==true){
            notificarObservadores(Eventos.OFRECER_LAS_20);
        }
    }
    private void comprobar_termino_partida() throws RemoteException {
        Boolean rta=true;
        for(Jugador j:jugadores){
            if(!j.getMazo_jugador().isEmpty()){
                rta=false;
            }
        }
        if(rta==true){
            ganador_parcial.setPuntaje(10);
            notificarObservadores(Eventos.ULTIMAS_10);//SE SABE QUE CUANDO OCURREN LAS ULTIMAS 10, SE TERMINA LA PARTIDA, SERIA UN SIMPLE ANUNCIO
            if(reglas.determinar_si_hay_ganador()){
                ganador_parcial=reglas.determinar_quien_gano();
                notificarObservadores(Eventos.GANADOR_POR_PUNTOS);
            }
            else{
                repartir();
            }
        }

    }
    private void actualizar_turno(){
        int i=jugadores.indexOf(jugador_actual);
        i++;
        i=i%jugadores.size();
        jugador_actual=jugadores.get(i);
    }
    private Boolean validar_carta(Carta c){
        Boolean rta = true;
        ArrayList<Carta> cartasDisp = reglas.determinarQueCartaSiYQueNo(cartas_jugadas_en_la_mano, jugador_actual.getMazo_jugador());
        if (reglas.ValidarCartaCorrecta(c, cartasDisp) == false) {
            rta = false;
        }
        return rta;
    }
    public void canto_tute() throws RemoteException {//METODO PARA QUE LLAME EL CONTROLADOR
        notificarObservadores(Eventos.GANADOR_POR_TUTE);
    }
    public void canto_las_40() throws RemoteException {//METODO PARA QUE LLAME EL CONTROLADOR
        ganador_parcial.setPuntaje(40);
        notificarObservadores(Eventos.CANTA_LAS_40);
    }
    public void canto_las_20() throws RemoteException {//METODO PARA QUE LLAME EL CONTROLADOR
        ganador_parcial.setPuntaje(20);
        notificarObservadores(Eventos.CANTA_LAS_20);
    }


    //falta comprobar si la mano se termino y, en ese caso, determinar ganador donde el ganador es el nuevo jugador_actual.

}
