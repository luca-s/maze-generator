package Maze;

import java.util.ArrayList;
import java.util.List;
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

		// Create an empty initial maze, we will add a cell a time
		MazeNode[][] maze = new MazeNode[length][width];
		List<MazeNode> visitedCells = new ArrayList<MazeNode>();

		// Start building the maze from a random cell
		int row    = random.nextInt(length);
		int column = random.nextInt(width);
		
		MazeNode newCell = new MazeNode(row, column);
		int currentIdx = -1;
		
		// until we have visited all the cells in the maze
		while (visitedCells.size() < length * width)
		{
			
			if (newCell != null)
			{
				// Save the last built cell
				maze[newCell.row][newCell.column] = newCell;
				visitedCells.add(newCell);
				
				// Use last cell as current one
				currentIdx = visitedCells.size() -1;
			}
			else
			{
				currentIdx--;
			}
			
			if (currentIdx < 0)
			{
				// Pick a random cell from the already visited ones
				currentIdx = random.nextInt(visitedCells.size());
			}
			
			MazeNode current = visitedCells.get(currentIdx);
			newCell = null;

			/*
			 * look for an unused cell next to the current one and connect to it
			 * (if any)
			 */
			Direction randomDirection = Direction.fromInt(random.nextInt(4));
			boolean clowise = random.nextInt(2) == 0;

			for (int i = 0; i < 4; i++) // look around in the 4 directions
			{
				row    = current.row;
				column = current.column;
				
				if (randomDirection == Direction.NORTH)
				{
					row--;
					if (row < 0 || maze[row][column] != null)
						continue;
					newCell = new MazeNode(row, column);
					current.connect(newCell, Direction.NORTH);
					break;
				}
				else if (randomDirection == Direction.SOUTH)
				{
					row++;
					if (row >= length || maze[row][column] != null)
						continue;
					newCell = new MazeNode(row, column);
					current.connect(newCell, Direction.SOUTH);
					break;
				}
				else if (randomDirection == Direction.WEST)
				{
					column--;
					if (column < 0 || maze[row][column] != null)
						continue;
					newCell = new MazeNode(row, column);
					current.connect(newCell, Direction.WEST);
					break;
				}
				else if (randomDirection == Direction.EAST)
				{
					column++;
					if (column >= width || maze[row][column] != null)
						continue;
					newCell = new MazeNode(row, column);
					current.connect(newCell, Direction.EAST);
					break;
				}

				// look around
				if (clowise)
					randomDirection = randomDirection.right();
				else
					randomDirection = randomDirection.left();
			}
								
		}

		return maze;
	}

}
