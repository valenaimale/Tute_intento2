package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mazo implements Serializable {
    private ArrayList<Carta> mazo;

    public Mazo() {
        mazo = new ArrayList<>();

        String[] palos = {"BASTO","COPA", "ESPADA", "ORO", };
        int[] numeros = {1, 2, 3, 4, 5, 6, 7, 10, 11, 12};
        Map<Integer, Integer> orden = new HashMap<>();
        orden.put(1, 1);
        orden.put(3, 2);
        orden.put(12, 3);
        orden.put(11, 4);
        orden.put(10, 5);
        orden.put(7, 6);
        orden.put(6, 7);
        orden.put(5, 8);
        orden.put(4, 9);
        orden.put(2, 10);

        Map<Integer, Integer> valor = new HashMap<>();
        valor.put(1, 11);
        valor.put(3, 10);
        valor.put(12, 4);
        valor.put(11, 3);
        valor.put(10, 2);
        valor.put(7, 0);
        valor.put(6, 0);
        valor.put(5, 0);
        valor.put(4, 0);
        valor.put(2, 0);
        int i=0;
        for (String palo : palos) {
            for (int numero : numeros) {
                i++;
                int vj = valor.get(numero);
                int ord = orden.get(numero);
                mazo.add(new Carta(numero, palo, vj, ord, i));
            }
        }
    }

    public void setMazo(Carta carta){
        this.mazo.add(carta);
    }

    public void mezclate(){
        Collections.shuffle(mazo);
    }

    public ArrayList<Carta> getMazo() {
        return mazo;
    }
}
