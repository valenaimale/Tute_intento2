package Model;

import java.util.ArrayList;

public class ResolutorJugada { //tiene la responsabilidad de encargarse de todo lo que hay que hacer cuando termina una jugada
                               //calcular tantos del ganador, ver si puede cantar algo, ver si hay que repartir,
    private Jugador ganador_final;
    private Jugador ganador_baza;
    private Mazo mazo;
    private Estado_ganador_baza estado_ganador_baza;
    private String palo_del_triunfo;

    public ResolutorJugada(Mazo mazo){
        inicializar(mazo);
    }
    private void inicializar(Mazo mazo){
        this.mazo=mazo;
        estado_ganador_baza=Estado_ganador_baza.NADA;
    }
    public void reiniciar(Mazo mazo){
        ganador_final=null;
        ganador_baza=null;
        estado_ganador_baza=Estado_ganador_baza.NADA;
        palo_del_triunfo=null;
        setMazo(mazo);
    }
    public void chequeo_canto_ganador_baza(){
        cambiar_estado(Estado_ganador_baza.NADA);
        ArrayList<Integer> ids= mapear_cartas_a_id(ganador_baza.getMazo_jugador());
        chequear_ultimas_10();
        if(tute(ids)){
            cambiar_estado(Estado_ganador_baza.TUTE);
        }
        else{
            switch (palo_del_triunfo){
                case "BASTO":
                     System.out.println("chequeo_ganador_baza. Basto");
                     if(ids.contains(9) && ids.contains(10)){
                         cambiar_estado(Estado_ganador_baza.LAS_40);
                     }
                     else if((ids.contains(19) && ids.contains(20)) || (ids.contains(29) && ids.contains(30)) || (ids.contains(39) && ids.contains(40))){
                         cambiar_estado(Estado_ganador_baza.LAS_20);
                     }
                     break;
                case "COPA":
                    System.out.println("chequeo_ganador_baza. Copa");

                    if(ids.contains(19) && ids.contains(20)){
                        cambiar_estado(Estado_ganador_baza.LAS_40);
                    }
                    else if((ids.contains(9) && ids.contains(10)) || (ids.contains(29) && ids.contains(30)) || (ids.contains(39) && ids.contains(40))){
                        cambiar_estado(Estado_ganador_baza.LAS_20);
                    }
                    break;
                case "ESPADA":
                    System.out.println("chequeo_ganador_baza. Espada");
                    if(ids.contains(29) && ids.contains(30)){
                        cambiar_estado(Estado_ganador_baza.LAS_40);
                    }
                    else if((ids.contains(9) && ids.contains(10)) || (ids.contains(19) && ids.contains(20)) || (ids.contains(39) && ids.contains(40))){
                        cambiar_estado(Estado_ganador_baza.LAS_20);
                    }
                    break;
                case "ORO":
                    System.out.println("chequeo_ganador_baza. Oro");
                    if(ids.contains(39) && ids.contains(40)){
                        cambiar_estado(Estado_ganador_baza.LAS_40);
                    }
                    else if((ids.contains(9) && ids.contains(10)) || (ids.contains(19) && ids.contains(20)) || (ids.contains(29) && ids.contains(30))){
                        cambiar_estado(Estado_ganador_baza.LAS_20);
                    }
                    break;
            }
        }
    }
    public void actualizar_puntaje(ArrayList<Carta> baza_ganada){
        int puntos_sumados=determinar_tantos(baza_ganada);
        ganador_baza.incrementar_puntaje(puntos_sumados);
    }
    public void aceptacion_canto(){
        switch (estado_ganador_baza){
            case LAS_40:
                ganador_baza.incrementar_puntaje(40);
                break;
            case LAS_20:
                ganador_baza.incrementar_puntaje(20);
                break;
        }
    }
    private int determinar_tantos(ArrayList<Carta> baza_ganada){
        int suma=0;
        for(Carta carta:baza_ganada){
            suma=carta.getValor_en_juego()+suma;
        }
        return suma;
    }
    private void chequear_ultimas_10(){
        if(mazo.getMazo().size()==40){
            ganador_baza.incrementar_puntaje(10);
            estado_ganador_baza=Estado_ganador_baza.ULTIMAS_10;
        }
    }
    private ArrayList<Integer> mapear_cartas_a_id(ArrayList<Carta> cartas_ganador_baza){
        ArrayList<Integer> ids=new ArrayList<>();
        for(Carta c:cartas_ganador_baza){
            ids.add(c.getId());
        }
        return ids;
    }
    private void cambiar_estado(Estado_ganador_baza estado_ganador_baza){
        this.estado_ganador_baza=estado_ganador_baza;
    }
    private Boolean tute(ArrayList<Integer> ids){
        Boolean tute=false;
        if((ids.contains(10) && ids.contains(20) && ids.contains(30) && ids.contains(40)) || (ids.contains(9) && ids.contains(19) && ids.contains(29) && ids.contains(39))){
            tute=true;
        }
        return tute;
    }
    public void agregar_carta_mazo(Carta carta){
        mazo.setMazo(carta);
    }

    public Boolean chequear_si_hay_ganador(ArrayList<Jugador> jugadors){
        Boolean rta=false;
        int puntajeGanador=0;
        for(Jugador jugador:jugadors){
            if(jugador.getPuntaje()>puntajeGanador && jugador.getPuntaje()>=101){
                puntajeGanador=jugador.getPuntaje();
                rta=true;
            }
            else if(jugador.getPuntaje()==puntajeGanador){
                rta=false;
            }
        }
        return rta;
    }

    public void actualizar_ganador(ArrayList<Jugador> jugadors){
        ganador_final=jugadors.get(0);
        for(Jugador jugador:jugadors){
            System.out.println("Jugadores en resolutor.actualizar_ganador: "+jugador.getNombre());
            if(jugador.getPuntaje()>ganador_final.getPuntaje()){
                ganador_final=jugador;
            }
        }
        System.out.println("Ganador final: "+ganador_final.getNombre());
    }

    public Estado_ganador_baza getEstado_ganador_baza() {
        return estado_ganador_baza;
    }
    public String getPalo_del_triunfo() {
        return palo_del_triunfo;
    }
    public Jugador getGanador_final() {
        return ganador_final;
    }

    public Mazo getMazo() {
        return mazo;
    }
    public void setGanador_baza(Jugador ganador_baza){
        this.ganador_baza=ganador_baza;
    }

    public void setPalo_del_triunfo(String palo_del_triunfo) {
        this.palo_del_triunfo = palo_del_triunfo;
        System.out.println("El palo del triunfo es: "+palo_del_triunfo);
    }
    private void setMazo(Mazo mazo){
        this.mazo=mazo;
    }
}
