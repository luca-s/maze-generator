package Maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;

/**
 * Questo componente da una rappresentazione di un labirinto creato con il
 * metodo newMaze. Per disegnare il labirinto bisogna definire il metodo
 * drawMaze che viene chiamato quando il labirinto subisce qualche modifica o
 * quando il componente viene ridimensionato. Il metodo paint si occupa di
 * disegnare il buffer al centro del componente, quindi drawMaze dovr� disegnare
 * nel buffer e aggiornare opportunamente i campi bufferWidth e bufferHeight.
 * Dovr� inoltre chiamare il metodo repaint per visualizzare quanto disegnato.
 */
public abstract class graphicMaze extends JPanel {
	
	/** la classe che utilizzo per generare i labirinti */
	protected Maze m = new Maze();
	/** Il labirinto creato con la classe Maze */
	protected MazeNode[][] maze;
	/** la larghezza del labirinto */
	protected int mazeWidth = 30;
	/** la lunghezza del labirinto */
	protected int mazeLength = 30;
	/** posizione corrente del giocatore */
	protected MazeNode player;
	/**
	 * direzione in cui e' rivolto il giocatore, si dovrebbero utilizzare i
	 * valori della classe direction
	 */
	protected Direction lookAt = Direction.EAST;
	/** uscita del labirinto */
	protected MazeNode exit;
	/** un buffer in cui disegnare il labirinto */
	protected Image buffer;
	/** la larghezza dell'immagine di buffer */
	protected int bufferWidth = 600;
	/** l'altezza dell'immagine di buffer */
	protected int bufferHeight = 450;
	/**
	 * dovrebbe essere utilizzato per poter disegnare con il graphics del buffer
	 */
	protected Graphics b;
	/** colore di sfondo del componente */
	protected Color backgroundColor = Color.white;

	/** Crea un componente graphicMaze in cui non c'e' ancora un labirinto. */
	public graphicMaze() {
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				drawMaze();
			}
		});
	}

	/**
	 * Disegna il buffer nel componente. Se il buffer pu� essere disegnato
	 * completamente esso viene posto al centro del componente. Per stabilire le
	 * dimensioni del buffer si fa riferimento ai campi bufferWidth e
	 * bufferHeight.
	 */
	public void paint(Graphics g) {

		super.paint(g);
		if (buffer == null)
			return;

		if (player == exit && lookAt == Direction.EAST)
			printMessage(b, "You are at the Exit", bufferWidth, bufferHeight / 2);

		int x = (getWidth() - bufferWidth) / 2;
		if (x < 0)
			x = 0;
		int y = (getHeight() - bufferHeight) / 2;
		if (y < 0)
			y = 0;

		g.drawImage(buffer, x, y, null);

	}

	/**
	 * Scrive la stringa s al centro del contesto grafico g.
	 * 
	 * @param g
	 *            dove verra' scritto s
	 * @param s
	 *            cio' che voglio scrivere
	 * @param x
	 *            la larghezza dell'immagine in cui si deve scrivere
	 * @param y
	 *            a che altezza voglio scrivere s
	 */
	protected void printMessage(Graphics g, String s, int x, int y) {
		if (g == null)
			return;
		x = (x - g.getFontMetrics().stringWidth(s)) / 2;
		if (x < 0)
			return;
		g.setColor(Color.blue);
		g.drawString(s, x - 1, y);
		g.drawString(s, x + 1, y);
		g.drawString(s, x, y - 1);
		g.drawString(s, x, y + 1);
		g.setColor(Color.white);
		g.drawString(s, x, y);
	}

	protected abstract void drawMaze();

	/**
	 * Setta il campo backgroundColor a c e chiama drawMaze().
	 * 
	 * @param c
	 *            il nuovo colore dello sfondo
	 */
	public void setColor(Color c) {
		backgroundColor = c;
		drawMaze();
	}

	/**
	 * Setta i campi di questo graphicMaze come quelli di gm in modo che questo
	 * componente condivida lo stesso labirinto di gm. Viene chiamato il metodo
	 * drawMaze per disegnare il labirinto.
	 * 
	 * @param gm
	 *            il graphicMaze di cui vogliamo rappresentare il labirinto
	 */
	public void setFromMaze(graphicMaze gm) {
		maze = gm.maze;
		mazeWidth = gm.mazeWidth;
		mazeLength = gm.mazeLength;
		player = gm.player;
		lookAt = gm.lookAt;
		exit = gm.exit;
		setColor(gm.backgroundColor);
		drawMaze();
	}

	/**
	 * Permette di selezionare la classe che genera i labirinti. Se m e' uguale
	 * a null non viene cambiato il generatore di labirinti.
	 * 
	 * @param m
	 *            il nuovo generatore di labirinti
	 */
	public void setMazeBuilder(Maze m) {
		if (m != null)
			this.m = m;
	}

	/**
	 * Crea un nuovo labirinto e lo disegna chiamando il metodo drawMaze().
	 * Width e length rappresenano il numero di celle del labirinto in larghezza
	 * per lunghezza e non la dimensione in pixel. Ovvero verra' creato un
	 * labirinto maze[length][width] con la classe Maze.
	 * 
	 * @param width
	 *            larghezza del labirinto
	 * @param length
	 *            lunghezza del labirinto
	 */
	public void newMaze(int width, int length) {

		if (width < 3)
			width = 30;
		if (length < 3)
			width = 30;
		mazeWidth = width;
		mazeLength = length;
		maze = m.create(mazeLength, mazeWidth);
		player = maze[0][0];
		lookAt = Direction.EAST;
		exit = maze[(int) (Math.random() * (double) mazeLength)][mazeWidth - 1];
		exit.east = exit;
		drawMaze();
	}

	/** Sposta il giocatore nella cella in direzione lookAt se non c'e' un muro */
	protected void move() {
		if (player.nextCell(lookAt) != null)
		{
			player = player.nextCell(lookAt);
		}
	}

}
