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
    public String repartida(ArrayList<Jugador> jugadores){
        mazo1.mezclate();
        System.out.println("Cartas en mazo antes de repartir: " + mazo1.getMazo().size());
        Carta carta_palo_triunfo=mazo1.getMazo().getLast();
        String palo_triunfo=carta_palo_triunfo.getNombre();
        int i=0;
        while(!mazo1.getMazo().isEmpty()){
            Jugador jugador=jugadores.get(i%jugadores.size());//% es modulo, hace que se repitan ciclicamente los indices de los jugadores
            Carta carta1=mazo1.getMazo().remove(0);
            jugador.recibir_carta(carta1);
            i++;
        }
        return palo_triunfo;
    }

}
