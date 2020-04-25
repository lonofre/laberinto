package mx.unam.fciencias.edd.laberinto;

import java.util.Random;

import mx.unam.fciencias.edd.util.Lista;

/**
 * Una Casilla es un cuadro en el Laberinto, la cual tiene posición y puede tener hasta cuatro paredes
 */
public class Casilla {

    private enum PuntoCardinal {
        NORTE, SUR, ESTE, OESTE
    }

    /**
     * Indica si la existe la Pared Norte
     */
    private boolean norte;
    /**
     * Indica si la existe la Pared Sur
     */
    private boolean sur;
    /**
     * Indica si la existe la Pared Este
     */
    private boolean este;
    /**
     * Indica si la existe la Pared Oeste
     */
    private boolean oeste;
    /**
     * Posición x-coordenada en la cuadrícula
     */
    private int x;
    /**
     * Posición y-coordenada en la cuadrícula
     */
    private int y;
    /**
     * El laberinto donde pertenece la cuadrícula
     */
    private Laberinto laberinto;
    /**
     * Marca si la Casilla ya ha sido visitada
     */
    private boolean visitada;

    /**
     * Genera una casilla vacia
     */
    protected Casilla() {
        this.x = -1;
        this.y = -1;
    }

    /**
     * Crea una casilla para que pueda ser utilizada en la generación del Laberinto
     * 
     * @param x         la x-coordenada en el laberinto
     * @param y         la y-coordenada en el laberinto
     * @param laberinto el laberinto donde va a pertenecer la casilla
     */
    protected Casilla(int x, int y, Laberinto laberinto) {
        this.norte = this.sur = this.este = this.oeste = false;
        this.x = x;
        this.y = y;
        this.laberinto = laberinto;
        this.visitada = false;
    }

    /**
     * Muestra si no hay vecinos sin visitar
     * @return true si no hay vecinos sin visitar, false en caso contrario
     */
    public boolean noHayVecinosSinVisitar() {
        Lista<Casilla> vecinos = obtenerVecinos();
        boolean todosVisitados = true;
        for (Casilla vecino : vecinos) {
            if (!vecino.fueVisitado()) {
                todosVisitados = false;
                break;
            }
        }
        return todosVisitados;
    }

    /**
     * Obtiene un vecino al azar que no ha sido visitado
     * @return una Casilla adyacente que no ha sido visitada
     */
    public Casilla obtenerVecinoSinVisitar() {
        Lista<Casilla> vecinos = obtenerVecinos();
        Random r = new Random();
        int rango = vecinos.longitud();
        do {
            int posicion = r.nextInt(rango);
            Casilla vecino = vecinos.getElemento(posicion);
            if (!vecino.fueVisitado()) {
                return vecino;
            } else {
                rango--;
                vecinos.eliminar(vecino);
            }
        } while (!vecinos.esVacia());
        return new Casilla();
    }

    /**
     * Obtiene la lista de vecinos que son adyacentes a la Casilla (que comparten pared)
     * @return una Lista Casillas adyacentes
     */
    public Lista<Casilla> obtenerVecinos() {
        Lista<Casilla> vecinos = new Lista<Casilla>();

        // Se añaden las coordenadas así para poder recorrer los vecinos más fácilmente
        int[][] vecinosCoordenadas = { { y, x + 1 }, { y, x - 1 }, { y + 1, x }, { y - 1, x } };
        for (int i = 0; i < vecinosCoordenadas.length; i++) {
            int x = vecinosCoordenadas[i][1];
            int y = vecinosCoordenadas[i][0];

            try {
                Casilla casilla = laberinto.obtenerCasilla(x, y);
                vecinos.agregar(casilla);
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        return vecinos;
    }

    /**
     * Devuelve si la la casilla ya ha sido visitada
     * 
     * @return true si la casilla ya ha sido visitada, false en caso contrario
     */
    public boolean fueVisitado() {
        return visitada;
    }

    /**
     * Marca a la Casilla como visitada
     */
    public void visitar() {
        this.visitada = true;
    }
    
    /**
     * Visita una Casilla adyacente si no ha sido visitada
     * @param casilla la Casilla a visitar
     */
    public void visitar(Casilla casilla) {
        Lista<Casilla> vecinos = obtenerVecinos();
        if (vecinos.contiene(casilla) && !casilla.fueVisitado()) {
            switch (obtenerPuntoCardinal(casilla)) {
                case NORTE:
                    norte = casilla.sur = casilla.visitada = true;
                    break;
                case SUR:
                    sur = casilla.norte = casilla.visitada = true;
                    break;
                case ESTE:
                    este = casilla.oeste = casilla.visitada = true;
                    break;
                case OESTE:
                    oeste = casilla.este = casilla.visitada = true;
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Obtiene el Punto Cardinal de una Casilla adyacente
     * @param casilla la Casilla adyacente
     * @return el Punto Cardinal
     */
    private PuntoCardinal obtenerPuntoCardinal(Casilla casilla) {
        if (x - casilla.x == -1) {
            return PuntoCardinal.ESTE;
        } else if (x - casilla.x == 1) {
            return PuntoCardinal.OESTE;
        } else if (y - casilla.y == -1) {
            return PuntoCardinal.NORTE;
        } else {
            return PuntoCardinal.SUR;
        }
    }

}