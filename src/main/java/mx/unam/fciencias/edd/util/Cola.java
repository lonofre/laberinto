package mx.unam.fciencias.edd.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p> Clase concreta para modelar la estructura de datos Cola</p>
 * <p>Esta clase implementa una Cola genérica, es decir que es homogénea pero
 * puede tener elementos de cualquier tipo.
 * @author Alejandro Hernández Mora <alejandrohmora@ciencias.unam.mx>
 * @version 1.0
 * @param <T> Tipo que tienen los objetos que guarda esta cola.
 */
public class Cola<T> implements Coleccionable<T> {

    private class Nodo {

        /**
         * El elemento del nodo.
         */
        public T elemento;
        /**
         * El siguiente nodo.
         */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         *
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }
    }

    private class IteradorCola implements Iterator<T> {
        public Nodo siguiente;

        public IteradorCola() {
            siguiente = inicio;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
           return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override
        public T next() {
            T elemento = siguiente.elemento;
            siguiente = siguiente.siguiente;
            return elemento;
        }

        @Override
        public void remove() {
	        //Aqui va tu codigo
        }

    }
    
    /* Nodo inicial de la cola*/
    private Nodo inicio;
    /* Nodo final de la cola*/
    private Nodo rabo;
    /* Tamaño de la cola*/
    private int elementos;


    /**
     * Constructor por omisión de la clase.
     */
    public Cola() {
        //Aqui no hay que hacer nada, 
        //ya que los valores por default nos sirven al crear un objeto.
    }

    /**
     * Constructor que recibe un arreglo de elementos de tipo <code>T</code>.
     * Crea una cola con los elementos del arreglo.
     * @param elementos El arreglo que se recibe como parámetro.
     */
    public Cola(T[] elementos) {
        for (T e : elementos) {
            queue(e);
        }
    }

    
    /**
     * Constructor que recibe una colección de tipo {@link Coleccionable}
     * de elementos de tipo <code>T</code> y los agrega a la nueva cola.
     * @param elementos La colección de elementos a agregar.
     */
    public Cola(Coleccionable<T> elementos) {
        for (T e : elementos) {
            queue(e);
        }
    }

    /**
     * Agrega un elemento en el rabo de la Cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     * <code>null</code>.
     */
    public void queue(T elemento) throws IllegalArgumentException{
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser null");
        }
        Nodo nuevoNodo = new Nodo(elemento);
        if(inicio == null){
            inicio = rabo = nuevoNodo;
        } else {
            rabo.siguiente = nuevoNodo;
            rabo = nuevoNodo;
        }
        elementos++;
    }

    /**
     * Elimina el elemento del inicio de la Cola y lo regresa.
     * @throws NoSuchElementException si la cola es vacía
     * @return el elemento en el inicio de la Cola.
     */
    public T dequeue() throws NoSuchElementException{
        if(esVacio()){
            throw new NoSuchElementException("El elemento no existe");
        }
        T elemento = inicio.elemento;
        if(elementos == 1){
            inicio = rabo = null;
        } else {
            inicio = inicio.siguiente;
        }
        elementos--;
        return elemento;
    }

    /**
     * Nos permite ver el elemento en el inicio de la Cola
     *
     * @return el elemento en un extremo de la estructura.
     */
    public T peek() {
        if(inicio == null){
            return null;
        } 
        return inicio.elemento;
    }

    /**
     * Agrega un elemento a la Cola.
     *
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     * <code>null</code>.
     */
    @Override
    public void agrega(T elemento) throws IllegalArgumentException {
        queue(elemento);
    }

    /**
     * Nos dice si un elemento está contenido en la Cola.
     *
     * @param elemento el elemento que queremos verificar si está contenido en
     * la Cola.
     * @return <code>true</code> si el elemento está contenido en la Cola,
     * <code>false</code> en otro caso.
     */
    @Override
    public boolean contiene(T elemento) {
        for (T e : this) {
            if(e.equals(elemento)) return true;
        }
        return false;
    }

    /**
     * Elimina un elemento de la Cola.
     * @throws NoSuchElementException si la cola es vacía
     * @param elemento el elemento a eliminar.
     */
    @Override
    public void elimina(T elemento) throws NoSuchElementException{
        dequeue();
    }

    /**
     * Nos dice si la Cola está vacía.
     *
     * @return <tt>true</tt> si la Cola no tiene elementos,
     * <tt>false</tt> en otro caso.
     */
    @Override
    public boolean esVacio() {
        return inicio == null;
    }

    /**
     * Regresa el número de elementos en la Cola.
     *
     * @return el número de elementos en la Cola.
     */
    @Override
    public int getTamanio() {
       return elementos;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorCola();
    }

    @Override
    public String toString() {
        String s = "[";
        Nodo n = this.inicio;
        while (n != null) {
            if (n.siguiente == null) {
                s += n.elemento;
            } else {
                s += n.elemento + ",";
            }
            n = n.siguiente;
        }
        s += "]";
        return s;

    }

}
