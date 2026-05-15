package Model;

import java.util.ArrayList;

public class Crupier {
    private Mazo mazo1;

    public Crupier(Mazo mazo1) {
        inicializar(mazo1);
    }
    private void inicializar(Mazo mazo1){
        this.mazo1=mazo1;
    }
    /*public Carta repartida(ArrayList<Jugador> jugadores){
        mazo1.mezclate();
        System.out.println("Cartas en mazo antes de repartir: " + mazo1.getMazo().size());
        Carta carta_palo_triunfo=mazo1.getMazo().getLast();
        int i=0;
        while(!mazo1.getMazo().isEmpty()){
            Jugador jugador=jugadores.get(i%jugadores.size());//% es modulo, hace que se repitan ciclicamente los indices de los jugadores
            Carta carta1=mazo1.getMazo().remove(0);
            jugador.recibir_carta(carta1);
            i++;
        }
        return carta_palo_triunfo;
    }*/
    public Carta repartida(ArrayList<Jugador> jugadores){
        for(Jugador j: jugadores){
            switch (j.getId()){
                case 0:
                    j.recibir_carta(mazo1.getMazo().remove(8));
                    j.recibir_carta(mazo1.getMazo().remove(8));
                    break;
                case 1:
                    j.recibir_carta(mazo1.getMazo().remove(16));
                    j.recibir_carta(mazo1.getMazo().remove(16));
                    break;
                case 2:
                    j.recibir_carta(mazo1.getMazo().remove(24));
                    j.recibir_carta(mazo1.getMazo().remove(24));
                    break;
                case 3:
                    j.recibir_carta(mazo1.getMazo().remove(32));
                    j.recibir_carta(mazo1.getMazo().remove(32));
                    break;
            }
        }
        mazo1.mezclate();
        System.out.println("Cartas en mazo antes de repartir: " + mazo1.getMazo().size());
        Carta carta_palo_triunfo=mazo1.getMazo().getLast();
        int i=0;
        while(!mazo1.getMazo().isEmpty()){
            Jugador jugador=jugadores.get(i%jugadores.size());//% es modulo, hace que se repitan ciclicamente los indices de los jugadores
            Carta carta1=mazo1.getMazo().remove(0);
            jugador.recibir_carta(carta1);
            i++;
        }
        return carta_palo_triunfo;
    }
    /*public Carta repartida(ArrayList<Jugador> jugadores){
        for(Jugador j: jugadores){
            switch (j.getId()){
                case 0:
                    j.recibir_carta(mazo1.getMazo().remove(9));
                    j.recibir_carta(mazo1.getMazo().remove(18));
                    j.recibir_carta(mazo1.getMazo().remove(27));
                    j.recibir_carta(mazo1.getMazo().remove(36));
                    break;
                case 1:
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));

                    break;
                case 2:
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    break;
                case 3:
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    j.recibir_carta(mazo1.getMazo().remove(0));
                    break;
            }
        }
        mazo1.mezclate();
        System.out.println("Cartas en mazo antes de repartir: " + mazo1.getMazo().size());
        Carta carta_palo_triunfo=mazo1.getMazo().getLast();
        int i=0;
        while(!mazo1.getMazo().isEmpty()){
            Jugador jugador=jugadores.get(i%jugadores.size());//% es modulo, hace que se repitan ciclicamente los indices de los jugadores
            Carta carta1=mazo1.getMazo().remove(0);
            jugador.recibir_carta(carta1);
            i++;
        }
        return carta_palo_triunfo;
    }*/
    public void setMazo1(Mazo mazo1){
        this.mazo1=mazo1;
    }
}
