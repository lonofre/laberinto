package mx.unam.fciencias.edd.laberinto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * LaberintoImage produce imagenes del Laberinto
 */
public class LaberintoImage {

    /**
     * La longitud en px de cada casilla
     */
    private static int longitudCasilla = 50;

    /**
     * La longitud del borde de la Casilla, que hace referenncia a la pared
     */
    private static int borde = 5;
    
    /**
     * Cadena para generar un String aleatorio
     */
    static private final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static  private SecureRandom rnd = new SecureRandom();

    /**
     * Genera una cadena aleatoria
     * @param longitud la longitud de la cadena
     * @return un String generado aleatoriamente
     */
    static private String randomString(int longitud) {
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * Genera una imagen que representa al Laberinto
     * @param laberinto Laberinto a representar
     * @param directorio un String con el directorio donde se va a guardar la imagen
     * @throws IOException si no se puede crear la imagen
     */
    public static void hacerImagen(Laberinto laberinto, String directorio) throws IOException {
        int alto = (laberinto.getAlto() * (longitudCasilla + borde)) + borde;
        int ancho = (laberinto.getAncho() * (longitudCasilla + borde)) + borde;
        BufferedImage bufferedImage = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        // Para llenar todo de negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ancho, alto);

        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 30, 30);

        for (int y = 0; y < laberinto.getAlto(); y++) {
            for (int x = 0; x < laberinto.getAncho(); x++) {
                Casilla casilla = laberinto.obtenerCasilla(x, y);

                int xPosicion = x*(longitudCasilla + borde) + borde;
                int yPosicion = y*(longitudCasilla + borde) + borde;
                int anchoCasilla = longitudCasilla;
                int altoCasilla = longitudCasilla;

                /*
                    // Se hace para simular que hay un borde, lo que hace es extender 
                    el tamaño de la casilla al restar la posición y compensar el tamaño añadiendole lo restado
                */
                if(casilla.isSur()){
                    yPosicion -= borde;
                    altoCasilla += borde;
                }
                if(casilla.isNorte()){
                    altoCasilla += borde;
                }
                if(casilla.isOeste()){
                    xPosicion -= borde;
                    anchoCasilla += borde;
                }
                if(casilla.isEste()){
                    anchoCasilla += borde;
                }

                g2d.setColor(Color.WHITE);
                g2d.fillRect(xPosicion, yPosicion, anchoCasilla, altoCasilla);
            }
        }

        g2d.dispose();

        File file = new File(directorio + "/ "+ randomString(10)+".png");
        ImageIO.write(bufferedImage, "png", file);
    }

}