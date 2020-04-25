package mx.unam.fciencias.edd.util;


import java.util.Iterator;

/**
 * <p>
 * Clase concreta para modelar la estructura de datos Lista
 * </p>
 * <p>
 * Esta clase implementa una Lista genérica, es decir que es homogénea pero
 * puede tener elementos de cualquier tipo.
 * 
 * @author Alejandro Hernández Mora <alejandrohmora@ciencias.unam.mx>
 * @version 1.0
 */
public class Lista<T> implements Listable<T>, Iterable<T> {

    /* Clase interna para construir la estructura */
    private class Nodo {
        /* Referencias a los nodos anterior y siguiente */
        public Nodo anterior, siguiente;
        /* El elemento que almacena un nodo */
        public T elemento;

        /* Unico constructor de la clase */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }

        public boolean equals(Nodo n) {
            if (n.elemento.equals(this.elemento)) {
                return true;
            }
            return false;
        }
    }

    private class IteradorLista<T> implements Iterator<T> {
        /* La lista a recorrer */
        /* Elementos del centinela que recorre la lista */
        private Lista<T>.Nodo siguiente;

        @SuppressWarnings("unchecked")
        public IteradorLista() {
            siguiente = (Lista<T>.Nodo) cabeza;
        }

        @Override
        public boolean hasNext() {
            return siguiente != null ? true : false;
        }

        @Override
        public T next() {
            if (siguiente == null) {
                return null;
            }
            T elemento = siguiente.elemento;
            siguiente = siguiente.siguiente;
            return elemento;
        }

        @Override
        public void remove() {
            Iterator.super.remove(); // To change body of generated methods, choose Tools | Templates.
        }
    }

    /* Atributos de la lista */
    private Nodo cabeza, cola;
    private int longitud;

    /**
     * Método que nos dice si las lista está vacía.
     * 
     * @return <code>true</code> si el conjunto está vacío, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return cabeza == null ? true : false;

    }

    /**
     * Método para eliminar todos los elementos de una lista
     */
    public void vaciar() {
        cabeza = null;
        cola = null;
        longitud = 0;
    }

    /**
     * Método para obtener el tamaño de la lista
     * 
     * @return tamanio Número de elementos de la lista.
     **/
    public int longitud() {
        return longitud;
    }

    /**
     * Método para agregar un elemento a la lista.
     * 
     * @param elemento Objeto que se agregará a la lista.
     */
    public void agregar(T elemento) {
        Nodo nodo = new Nodo(elemento);
        if (esVacia()) {
            cabeza = nodo;
            cola = nodo;
        } else {
            cola.siguiente = nodo;
            nodo.anterior = cola;
            cola = nodo;
        }
        longitud++;
    }

    /**
     * Método para agregar al inicio un elemento a la lista.
     * 
     * @param elemento Objeto que se agregará al inicio de la lista.
     */
    public void agregarAlInicio(T elemento) {
        Nodo nodo = new Nodo(elemento);
        if (esVacia()) {
            cola = nodo;
        } else {
            cabeza.anterior = nodo;
        }
        nodo.siguiente = cabeza;
        cabeza = nodo;
        longitud++;
    }

    /**
     * Método para agregar al final un elemento a la lista.
     * 
     * @param elemento Objeto que se agregará al inicio de la lista.
     */
    public void agregarAlFinal(T elemento) {
        Nodo nodo = new Nodo(elemento);
        if (esVacia()) {
            cabeza = nodo;
        } else {
            cola.siguiente = nodo;
        }
        nodo.anterior = cola;
        cola = nodo;
        longitud++;
    }

    /**
     * Método para verificar si un elemento pertenece a la lista.
     * 
     * @param elemento Objeto que se va a buscar en la lista.
     * @return <code>true</code> si el elemento esta en el lista y false en otro
     *         caso.
     */
    public boolean contiene(T elemento) {
        return buscar(elemento) != null;
    }

    /**
     * Método para eliminar un elemento de la lista.
     * 
     * @param elemento Objeto que se eliminara de la lista.
     */
    public void eliminar(T elemento) {
        if (esVacia())
            return;
        if (longitud == 1) {
            cabeza = null;
            cola = null;
        } else {
            Nodo nodo = buscar(elemento);
            if (nodo == cabeza) {
                cabeza = cabeza.siguiente;
                cabeza.anterior = null;
            } else if (nodo == cola) {
                cola = cola.anterior;
                cola.siguiente = null;
            } else {
                // El caso cuando el elemento a eliminar se encuentra en medio de la lista
                nodo.anterior.siguiente = nodo.siguiente;
                nodo.siguiente.anterior = nodo.anterior;
            }
        }
        longitud--;
    }

    /**
     * Método para buscar un nodo de la lista
     * 
     * @param elemento el elemento a buscar
     * @return el Nodo donde se encuentra el elemento buscado
     */
    public Nodo buscar(T elemento) {
        if (elemento == null) {
            return null;
        }
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.elemento.equals(elemento)) {
                return aux;
            }
            aux = aux.siguiente;
        }
        return null;
    }

    /**
     * Método que devuelve la posición en la lista que tiene la primera aparición
     * del <code> elemento</code>.
     * 
     * @param elemento El elemnto del cuál queremos saber su posición.
     * @return i la posición del elemento en la lista, -1, si no se encuentra en
     *         ésta.
     */
    public int indiceDe(T elemento) {
        int indice = 0;
        for (T e : this) {
            if (e.equals(elemento)) {
                return indice;
            }
            indice++;
        }
        return -1;
    }

    /**
     * Método que nos dice en qué posición está un elemento en la lista
     * 
     * @param i La posición cuyo elemento deseamos conocer.
     * @return <code> elemento </code> El elemento que contiene la lista,
     *         <code>null</code> si no se encuentra
     * @throws IndexOutOfBoundsException Si el índice es < 0 o >longitud()
     */
    public T getElemento(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= longitud) {
            throw new IndexOutOfBoundsException();
        }
        // Comparación para verificar desde donde comenzar la iteración
        boolean avanzarSiguiente = (i < longitud / 2);
        Nodo nodo = (avanzarSiguiente) ? cabeza : cola;
        T elemento = null;
        int indice = (avanzarSiguiente) ? 0 : longitud - 1;

        // Iteracion, depende del resultado de avanzarSiguiente
        while (nodo != null) {
            if (indice == i) {
                elemento = nodo.elemento;
                break;
            }
            nodo = (avanzarSiguiente) ? nodo.siguiente : nodo.anterior;
            indice += (avanzarSiguiente) ? 1 : -1;
        }

        return elemento;
    }

    /**
     * Método que devuelve una copia de la lista, pero en orden inverso
     * 
     * @return Una copia con la lista l revés.
     */
    public Lista<T> reversa() {
        Lista<T> lista = new Lista<T>();
        for (T elemento : this) {
            lista.agregarAlInicio(elemento);
        }
        return lista;
    }

    /**
     * Método que devuelve una copi exacta de la lista
     * 
     * @return la copia de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<T>();
        for (T elemento : this) {
            lista.agregar(elemento);
        }
        return lista;
    }

    /**
     * Método que nos dice si una lista es igual que otra.
     * 
     * @param o objeto a comparar con la lista.
     * @return <code>true</code> si son iguales, <code>false</code> en otro caso.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Lista<?>) {
            Lista<T> lista = (Lista<T>) o;
            if (lista.longitud == this.longitud) {
                // Se iteran las dos listas
                Iterator<T> iterador1 = this.iterator();
                Iterator<T> iterador2 = lista.iterator();
                while (iterador1.hasNext()) {
                    if (!iterador1.next().equals(iterador2.next())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Método que devuelve un iterador sobre la lista
     * 
     * @return java.util.Iterador -- iterador sobre la lista
     */
    @Override
    public java.util.Iterator<T> iterator() {
        return new IteradorLista<T>();
    }

    /**
     * Método que devuelve una copia de la lista.
     * 
     * @param <T> Debe ser un tipo que extienda Comparable, para poder distinguir el
     *            orden de los elementos en la lista.
     * @param l   La lista de elementos comparables.
     * @return copia de la lista ordenada.
     */
    public static <T extends Comparable<T>> Lista<T> mergesort(Lista<T> l) {
        if (l.longitud < 2)
            return l;
        Lista<T> l1 = new Lista<T>();
        Lista<T> l2 = new Lista<T>();
        int contador = 0;
        for (T elemento : l) {
            if (contador++ < l.longitud / 2) {
                l1.agregar(elemento);
            } else {
                l2.agregar(elemento);
            }
        }
        return merge(mergesort(l1), mergesort(l2));
    }

    /**
     * 
     * @param <T> Debe ser un tipo que extienda Comparable, para poder distinguir el
     *            orden de los elementos en la lista.
     * @param l1  Una lista de elementos comparables
     * @param l2  Una lista de elementos comparables
     * @return Una lista ordenada
     */
    private static <T extends Comparable<T>> Lista<T> merge(Lista<T> l1, Lista<T> l2) {
        Lista<T> l = new Lista<T>();
        Lista<T>.Nodo aux1 = l1.cabeza;
        Lista<T>.Nodo aux2 = l2.cabeza;

        // Se recorren los nodos de las listas
        while (aux1 != null && aux2 != null) {
            if (aux1.elemento.compareTo(aux2.elemento) <= 0) {
                l.agregar(aux1.elemento);
                aux1 = aux1.siguiente;
            } else {
                l.agregar(aux2.elemento);
                aux2 = aux2.siguiente;
            }
        }

        // Se valida lo sobrante de cada lista, en caso de que exista
        while (aux1 != null) {
            T elemento = aux1.elemento;
            l.agregar(elemento);
            aux1 = aux1.siguiente;

        }
        while (aux2 != null) {
            T elemento = aux2.elemento;
            l.agregar(elemento);
            aux2 = aux2.siguiente;
        }

        return l;
    }

    @Override
    public String toString() {
        String s = "[";
        Iterator<T> it = iterator();
        while(it.hasNext()){
            s += it.next();
            if(it.hasNext()){
                s += ","; 
            }
        }

        s += "]";
        return s;
    }

}
