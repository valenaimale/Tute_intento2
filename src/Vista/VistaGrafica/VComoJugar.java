package Vista.VistaGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VComoJugar extends JFrame{
    JPanel panel_principal;
    JTextArea explicacion_como_jugar;
    JButton volver_menu_principal;
    JScrollPane scroll;
    VistaGrafica vistaGrafica;

    public VComoJugar(VistaGrafica vistaGrafica){
        inicializar(vistaGrafica);
    }
    private void inicializar(VistaGrafica vistaGrafica){
        this.vistaGrafica=vistaGrafica;
        panel_principal = new JPanel(new BorderLayout());
        explicacion_como_jugar = new JTextArea();
        scroll = new JScrollPane(explicacion_como_jugar);
        volver_menu_principal = new JButton("Volver");
        panel_principal.add(scroll,BorderLayout.CENTER);
        panel_principal.add(volver_menu_principal,BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setContentPane(panel_principal);
        setExplicacion_como_jugar();
        setVisible(false);
        setSize(900, 500);
        setLocationRelativeTo(null);
        volver_menu_principal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                vistaGrafica.mostrar_menu_principal();
            }
        });
    }
    private void println(String cadena){
        explicacion_como_jugar.append(cadena+"\n");
    }
    private void setExplicacion_como_jugar(){
        println("Bienvenido al Tute, esta es una guia sobre como jugar al Tute de a 4 jugadores!");
        println("---------------------");
        println("El objetivo del juego es sumar la mayor cantidad de puntos mediante bazas ganadas y los cantos.");
        println("---------------------");
        println("¿Que es una baza?");
        println("");
        println("Se denomina baza a cada ronda en la cual cada jugador tira obligatoriamente una sola carta. Cuando todos tiran, el ganador de la misma se lleva esas cartas para contar y sumar los puntos.");
        println("---------------------");
        println("Preparacion y reparto:");
        println("");
        println("-Se usa la baraja española de 40 cartas (sin ochos ni nueves).");
        println("-Se reparten todas las cartas, es decir, cada jugador recibe 10 cartas.");
        println("-El palo de la ultima carta repartida, es el que va a ser el 'palo del triunfo' hasta que se vuelva a repartir una proxima vez.");
        println("---------------------");
        println("Valor de las cartas:");
        println("");
        println("Para saber quien es el ganador, se deben sumar los puntos de las cartas de las bazas que haya ganado cada jugador.");
        println("Las cartas tienen un valor en puntos, una vez obtenida una baza. Tambien, tienen un orden al momento de tirar (que carta mata a la otra).");
        println("Valor de las cartas en puntos donde la que esta mas arriba 'mata' la que esta mas abajo:");
        println("1 -> 11 puntos");
        println("3 -> 10 puntos");
        println("12 -> 4 puntos");
        println("11 -> 3 puntos");
        println("10 -> 2 puntos");
        println("7 -> 0 puntos");
        println("6 -> 0 puntos");
        println("5 -> 0 puntos");
        println("4 -> 0 puntos");
        println("2 -> 0 puntos");
        println("");
        println("---------------------");
        println("Ultima baza:");
        println("");
        println("El ganador de la ultima baza, es decir, antes que los jugadores se queden sin cartas por tirar, se lleva 10 puntos adicionales solo por ganarla.");
        println("---------------------");
        println("Reglas al tirar:");
        println("");
        println("Existen ciertas reglas estrictas que respetar al tirar una carta:");
        println("Caso en el cual la primer carta tirada en la baza NO es del palo del triunfo:");
        println("-Si el primer jugador tira oro, tenes que tirar oro.");
        println("-Si no tenes oro, estas obligado a tirar una carta del palo del triunfo. Si tampoco tenes del palo del triunfo, podes tirar cualquiera (perdes automaticamente la baza).");
        println("-Si un jugador anterior a vos ya tiro una carta del palo del triunfo porque no tenia oro, y vos tampoco tenes oro, estas obligado a tirar una carta del palo del triunfo mas alta que la carta del palo del triunfo que tiro el jugador anterior. Si no tenes una mas alta, podes tirar cualquier carta (perdes automaticamente la baza).");
        println("---------------------");
        println("Ejemplo de este caso, donde el palo del triunfo es copa:");
        println("JUGADOR 1-> 5 DE ORO");
        println("JUGADOR 2-> 4 DE ORO");
        println("JUGADOR 3-> 5 DE COPA (no tiene de oro)");
        println("JUGADOR 4-> 6 DE COPA (no tiene de oro y esta obligado a superar al 5 de copa)");
        println("---------------------");
        println("Caso en el cual la primer carta tirada en la baza es del palo del triunfo:");
        println("-Si el primer jugador tira una carta del palo del triunfo, los demas jugadores estan obligados a superar la mas alta que se haya tirado.");
        println("-Si no podes superar a la mas alta que se haya tirado, podes tirar cualquier carta (perdes automaticamente la baza).");
        println("Ejemplo de este caso, donde el palo del triunfo es copa:");
        println("JUGADOR 1-> 5 DE COPA");
        println("JUGADOR 2-> 7 DE COPA (esta obligado a superarla)");
        println("JUGADOR 3-> 12 DE COPA (esta obligado a superarla)");
        println("JUGADOR 4-> 6 DE BASTO (al no tener una mayor al 12 y que, ademas, sea de copa, tira cualquiera)");
        println("---------------------");
        println("El jugador que comienza la baza puede tirar cualquier carta.");
        println("---------------------");
        println("¿Quien gana la baza?");
        println("");
        println("-Si no se jugo ninguna carta del palo del triunfo, el ganador es aquel que haya tirado la carta mas alta del palo de la primer carta tirada en la misma.");
        println("-Si se jugo una carta del palo del triunfo, el ganador es aquel que haya tirado la carta mas alta del palo del triunfo.");
        println("---------------------");
        println("Orden de tirada:");
        println("");
        println("La baza la comienza el jugador que gano la anterior baza, siguiendo el que se encuentra a su derecha hasta que todos tiren una carta.");
        println("---------------------");
        println("Cantos:");
        println("");
        println("Unicamente el ganador de la baza puede realizar un canto, los mismos son los siguientes:");
        println("-LAS 20-> el ganador de la baza debe tener en su mazo un 11 y un 12 del mismo palo, que no sea del palo del triunfo. Al cantarla suma 20 puntos.");
        println("-LAS 40-> el ganador de la baza debe tener en su mazo un 11 y un 12 del mismo palo que el del triunfo. Al cantarla suma 40 puntos.");
        println("-TUTE-> el ganador de la baza debe tener en su mazo los cuatro 11 o los cuatro 12. Al cantarlo gana el juego.");
        println("---------------------");
        println("¿Quien gana el juego?");
        println("");
        println("-Aquel jugador que cante tute.");
        println("-Aquel jugador que, una vez que todos los jugadores tiraron sus cartas, tiene 101 puntos o mas superando a cualquiera de sus contrincantes. Es decir, si el jugador A tiene 114 y el jugador B tiene 105, el ganador es el jugador A. Si dos jugadores tienen 105 puntos, por ejemplo, se vuelve a repartir. Si ningun jugador tiene 101 puntos o mas, se vuelve a repartir.");
    }

}
