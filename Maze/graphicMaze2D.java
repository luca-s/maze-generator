package Maze;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Disegna un labirinto dall'alto e permette di muoversi al suo interno con le
 * frecce direzionali.
 */

public class graphicMaze2D extends graphicMaze implements KeyListener,
		java.io.Serializable {
	/**
	 * Mantiene il valore corrente dello zoom. Lo zoom viene utilizzato per
	 * disegnare il labirinto in proporzione alle dimensioni del componente.
	 */
	protected int zoom = 20;

	/**
	 * Crea un componente inizialmente privo di labirinto.
	 */
	public graphicMaze2D() {
		setColor(backgroundColor);
	}

	protected void drawMaze() {// disegno la vista dall'alto

		if (maze == null)
			return;

		int x = getWidth() / mazeWidth;
		int y = getHeight() / mazeLength;

		zoom = x < y ? x : y;

		bufferWidth = zoom * mazeWidth + 1;
		bufferHeight = zoom * mazeLength + 1;

		buffer = new BufferedImage(bufferWidth, bufferHeight,
				BufferedImage.TYPE_INT_RGB);
		b = buffer.getGraphics();

		// coloro lo sfondo
		b.setColor(backgroundColor);
		b.fillRect(0, 0, zoom * mazeWidth + 1, zoom * mazeLength + 1);
		// disegno il labirinto
		b.setColor(Color.black);
		for (y = 0; y < mazeLength; y++)
			for (x = 0; x < mazeWidth; x++) {

				if (maze[y][x] == null)
					continue;

				if (maze[y][x].north == null)// se c'e' il muro nord lo disegno
					b.drawLine(x * zoom, y * zoom, (x + 1) * zoom, y * zoom);
				if (maze[y][x].south == null)// se c'e' il muro sud lo disegno
					b.drawLine(x * zoom, (y + 1) * zoom, (x + 1) * zoom,
							(y + 1) * zoom);
				if (maze[y][x].west == null)// se c'e' il muro ovest lo disegno
					b.drawLine(x * zoom, y * zoom, x * zoom, (y + 1) * zoom);
				if (maze[y][x].east == null)// se c'e' il muro est lo disegno
					b.drawLine((x + 1) * zoom, y * zoom, (x + 1) * zoom,
							(y + 1) * zoom);
			}
		// disegno il giocatore
		if (player != null) {
			b.setXORMode(Color.red);
			b.fillRect(player.column * zoom + 1, player.row * zoom + 1,
					zoom - 1, zoom - 1);
			b.setPaintMode();
		}

		repaint();
	}

	/**
	 * Disegna il percorso per trovare l'uscita dall'inizio del labirinto o
	 * dalla posizione corrente del giocatore.
	 * 
	 * @param fromBeginning
	 *            se � vero verr� trovata la strada dall'inizio altrimenti da
	 *            dove si trova il giocatore
	 */
	public void findExit(boolean fromBeginning) {

		if (maze == null)
			return;

		setColor(backgroundColor);
		b.setXORMode(Color.magenta);
		DepthFirstSearch(null, fromBeginning ? maze[0][0] : player);
		b.setPaintMode();

		repaint();
	}

	private boolean DepthFirstSearch(MazeNode prew, MazeNode current) {

		if (current == null)
			return false;

		boolean found = false;
		
		if (current == exit)
			found = true;
		
		if (!found && current.north != prew)
			found = DepthFirstSearch(current, current.north);
		
		if (!found && current.south != prew)
			found = DepthFirstSearch(current, current.south);
			
		if (!found && current.west != prew)
			found = DepthFirstSearch(current, current.west);
		
		if (!found && current.east != prew)
			found = DepthFirstSearch(current, current.east);

		if (found) // if I am part of the exit path, colour myself
		{
			b.fillRect(current.column * zoom, current.row * zoom, zoom, zoom);
		}
		
		return found;
	}

	// gestione degli eventi
	/**
	 * Quando vengono premute le frecce direzionali viene cancellata la vecchia
	 * posizione del giocatore e viene disegnata quella nuova
	 */
	public void keyPressed(KeyEvent e) {

		if (player == exit && lookAt == Direction.EAST)
			return;
		// cancello la vecchia posizione del giocatore
		b.setXORMode(Color.red);
		b.fillRect(player.column * zoom + 1, player.row * zoom + 1, zoom - 1,
				zoom - 1);

		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			lookAt = Direction.SOUTH;
			break;
		case KeyEvent.VK_LEFT:
			lookAt = Direction.WEST;
			break;
		case KeyEvent.VK_RIGHT:
			lookAt = Direction.EAST;
			break;
		case KeyEvent.VK_UP:
			lookAt = Direction.NORTH;
			break;
		}

		move();

		// disegno la nuova posizione del giocatore
		b.fillRect(player.column * zoom + 1, player.row * zoom + 1, zoom - 1,
				zoom - 1);
		b.setPaintMode();

		repaint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

}
