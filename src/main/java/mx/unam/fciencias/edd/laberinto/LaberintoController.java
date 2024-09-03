package mx.unam.fciencias.edd.laberinto;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LaberintoController es un controlador que genera Laberintos y crea imagenes a partir de ellos
 */
@Controller
public class LaberintoController {

    private static String directorio;
    private static Laberinto laberinto;

    /**
     * Redirecciona a la página de inicio
     * @return el template de la página de inicio
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Asigna el directorio donde se va a guardar la imagen que se genere a partir del Laberinto
     * @param requestBody un JSON con atributo dir que indica el directorio donde se guardarán las imagenes creadas
     * @return ResponseEntity con estatus OK si el directorio es válido, BAD_REQUEST en otro caso
     */
    @PostMapping(value = "/directorio", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> directorio(@RequestBody JsonNode requestBody) {
        String dir = requestBody.get("dir").asText();
        File archivo = new File(dir);
        JsonObject json = new JsonObject();
        if (archivo.exists()) {
            if (archivo.isDirectory()) {
                directorio = archivo.getAbsolutePath();
                json.addProperty("directorio", directorio);

                return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
            }
        }
        json.addProperty("error", "El directorio no es válido");
        return new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Crea una imagen en base a un laberinto que ya ha sido creada anteriormente
     * @return un ResponseEntity con estatus OK si se pudo crear la imagen, BAD_REQUEST en otro caso
     */
    @GetMapping(value = "/laberinto/imagen", produces = "application/json")
    public ResponseEntity<String> crearImagen() {
        if (laberinto == null || directorio == "") {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            LaberintoImage.hacerImagen(laberinto, directorio);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Crea un camino a partir de un par de coordenadas, las cuales son el inicio y el fin. Debido a la forma en que se creo el laberinto, sólo hay un
     * solo posible camino
     * @param requestBody un JSON que contiene las coordenadas de la Casilla de inicio y la Casilla de fin
     * @return un ResponseEntity que contiene un arreglo de Casilla que indica las casiilas que hay que recorrer
     * para llegar al inicio y al fin
     */
    @RequestMapping(value = "/laberinto/camino", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Casilla[]> camino(@RequestBody JsonNode requestBody) {
        if (laberinto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Para obtener los valores del json
        int x1 = requestBody.get("x1").asInt();
        int y1 = requestBody.get("y1").asInt();
        int x2 = requestBody.get("x2").asInt();
        int y2 = requestBody.get("y2").asInt();

        Casilla[] camino = laberinto.obtenerCamino(x1, y1, x2, y2);
        return new ResponseEntity<Casilla[]>(camino, HttpStatus.OK);
    }

    /**
     * Crea el Laberinto y devuelve sus Casillas, para que pueda ser leído
     * @param ancho el número de casillas que tiene en el ancho
     * @param alto el número de casillas que tiene en el alto
     * @return un arreglo de Casillas que representa al laberinto
     */
    @RequestMapping(value = "/laberinto", method = RequestMethod.GET , produces = "application/json")
    public ResponseEntity<Casilla[][]> laberinto(@RequestParam Integer ancho, @RequestParam Integer alto){
        if(ancho > 50 || ancho <= 0 || alto > 50 || alto <= 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        laberinto = new Laberinto(alto,ancho);
        return new ResponseEntity<Casilla[][]>(laberinto.getCasillas(), HttpStatus.ACCEPTED);
    }



}