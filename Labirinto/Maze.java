package Labirinto;

import java.util.*;

/**
  *Questa classe genera labirinti rettangolari di dimensione width * length
  * Il labirinto viene ritornato dal metodo create() come un array bidimensionale (array[length][width])
  * di MazeNode.
  * L'entrata e l'uscita del labirinto possono essere scelte arbitrariamente
  * poiche' qualunque cella può essere raggiunta da qualsiasi altra.
  */
public class Maze{

 /**generatore di numeri casuali*/
 protected static Random aCaso = new Random();

 protected Maze(){}//costruttore

/**Crea un labirinto di dimensioni length * width. Le dimensioni
  *devono essere maggiori di 2 altrimenti viene assegnato
  *un valore di default(40)
  * @param length 	lunghezza del labirinto
  * @param width		larghezza del labirinto
  * @return              un labirinto rappresentato come array bidimensionale di MazeNode
  */
 public MazeNode[][] create(int length, int width){
 	//controllo che le dimensioni passate siano almeno quelle
	//minime atrimenti assegno a length e width dei valori di default
 	if(length<=2) length = 40;
	if(width<=2) width = 40;

	return buildMaze(length,width);
 }

/**Crea un labirinto con le dimensioni di default 40*40
  *
  *@return              un labirinto rappresentato come array bidimensionale di MazeNode
  */
 public MazeNode[][] create(){
		return create(-1,-1);
 }

 	//e' il metodo che effettivamente crea il labirinto
 static private MazeNode[][] buildMaze(int length, int width){

		//il labirinto da ritornare
	MazeNode [][] maze = new MazeNode[length][width];
		//il mio stack
	MazeNode []stack = new MazeNode[length*width];
	int stackElements = 0;
	int stackBeginning = 0;
	//scelgo che algoritmo utilizzare per creare il labirinto
	//si può anche fare casuale ma l'2 è quello con risultati migliori
	int algorithmNumber = 2;//aCaso.nextInt(3);
	//current si sposta nel grafo per creare le strade del labirinto
	MazeNode current = null;
	int ran,i;

	int row = aCaso.nextInt(length);
        int column = aCaso.nextInt(width);

	current = stack[stackElements++] = maze[row][column] = new MazeNode(row,column);

	while( stackElements > stackBeginning ){//finchè ci sono elementi nello stack

		ran = aCaso.nextInt(4);
  	//cerco una cella vicina alla corrente che non sia gia' stata "scavata"
  	// e la collego a quella corrente
		search:
		for(i=0;i<4;i++)
			switch((ran+i)%4){
				case direction.north :
					if( row <= 0 ||
					   maze[row-1][column] != null) continue;
					row--;
					maze[row][column] = new MazeNode(null,current,null,null,row,column);
					break search;
				case direction.south :
					if( row >= length-1 ||
					   maze[row+1][column] != null) continue;
					row++;
					maze[row][column] = new MazeNode(current,null,null,null,row,column);
					break search;
				case direction.west :
					if( column <= 0 ||
					   maze[row][column-1] != null) continue;
					column--;
					maze[row][column] = new MazeNode(null,null,null,current,row,column);
					break search;
				case direction.east :
					if( column >= width-1 ||
					   maze[row][column+1] != null) continue;
					column++;
					maze[row][column] = new MazeNode(null,null,current,null,row,column);
					break search;
				}


		//se non ci sono più celle libere adiacenti
		//scelgo un'altra cella da cui riprendere a "scavare"
		if( current == maze[row][column] )
		//in base all'algoritmo decido da che cella riprendere
		   switch(algorithmNumber){
		   	case 0://utilizzo lo stack come coda
				current = stack[stackBeginning++];
				row = current.row;
				column = current.column;
				break;
			case 1://utilizzo lo stack come stack
				stackElements--;
				if(stackElements > stackBeginning){
					row = stack[stackElements-1].row;
					column = stack[stackElements-1].column;
					current = stack[stackElements-1];
				} break;
			case 2://utilizzo lo stack anche come coda a turno
				if(i%2==0)
					current = stack[stackBeginning++];
				else
					current = stack[--stackElements-1];
				row = current.row;
				column = current.column;
				break;
			}
		else	current = stack[stackElements++] = maze[row][column];

	}

	return maze;
 }

}
