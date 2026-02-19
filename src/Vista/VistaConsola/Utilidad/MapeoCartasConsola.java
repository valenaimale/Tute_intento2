package Vista.VistaConsola.Utilidad;

import java.util.HashMap;

public class MapeoCartasConsola {
    private HashMap<Integer, String> mapeo;
    public MapeoCartasConsola(){
        mapeo=new HashMap<>();
        mapear();
    }
    private void mapear(){
        mapeo.put(1,"1 BASTO");
        mapeo.put(2,"2 BASTO");
        mapeo.put(3,"3 BASTO");
        mapeo.put(4,"4 BASTO");
        mapeo.put(5,"5 BASTO");
        mapeo.put(6,"6 BASTO");
        mapeo.put(7,"7 BASTO");
        mapeo.put(8,"10 BASTO");
        mapeo.put(9,"11 BASTO");
        mapeo.put(10,"12 BASTO");
        mapeo.put(11,"1 COPA");
        mapeo.put(12,"2 COPA");
        mapeo.put(13,"3 COPA");
        mapeo.put(14,"4 COPA");
        mapeo.put(15,"5 COPA");
        mapeo.put(16,"6 COPA");
        mapeo.put(17,"7 COPA");
        mapeo.put(18,"10 COPA");
        mapeo.put(19,"11 COPA");
        mapeo.put(20,"12 COPA");
        mapeo.put(21,"1 ESPADA");
        mapeo.put(22,"2 ESPADA");
        mapeo.put(23,"3 ESPADA");
        mapeo.put(24,"4 ESPADA");
        mapeo.put(25,"5 ESPADA");
        mapeo.put(26,"6 ESPADA");
        mapeo.put(27,"7 ESPADA");
        mapeo.put(28,"10 ESPADA");
        mapeo.put(29,"11 ESPADA");
        mapeo.put(30,"12 ESPADA");
        mapeo.put(31,"1 ORO");
        mapeo.put(32,"2 ORO");
        mapeo.put(33,"3 ORO");
        mapeo.put(34,"4 ORO");
        mapeo.put(35,"5 ORO");
        mapeo.put(36,"6 ORO");
        mapeo.put(37,"7 ORO");
        mapeo.put(38,"10 ORO");
        mapeo.put(39,"11 ORO");
        mapeo.put(40,"12 ORO");
    }
    public String obtener_carta(int id){
        String nombre_carta=mapeo.get(id);
        return nombre_carta;
    }
}
