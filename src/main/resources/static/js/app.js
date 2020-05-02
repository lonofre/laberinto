/**
 * Este componente representa una casilla, sirve para actualizar los estilos 
 * si se realiza un camino de una casilla a otra
 */
let casillaComponente = {
    data: function () {
        return {

        }
    },
    props: ['casilla', 'alto', 'ancho'],
    methods: {
        seleccionarCasilla() {
            this.$emit('seleccionada', this.casilla);
        },
    },
    computed: {
        /**
        * Devuelve algunas clases de CSS para cambiar el estilo de una casilla, 
        * dependiendo de las paredes que estén abiertas
        * @param {Casilla} casilla una casilla del laberinto
        */
        estilo() {
            let estilos = [];
            let casilla = this.casilla;
            if (!casilla.norte || casilla.y === this.alto) estilos.push('norte'); else estilos.push('no-norte');
            if (!casilla.sur || casilla.y === 0) estilos.push('sur'); else estilos.push('no-sur');
            if (!casilla.este || casilla.x === this.ancho - 1) estilos.push('este'); else estilos.push('no-este');
            if (!casilla.oeste || casilla.x === 0) estilos.push('oeste'); else estilos.push('no-oeste');
            if (casilla.inicio) estilos.push('inicio');
            if (casilla.fin) estilos.push('fin');
            if (casilla.camino) estilos.push('camino')
            return estilos;
        }
    },
    template: '<div v-on:click="seleccionarCasilla" class="casilla hover:bg-purple-400"  v-bind:class="estilo"  ></div>'
}



let app = new Vue({
    el: '#app',
    data: {
        laberinto: [],
        laberintoEstilo: {
            maxHeight: '',
            maxWidth: ''
        },
        habilitado: false,
        alto: 30,
        ancho: 30,
        directorio: '',
        inicio: null,
        fin: null,
        mensajeDirectorio: '',
        mostrarMensaje: false,
    },
    components: {
        'casilla-componente': casillaComponente
    },
    methods: {

        /**
         * Establece el directorio donde se guardará la imagen
         */
        establecerDirectorio() {
            let url = `${window.location.href}/directorio`;
            let self = this;
            let data = {
                dir: this.directorio
            }
            fetch(url, {
                method: 'post',
                body: JSON.stringify(data),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(respuesta => {
                console.log(respuesta);
                if (respuesta.status == 200) {
                    self.habilitado = true;
                    self.obtenerLaberinto();
                    return respuesta.json();
                }else {
                    self.mensajeDirectorio = 'Directorio incorrecto, inténtalo de nuevo'
                }
            }).then(json => {
                self.directorio = json.directorio;
            })
        },

        /**
         * Obtiene el laberinto
         */
        obtenerLaberinto() {
            if (this.alto <= 0 || this.alto > 50 || this.ancho <= 0 || this.ancho > 50) return;
            let url = `${window.location.href}/laberinto?alto=${this.alto}&ancho=${this.ancho}`;
            let self = this;
            fetch(url).then(respuesta => {
                return respuesta.json();
            }).then(json => {
                self.laberinto = json;
            });
        },
        /**
         * Crea una imagen a partir del laberinto
         */
        crearImagen() {
            let url = `${window.location.href}/laberinto/imagen`;
            let self = this;
            fetch(url).then(respuesta => {
                if (respuesta.ok) {

                    // Para mostrar el mensaje que ha sido guardada la imagen
                    self.mostrarMensaje = true;
                    setTimeout(() => {
                        self.mostrarMensaje = false;
                    }, 3000);
                }
            })
        },
        /**
         * Fija una casilla como el inicio o el fin del camino
         * @param {Casilla} casilla 
         */
        fijarPunto(casilla) {
            if (this.inicio === null) {
                this.inicio = casilla;
                casilla.inicio = true;
                this.editarCasilla(casilla);
            } else if (this.fin === null) {
                this.fin = casilla;
                casilla.fin = true;
                this.editarCasilla(casilla);
                this.crearCamino();
            } else {
                this.inicio = null;
                this.fin = null;
                this.restablecerCasillas();
                this.fijarPunto(casilla);
            }
        },
        /**
         * Restablece las casillas a su estilo inicial
         */
        restablecerCasillas() {
            for (let i = 0; i < this.laberinto.length; i++) {
                let fila = this.laberinto[i];
                for(let j = 0; j < fila.length; j++){
                    let casilla = fila[j];
                    casilla.camino = false;
                    casilla.inicio = false;
                    casilla.fin = false;
                    fila[j] = JSON.parse(JSON.stringify(casilla))
                }
                this.$set(app.laberinto, i, fila);
            }
        },
        /**
         * Crea un camino a partir de dos casillas seleccionadas
         */
        crearCamino() {
            let url = `${window.location.href}/laberinto/camino`;
            let self = this;
            let data = {
                x1: this.inicio.x,
                y1: this.inicio.y,
                x2: this.fin.x,
                y2: this.fin.y
            }
            fetch(url, {
                method: 'post',
                body: JSON.stringify(data),
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(respuesta => {
                return respuesta.json();
            }).then(json => {
                let casillas = json;
                casillas.forEach(casilla => {
                    
                    // Esto se hace para mantener las propiedades inicio y fin es las casillas correspondientes
                    let anteriorCasilla = self.laberinto[casilla.y][casilla.x];
                    anteriorCasilla.camino = true;
                    this.editarCasilla(anteriorCasilla);
                })
            })
        },
        /**
         * Edita una casilla para que se puedan mostrar los cambios
         * @param {Casilla} nuevaCasilla 
         */
        editarCasilla(nuevaCasilla){
            let fila = this.laberinto[nuevaCasilla.y];
            fila[nuevaCasilla.x] = JSON.parse(JSON.stringify(nuevaCasilla))
            this.$set(app.laberinto, nuevaCasilla.y, fila);
        }
    },
    computed: {
        /**
         * Para dar retroalimentación al usuario si los datos ingresados son correctos
         */
        mensajeCasillas(){
            if(this.alto > 50 || this.ancho > 50){
                return "El alto y ancho del laberinto no puede superar las 50 casillas";
            } else if(this.alto < 0 || this.ancho < 0 || isNaN(this.alto) || isNaN(this.ancho)){
                return "La entrada no es válida";
            }
            return "";
        }
    }

})


