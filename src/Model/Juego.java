package Model;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static Model.Eventos.*;

public class Juego extends ObservableRemoto implements IJuego {
    private ArrayList<Jugador> jugadores;
    private Mazo mazo;
    private Crupier crupier;
    private int siguiente_id=-1;
    private Jugador jugador_actual;
    private Carta carta_palo_triunfo;
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
        if(jugadores.size()==1){
            jugador_actual=jugador;
        }
        notificarObservadores(JUGADOR_AGREGADO);
        if(jugadores.size()==4){
            reglas.setJugadores(jugadores);
            notificarObservadores(COMENZAR_JUEGO);
            repartir();
        }
    }
    public void repartir() throws RemoteException {
        carta_palo_triunfo=crupier.repartida(jugadores);
        reglas.setPalo_triunfo(carta_palo_triunfo.getPalo());
        System.out.println("A");
        notificarObservadores(CARTAS_REPARTIDAS);
    }
    public int siguienteId(){
        siguiente_id=siguiente_id+1;
        return siguiente_id;
    }
    public Jugador getJugador_actual(){
        return jugador_actual;
    }
    public Jugador getGanador_parcial(){
        return ganador_parcial;
    }
    public ArrayList<Jugador> getJugadores() throws RemoteException{
        return jugadores;
    }
    public Regla getReglas(){
        return reglas;
    }
    public ArrayList<Carta> getCartas_jugadas_en_la_mano(){
        return cartas_jugadas_en_la_mano;
    }
    public Carta getPalo_triunfo(){
        return carta_palo_triunfo;
    }
    public void tirada_de_carta(int id) throws RemoteException {
        for(Carta c:jugador_actual.getMazo_jugador()) {
            if(c.getId()==id){
                jugador_actual.tirar_carta(c);//se quita la carta del mazo del jugador
                cartas_jugadas_en_la_mano.add(c);//agrego carta a la mano
                mazo.getMazo().add(c);//devuelvo carta al mazo
                System.out.println("B");
                notificarObservadores(Eventos.CARTA_TIRADA);
                if(reglas.determinar_ganador_parcial(c,cartas_jugadas_en_la_mano)){
                    ganador_parcial=jugador_actual;
                }
                if(comprobar_termino_mano()==false){
                    actualizar_turno();
                }
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            }

        }

    }
    private Boolean comprobar_termino_mano() throws RemoteException {
        Boolean rta=false;
        if(cartas_jugadas_en_la_mano.size()==jugadores.size()){
            System.out.println("Entro al if de termino de mano. Mano terminada");
            rta=true;
            jugador_actual=ganador_parcial;
            System.out.println("Mano terminada. El jugador actual es: "+jugador_actual.getNombre());
            ganador_parcial.setBazasGanadas(cartas_jugadas_en_la_mano);
            cartas_jugadas_en_la_mano.clear();
            actualizar_puntaje();
            comprobar_cantos();
            notificarObservadores(Eventos.MANO_TERMINADA);//ACA SE DEBERIA MOSTRAR UN ANUNCIO DE QUE LA MANO TERMINO Y QUE
                                                          //EL GANADOR DE LA MISMA FUE TAL
            comprobar_termino_partida();
        }
        return rta;
    }
    private void actualizar_puntaje(){
        int sumado=reglas.determinar_tantos(ganador_parcial.getBazasGanadas());
        ganador_parcial.incrementar_puntaje(sumado);
    }
    private void comprobar_cantos() throws RemoteException {
        if(reglas.determinar_si_puede_cantar_tute(ganador_parcial.getMazo_jugador())==true){
            System.out.println(jugador_actual.getNombre() + " puede cantar tute");
            notificarObservadores(Eventos.OFRECER_TUTE);
        }
        else if(reglas.determinar_si_puede_cantar_las40(ganador_parcial.getMazo_jugador())==true){
            System.out.println(jugador_actual.getNombre() + " puede cantar las 40");
            notificarObservadores(Eventos.OFRECER_LAS_40);
        }
        else if(reglas.determinar_si_puede_cantar_las20(ganador_parcial.getMazo_jugador())==true){
            System.out.println(jugador_actual.getNombre() + " puede cantar las 20");
            notificarObservadores(Eventos.OFRECER_LAS_20);
        }
    }
    private void comprobar_termino_partida() throws RemoteException {
        Boolean rta=true;
        for(Jugador j:jugadores){
            if(!j.getMazo_jugador().isEmpty()){
                rta=false;
                break;
            }
        }
        if(rta==true){
            System.out.println("La partida termino. Hay que volver a repartir");
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
    public ArrayList<Integer> cartas_posibles(){
        ArrayList<Carta> posibles=reglas.determinarQueCartaSiYQueNo(cartas_jugadas_en_la_mano,jugador_actual.getMazo_jugador());
        ArrayList<Integer> ids_posibles=new ArrayList<>();
        for(Carta c:jugador_actual.getMazo_jugador()){
            for(Carta pos:posibles){
                if(c==pos){
                    ids_posibles.add(c.getId());
                }
            }
        }
        System.out.println("JUEGO. cartas_posibles retornando ids posibles para: "+jugador_actual.getNombre());
        return ids_posibles;
    }


    //falta comprobar si la mano se termino y, en ese caso, determinar ganador donde el ganador es el nuevo jugador_actual.

}
