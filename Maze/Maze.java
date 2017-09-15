package Maze;

import java.util.Random;


/**
 * Questa classe genera labirinti rettangolari di dimensione width * length Il
 * labirinto viene ritornato dal metodo create() come un array bidimensionale
 * (array[length][width]) di MazeNode. L'entrata e l'uscita del labirinto
 * possono essere scelte arbitrariamente poiche' qualunque cella pu� essere
 * raggiunta da qualsiasi altra.
 */
public class Maze {

	protected static Random random = new Random();

	protected Maze() {
	}

	/**
	 * Crea un labirinto di dimensioni length * width. Le dimensioni devono
	 * essere maggiori di 2 altrimenti viene assegnato un valore di default(40)
	 * 
	 * @param length
	 *            lunghezza del labirinto
	 * @param width
	 *            larghezza del labirinto
	 * @return un labirinto rappresentato come array bidimensionale di MazeNode
	 */
	public MazeNode[][] create(int length, int width) {
		// controllo che le dimensioni passate siano almeno quelle
		// minime atrimenti assegno a length e width dei valori di default
		if (length <= 2)
			length = 40;
		if (width <= 2)
			width = 40;

		return buildMaze(length, width);
	}

	/**
	 * Crea un labirinto con le dimensioni di default 40*40
	 * 
	 * @return un labirinto rappresentato come array bidimensionale di MazeNode
	 */
	public MazeNode[][] create() {
		return create(-1, -1);
	}

	static private MazeNode[][] buildMaze(int length, int width) {

		// scelgo che algoritmo utilizzare per creare il labirinto
		// si pu� anche fare casuale ma l'2 � quello con risultati migliori
		int algorithmNumber = 2;// aCaso.nextInt(3);
		
		MazeNode[][] maze = new MazeNode[length][width];
		MazeNode[] stack = new MazeNode[length * width];
		int stackElements = 0;
		int stackBeginning = 0;
				
		int row    = random.nextInt(length);
		int column = random.nextInt(width);

		MazeNode  current = stack[stackElements++] = maze[row][column] = new MazeNode(row, column);

		while (stackElements > stackBeginning) // until there are elements in the stack
		{

			int randomInt = random.nextInt(4);
			
			// look for an unused cell next to the current one and connect it (if any)
			search:
			for (int i = 0; i < 4; i++)
			{
				Direction randomDirection = Direction.fromInt( (randomInt + i) % 4 );
				
				switch (randomDirection) {
				case NORTH:
					if (row <= 0 || maze[row - 1][column] != null)
						continue;
					row--;
					maze[row][column] = new MazeNode(null, current, null, null, row, column);
					break search;
				case SOUTH:
					if (row >= length - 1 || maze[row + 1][column] != null)
						continue;
					row++;
					maze[row][column] = new MazeNode(current, null, null, null, row, column);
					break search;
				case WEST:
					if (column <= 0 || maze[row][column - 1] != null)
						continue;
					column--;
					maze[row][column] = new MazeNode(null, null, null, current, row, column);
					break search;
				case EAST:
					if (column >= width - 1 || maze[row][column + 1] != null)
						continue;
					column++;
					maze[row][column] = new MazeNode(null, null, current, null, row, column);
					break search;
				}
			}

			// if there are no more unused cells around current one, then move to another cell
			if (current == maze[row][column])
				// in base all'algoritmo decido da che cella riprendere
				switch (algorithmNumber) {
				case 0:// utilizzo lo stack come coda
					current = stack[stackBeginning++];
					row = current.row;
					column = current.column;
					break;
				case 1:// utilizzo lo stack come stack
					stackElements--;
					if (stackElements > stackBeginning) {
						row = stack[stackElements - 1].row;
						column = stack[stackElements - 1].column;
						current = stack[stackElements - 1];
					}
					break;
				case 2:// utilizzo lo stack anche come coda a turno
					int i = random.nextInt(2);
					if (i % 2 == 0)
						current = stack[stackBeginning++];
					else
						current = stack[--stackElements - 1];
					row = current.row;
					column = current.column;
					break;
				}
			else
				current = stack[stackElements++] = maze[row][column];

		}

		return maze;
	}

}
