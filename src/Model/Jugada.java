package Model;

import java.util.ArrayList;

public class Jugada {//tendria la responsabilidad de la jugada literalmente, actualizar el jugador actual, actualizar ganador parcial
    private ArrayList<Carta> mis_cartas;
    private ArrayList<Jugador> mis_jugadores;
    private Jugador mi_ganador;
    private Jugador mi_actual;
    private Estado_jugada que_hago;
    private String palo_triunfo;

    public Jugada(ArrayList<Jugador> mis_jugadores){
        inicializar(mis_jugadores);
    }
    public void reinicio(){
        palo_triunfo=null;
        mi_actual=null;
        mi_ganador=null;
        //mis_jugadores.clear();//aunque ya hizo .clear el juego, tambien se deberia borrar aca
    }
    private void inicializar(ArrayList<Jugador> mis_jugadores){
        this.mis_jugadores=mis_jugadores;
        this.mi_ganador=mi_actual;
        this.que_hago=Estado_jugada.TERMINADA;
        this.mis_cartas=new ArrayList<>();
    }
    public void recibo_carta(Carta carta_tirada){
        actualizo_lo_que_hago();
        mis_cartas.add(carta_tirada);
        mi_actual.tirar_carta(carta_tirada);
        actualizar_mi_ganador();
        if(mis_cartas.size()==4){
            que_hago=Estado_jugada.TERMINADA;
        }
    }
    public void actualizar_turno(){
        int i=mis_jugadores.indexOf(mi_actual);
        i++;
        i=i%mis_jugadores.size();
        mi_actual=mis_jugadores.get(i);
    }
    private void actualizar_mi_ganador(){
        Carta carta_ganadora=mis_cartas.getFirst();
        String palo_de_la_mano=mis_cartas.getFirst().getPalo();
        Carta carta_tirada=mis_cartas.getLast();
        if(palo_de_la_mano.equals(palo_triunfo)){//si la primer carta de la mano es la misma que la del palo del triunfo
            for(Carta c:mis_cartas){
                if(carta_ganadora.getOrden()>c.getOrden() && c.getPalo().equals(palo_triunfo)){//si la carta actual (c) tiene menor orden a la que va ganando y tiene el mismo palo que el palo del triunfo, es la ganadora actual
                    carta_ganadora=c;
                }
            }
        }
        else{
            for(Carta c:mis_cartas){
                if(carta_ganadora.getPalo().equals(palo_de_la_mano) && c.getPalo().equals(palo_triunfo)){//si la carta ganadora hasta ahora es del palo de la mano y la actual es del palo del triunfo, es nueva ganadora
                    carta_ganadora=c;
                }
                else if(carta_ganadora.getPalo().equals(palo_de_la_mano) && c.getPalo().equals(palo_de_la_mano) && carta_ganadora.getOrden()>c.getOrden()){//si la carta ganadora hasta ahora es del palo de la mano y la actual tambien es del palo y la actual tiene menor orden que la ganadora, es la ganadora actual
                    carta_ganadora=c;
                }
                else if(carta_ganadora.getPalo().equals(palo_triunfo) && c.getPalo().equals(palo_triunfo) && carta_ganadora.getOrden()>c.getOrden()){//si la carta ganadora hasta ahora es del palo del triunfo y la actual tambien es del palo del triunfo y ademas tiene menor orden que la ganadora, es la nueva ganadora
                    carta_ganadora=c;
                }
            }
        }
        if(carta_tirada==carta_ganadora){
            mi_ganador=mi_actual;
        }
    }
    public void cerrar_jugada(){
        mis_cartas.clear();
        mi_actual=mi_ganador;
    }
    public ArrayList<Carta> determinar_cartas_disponibles() {
        ArrayList<Carta> cartas_disponibles;
        if (mis_cartas.isEmpty()) {
            cartas_disponibles = mi_actual.getMazo_jugador();
        } else {
            cartas_disponibles = determinar_cartas_disponibles_auxiliar();
        }
        return cartas_disponibles;
    }
    private ArrayList<Carta> determinar_cartas_disponibles_auxiliar() {
        ArrayList<Carta> cartasQuePuedeTirarElJug = new ArrayList<>();
        String paloDeLaMano=mis_cartas.getFirst().getPalo();

        ArrayList<Carta> cartasDelJugador=mi_actual.getMazo_jugador();
        if (palo_triunfo == paloDeLaMano) {
            for (Carta carta:cartasDelJugador) {
                if (carta.getPalo().equals(palo_triunfo)) {
                    if (carta.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo()) {//si tiene el palo del triunfo y es un orden menor que la carta mas alta del palo del triunfo, esta obligado a tirar esa. Si no es asi (si no tiene una carta de menor orden que la que va ganando del triunfo) puede tirar cualquiera
                        cartasQuePuedeTirarElJug.add(carta);
                    }
                }
            }
            if (cartasQuePuedeTirarElJug.isEmpty()) {
                cartasQuePuedeTirarElJug = cartasDelJugador;
            }
        } else {
            if (determinarSiTieneDelMismoPalo()) {
                for (Carta carta1:cartasDelJugador) {
                    if (carta1.getPalo().equals(paloDeLaMano)) {
                        cartasQuePuedeTirarElJug.add(carta1);
                    }
                }
            } else if (determinarSiTieneElPaloDelTriunfo()) {
                for (Carta carta2:cartasDelJugador) {
                    if (carta2.getPalo().equals(palo_triunfo)) {
                        if (carta2.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo()) {
                            cartasQuePuedeTirarElJug.add(carta2);
                        }
                    }
                }
            } else {
                cartasQuePuedeTirarElJug = cartasDelJugador;
            }
        }
        return cartasQuePuedeTirarElJug;

    }
    //JUGADA PERO ES PRIVADO
    private Boolean determinarSiTieneDelMismoPalo () {//este metodo devuelve true si tiene del palo de la mano
        String paloDeLaMano=mis_cartas.getFirst().getPalo();
        for (Carta carta : mi_actual.getMazo_jugador()) {
            if (carta.getPalo().equals(paloDeLaMano)) {
                return true;
            }
        }
        return false;
    }
    //JUGADA PERO ES PRIVADO
    private Boolean determinarSiTieneElPaloDelTriunfo() {//este metodo devuelve true si tiene del palo del triunfo que ademas lo tiene que superar
        for (Carta carta : mi_actual.getMazo_jugador()) {
            if (carta.getPalo().equals(palo_triunfo)) {
                if (carta.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo()) {
                    return true;
                }
            }
        }
        return false;
    }
    //JUGADA PERO PRIV
    private int determinarCartaMasAltaDelPaloDelTriunfo (){
        int orden = 1000;
        for (Carta carta : mis_cartas) {
            if (carta.getPalo().equals(palo_triunfo)) {
                if (carta.getOrden() < orden) {
                    orden = carta.getOrden();
                }
            }
        }
        return orden;
    }

    /*
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
    */
    private void actualizo_lo_que_hago(){
        if(que_hago!=Estado_jugada.JUGANDO){
            que_hago=Estado_jugada.JUGANDO;
        }
    }
    public void setMi_actual(Jugador mi_actual){
        this.mi_actual=mi_actual;
    }
    public Jugador getMi_ganador() {
        return mi_ganador;
    }
    public Jugador getMi_actual() {
        return mi_actual;
    }
    public Estado_jugada getQue_hago() {
        return que_hago;
    }
    public ArrayList<Carta> getMis_cartas() {
        return mis_cartas;
    }
    public void setPalo_triunfo(String palo_triunfo) {
        this.palo_triunfo = palo_triunfo;
    }
}
