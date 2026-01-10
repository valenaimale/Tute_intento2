package Model;

import java.io.Serializable;

public class Carta implements Serializable {
    private int numero;
    private String palo;
    private int valor_en_juego;//valor de las cartas a la hora de contar los puntos
    private int orden;//nos dice la jerarquia de una carta con respecto a otra donde la que tine mas jerarquia tiene un 1 y la segunda de mas jerarquia un 2
    private int id;

    public Carta(int numero, String palo, int valor_en_juego, int orden, int id) {
        this.numero = numero;
        this.palo = palo;
        this.valor_en_juego = valor_en_juego;
        this.orden = orden;
        this.id=id;

    }
    public String getNombre(){
        return numero + " " + palo;
    }

    public int getNumero() {
        return numero;
    }


    public String getPalo() {
        return palo;
    }


    public int getOrden() {
        return orden;
    }


    public int getValor_en_juego() {
        return valor_en_juego;
    }

    public int getId(){
        return id;
    }

}
