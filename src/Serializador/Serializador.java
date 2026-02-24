package Serializador;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Serializador { //MANEJA LA LOGICA
    private String fileName; //nombre del archivo en el que voy a guardar datos

    public Serializador(String fileName){
        super();
        this.fileName = fileName; //se le pasa el archivo para que se lea siempre de él
    }

    public boolean writeOneObject(Object obj){
        boolean respuesta = false;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName, true)); //creo un objeto del tipo ObjectOutputStream
            oos.writeObject(obj); //lo escribimos
            oos.close(); //lo cerramos para que quede protegido
            respuesta = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public boolean addOneObject(Object obj) { //agregar objetos a un archivo
        boolean respuesta = false;
        try {
            AddableObjectOutputStream oos = new AddableObjectOutputStream (new FileOutputStream(fileName,true));
            oos.writeObject(obj);
            oos.close();
            respuesta = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }


    public Object readFirstObject() { //metodo para que se pueda leer un solo objeto y listo
        Object respuesta = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fileName));

            respuesta = ois.readObject();

            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    public Object[] readObjects() { //leer objetos uno a uno
        Object[] respuesta;
        ArrayList<Object> listOfObject = new ArrayList<Object>(); //construimos un arrayList de objetos y le vamos agregando uno a uno los objetos que leemos
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fileName));

            Object r = ois.readObject(); //leemos el primer objeto
            while (r !=null) //si no fue nulo lo agregamos
            {
                listOfObject.add(r);
                r = ois.readObject();
            }
            ois.close();

        }catch (EOFException e) { //cuando no hay mas objetos en el archivo, termina aca, no queda null
            System.out.println("Lectura completada");

        }catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!listOfObject.isEmpty() ) {
            respuesta = new Object[listOfObject.size()];
            int count = 0;
            for(Object o : listOfObject)
                respuesta[count ++] = o;
        } else {
            respuesta = null;
        }
        return respuesta;
    }
}
