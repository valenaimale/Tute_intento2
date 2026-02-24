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
    private String nombre_ganador_final;
    private Timer timer;

    private void println_cartas(String carta){
        cartas.append(carta +"\n");
    }
    private void mostrar_cartas(){
        cartas.setText("");
        println_cartas("Tus cartas:");
        for (Integer i:indicecarta_idcarta.keySet()){
            println_cartas(i+"- "+mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i)));
        }
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
    }
    private void mostrar_palo_triunfo(String palo_triunf){
        palo_triunfo.setText("");
        palo_triunfo.append("Palo del triunfo:\n");
        palo_triunfo.append(palo_triunf);
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
        timer=new Timer(15000, e -> {

        });
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
        setBounds(100,100,900, 500);
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
                procesar_inicio(cadena);
                break;
            case EstadoConsola.MOSTRAR_INICIO:
                panel_escritura.setVisible(false);
                estado=EstadoConsola.ESPERANDO_JUGADORES;
                controlador.iniciar_player(cadena);
                break;
            case EstadoConsola.TURNO_ACTUAL:
                procesar_tirada_de_carta(cadena);
                break;
            case EstadoConsola.OFRECER_CANTO:
                procesar_respuesta_a_canto(cadena);
                break;
            /*case EstadoConsola.RESPUESTA_ANUNCIO:
                //texto_entrada.setVisible(false);
                panel_escritura.setVisible(false);
                texto_salida.setText("");
                controlador.procesar_eventos_pendientes();
                break;*/
        }
    }
    @Override
    public void iniciar(){
        setVisible(true);
        estado=EstadoConsola.MENU_PRINCIPAL;
        mostrar_menu_principal();

    }
    private void mostrar_menu_principal(){
        println("Seleccione una de estas opciones ingresando el numero junto a las mismas y luego presionando enter:");
        println("1 - Jugar");
        println("2 - Como jugar");
    }
    private void procesar_inicio(String cadena){
        switch (cadena){
            case "1":
                mostrar_inicio();
                break;
            case "2":
                mostrar_como_jugar();
                break;
            default:
                println("Opcion no valida!");
                mostrar_menu_principal();
                break;
        }
    }
    public void mostrar_inicio(){
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
        if(estado==EstadoConsola.ESPERANDO_JUGADORES){
            texto_salida.setText("Esperando que haya 4 jugadores...\n");
            mostrar_espera();
        }
    }
    private void mostrar_espera(){
        for(Integer i:id_nombre.keySet()){
            println("ID:"+i+"  -NOMBRE:"+id_nombre.get(i));
        }
    }
    @Override
    public void no_mostrar_espera(int cantidad_jugadores, int id_jugador) {
        setTitle("Vista de: "+id_nombre.get(id_jugador));
        texto_salida.setText("");
        println("Ya hay "+ cantidad_jugadores + " jugadores. El juego va a comenzar!");
        println("Comienza la baza. Es turno de "+nombre_ganador_baza);
    }
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
        System.out.println("setCartas_clicleables desde vista de consola");
        System.out.println("cartas clicleables: (aca se ve si el problema es el modelo o la vista):\n");
        for(Integer i:ids_posibles){
            System.out.println(mapeoCartasConsola.obtener_carta(i));
        }
        estado=EstadoConsola.TURNO_ACTUAL;
        println("");
        for(Integer i:indicecarta_idcarta.keySet()){
            Integer idCarta=indicecarta_idcarta.get(i);
            if(ids_posibles.contains(idCarta)){
                indicecarta_idcartaclicleable.put(i, idCarta);
            }
        }
        System.out.println("Cartas clicleables (aca se ve si se agregaron o no):");
        for(Integer i: indicecarta_idcartaclicleable.keySet()){
            System.out.println(mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i)));
        }
        System.out.println("Cartas del HashMap indicecarta_idcarta: \n");
        for(Integer i:indicecarta_idcarta.keySet()){
            System.out.println(mapeoCartasConsola.obtener_carta(indicecarta_idcarta.get(i)));
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
        //texto_entrada.setVisible(true);
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
                println("Indice no disponible. Seleccione uno disponible:");
                mostrar_clicleables();
            }

        } catch (NumberFormatException e) {
            println("Entrada inválida. Ingrese un número:");
            mostrar_clicleables();
        }
    }

    @Override
    public void agregar_carta_mano(int id_carta, int id_jugador) {
        cartas_jugadas_en_la_mano.add(mapeoCartasConsola.obtener_carta(id_carta));
        texto_salida.setText("\n");
        println(id_nombre.get(id_jugador)+ " tiro el "+ mapeoCartasConsola.obtener_carta(id_carta));
        println("");
        println("Cartas tiradas en la mano hasta ahora:");
        for(String s:cartas_jugadas_en_la_mano){
            println(s);
        }
        mostrar_cartas();
    }
    @Override
    public void oferta_las_40() {
        estado=EstadoConsola.OFRECER_CANTO;
        println("");
        println("Podes cantar las 40! Ingrese 1 para cantar o 2 para no cantar y luego presione enter!");
        panel_escritura.setVisible(true);
        //texto_entrada.setVisible(true);

    }
    @Override
    public void oferta_las_20() {
        estado=EstadoConsola.OFRECER_CANTO;
        println("");
        println("Podes cantar las 20! Ingrese 1 para cantar o 2 para no cantar y luego presione enter!");
        //texto_entrada.setVisible(true);
        panel_escritura.setVisible(true);
    }
    @Override
    public void oferta_tute() {
        estado=EstadoConsola.OFRECER_CANTO;
        println("");
        println("Podes cantar tute! Ingrese 1 para cantar o 2 para no cantar y luego presione enter!");
        //texto_entrada.setVisible(true);
        panel_escritura.setVisible(true);
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
                println("Caracter incorrecto, por favor ingrese 1 para cantar o 2 para no cantar!");
        }
    }
    @Override
    public void canta_las_40(String nombre) throws RemoteException {
        //texto_salida.setText("");
        println("");
        println(nombre+ " canto las 40. Suma 40 puntos!");
        controlador.procesar_eventos_pendientes();
        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);
    }
    @Override
    public void canta_las_20(String nombre) throws RemoteException {
        //texto_salida.setText("");
        println("");
        println(nombre+ " canto las 20. Suma 20 puntos!");
        controlador.procesar_eventos_pendientes();

        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);

    }
    @Override
    public void canta_tute() throws RemoteException {
        //texto_salida.setText("");
        println("");
        println(nombre_ganador_final+ " canto tute. Es el ganador del juego!");
        //HAY QUE MOSTRAR OPCIONES DE "VOLVER A JUGAR" Y "SALIR"
        //controlador.procesar_eventos_pendientes();
        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);

    }

    @Override
    public void gana_ultimas_10(String nombre) throws RemoteException {
        println("");
        println(nombre+" gano la ultima baza. Suma 10 puntos!");
        controlador.procesar_eventos_pendientes();

        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);

    }
    @Override
    public void gana_por_puntos() throws RemoteException {
        println(nombre_ganador_final + " sumo 101 puntos o mas. Es el ganador del juego!");
        //HAY QUE MOSTRAR OPCIONES DE "VOLVER A JUGAR" Y "SALIR"
        //controlador.procesar_eventos_pendientes();
        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);

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
        mostrar_puntajes_permanentes();
        println("");
        println(nombre_ganador_baza + " gano la baza. Puntajes:");
        for(Integer i:id_puntaje.keySet()){
            println("-NOMBRE:"+ id_nombre.get(i)+ "  -ID:"+i+"  -PUNTAJE:"+id_puntaje.get(i));
        }
        controlador.procesar_eventos_pendientes();
        println("Comienza la baza");//probar a ver si cuando termina la partida, no se ejecuta
        mostrar_cartas();
        //texto_entrada.setVisible(true);
        //panel_escritura.setVisible(true);

    }

    @Override
    public void cierre_juego(String nombre_ganador) {
        println("El juego termino! El ganador es "+nombre_ganador);
        timer.start();
    }

    @Override
    public void mostrar_turno(int id) {
        println("Turno de "+id_nombre.get(id));
    }

    @Override
    public void setear_ganador(String nombre) {
        nombre_ganador_final=nombre;
    }


    public void print(String cadena){
        texto_salida.append(cadena);
    }
    public void println(String cadena){
        print(cadena + "\n");
    }


    private void mostrar_como_jugar(){

    }
















}
