package Model;

import java.util.ArrayList;

public class Regla {
    private ArrayList<Jugador> jugadores;
    private String palo_triunfo;

    public Regla() {
    }
    public void setPalo_triunfo(String palo_triunfo){
        this.palo_triunfo=palo_triunfo;
    }
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
    }
    public int determinar_tantos(ArrayList<Carta> bazasGanadasXJug){
        int suma=0;
        for(Carta carta:bazasGanadasXJug){
            suma=carta.getValor_en_juego()+suma;
        }
        return suma;
    }

    public Boolean determinar_si_hay_ganador(){
        Boolean rta=false;
        int puntajeGanador=0;
        for(Jugador jugador:jugadores){
            if(jugador.getPuntaje()>puntajeGanador && jugador.getPuntaje()>101){
                rta=true;
            }
            else if(jugador.getPuntaje()==puntajeGanador){
                rta=false;
            }
        }
        return rta;
    }

    public Jugador determinar_quien_gano(){
        Jugador ganador=jugadores.get(0);
        for(Jugador jugador:jugadores){
            if(jugador.getPuntaje()>ganador.getPuntaje()){
                ganador=jugador;
            }
        }
        return ganador;
    }

    public ArrayList<Carta> determinarQueCartaSiYQueNo(ArrayList<Carta> cartas, ArrayList<Carta> cartasDelJugador) {
        ArrayList<Carta> cartasQuePuedeTirarElJug;
        if (cartas.isEmpty()) {
            cartasQuePuedeTirarElJug = cartasDelJugador;
        } else {
            String paloDeLaMano = cartas.get(0).getPalo();
            cartasQuePuedeTirarElJug = secundaryMethod(paloDeLaMano, cartasDelJugador, cartas);
        }
        return cartasQuePuedeTirarElJug;
    }

    private ArrayList<Carta> secundaryMethod(String paloDeLaMano, ArrayList<Carta> cartasDelJugador, ArrayList<Carta> cartas) {
        ArrayList<Carta> cartasQuePuedeTirarElJug = new ArrayList<>();
        if (palo_triunfo == paloDeLaMano) {
            for (Carta carta:cartasDelJugador) {
                if (carta.getPalo().equals(palo_triunfo)) {
                    if (carta.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo(palo_triunfo, cartas)) {//si tiene el palo del triunfo y es un orden menor que la carta mas alta del palo del triunfo, esta obligado a tirar esa. Si no es asi (si no tiene una carta de menor orden que la que va ganando del triunfo) puede tirar cualquiera
                        cartasQuePuedeTirarElJug.add(carta);
                    }
                }
            }
            if (cartasQuePuedeTirarElJug.isEmpty()) {
                cartasQuePuedeTirarElJug = cartasDelJugador;
            }
        } else {
            if (determinarSiTieneDelMismoPalo(cartasDelJugador, paloDeLaMano) == true) {
                for (Carta carta1:cartasDelJugador) {
                    if (carta1.getPalo().equals(paloDeLaMano)) {
                        cartasQuePuedeTirarElJug.add(carta1);
                    }
                }
            } else if (determinarSiTieneElPaloDelTriunfo(cartasDelJugador, cartas) == true) {
                for (Carta carta2:cartasDelJugador) {
                    if (carta2.getPalo().equals(palo_triunfo)) {
                        if (carta2.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo(palo_triunfo, cartas)) {
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
    private Boolean determinarSiTieneDelMismoPalo (ArrayList < Carta > cartasDelJugador, String palo) {//este metodo devuelve true si tiene del palo de la mano
        for (Carta carta : cartasDelJugador) {
            if (carta.getPalo().equals(palo)) {
                return true;
            }
        }
        return false;
    }

    private Boolean determinarSiTieneElPaloDelTriunfo(ArrayList < Carta > cartasDelJugador, ArrayList < Carta > cartas) {//este metodo devuelve true si tiene del palo del triunfo que ademas lo tiene que superar
        for (Carta carta : cartasDelJugador) {
            if (carta.getPalo().equals(palo_triunfo)) {
                if (carta.getOrden() < determinarCartaMasAltaDelPaloDelTriunfo(palo_triunfo, cartas)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int determinarCartaMasAltaDelPaloDelTriunfo (String palo, ArrayList < Carta > cartas){
        int orden = 1000;
        for (Carta carta : cartas) {
            if (carta.getPalo().equals(palo)) {
                if (carta.getOrden() < orden) {
                    orden = carta.getOrden();
                }
            }
        }
        return orden;
    }


    public Boolean ValidarCartaCorrecta(Carta cartaTiradaPorJugador, ArrayList<Carta> cartasQuePuedeTirar) {
        Boolean rta = false;
        for (Carta carta : cartasQuePuedeTirar) {
            if (cartaTiradaPorJugador == carta) {
                rta = true;
            }
        }
        return rta;
    }
    public Boolean determinar_ganador_parcial(Carta cartaTirada, ArrayList<Carta> cartas){
        Boolean rta = false;
        Carta cartaGanadora = cartas.get(0);
        for (Carta carta : cartas) {
            if (!cartaGanadora.getPalo().equals(palo_triunfo)) {//si la carta que va ganando no es del palo del triunfo
                if (carta.getOrden() < cartaGanadora.getOrden() && carta.getPalo().equals(cartaGanadora.getPalo())) {//si la carta actual tiene menor orden (es mejor) que la carta que va ganando y ademas son del mismo palo, se actualiza la carta actual por la carta que va ganando
                    cartaGanadora = carta;
                } else if (!cartaGanadora.getPalo().equals(carta.getPalo()) && carta.getPalo().equals(palo_triunfo)) {//si la carta actual es de distinto palo a la carta que va ganando y, la carta actual, ademas, es del palo del triunfo, se actualiza carta actual como carta que va ganando
                    cartaGanadora = carta;
                }
            } else {//si la carta que va ganando es del palo del triunfo
                if (carta.getOrden() < cartaGanadora.getOrden() && carta.getPalo().equals(cartaGanadora.getPalo())) {//si la carta actual es del palo del triunfo y ademas tiene menor orden (es mejor) que la que va ganando, se actualiza la carta actual como la carta que va ganando
                    cartaGanadora = carta;
                }
            }
        }
        if (cartaGanadora == cartaTirada) {
            rta = true;
        }
        System.out.println("La carta ganadora es: "+ cartaGanadora.getNombre());
        return rta;
    }
    public Boolean determinar_si_puede_cantar_tute(ArrayList<Carta> cartas_en_mano) {
        Boolean rta = false;
        int cantidad11 = 0;
        int cantidad12 = 0;
        for (Carta carta:cartas_en_mano){
            if (carta.getNumero() == 12) {
                cantidad12++;
            }
            if (carta.getNumero() == 11) {
                cantidad11++;
            }
        }
        if (cantidad11 >= 4 || cantidad12 >= 4) {
            rta = true;
        }
        return rta;
    }


    public Boolean determinar_si_puede_cantar_las20(ArrayList<Carta> cartas_en_mano) {
        Boolean rta = false;

        for (Carta carta:cartas_en_mano) {
            for (Carta carta2:cartas_en_mano) {
                if (methodAux20(carta, carta2) == true) {
                    rta = true;
                }
            }
        }
        return rta;
    }


    public Boolean determinar_si_puede_cantar_las40(ArrayList<Carta> cartas_en_mano) {
        Boolean rta = false;

        for (Carta carta:cartas_en_mano) {
            for (Carta carta2:cartas_en_mano) {
                if (methodAux40(carta, carta2) == true) {
                    rta = true;
                }
            }
        }
        return rta;
    }

    private Boolean methodAux20(Carta carta1, Carta carta2) {
        Boolean rta = false;
        if (carta1.getPalo().equals(carta2.getPalo())) {
            if (carta1.getNumero() == 12 && carta2.getNumero() == 11 || carta1.getNumero() == 11 && carta2.getNumero() == 12) {
                rta = true;
            }
        }
        return rta;
    }

    private Boolean methodAux40(Carta carta1, Carta carta2) {
        Boolean rta = false;
        if (carta1.getPalo().equals(carta2.getPalo())) {
            if (carta1.getNumero() == 12 && carta2.getNumero() == 11 || carta1.getNumero() == 11 && carta2.getNumero() == 12) {
                if (carta1.getPalo().equals(palo_triunfo)) {
                    rta = true;
                }
            }
        }
        return rta;
    }
}
