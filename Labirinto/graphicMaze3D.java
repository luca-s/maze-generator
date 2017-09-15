package Labirinto;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
 * Disegna un labirinto dal suo interno e permette di muoversi con le frecce
 * direzionali
 */
public class graphicMaze3D extends graphicMaze implements KeyListener,
		java.io.Serializable {

	private Color[] color = new Color[4];
	private String where[] = { "NORTH", "EAST", "SOUTH", "WEST" };

	/**
	 * Crea un componente inizialmente privo di labirinto. Fino a che non viene
	 * invocato il metodo newMaze il componente visualizza l'immagine
	 * "maze2.jpg"
	 */
	public graphicMaze3D() {
		setColor(backgroundColor);
		Toolkit t = Toolkit.getDefaultToolkit();
		buffer = t.getImage("./maze2.jpg");
		MediaTracker m = new MediaTracker(this);
		m.addImage(buffer, 1);
		try {
			m.waitForAll();
		} catch (InterruptedException e) {
		}
	}

	protected void drawMaze() {

		if (maze == null)
			return;
		// creo un buffer quadrato scegliendo la dimensione pi�
		// restrittiva tra la larghezza del componenete e la sua altezza
		bufferHeight = bufferWidth = getWidth() < getHeight() ? getWidth()
				: getHeight();
		buffer = new BufferedImage(bufferWidth, bufferWidth,
				BufferedImage.TYPE_INT_RGB);

		b = buffer.getGraphics();
		updateMaze();
	}

	/** Disegna la vista corrente. */
	private void updateMaze() {
		// disegno il cielo e il pavimento
		b.setColor(Color.cyan);
		b.fillRect(0, 0, bufferWidth, bufferWidth / 2);
		b.setColor(Color.green);
		b.fillRect(0, bufferWidth / 2, bufferWidth, bufferWidth / 2);

		// in base a dove � rivolto il giocatore definisco i
		// colori per le parati di sinistra di destra e di fronte
		Color cfront = color[lookAt];
		Color cleft = color[(lookAt + 3) % 4];
		Color crigth = color[(lookAt + 1) % 4];

		MazeNode current = player;
		MazeNode leftSide = null;
		MazeNode rightSide = null;

		int cellSide = bufferWidth * 3 / 10;
		int insideCell = cellSide / 4;
		int[] lx = { -insideCell, -insideCell, -insideCell, -insideCell };
		int[] ly = { -insideCell, -insideCell, bufferWidth + insideCell,
				bufferWidth + insideCell };
		int[] rx = { bufferWidth + insideCell, bufferWidth + insideCell,
				bufferWidth + insideCell, bufferWidth + insideCell };
		int[] ry = { -insideCell, -insideCell, bufferWidth + insideCell,
				bufferWidth + insideCell };
		int z = 2;

		while (current != null) {

			leftSide = current.nextCell((lookAt + 3) % 4);
			rightSide = current.nextCell((lookAt + 1) % 4);

			// disegno la parete sinistra
			if (leftSide == null) {// se c'e' il muro
				lx[0] = lx[1];
				ly[0] = ly[1];
				lx[1] += cellSide / z;
				ly[1] += cellSide / z;
				lx[3] = lx[2];
				ly[3] = ly[2];
				lx[2] += cellSide / z;
				ly[2] -= cellSide / z;
				if (ly[1] > bufferWidth / 2)
					ly[1] = ly[2] = lx[1] = lx[2] = bufferWidth / 2;
				b.setColor(cleft);
				b.fillPolygon(lx, ly, 4);
			} else {// se non c'e' muro
				lx[0] = lx[1];
				lx[1] += cellSide / z;
				ly[1] += cellSide / z;
				ly[0] = ly[1];
				lx[3] = lx[2];
				lx[2] += cellSide / z;
				ly[2] -= cellSide / z;
				ly[3] = ly[2];
				if (ly[1] > bufferWidth / 2)
					ly[1] = ly[2] = lx[1] = lx[2] = ly[0] = ly[3] = bufferWidth / 2;
				b.setColor(cfront);
				b.fillPolygon(lx, ly, 4);
			}
			// disegno lo spessore dei muri
			lx[0] = lx[1];
			ly[0] = ly[1];
			lx[1] += cellSide / 4 / z;
			ly[1] += cellSide / 4 / z;
			lx[3] = lx[2];
			ly[3] = ly[2];
			lx[2] += cellSide / 4 / z;
			ly[2] -= cellSide / 4 / z;
			if (ly[1] > bufferWidth / 2)
				ly[1] = ly[2] = lx[1] = lx[2] = bufferWidth / 2;

			b.setColor(cleft);
			b.fillPolygon(lx, ly, 4);

			// disegno la parete destra
			if (rightSide == null) {// se c'e' il muro
				rx[0] = rx[1];
				ry[0] = ry[1];
				rx[1] -= cellSide / z;
				ry[1] += cellSide / z;
				rx[3] = rx[2];
				ry[3] = ry[2];
				rx[2] -= cellSide / z;
				ry[2] -= cellSide / z;
				if (ry[1] > bufferWidth / 2)
					ry[1] = ry[2] = rx[1] = rx[2] = bufferWidth / 2;
				b.setColor(crigth);
				b.fillPolygon(rx, ry, 4);
			} else {// se non c'e' il muro
				rx[0] = rx[1];
				rx[1] -= cellSide / z;
				ry[1] += cellSide / z;
				ry[0] = ry[1];
				rx[3] = rx[2];
				rx[2] -= cellSide / z;
				ry[2] -= cellSide / z;
				ry[3] = ry[2];
				if (ry[1] > bufferWidth / 2)
					ry[1] = ry[2] = rx[1] = rx[2] = ry[0] = ry[3] = bufferWidth / 2;
				b.setColor(cfront);
				b.fillPolygon(rx, ry, 4);
			}
			// disegno lo spessore dei muri
			rx[0] = rx[1];
			ry[0] = ry[1];
			rx[1] -= cellSide / 4 / z;
			ry[1] += cellSide / 4 / z;
			rx[3] = rx[2];
			ry[3] = ry[2];
			rx[2] -= cellSide / 4 / z;
			ry[2] -= cellSide / 4 / z;
			if (ry[1] > bufferWidth / 2)
				ry[1] = ry[2] = rx[1] = rx[2] = bufferWidth / 2;

			b.setColor(crigth);
			b.fillPolygon(rx, ry, 4);

			current = current.nextCell(lookAt);
			if (current == exit && lookAt == direction.east) {
				cfront = Color.black;
				break;
			}
			z++;
			if (z % 2 == 0)
				z++;

		}
		// disegno quello che c'e' di fronte
		b.setColor(cfront);
		b.fillRect(lx[0], ly[0], rx[0] - lx[0], ly[3] - ly[0]);
		// scrivo dove � rivolto il giocatore
		scrivi(b, "Facing : " + where[lookAt], bufferWidth, bufferWidth * 2 / 3);

		// disegno la piantina del labirinto
		int i = mazeLength > mazeWidth ? mazeLength : mazeWidth;
		int zoom = bufferWidth / i / 2;
		b.setColor(Color.black);
		for (int y = 0; y < mazeLength; y++)
			for (int x = 0; x < mazeWidth; x++) {
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
		// b.setXORMode(Color.magenta);
		b.setColor(Color.black);
		b.fillRect(player.column * zoom + 1, player.row * zoom + 1, zoom - 1,
				zoom - 1);
		b.setPaintMode();

		repaint();
	}

	public void setColor(Color c) {
		// definisco i colori per le pareti
		color[0] = c.darker();// parete nord
		color[1] = c.darker().darker();// parete est
		color[2] = c.darker();// parete sud
		color[3] = c;// parete ovest
		super.setColor(c);
	}

	// gestione degli eventi
	/**
	 * Quando vengono premute le frecce direzionali viene cancellata la vecchia
	 * posizione del giocatore e viene disegnata quella nuova
	 */
	public void keyPressed(KeyEvent e) {
		// se sono alla fine non mi muovo pi�
		if (player == exit && lookAt == direction.east)
			return;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			lookAt = (lookAt + 2) % 4;
			move();
			lookAt = (lookAt + 2) % 4;
			break;
		case KeyEvent.VK_LEFT:
			lookAt = (lookAt + 3) % 4;
			break;
		case KeyEvent.VK_RIGHT:
			lookAt = (lookAt + 1) % 4;
			break;
		case KeyEvent.VK_UP:
			move();
			break;
		}

		// disegno la nuova posizione del giocatore
		updateMaze();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

}
