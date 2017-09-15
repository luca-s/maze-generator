package Maze;

/**
 * Questa classe rappresenta una singola cella del labirinto. Una cella
 * rappresenta una posizione all'interno del labirinto ed e' collegata ad altre
 * quattro celle, una per ogni direzione (nord,sud, est,ovest) in cui ci si pu�
 * muovere. Se una delle celle adiacenti � null significa che c'e' un muro;
 */
public class MazeNode implements java.io.Serializable {
	
	/** la cella a nord di questa */
	protected MazeNode north;
	/** la cella a sud di questa */
	protected MazeNode south;
	/** la cella a ovest di questa */
	protected MazeNode west;
	/** la cella a east di questa */
	protected MazeNode east;

	/** l'indice di riga dell'array rappresentante il labirinto */
	public final int row;
	/** l'indice di colonna dell'array rappresentante il labirinto */
	public final int column;

	/**
	 * crea una cella isolata. r(row) e c(column) rappresentano la posizione
	 * della cella all'interno dell'array rappresentante il labirinto;
	 * 
	 * @param r
	 *            l'indice di riga dell'array rappresentante il labirinto
	 * @param c
	 *            l'indice di colonna dell'array rappresentante il labirinto
	 */
	public MazeNode(int r, int c) {
		north = south = west = east = null;
		row = r;
		column = c;
	}

	/**
	 * crea una cella collegata a north,south,east e west r(row) e c(column)
	 * rappresentano la posizione della cella all'interno dell'array
	 * rappresentante il labirinto;
	 * 
	 * @param r
	 *            l'indice di riga dell'array rappresentante il labirinto
	 * @param c
	 *            l'indice di colonna dell'array rappresentante il labirinto
	 * @param north
	 *            la cella che si trovera' a nord di questa
	 * @param south
	 *            la cella che si trovera' a south di questa
	 * @param west
	 *            la cella che si trovera' a west di questa
	 * @param east
	 *            la cella che si trovera' a east di questa
	 */
	public MazeNode(MazeNode north, MazeNode south, MazeNode west,
			MazeNode east, int r, int c) {
		connect(north, Direction.NORTH);
		connect(south, Direction.SOUTH);
		connect(west, Direction.WEST);
		connect(east, Direction.EAST);
		row = r;
		column = c;
	}

	/**
	 * Connette questa cella a toJoin e toJoin a questa.
	 * 
	 * @param toJoin
	 *            la cella che verra' connessa a questa
	 * @param where
	 *            dove verra' connessa la cella toJoin, where dovrebbe
	 *            utilizzare i valori definiti dalla classe direction
	 */
	public void connect(MazeNode toJoin, Direction where) {
		if (toJoin == null) {
			disconnect(where);
			return;
		}
		switch (where) {
		case NORTH:
			north = toJoin;
			toJoin.south = this;
			break;
		case SOUTH:
			south = toJoin;
			toJoin.north = this;
			break;
		case WEST:
			west = toJoin;
			toJoin.east = this;
			break;
		case EAST:
			east = toJoin;
			toJoin.west = this;
			break;

		}
	}

	/**
	 * Scollega la cella da quella indicata da where.
	 * 
	 * @param where
	 *            dove verra' scollegata la cella, where dovrebbe utilizzare i
	 *            valori dei campi definiti dalla classe direction
	 */
	public void disconnect(Direction where) {
		switch (where) {
		case NORTH:
			if (north == null)
				return;
			north.south = null;
			north = null;
			break;
		case SOUTH:
			if (south == null)
				return;
			south.north = null;
			south = null;
			break;
		case WEST:
			if (west == null)
				return;
			west.east = null;
			west = null;
			break;
		case EAST:
			if (east == null)
				return;
			east.west = null;
			east = null;
			break;

		}
	}

	/**
	 * La cella in direzione where viene posta a uguale a toJoin.
	 * 
	 * @param toJoin
	 *            il nuovo valore del campo indicato da where
	 * @param where
	 *            indica quale campo verra' posto uguale a toJoin, where
	 *            dovrebbe utilizzare i valori definiti dalla classe direction
	 */
	public void set(MazeNode toJoin, Direction where) {
		switch (where) {
		case NORTH:
			north = toJoin;
			break;
		case SOUTH:
			south = toJoin;
			break;
		case WEST:
			west = toJoin;
			break;
		case EAST:
			east = toJoin;
			break;

		}
	}

	/**
	 * ritorna la cella nella direzione indicata da where
	 * 
	 * @param where
	 *            la direzione della cella da ritornare, where dovrebbe
	 *            utilizzare i valori dei campi della classe direction
	 */
	public MazeNode nextCell(Direction where) {
		switch (where) {
		case NORTH:
			return north;
		case SOUTH:
			return south;
		case WEST:
			return west;
		case EAST:
			return east;
		}
		return null;
	}

}
