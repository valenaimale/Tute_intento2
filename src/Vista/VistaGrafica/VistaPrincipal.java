package Vista.VistaGrafica;

import Controlador.Controlador;
import Model.Jugador;

import java.util.ArrayList;

public class VistaPrincipal {
    private Controlador controlador;
    private Menu_principal menu_principal;
    private VentanaInicioJugador ventanaInicioJugador;
    private EsperandoJugadores esperando;

    public VistaPrincipal(Controlador controlador){
        this.controlador=controlador;
        controlador.setVistaPrincipal(this);
    }
    public void iniciar(){
        ventanaInicioJugador=new VentanaInicioJugador(this, controlador);
        menu_principal=new Menu_principal(controlador, this);
        esperando= new EsperandoJugadores();
        mostrar_menu_principal();
    }
    public void mostrar_menu_principal(){
        menu_principal.setVisible(true);
    }
    public void mostrar_inicio(){
        ventanaInicioJugador.setVisible(true);
    }
    public void mostrar_esperando(){
        esperando.setVisible(true);
    }
    public void no_mostrar_espera(){
        esperando.setVisible(false);
    }
    public void agregar_jugador_a_la_espera(ArrayList<Jugador> jugadores){
        esperando.agregar(jugadores);
    }



}
