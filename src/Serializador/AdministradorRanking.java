package Serializador;

import Model.Jugador;

import java.io.*;
import java.util.ArrayList;

public class AdministradorRanking {
    private final String archivo = "Datos.dat"; //archivo donde guardamos el ranking

    public void guardarRanking(ArrayList<Jugador> jugadores) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(this.archivo))) { //abre el archivo en modo append. crea un objectputputstream. serializo el objeto y lo  guardo en el archivo
            salida.writeObject(jugadores); // guardo la lista completa como un solo archivo
            System.out.println("Jugadores guardados");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al guardar los jugadores");
        }
    }

    public ArrayList<Jugador> cargarRanking() {
        ArrayList<Jugador> jugadores = null;
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(this.archivo))) { //abre al archivo, lee los bytes y reconstruye el objeto serializado.
            jugadores = (ArrayList<Jugador>) entrada.readObject(); //convierte lo que lee en un ArrayList<Jugador>
            System.out.println("Jugadores cargados ");
        } catch (FileNotFoundException e) { //si el archivo no existe
            System.err.println("lista vacia");
            jugadores = new ArrayList<Jugador>(); //devuelve una lista vacia
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error al cargar los jugadores");
        }
        return jugadores;
    }

    public void vaciarRanking() {
        guardarRanking(new ArrayList<Jugador>());
    }
}
