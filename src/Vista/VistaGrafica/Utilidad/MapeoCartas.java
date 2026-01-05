package Vista.VistaGrafica.Utilidad;

import javax.swing.*;
import java.util.HashMap;

public class MapeoCartas {
    private HashMap<String, String> mapeo;

    public MapeoCartas() {
        mapeo=new HashMap<>();
        mapear();
    }
    private void mapear(){
        mapeo.put("1 BASTO","1 BASTO.png");
        mapeo.put("2 BASTO","2 BASTO.png");
        mapeo.put("3 BASTO","3 BASTO.png");
        mapeo.put("4 BASTO","4 BASTO.png");
        mapeo.put("5 BASTO","5 BASTO.png");
        mapeo.put("6 BASTO","6 BASTO.png");
        mapeo.put("7 BASTO","7 BASTO.png");
        mapeo.put("10 BASTO","10 BASTO.png");
        mapeo.put("11 BASTO","11 BASTO.png");
        mapeo.put("12 BASTO","12 BASTO.png");
        mapeo.put("1 COPA","1 COPA.png");
        mapeo.put("2 COPA","2 COPA.png");
        mapeo.put("3 COPA","3 COPA.png");
        mapeo.put("4 COPA","4 COPA.png");
        mapeo.put("5 COPA","5 COPA.png");
        mapeo.put("6 COPA","6 COPA.png");
        mapeo.put("7 COPA","7 COPA.png");
        mapeo.put("10 COPA","10 COPA.png");
        mapeo.put("11 COPA","11 COPA.png");
        mapeo.put("12 COPA","12 COPA.png");
        mapeo.put("1 ESPADA","1 ESPADA.png");
        mapeo.put("2 ESPADA","2 ESPADA.png");
        mapeo.put("3 ESPADA","3 ESPADA.png");
        mapeo.put("4 ESPADA","4 ESPADA.png");
        mapeo.put("5 ESPADA","5 ESPADA.png");
        mapeo.put("6 ESPADA","6 ESPADA.png");
        mapeo.put("7 ESPADA","7 ESPADA.png");
        mapeo.put("10 ESPADA","10 ESPADA.png");
        mapeo.put("11 ESPADA","11 ESPADA.png");
        mapeo.put("12 ESPADA","12 ESPADA.png");
        mapeo.put("1 ORO","1 ORO.png");
        mapeo.put("2 ORO","2 ORO.png");
        mapeo.put("3 ORO","3 ORO.png");
        mapeo.put("4 ORO","4 ORO.png");
        mapeo.put("5 ORO","5 ORO.png");
        mapeo.put("6 ORO","6 ORO.png");
        mapeo.put("7 ORO","7 ORO.png");
        mapeo.put("10 ORO","10 ORO.png");
        mapeo.put("11 ORO","11 ORO.png");
        mapeo.put("12 ORO","12 ORO.png");
    }
    public ImageIcon obtener_carta(String clave){
        String nombre_real=mapeo.get(clave);
        ImageIcon imagen_carta= new ImageIcon("src/Imagenes_cartas/" + nombre_real);
        return imagen_carta;
    }


}
