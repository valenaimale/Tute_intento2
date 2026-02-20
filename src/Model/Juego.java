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
    private Carta carta_palo_triunfo;
    private ArrayList<Carta> cartas_jugadas_en_la_mano;
    private Regla reglas;
    private Jugador ganador_parcial;
    private Estado_ganador_baza estado_cantos;
    private Jugador ganador_final;

    public Juego(){
        inicializar();
    }
    private void inicializar(){
        jugadores=new ArrayList<>();
        mazo= new Mazo();
        crupier=new Crupier(mazo);
        cartas_jugadas_en_la_mano=new ArrayList<>();
        reglas=new Regla();
        estado_cantos= Estado_ganador_baza.NADA;
    }
    public void iniciar_jugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        notificarObservadores(JUGADOR_AGREGADO);
        switch (jugadores.size()){
            case 1:
                jugador_actual=jugador;
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                reglas.setJugadores(jugadores);
                notificarObservadores(COMENZAR_JUEGO);
                repartir();
                break;
        }
    }

    public void repartir() throws RemoteException {
        carta_palo_triunfo=crupier.repartida(jugadores);
        reglas.setPalo_triunfo(carta_palo_triunfo.getPalo());
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
    public Estado_ganador_baza getEstado_cantos(){
        return estado_cantos;
    }
    public Jugador getGanador(){
        return ganador_final;
    }
    public void tirada_de_carta(int id) throws RemoteException {
        for(Carta c:jugador_actual.getMazo_jugador()) {
            if(c.getId()==id){
                jugador_actual.tirar_carta(c);//se quita la carta del mazo del jugador
                cartas_jugadas_en_la_mano.add(c);//agrego carta a la mano
                mazo.getMazo().add(c);//devuelvo carta al mazo
                notificarObservadores(Eventos.CARTA_TIRADA);
                actualizar_ganador_parcial(c, cartas_jugadas_en_la_mano);//si es que hay un nuevo ganador parcial de la mano
                if(!comprobar_termino_mano()){//si la mano no termino, entonces se actualiza el turno
                    actualizar_turno();
                }
                else{//si la mano termino
                    for(Carta ca: cartas_jugadas_en_la_mano){
                        System.out.println(ca.getNombre());
                    }
                    cerrar_mano();//el jugador actual es el ganador parcial, se le suma el puntaje al ganador parcial, se borran las cartas de la mesa y se cambia el estado de los cantos, si es necesario
                    if(comprobar_termino_partida()){//si la partida no termino, solo termina la mano. El controlador se fija si hay algo para cantar y luego muestra los puntajes actuales
                        cerrar_partida();//le suma 10 al ganador parcial, por las ultimas 10 y, si Estado_cantos==NADA, comprueba si hay ganador. Si tiene algo para cantar,
                    }
                    auxiliar();
                }
                break;
            }
        }
    }
    private void auxiliar() throws RemoteException {
        switch (estado_cantos){
            case Estado_ganador_baza.NADA :
                notificarObservadores(Eventos.MANO_TERMINADA);
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            case Estado_ganador_baza.TUTE:
                System.out.println("OFRECER_TUTE");
                notificarObservadores(Eventos.OFRECER_TUTE);
                break;
            case Estado_ganador_baza.LAS_20:
                System.out.println("OFRECER_LAS_20");
                notificarObservadores(Eventos.OFRECER_LAS_20);
                break;
            case Estado_ganador_baza.LAS_40:
                System.out.println("OFRECER_LAS_40");
                notificarObservadores(Eventos.OFRECER_LAS_40);
                break;
            case Estado_ganador_baza.ULTIMAS_10:
                if(chequear_ganador()){
                    notificarObservadores(Eventos.ULTIMAS_10);
                    notificarObservadores(Eventos.GANADOR_POR_PUNTOS);
                    notificarObservadores(Eventos.MANO_TERMINADA);
                    notificarObservadores(Eventos.TERMINO_JUEGO);
                }
                else{
                    notificarObservadores(Eventos.ULTIMAS_10);
                    notificarObservadores(Eventos.MANO_TERMINADA);
                    //notificarObservadores(Eventos.ACTUALIZACION_TURNO);

                    repartir();
                }
                break;
        }
    }
    /*

    QUE OPCIONES TENGO?
    1) NO TERMINA LA MANO-> SE ACTUALIZA EL TURNO
    2) TERMINA LA MANO Y NO LA PARTIDA-> SE COMPRUEBA SI HAY PARA CANTAR Y NO SE ACTUALIZA EL TURNO (JUGADOR_ACTUAL=JUGADOR_GANADOR)
    3) TERMINA LA MANO Y LA PARTIDA -> SE COMPRUEBA SI HAY COSAS PARA CANTAR Y SE SUMAN 10 PARA EL GANADOR, LUEGO SE COMPRUEBA SI HAY GANADOR Y, SI ES ASI, SE AVISA

    */
    private void actualizar_ganador_parcial(Carta c, ArrayList<Carta> cartas_jugadas_en_la_mano){
        if(reglas.determinar_ganador_parcial(c,cartas_jugadas_en_la_mano)){
            ganador_parcial=jugador_actual;
        }
    }
    private Boolean comprobar_termino_mano() throws RemoteException {
        Boolean rta=false;
        if(cartas_jugadas_en_la_mano.size()==jugadores.size()){
            rta=true;
        }
        return rta;
    }
    private void cerrar_mano() throws RemoteException {
        jugador_actual=ganador_parcial;
        //ganador_parcial.setBazasGanadas(cartas_jugadas_en_la_mano);
        actualizar_puntaje();
        cartas_jugadas_en_la_mano.clear();
        estado_cantos= Estado_ganador_baza.NADA;
        comprobar_cantos();
    }
    private void actualizar_puntaje(){
        int sumado=reglas.determinar_tantos(cartas_jugadas_en_la_mano);
        ganador_parcial.incrementar_puntaje(sumado);
    }
    public void comprobar_cantos() throws RemoteException {
        if(reglas.determinar_si_puede_cantar_las20(ganador_parcial.getMazo_jugador())){
            estado_cantos= Estado_ganador_baza.LAS_20;
        }
        else if(reglas.determinar_si_puede_cantar_las40(ganador_parcial.getMazo_jugador())){
            estado_cantos= Estado_ganador_baza.LAS_40;
        }
        else if(reglas.determinar_si_puede_cantar_tute(ganador_parcial.getMazo_jugador())){
            estado_cantos= Estado_ganador_baza.TUTE;
        }
    }
    private Boolean comprobar_termino_partida() throws RemoteException {
        Boolean rta=true;
        if(mazo.getMazo().size()!=40){
            rta=false;
        }
        return rta;
    }
    private void cerrar_partida() throws RemoteException {
        estado_cantos= Estado_ganador_baza.ULTIMAS_10;
        ganador_parcial.setPuntaje(10);
    }


    private void actualizar_turno() throws RemoteException {
        int i=jugadores.indexOf(jugador_actual);
        i++;
        i=i%jugadores.size();
        jugador_actual=jugadores.get(i);
        notificarObservadores(Eventos.ACTUALIZACION_TURNO);
    }

    public void reiniciar_juego(){

    }



    public void canto_positivo() throws RemoteException{
        switch (estado_cantos){
            case Estado_ganador_baza.LAS_20:
                ganador_parcial.setPuntaje(20);
                System.out.println("CANTA_LAS_20");
                notificarObservadores(Eventos.CANTA_LAS_20);//controlador muestra que un jugador canto las 20 y muestra puntajes
                notificarObservadores(Eventos.MANO_TERMINADA);//AGREGADOS MIYI
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            case Estado_ganador_baza.LAS_40://controlador muestra que un jugador canto las 40 y muestra puntajes
                ganador_parcial.setPuntaje(40);
                System.out.println("CANTA_LAS_40");
                notificarObservadores(Eventos.CANTA_LAS_40);
                notificarObservadores(Eventos.MANO_TERMINADA);//AGREGADOS MIYI
                notificarObservadores(Eventos.ACTUALIZACION_TURNO);
                break;
            case Estado_ganador_baza.TUTE://controlador muestra que el jugador actual gano por tute
                ganador_final=ganador_parcial;
                notificarObservadores(Eventos.GANADOR_POR_TUTE);
                notificarObservadores(Eventos.TERMINO_JUEGO);
                break;
        }
        estado_cantos= Estado_ganador_baza.NADA;
    }
    public void canto_negativo() throws RemoteException {//el usuario no quiere cantar tute ni las 40 ni las 20
        notificarObservadores(Eventos.MANO_TERMINADA);
        notificarObservadores(Eventos.ACTUALIZACION_TURNO);
        estado_cantos= Estado_ganador_baza.NADA;
    }
    public Boolean chequear_ganador() throws RemoteException {
        Boolean rta=false;
        if(reglas.determinar_si_hay_ganador()){
            rta=true;
            ganador_final=reglas.determinar_quien_gano();
        }
        return rta;
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
        return ids_posibles;
    }


    //falta comprobar si la mano se termino y, en ese caso, determinar ganador donde el ganador es el nuevo jugador_actual.

}