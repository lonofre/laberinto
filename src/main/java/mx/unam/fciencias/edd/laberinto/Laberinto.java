package mx.unam.fciencias.edd.laberinto;

import java.util.Random;

import mx.unam.fciencias.edd.util.Lista;
import mx.unam.fciencias.edd.util.Pila;

/**
 * Un laberinto es una colección de Casillas donde puedes llegar de un punto a otro
 */
public class Laberinto {
    
    /**
     * La cuadrícula o matriz donde se guardan las Casillas
     */
    private Casilla[][] casillas;
    /**
     * El número de casillas que tiene de alto el Laberinto
     */
    private int alto;
    /**
     * El número de casillas que tiene de ancho el Laberinto
     */
    private int ancho;

    /**
     * Genera un Laberinto de acuerdo a un ancho y alto de longitud
     * @param alto el número de casillas que tendrá de alto el Laberinto
     * @param ancho el número de casillas que tendrá de ancho el laberinto
     */
    public Laberinto(int alto, int ancho){
        this.alto = alto;
        this.ancho = ancho;
        this.casillas = generarCasillas();
        generarLaberinto();
    }

    /**
     * Genera al laberinto
     */
    private void generarLaberinto(){
        Casilla casilla = obtenerCasillaAlAzar();
        Pila<Casilla> pila = new Pila<Casilla>();
        pila.push(casilla);
        while(!pila.esVacio()){
            casilla = pila.peek();
            if(casilla.noHayVecinosSinVisitar()){
                pila.pop();
            } else {
                Casilla vecino = casilla.obtenerVecinoSinVisitar();
                casilla.visitar(vecino);
                pila.push(vecino);
            }
        }
    }

    /**
     * Obtiene una Casilla al azar
     * @return una Casilla
     */
    private Casilla obtenerCasillaAlAzar(){
        Random r = new Random();
        int x = r.nextInt(ancho);
        int y = r.nextInt(alto);
        return casillas[y][x];
    }


    /**
     * Genera las Casillas con todas las paredes habilitadas
     * @return un arreglo con las Casillas
     */
    private Casilla[][] generarCasillas(){
        Casilla[][] casillas = new Casilla[alto][ancho];
        for (int i = 0; i < casillas.length; i++) {
            for (int j = 0; j < casillas[i].length; j++) {
                casillas[i][j] = new Casilla(i,j, this);
            }
        }
        return casillas;
    }

    /**
     * Regresa una Casilla del Laberinto de acuerdo a sus coordenadas
     * @param x la coordenada x
     * @param y la coordenada y
     * @throws IllegalArgumentException si la coordenada no está en el alcande del Laberinto
     * @return la Casilla con la correspondiente coordenada
     */
    protected Casilla obtenerCasilla(int x, int y){
        if(x < 0 || x >= ancho){
            throw new IllegalArgumentException("El laberinto no tiene el alcance para la coordenada x: " + x);
        }
        if(y < 0 || y >= alto){
            throw new IllegalArgumentException("El laberinto no tiene el alcance para la coordenada y: " + y);
        }
        return casillas[y][x];
    }


}