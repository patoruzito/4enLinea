package juego;

public class CuatroEnLinea {

	private Casillero[][] tablero;
	private String nombreJugadorRojo;
	private String nombreJugadorAmarillo;
	private int cantidadFichas;
	private boolean turnoJugadorRojo;
	private boolean ganoRojo;

	/**
	 * pre : 'filas' y 'columnas' son mayores o iguales a 4. post: empieza el
	 * juego entre el jugador que tiene fichas rojas, identificado como
	 * 'jugadorRojo' y el jugador que tiene fichas amarillas, identificado como
	 * 'jugadorAmarillo'. Todo el tablero está vacío.
	 * 
	 * @param filas
	 *            : cantidad de filas que tiene el tablero.
	 * @param columnas
	 *            : cantidad de columnas que tiene el tablero.
	 * @param nombreJugadorRojo
	 *            : nombre del jugador con fichas rojas.
	 * @param nombreJugadorAmarillo
	 *            : nombre del jugador con fichas amarillas.
	 */
	public CuatroEnLinea(int filas, int columnas, String nombreJugadorRojo,
			String nombreJugadorAmarillo) {
		if (filas < 4 || columnas < 4) {
			Error error = new Error("Cantidad de fila o columna no permitida");
			throw error;
		}
		if (nombreJugadorRojo == nombreJugadorAmarillo) {
			Error error = new Error(
					"El nombre de los jugadores no puede ser el mismo");
			throw error;
		}
		tablero = new Casillero[filas][columnas];
		turnoJugadorRojo = true;
		for (int fila = 0; fila < filas; fila++) {
			for (int columna = 0; columna < columnas; columna++) {
				tablero[fila][columna] = Casillero.VACIO;
			}
		}
		this.nombreJugadorRojo = nombreJugadorRojo;
		this.nombreJugadorAmarillo = nombreJugadorAmarillo;
		cantidadFichas = filas * columnas;
	}

	/**
	 * post: devuelve la cantidad máxima de fichas que se pueden apilar en el
	 * tablero.
	 */
	public int contarFilas() {
		int cantidadFilas = tablero.length;
		return cantidadFilas;
	}

	/**
	 * post: devuelve la cantidad máxima de fichas que se pueden alinear en el
	 * tablero.
	 */
	public int contarColumnas() {
		int cantidadColumnas = tablero[0].length;
		return cantidadColumnas;
	}

	/**
	 * pre : fila está en el intervalo [1, contarFilas()], columnas está en el
	 * intervalo [1, contarColumnas()]. post: indica qué ocupa el casillero en
	 * la posición dada por fila y columna.
	 * 
	 * @param fila
	 * @param columna
	 */
	public Casillero obtenerCasillero(int fila, int columna) {
		if (columna < 1 || columna > contarColumnas()) {
			Error error = new Error("no existe la columna");
			throw error;
		}
		if (fila < 1 || fila > contarFilas()) {
			Error error = new Error("no existe la fila");
			throw error;
		}
		return tablero[fila - 1][columna - 1];
	}

	/**
	 * pre : el juego no terminó, columna está en el intervalo [1,
	 * contarColumnas()] y aún queda un Casillero.VACIO en la columna indicada.
	 * post: deja caer una ficha en la columna indicada.
	 * 
	 * @param columna
	 */
	public void soltarFichaEnColumna(int columna) {
		if (columna == 0 || contarColumnas() < columna) {
			Error error = new Error("No existe la columna");
			throw error;
		}
		if (termino()) {
			Error error = new Error(
					"El juego ya termino, ya no se pueden arrojar fichas");
			throw error;
		}
		boolean sePudoColocarFicha = false;
		int filaDondeEmpieza = contarFilas() - 1;
		int posicionAcolocar = 0;
		//Se verifica si se puede tirar una ficha
		while (!sePudoColocarFicha && filaDondeEmpieza >= 0) {
			if (tablero[filaDondeEmpieza][columna - 1] == Casillero.VACIO) {
				sePudoColocarFicha = true;
				cantidadFichas--;
				posicionAcolocar = filaDondeEmpieza;
			}
			filaDondeEmpieza--;
		}
		// Si no se pudo arrojar ficha, es porque no hay casillero donde arrojar ; y tira este error:
		if (!sePudoColocarFicha) {
			Error error = new Error(
				"No se puede arreojar mas fichas en la columna");
			throw error;
		} else {
			if (turnoJugadorRojo) {
				tablero[posicionAcolocar][columna - 1] = Casillero.ROJO;
				turnoJugadorRojo = false;
			} else {
				tablero[posicionAcolocar][columna - 1] = Casillero.AMARILLO;
				turnoJugadorRojo = true;
			}
		}
	}

	/**
	 * post: indica si el juego terminó porque uno de los jugadores ganó o no
	 * existen casilleros vacíos.
	 */
	public boolean termino() {
		return (cantidadFichas == 0 || hayCombinacion());
	}

	/**
	 * post: indica si el juego terminó y tiene un ganador.
	 */
	public boolean hayGanador() {
		return (hayCombinacion());
	}

	/**
	 * pre : el juego terminó. post: devuelve el nombre del jugador que ganó el
	 * juego.
	 */
	public String obtenerGanador() {
		String ganador = null;
		if (hayGanador()) {
			if (ganoRojo && hayGanador()) {
				ganador = nombreJugadorRojo;
			} else {
				ganador = nombreJugadorAmarillo;
			}
		}
		return ganador;
	}

	// -------------------------------ZONA METODOS  PRIVADOS-------------------------------
	// Recorre el tablero para ver si hay combinaciones 
	private boolean hayCombinacion() {
		for (int fila = contarFilas() - 1; fila >= 0; fila--) {
			for (int columna = 0; columna < contarColumnas() - 1; columna++) {
				if (combinacionRojaHorizontal(fila, columna)
						|| combinacionRojaDiagonales(fila, columna)
						|| combinacionRojaVertical(fila, columna)) {
					return true;
				}
				if (combinacionAmarillaHorizontal(fila, columna)
						|| combinacionAmarillaDiagonales(fila, columna)
						|| combinacionAmarillaVertical(fila, columna)) {
					return true;
				}
			}
		}
		return false;
	}

	// Metodos de ombinaciones rojas
	private boolean combinacionRojaHorizontal(int fila, int columna) {// funciona
		if (fila >= 0 && fila < contarFilas() && columna >= 0
				&& columna < contarColumnas() && columna + 3 < contarColumnas()
				&& tablero[fila][columna] == Casillero.ROJO
				&& tablero[fila][columna + 1] == Casillero.ROJO
				&& tablero[fila][columna + 2] == Casillero.ROJO
				&& tablero[fila][columna + 3] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		}
		return false;
	}

	private boolean combinacionRojaVertical(int fila, int columna) {// funciona
		if (fila + 3 < contarFilas() && fila >= 0 && fila < contarFilas()
				&& columna >= 0 && columna < contarColumnas()
				&& tablero[fila][columna] == Casillero.ROJO
				&& tablero[fila + 1][columna] == Casillero.ROJO
				&& tablero[fila + 2][columna] == Casillero.ROJO
				&& tablero[fila + 3][columna] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		} else {
			return false;
		}
	}

	private boolean combinacionRojaDiagonales(int fila, int columna) {
		if (fila-3 >= 0&&fila>=0
				&& fila < contarFilas()
				&& columna + 3 < contarColumnas()
				&& tablero[fila][columna] == Casillero.ROJO// funciona
				&& tablero[fila - 1][columna + 1] == Casillero.ROJO
				&& tablero[fila - 2][columna + 2] == Casillero.ROJO
				&& tablero[fila - 3][columna + 3] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		}
		if (fila-3 >= 0 && columna-3>= 0 &&fila<contarFilas()&&columna<contarColumnas()
				&& tablero[fila][columna] == Casillero.ROJO
				&& tablero[fila - 1][columna - 1] == Casillero.ROJO
				&& tablero[fila - 2][columna - 2] == Casillero.ROJO
				&& tablero[fila - 3][columna - 3] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		}
		if (fila>=0 && fila + 3 < contarFilas()
				&& columna-3 >= 0
				&& columna < contarColumnas()// funciona
				&& tablero[fila][columna] == Casillero.ROJO
				&& tablero[fila + 1][columna - 1] == Casillero.ROJO
				&& tablero[fila + 2][columna - 2] == Casillero.ROJO
				&& tablero[fila + 3][columna - 3] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		}
		if (fila + 3 < contarFilas()
				&& columna >= 0
				&& columna+3 < contarColumnas()// funciona
				&& tablero[fila][columna] == Casillero.ROJO
				&& tablero[fila + 1][columna + 1] == Casillero.ROJO
				&& tablero[fila + 2][columna + 2] == Casillero.ROJO
				&& tablero[fila + 3][columna + 3] == Casillero.ROJO) {
			ganoRojo = true;
			return true;
		}
		return false;
	}

	// Metodos combinaciones amarillas
	private boolean combinacionAmarillaHorizontal(int fila, int columna) {// funciona
		if (fila >= 0 && fila <= contarFilas() - 1
				&& columna + 3 < contarColumnas() && columna >= 0
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila][columna + 1] == Casillero.AMARILLO
				&& tablero[fila][columna + 2] == Casillero.AMARILLO
				&& tablero[fila][columna + 3] == Casillero.AMARILLO) {
			return true;
		}
		return false;
	}

	private boolean combinacionAmarillaVertical(int fila, int columna) {// funciona
		if (fila - 3 >= 0 && columna >= 0 && columna < contarColumnas()
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila - 1][columna] == Casillero.AMARILLO
				&& tablero[fila - 2][columna] == Casillero.AMARILLO
				&& tablero[fila - 3][columna] == Casillero.AMARILLO) {
			return true;
		}
		return false;

	}

	private boolean combinacionAmarillaDiagonales(int fila, int columna) {
		if (fila >= 3
				&& fila <= contarFilas()
				&& columna + 3 < contarColumnas()// funciona
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila - 1][columna + 1] == Casillero.AMARILLO
				&& tablero[fila - 2][columna + 2] == Casillero.AMARILLO
				&& tablero[fila - 3][columna + 3] == Casillero.AMARILLO) {
			return true;
		}
		if (fila + 3 < contarFilas()
				&& columna >= 3
				&& columna < contarColumnas()// funciona
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila + 1][columna - 1] == Casillero.AMARILLO
				&& tablero[fila + 2][columna - 2] == Casillero.AMARILLO
				&& tablero[fila + 3][columna - 3] == Casillero.AMARILLO) {
			return true;
		}
		if (fila >= 3 && fila < contarFilas() && columna >= 3
				&& columna < contarColumnas()
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila - 1][columna - 1] == Casillero.AMARILLO
				&& tablero[fila - 2][columna - 2] == Casillero.AMARILLO
				&& tablero[fila - 3][columna - 3] == Casillero.AMARILLO) {
			return true;
		}
		if (fila + 3 < contarFilas()
				&& columna >= 0
				&& columna < contarColumnas()//  no funciona
				&& tablero[fila][columna] == Casillero.AMARILLO
				&& tablero[fila + 1][columna + 1] == Casillero.AMARILLO
				&& tablero[fila + 2][columna + 2] == Casillero.AMARILLO
				&& tablero[fila + 3][columna + 3] == Casillero.AMARILLO) {
			return true;
		}
		return false;
	}

}

