package mx.unam.fciencias.edd.laberinto;

import java.io.Serializable;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import mx.unam.fciencias.edd.util.Lista;

/**
 * Una Casilla es un cuadro en el Laberinto, la cual tiene posición y puede
 * tener hasta cuatro paredes
 */
public class Casilla implements Serializable {

    private static final long serialVersionUID = -5759297859583844470L;

    private enum PuntoCardinal {
        NORTE, SUR, ESTE, OESTE
    }

    /**
     * Indica si está habilitada la Pared Norte
     */
    private boolean norte;
    /**
     * Indica si está habilitada la Pared Sur
     */
    private boolean sur;
    /**
     * Indica si está habilitada la Pared Este
     */
    private boolean este;
    /**
     * Indica si está habilitada la Pared Oeste
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
    @JsonIgnore
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
     * 
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
     * Muestra si no hay vecinos sin visitar
     * 
     * @return true si no hay vecinos sin visitar, false en caso contrario
     */
    public boolean noHayVecinosSinVisitarConPaso() {
        Lista<Casilla> vecinos = obtenerVecinos();
        boolean todosVisitados = true;
        for (Casilla vecino : vecinos) {
            if (!vecino.fueVisitado() && hayPaso(vecino)) {
                todosVisitados = false;
                break;
            }
        }
        return todosVisitados;
    }



    /**
     * Obtiene un vecino al azar que no ha sido visitado
     * 
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
     * Obtiene un vecino al azar que no ha sido visitado y donde hay paso
     * 
     * @return
     */
    public Casilla obtenerVecinoDisponible() {
        Lista<Casilla> vecinos = obtenerVecinos();
        Random r = new Random();
        int rango = vecinos.longitud();
        do {
            int posicion = r.nextInt(rango);
            Casilla vecino = vecinos.getElemento(posicion);
            if (!vecino.fueVisitado() && hayPaso(vecino)) {
                return vecino;
            } else {
                rango--;
                vecinos.eliminar(vecino);
            }
        } while (!vecinos.esVacia());
        return new Casilla();
    }

    public boolean hayPaso(Casilla vecino) {

        switch (obtenerPuntoCardinal(vecino)) {
            case NORTE:
                return norte;
            case SUR:
                return sur;

            case ESTE:
                return este;
            case OESTE:
                return oeste;
            default:
                return false;
        }

    }

    /**
     * Obtiene la lista de vecinos que son adyacentes a la Casilla (que comparten
     * pared)
     * 
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
     * Marca a la Casilla ha su estado de no visitado
     */
    public void desmarcarVisitada() {
        this.visitada = false;
    }

    /**
     * Visita una Casilla adyacente si no ha sido visitada
     * 
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
     * 
     * @param casilla la Casilla adyacente
     * @return el Punto Cardinal
     */
    private PuntoCardinal obtenerPuntoCardinal(Casilla casilla) {
        if (casilla.x - x == 1) {
            return PuntoCardinal.ESTE;
        } else if (casilla.x - x == -1) {
            return PuntoCardinal.OESTE;
        } else if (casilla.y - y == 1) {
            return PuntoCardinal.NORTE;
        } else {
            return PuntoCardinal.SUR;
        }
    }

    public boolean isNorte() {
        return norte;
    }

    public void setNorte(boolean norte) {
        this.norte = norte;
    }

    public boolean isSur() {
        return sur;
    }

    public void setSur(boolean sur) {
        this.sur = sur;
    }

    public boolean isEste() {
        return este;
    }

    public void setEste(boolean este) {
        this.este = este;
    }

    public boolean isOeste() {
        return oeste;
    }

    public void setOeste(boolean oeste) {
        this.oeste = oeste;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Casilla [este=" + este + ", norte=" + norte + ", oeste=" + oeste + ", sur=" + sur + ", visitada="
                + visitada + ", x=" + x + ", y=" + y + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Casilla) {
            Casilla c = (Casilla) obj;
            if (c.x == x && c.y == y) {
                if (c.norte != norte || c.sur != sur) {
                    return false;
                }
                if (c.este != este || c.oeste != oeste) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}