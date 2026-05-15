package Model;
import Serializador.AdministradorRanking;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import static Model.Eventos.*;
import Exception.*;
public class Juego extends ObservableRemoto implements Serializable, IJuego {
    private ArrayList<Jugador> jugadores;
    private Crupier crupier;
    private int siguiente_id=-1;
    private ResolutorJugada resolutorJugada;
    private Jugada jugada;
    private Boolean partida_iniciada=false;
    private ArrayList<Integer> jugadores_confirmados;
    private AdministradorRanking administradorRanking=new AdministradorRanking();
    private ArrayList<Jugador> ranking;

    public Juego(){
        inicializar();
    }
    private void inicializar() {
        jugadores = new ArrayList<>();
        jugadores_confirmados = new ArrayList<>();
        Mazo mazo_juego = new Mazo();
        resolutorJugada = new ResolutorJugada(mazo_juego);
        crupier = new Crupier(mazo_juego);
        jugada = new Jugada(jugadores);
        this.ranking = this.administradorRanking.cargarRanking();
    }

    @Override
    public int iniciarJugador(String nombre) throws RemoteException, NombreInvalido, PartidaIniciada {
        if(partida_iniciada){
            throw new PartidaIniciada("La partida ya esta comenzada, intentelo mas tarde");
        }
        if(nombre_existente(nombre)){
            throw new NombreInvalido("Este nombre ya esta en uso.");
        }
        Jugador jugador = new Jugador(nombre);
        jugador.setId(siguienteId());
        jugadores.add(jugador);
        if(jugadores.size() == 1){
            jugada.setMi_actual(jugador);
        }
        return jugador.getId();
    }
    public void jugador_agregado() throws RemoteException {
        notificarObservadores(JUGADOR_AGREGADO);
        if(jugadores.size()==4){
            partida_iniciada = true;
            notificarObservadores(COMENZAR_JUEGO);
            repartir();
        }
    }

    @Override
    public Object[][] getTablaRanking() throws RemoteException{
        Object[][] datos=new Object[this.ranking.size()][3];
        int i=0;
        for(Jugador j: this.ranking){
            datos[i][0] = j.getNombre();
            datos[i][1] = j.getPuntaje();
            datos[i][2] = j.getFecha_ranking();
            i++;
        }
        return datos;
    }




    private int siguienteId() throws RemoteException {
        siguiente_id=siguiente_id+1;
        return siguiente_id;
    }

    @Override
    public ArrayList<Jugador> getJugadores() throws RemoteException {
        return jugadores;
    }

    @Override
    public String getPalo_triunfo() throws RemoteException {
        return resolutorJugada.getPalo_del_triunfo();
    }


    private void repartir() throws RemoteException {
        Carta carta_palo_triunfo=crupier.repartida(jugadores);
        jugada.setPalo_triunfo(carta_palo_triunfo.getPalo());
        resolutorJugada.setPalo_del_triunfo(carta_palo_triunfo.getPalo());
        notificarObservadores(CARTAS_REPARTIDAS);
    }
    public void tirada_de_carta(int id) throws RemoteException {
        for(Carta c:jugada.getMi_actual().getMazo_jugador()) {
            if (c.getId() == id) {
                jugada.recibo_carta(c);//jugada recibe la carta tirada y actualiza ganador de baza si ese es el caso
                resolutorJugada.agregar_carta_mazo(c);//se devuelve la carta al mazo
                notificarObservadores(CARTA_TIRADA);//se notifica sobre la carta tirada
                break;
            }
        }
        if(jugada.getQue_hago()==Estado_jugada.TERMINADA){//si la jugada/baza termino (se tiraron 4 cartas)
            resolutorJugada.setGanador_baza(jugada.getMi_ganador());//se actualiza el ganador de la baza en resolutor jugada
            resolutorJugada.actualizar_puntaje(jugada.getMis_cartas());//el resolutor actualiza el puntaje del ganador
            resolutorJugada.chequeo_canto_ganador_baza();//el resolutor chequea si el ganador de la baza puede realizar algun canto
            jugada.cerrar_jugada();//se borran las cartas tiradas de la baza y se actualiza el actual a actual=ganador
            termino_jugada();//segun el estado del ganador de la baza, decidido por resolutor jugada, se notifica lo que corresponde
            //segun lo que cambio en el modelo
        }
        else{
            jugada.actualizar_turno();
            notificarObservadores(ACTUALIZACION_TURNO);
        }
    }


    private void termino_jugada() throws RemoteException {
        switch (resolutorJugada.getEstado_ganador_baza()){
            case NADA:
                notificarObservadores(BAZA_TERMINADA);
                break;
            case TUTE:
                notificarObservadores(OFRECER_TUTE);
                break;
            case LAS_40:
                notificarObservadores(OFRECER_LAS_40);
                break;
            case LAS_20:
                notificarObservadores(OFRECER_LAS_20);
                break;
            case ULTIMAS_10:
                notificarObservadores(ULTIMAS_10);
                break;
        }
    }


    public void canto_positivo() throws RemoteException{//el jugador ganador de la baza eligio realizar el canto
        switch (resolutorJugada.getEstado_ganador_baza()){
            case Estado_ganador_baza.LAS_20:
                resolutorJugada.aceptacion_canto();//se acepta el canto y el resolutor se encarga de sumar los puntos correspondientes (20)
                notificarObservadores(Eventos.CANTA_LAS_20);//se notifica el canto
                break;
            case Estado_ganador_baza.LAS_40:
                resolutorJugada.aceptacion_canto();//se acepta el canto y el resolutor se encarga de sumar los puntos correspondientes (40)
                notificarObservadores(Eventos.CANTA_LAS_40);//se notifica el canto
                break;
            case Estado_ganador_baza.TUTE:
                resolutorJugada.aceptacion_canto();//se acepta el canto y el resolutor se encarga de actualizar el ganador final
                notificarObservadores(Eventos.GANADOR_POR_TUTE);//se notifica el canto y que el jugador gano
                reiniciar_juego();//se borran los datos de esta partida por si se quiere volver a jugar
                break;
        }
    }
    public void canto_negativo() throws RemoteException {
        resolutorJugada.negacion_canto();//el resolutor cambia el estado del ganador de la baza a NADA
        notificarObservadores(Eventos.BAZA_TERMINADA);//se muestra que la mano termino, quien la gano y los puntajes
    }
    public ArrayList<Integer> cartas_posibles(){//metodo para obtener las cartas que puede tirar el jugador actual
        ArrayList<Carta> posibles=jugada.determinar_cartas_disponibles();
        ArrayList<Integer> ids_posibles=new ArrayList<>();
        for(Carta c:jugada.getMi_actual().getMazo_jugador()){
            for(Carta pos:posibles){
                if(c==pos){
                    ids_posibles.add(c.getId());
                }
            }
        }
        return ids_posibles;//se retornan los IDs con el objetivo de que el controlador obtenga solo Integers y no conozca al objeto
        //carta
    }

    @Override
    public Jugador getGanador() throws RemoteException {
        return resolutorJugada.getGanador_final();
    }
    @Override
    public Jugador getGanador_parcial() throws RemoteException {
        return jugada.getMi_ganador();
    }

    @Override
    public Jugador getJugador_actual() throws RemoteException {
        return jugada.getMi_actual();
    }

    @Override
    public ArrayList<Carta> getCartas_jugadas_en_la_mano() throws RemoteException {
        return jugada.getMis_cartas();
    }

    public void reiniciar_juego(){//se reinician los valores del juego
        jugadores.clear();
        siguiente_id=-1;
        partida_iniciada = false;
        Mazo mazo=new Mazo();
        crupier.setMazo1(mazo);
        jugada.reinicio();
        resolutorJugada.reiniciar(mazo);
    }
    public ArrayList<Integer> cartas_repartidas_al_jugador(int id_jugador) throws RemoteException {//se obtienen las cartas
        // repartidas al jugador con el id recibido por parametro
        ArrayList<Integer> id_cartas = new ArrayList<>();
        for(Jugador j: jugadores){
            if(j.getId()==id_jugador){
                for (Carta c : j.getMazo_jugador()) {
                    id_cartas.add(c.getId());
                }
            }
        }
        return id_cartas;//se retornan los IDs con el objetivo de que el controlador obtenga solo Integers y no conozca al objeto
        //carta
    }
    private Boolean nombre_existente(String nombre){
        ArrayList<String> nombres = new ArrayList<>();
        Boolean rta=false;
        for(Jugador j:jugadores){
            nombres.add(j.getNombre());
        }
        if(nombres.contains(nombre)){
            rta=true;
        }
        return rta;
    }

    public synchronized void confirmacion_baza_terminada(int id_confirmado) throws RemoteException {//es necesario el synchornized para darle
        //una especie de lock a este metodo, debido a que este mismo puede ser ejecutado en simultaneo por cada cliente, es decir,
        //todos los hilos clientes pueden ejecutar este metodo al mismo tiempo, lo que genera condiciones de carrera y mala lectura de los datos.
        //Ademas, es muy probable que esto ocurra (que los hilos clientes ejecuten este metodo al mismo tiempo) debido a que el timer
        //de expiracion que genera la ejecucion de este metodo, se acaba al mismo tiempo para todos los clientes.
        //Con synchronized los hilos ejecutan este metodo uno a la vez, si lo quieren ejecutar al mismo tiempo, uno entra y el otro
        //espera a que el otro termine
        System.out.println("Jugadores confirmados antes:\n");
        for(Integer i:jugadores_confirmados){
            System.out.println(i);
        }
        jugadores_confirmados.add(id_confirmado);
        System.out.println("Jugadores confirmados despues:\n");
        for(Integer i:jugadores_confirmados){
            System.out.println(i);
        }
        if(jugadores_confirmados.size()==4) {//significa que todos los jugadores confirmaron que vieron los puntajes y que estan listos
            //para seguir jugando
            jugadores_confirmados.clear();
            switch (resolutorJugada.getEstado_ganador_baza()) {
                case LAS_20, LAS_40, NADA://si no es la ultima mano se notifica la actualizacion del turno
                    notificarObservadores(ACTUALIZACION_TURNO);
                    break;
                case ULTIMAS_10://si esta fue la ultima mano
                    if (resolutorJugada.chequear_si_hay_ganador(jugadores)) {//se chequea si hay un ganador por puntos
                        resolutorJugada.actualizar_ganador(jugadores);//se actualiza el ganador final
                        notificarObservadores(GANADOR_POR_PUNTOS);//se notific que hay un ganador final
                        actualizar_ranking();//se actualiza el ranking
                        reiniciar_juego();//se reinician los valores de la partida
                    } else {//si no hay ganador final, se vuelve a repartir
                        repartir();
                    }
                    break;
            }
        }
    }
    private void actualizar_ranking(){
        ranking.add(resolutorJugada.getGanador_final());
        administradorRanking.guardarRanking(ranking);
    }
}