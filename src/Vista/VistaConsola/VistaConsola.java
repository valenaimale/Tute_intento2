package Vista.VistaConsola;

import Controlador.Controlador;
import Vista.IVista;
import Vista.VistaConsola.Utilidad.MapeoCartasConsola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class VistaConsola extends JFrame implements IVista {
    private JTextField texto_entrada;
    private Controlador controlador;
    private JTextArea texto_salida;
    private EstadoConsola estado;
    private JPanel panel_principal;
    private JPanel panel_este;
    private JLabel escribir_aca;
    private JPanel panel_escritura;
    private HashMap<Integer, String> id_nombre;//id jugadores asociados a su respectivo nombre
    private HashMap<Integer, Integer> id_puntaje;//id jugadores asociados a su respectivo puntaje
    private HashMap<Integer,Integer> indicecarta_idcarta;//indice que puede ingresar el jugador asociado al id de la carta
    private HashMap<Integer,Integer> indicecarta_idcartaclicleable;//indice de la carta que puede tirar el jugador junto con su id, una vez que la carta se tiro, se limpia el hashmap para la proxima tirada
    private MapeoCartasConsola mapeoCartasConsola;
    private ArrayList<String> cartas_jugadas_en_la_mano;
    private JTextArea cartas;
    private JTextArea puntajes;
    private JTextArea palo_triunfo;
    private JScrollPane scroll;
    private String nombre_ganador_baza;
    private String nombre_jugador;//jugador de esta vista


    public VistaConsola (Controlador controlador){
        inicializar(controlador);
    }
    private void inicializar(Controlador controlador){
        this.controlador=controlador;
        texto_entrada=new JTextField();
        texto_salida=new JTextArea();
        escribir_aca=new JLabel("Ingresar texto:");
        cartas=new JTextArea();
        cartas.setEditable(false);
        puntajes=new JTextArea();
        palo_triunfo=new JTextArea();
        palo_triunfo.setEditable(false);
        puntajes.setEditable(false);
        scroll=new JScrollPane(texto_salida);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel_principal=new JPanel(new BorderLayout());
        panel_escritura=new JPanel(new BorderLayout());
        panel_este=new JPanel(new BorderLayout());
        panel_este.add(new JScrollPane(cartas), BorderLayout.CENTER);
        panel_este.add(new JScrollPane(puntajes), BorderLayout.NORTH);
        panel_este.add(new JScrollPane(palo_triunfo),BorderLayout.SOUTH);
        panel_escritura.add(escribir_aca, BorderLayout.WEST);
        panel_escritura.add(texto_entrada,BorderLayout.CENTER);
        id_nombre=new HashMap<>();
        id_puntaje=new HashMap<>();
        indicecarta_idcarta=new HashMap<>();
        indicecarta_idcartaclicleable=new HashMap<>();
        mapeoCartasConsola=new MapeoCartasConsola();
        cartas_jugadas_en_la_mano=new ArrayList<>();
        texto_salida.setEditable(false);
        panel_principal.add(scroll,BorderLayout.CENTER);
        panel_principal.add(panel_escritura,BorderLayout.SOUTH);
        panel_principal.add(panel_este,BorderLayout.EAST);
        setContentPane(panel_principal);
        setSize(900, 500);
        setLocationRelativeTo(null);
        texto_entrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    procesar_entrada(texto_entrada.getText());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void procesar_entrada(String cadena) throws RemoteException {
        texto_entrada.setText("");
        switch (estado){
            case EstadoConsola.MENU_PRINCIPAL:
                texto_salida.setText("");
                procesar_menu_principal(cadena);
                break;
            case EstadoConsola.MOSTRAR_INICIO:
                panel_escritura.setVisible(false);
                estado=EstadoConsola.ESPERANDO_JUGADORES;
                nombre_jugador = cadena;
                setTitle("Vista de:"+ nombre_jugador);
                controlador.iniciar_player(cadena);
                break;
            case EstadoConsola.TURNO_ACTUAL:
                procesar_tirada_de_carta(cadena);
                break;
            case EstadoConsola.OFRECER_CANTO:
                procesar_respuesta_a_canto(cadena);
                break;
            case EstadoConsola.RESPUESTA_TERMINO:
                procesar_respuesta_termino(cadena);
                break;
            case EstadoConsola.MOSTRANDO_RANKING_COMO_JUGAR:
                procesar_respuesta_ranking_como_jugar(cadena);
                break;
            case EstadoConsola.MOSTRANDO_RANKING_AL_FINALIZAR:
                procesar_respuesta_ranking_al_finalizar(cadena);
                break;
            case EstadoConsola.RESPUESTA_MENSAJE_ERROR:
                procesar_respuesta_de_error(cadena);
                break;
            case EstadoConsola.RESPUESTA_PUNTAJE:
                panel_escritura.setVisible(false);
                controlador.respuesta_puntaje();
                break;
        }
    }
    @Override
    public void iniciar(){
        setVisible(true);
        mostrar_menu_principal();

    }

    @Override
    public void no_mostrar_espera() {

    }

    private void mostrar_menu_principal(){
        estado=EstadoConsola.MENU_PRINCIPAL;
        setTitle("MENU PRINCIPAL");
        texto_salida.setText("");
        println("Seleccione una de estas opciones ingresando el numero junto a las mismas y luego presionando enter:");
        println("1 - Jugar");
        println("2 - Como jugar");
        println("3 - Ver ranking");
    }
    private void procesar_menu_principal(String cadena) throws RemoteException {
        switch (cadena){
            case "1":
                mostrar_inicio();
                break;
            case "2":
                mostrar_como_jugar();
                break;
            case "3":
                estado=EstadoConsola.MOSTRANDO_RANKING_COMO_JUGAR;
                mostrar_ranking();
                break;
            default:
                println("Opcion no valida!");
                mostrar_menu_principal();
                break;
        }
    }
    private void mostrar_ranking() throws RemoteException {
        Object[][] tabla= controlador.getTablaRanking();
        texto_salida.setText("");
        setTitle("RANKING");
        println("Ranking historico de ganadores:");
        for(Object[] fila: tabla){
            print("-Nombre:"+fila[0]+"  ");
            print("-Puntaje ganador:"+fila[1]+"  ");
            println("-Fecha:"+fila[2]);
        }
        println("Ingrese 1 para volver y luego presione enter!");

    }
    private void procesar_respuesta_ranking_como_jugar(String cadena){
        switch (cadena){
            case "1":
                estado=EstadoConsola.MENU_PRINCIPAL;
                mostrar_menu_principal();
                break;
            default:
                println("Caracter incorrecto, por favor ingrese 1 para volver al menu principal");
                break;
        }
    }
    private void mostrar_inicio(){
        estado=EstadoConsola.MOSTRAR_INICIO;
        print("Ingrese su nombre para comenzar a jugar: ");
    }

    @Override
    public void limpiar_tablas() {
        id_puntaje.clear();
        id_nombre.clear();
    }

    @Override
    public void aniadir_jugador_a_tablas(int id, int puntaje, String nombre) {
        id_nombre.put(id,nombre);
        if(id_nombre.size()==1){
            nombre_ganador_baza=id_nombre.get(id);
        }
        id_puntaje.put(id,puntaje);
        //texto_salida.setText("Esperando que haya 4 jugadores...\n");
        //mostrar_espera();
    }

    /*@Override
    public void no_mostrar_espera(int cantidad_jugadores, int id_jugador) {
        setTitle("Vista de: "+nombre_jugador);
        texto_salida.setText("");
        println("Ya hay "+ cantidad_jugadores + " jugadores. El juego va a comenzar!");
        println("Comienza el juego. Es turno de "+nombre_ganador_baza);
    }*/
    @Override
    public void iniciar_valores_partida(ArrayList<Integer> ids_cartas, String palo_triunfo) {
        println("El palo del triunfo es: "+ palo_triunfo);
        int indice=1;
        for(Integer i:ids_cartas){
            indicecarta_idcarta.put(indice,i);
            indice++;
        }
        mostrar_cartas();
        mostrar_puntajes_permanentes();
        mostrar_palo_triunfo(palo_triunfo);
    }
    @Override
    public void setCartas_clicleables(ArrayList<Integer> ids_posibles) {
        estado=EstadoConsola.TURNO_ACTUAL;
        println("");
        for(Integer i:indicecarta_idcarta.keySet()){
            Integer idCarta=indicecarta_idcarta.get(i);
            if(ids_posibles.contains(idCarta)){
                indicecarta_idcartaclicleable.put(i, idCarta);
            }
        }
        println("Es tu turno. Para tirar ingresa el indice de una carta disponible y presiona enter!");
        mostrar_clicleables();
    }
    private void mostrar_clicleables(){
        for(Integer i:indicecarta_idcarta.keySet()){
            if(indicecarta_idcartaclicleable.containsKey(i)){
                println(i+"- "+mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i))+ " - DISPONIBLE");
            }
            else{
                println(i+"- "+mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i)));
            }
        }
        panel_escritura.setVisible(true);
    }
    private void procesar_tirada_de_carta(String indice_ingresado) throws RemoteException {
        try {
            int indice = Integer.parseInt(indice_ingresado);
            if(indicecarta_idcartaclicleable.containsKey(indice)){
                //texto_entrada.setVisible(false);
                panel_escritura.setVisible(false);
                indicecarta_idcartaclicleable.clear();
                int id_carta_tirada=indicecarta_idcarta.remove(indice);
                actualizar_indices();
                controlador.tira_carta(id_carta_tirada);
                mostrar_cartas();
            }
            else{
                println("Indice no disponible. Seleccione uno disponible y luego presione enter:");
                mostrar_clicleables();
            }

        } catch (NumberFormatException e) {
            println("Entrada inválida. Ingrese un número disponible y luego presione enter:");
            mostrar_clicleables();
        }
    }

    @Override
    public void agregar_carta_mano(int id_carta, int id_jugador) {
        cartas_jugadas_en_la_mano.add(mapeoCartasConsola.obtener_carta(id_carta));
        texto_salida.setText("\n");
        println(id_nombre.get(id_jugador)+ " tiro el "+ mapeoCartasConsola.obtener_carta(id_carta));
        println("");
        println("Cartas tiradas en la baza hasta ahora:");
        for(String s:cartas_jugadas_en_la_mano){
            println(s);
        }
        //mostrar_cartas();
    }


    private void procesar_respuesta_a_canto(String respuesta) throws RemoteException {
        switch (respuesta){
            case "1":
                controlador.eleccion_si();
                break;
            case "2":
                controlador.eleccion_no();
                break;
            default:
                println("Caracter incorrecto, por favor ingrese 1 para cantar o 2 para no cantar y luego presione enter!");
                break;
        }
    }


    @Override
    public void limpiar_cartas_mesa() {
        cartas_jugadas_en_la_mano.clear();
    }
    @Override
    public void actualizar_puntaje(int id, int puntaje, String nombre) {
        for(Integer i:id_puntaje.keySet()){
            if(i==id){
                id_puntaje.put(i,puntaje);
                break;
            }
        }
        nombre_ganador_baza=nombre;
    }
    @Override
    public void mostrar_puntajes() throws RemoteException {
        estado = EstadoConsola.RESPUESTA_PUNTAJE;
        mostrar_puntajes_permanentes();
        println("");
        //estado =EstadoConsola.RESPUESTA_PUNTAJES;
        println(nombre_ganador_baza + " gano la baza. Puntajes:");
        for(Integer i:id_puntaje.keySet()){
            println("-NOMBRE:"+ id_nombre.get(i)+ "  -ID:"+i+"  -PUNTAJE:"+id_puntaje.get(i));
        }
        //controlador.respuesta_puntaje();
        println("");
        println("Presione enter para continuar!");
        panel_escritura.setVisible(true);
    }



    @Override
    public void mostrar_turno(String nombre) {
        panel_escritura.setVisible(false);
        println("Turno de "+nombre);
    }


    @Override
    public void mostrar_mensaje_error(String error) {
        estado = EstadoConsola.RESPUESTA_MENSAJE_ERROR;
        texto_salida.setText("");
        println(error);
        println("Ingrese 1 para volver al menu principal");
        panel_escritura.setVisible(true);

    }

    private void procesar_respuesta_de_error(String cadena){
        switch (cadena){
            case "1":
                mostrar_menu_principal();
                break;
            default:
                println("Ingrese 1 para volver al menu principal");
                break;
        }

    }

    @Override
    public void canto(String s) throws RemoteException {
        println("");
        println(s);
        println("");
        controlador.respuesta_anuncio();

    }

    @Override
    public void cartel_ganador(String s) {
        estado = EstadoConsola.RESPUESTA_TERMINO;
        println("");
        println(s);
        println("Ingrese 1 para volver a jugar, 2 para salir o 3 para ver el ranking. Luego presione enter!");
        panel_escritura.setVisible(true);
    }

    @Override
    public void oferta_canto(String s) {
        estado=EstadoConsola.OFRECER_CANTO;
        println("");
        println(s+ "! Ingrese 1 para cantar o 2 para no cantar. Luego presione enter!");
        panel_escritura.setVisible(true);

    }

    @Override
    public void mostrar_esperando() {
        texto_salida.setText("Esperando que haya 4 jugadores...\n");
        for(Integer i:id_nombre.keySet()){
            println("ID:"+i+"  -NOMBRE:"+id_nombre.get(i));
        }
    }


    private void procesar_respuesta_termino(String cadena) throws RemoteException {
        switch (cadena){
            case "1":
                estado=EstadoConsola.ESPERANDO_JUGADORES;
                reiniciar_palo_triunfo();
                reiniciar_puntajes_permanenetes();
                panel_escritura.setVisible(false);
                controlador.iniciar_player(nombre_jugador);
                break;
            case "2":
                controlador.terminar();
                break;
            case "3":
                estado=EstadoConsola.MOSTRANDO_RANKING_AL_FINALIZAR;
                mostrar_ranking();
                break;
            default:
                println("Caracter incorrecto, por favor ingrese 1 para volver a jugar, 2 para salir o 3 para ver el ranking!");
                break;
        }
    }
    private void procesar_respuesta_ranking_al_finalizar(String cadena){
        switch (cadena){
            case "1":
                estado = EstadoConsola.RESPUESTA_TERMINO;
                texto_salida.setText("");
                setTitle("Vista de: "+nombre_jugador);
                println("Ingrese 1 para volver a jugar, 2 para salir o 3 para ver el ranking. Luego presione enter!");
                break;
            default:
                println("Caracter incorrecto, por favor ingrese 1 para volver a las opciones de termino!");
                break;
        }
    }

    private void mostrar_como_jugar(){
        estado=EstadoConsola.MOSTRANDO_RANKING_COMO_JUGAR;
        setTitle("GUIA COMO JUGAR AL TUTE");
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
        println("---------------------");
        println("---------------------");
        println("Ingrese 1 para volver al menu principal y luego presione enter!");
    }
    //METODOS AUXILIARES
    private void println_cartas(String carta){
        cartas.append(carta +"\n");
    }
    private void mostrar_cartas(){
        cartas.setText("");
        println_cartas("Tus cartas:");
        for (Integer i:indicecarta_idcarta.keySet()){
            println_cartas(i+"- "+mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i)));
        }
        panel_principal.revalidate();
    }
    private void println_puntajes(String puntaje){
        puntajes.append(puntaje+"\n");
    }
    private void mostrar_puntajes_permanentes(){
        puntajes.setText("");
        println_puntajes("Puntajes:");
        for(Integer i: id_puntaje.keySet()){
            println_puntajes("-NOMBRE:"+ id_nombre.get(i)+ "  -ID:"+i+"  -PUNTAJE:"+id_puntaje.get(i));
        }
        panel_principal.revalidate();
    }
    private void reiniciar_puntajes_permanenetes(){
        puntajes.setText("Puntajes:\n");
        panel_principal.revalidate();

    }
    private void mostrar_palo_triunfo(String palo_triunf){
        palo_triunfo.setText("");
        palo_triunfo.append("Palo del triunfo:\n");
        palo_triunfo.append(palo_triunf);
        panel_principal.revalidate();
    }
    private void reiniciar_palo_triunfo(){
        palo_triunfo.setText("Palo del triunfo:\n");
        panel_principal.revalidate();
    }
    private void actualizar_indices(){
        HashMap<Integer,Integer> indices_ids=new HashMap<>(indicecarta_idcarta);
        indicecarta_idcarta.clear();
        int contador=1;
        for(Integer i:indices_ids.keySet()){
            indicecarta_idcarta.put(contador,indices_ids.get(i));
            contador++;
        }
    }
    public void print(String cadena){
        texto_salida.append(cadena);
    }
    public void println(String cadena){
        print(cadena + "\n");
    }
    @Override
    public void limpiar_partida(){

    }

    @Override
    public void iniciar_posiciones_mano(int cantidad, int id_jugador) {

    }

    @Override
    public void no_mostrar_espera_confirmaciones() {

    }

    @Override
    public void esperar_confirmacion(String s) {

    }
}
