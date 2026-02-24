package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Jugador implements Serializable {
    private String nombre;
    private int puntaje;
    private ArrayList<Carta> mazo_jugador;
    //private ArrayList<Carta> bazasGanadas;
    private int id;
    private String fecha_ranking;

    public Jugador(String nombre) {
        inicializar(nombre);

    }
    private void inicializar(String nombre){
        this.nombre=nombre;
        this.puntaje=0;
        this.mazo_jugador=new ArrayList<>();
        LocalDate fecha_actual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fecha_ranking = fecha_actual.format(formato);
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }

    public void recibir_carta(Carta carta1){
        this.mazo_jugador.add(carta1);
    }

    public void tirar_carta(Carta c){
        mazo_jugador.remove(c);
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Carta> getMazo_jugador(){
        return mazo_jugador;
    }


    public int getPuntaje() {
        return puntaje;
    }

    public void incrementar_puntaje(int puntaje){
        this.puntaje+=puntaje;
    }

   /* public ArrayList<Carta> getBazasGanadas() {
        return bazasGanadas;
    }


    public void setBazasGanadas(ArrayList<Carta> bazaGanadas) {
        for(Carta carta:bazaGanadas){
            bazasGanadas.add(carta);
        }
    }*/
    public void setPuntaje(int incremento){
        puntaje=puntaje+incremento;
    }
    public String getFecha_ranking(){
        return fecha_ranking;
    }



}
