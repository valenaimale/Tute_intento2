package Vista.VistaGrafica.Utilidad;

import javax.swing.*;
import java.util.HashMap;

public class MapeoCartas {
    private HashMap<Integer, String> mapeo;

    public MapeoCartas() {
        mapeo=new HashMap<>();
        mapear();
    }
    private void mapear(){
        mapeo.put(1,"1 BASTO.png");
        mapeo.put(2,"2 BASTO.png");
        mapeo.put(3,"3 BASTO.png");
        mapeo.put(4,"4 BASTO.png");
        mapeo.put(5,"5 BASTO.png");
        mapeo.put(6,"6 BASTO.png");
        mapeo.put(7,"7 BASTO.png");
        mapeo.put(8,"10 BASTO.png");
        mapeo.put(9,"11 BASTO.png");
        mapeo.put(10,"12 BASTO.png");
        mapeo.put(11,"1 COPA.png");
        mapeo.put(12,"2 COPA.png");
        mapeo.put(13,"3 COPA.png");
        mapeo.put(14,"4 COPA.png");
        mapeo.put(15,"5 COPA.png");
        mapeo.put(16,"6 COPA.png");
        mapeo.put(17,"7 COPA.png");
        mapeo.put(18,"10 COPA.png");
        mapeo.put(19,"11 COPA.png");
        mapeo.put(20,"12 COPA.png");
        mapeo.put(21,"1 ESPADA.png");
        mapeo.put(22,"2 ESPADA.png");
        mapeo.put(23,"3 ESPADA.png");
        mapeo.put(24,"4 ESPADA.png");
        mapeo.put(25,"5 ESPADA.png");
        mapeo.put(26,"6 ESPADA.png");
        mapeo.put(27,"7 ESPADA.png");
        mapeo.put(28,"10 ESPADA.png");
        mapeo.put(29,"11 ESPADA.png");
        mapeo.put(30,"12 ESPADA.png");
        mapeo.put(31,"1 ORO.png");
        mapeo.put(32,"2 ORO.png");
        mapeo.put(33,"3 ORO.png");
        mapeo.put(34,"4 ORO.png");
        mapeo.put(35,"5 ORO.png");
        mapeo.put(36,"6 ORO.png");
        mapeo.put(37,"7 ORO.png");
        mapeo.put(38,"10 ORO.png");
        mapeo.put(39,"11 ORO.png");
        mapeo.put(40,"12 ORO.png");
    }
    public ImageIcon obtener_carta(int clave){
        String nombre_real=mapeo.get(clave);
        ImageIcon imagen_carta= new ImageIcon("src/Imagenes_cartas/" + nombre_real);
        return imagen_carta;
    }


}
