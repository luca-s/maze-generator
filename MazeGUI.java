import Labirinto.*;
import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** questa classe e' un'applicazione che genera dei labirinti e li risolve. */

public class MazeGUI extends JFrame {

	// il componente che si occupa di rappresentare il labirinto
	private graphicMaze2D labirinth2D = new graphicMaze2D();
	private graphicMaze3D labirinth3D = new graphicMaze3D();
	private CardLayout cl = new CardLayout();
	private JPanel p = new JPanel(cl);
	private boolean is3D = false;

	public MazeGUI() {
		super("MazeGUI");
		setSize(600, 450);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setJMenuBar(creaMenuBar());
		Container c = getContentPane();
		p.add("2D", labirinth2D);
		p.add("3D", labirinth3D);
		c.add(p);
		addKeyListener(labirinth2D);
		setVisible(true);
	}

	// metto la visuale del labirinto in prima persona
	private void inside() {
		if (is3D)
			return;
		removeKeyListener(labirinth2D);
		addKeyListener(labirinth3D);
		cl.show(p, "3D");
		labirinth3D.setMaze(labirinth2D);
		is3D = true;
	}

	// metto la visuale del labirinto dall'alto
	private void over() {
		if (!is3D)
			return;
		removeKeyListener(labirinth3D);
		addKeyListener(labirinth2D);
		cl.show(p, "2D");
		labirinth2D.setMaze(labirinth3D);
		is3D = false;
	}

	// questa funzione crea la MenuBar del Frame
	private JMenuBar creaMenuBar() {
		// aggiungo tutti i menu alla MenuBar
		JMenuBar barra = new JMenuBar();
		barra.add(file());
		barra.add(action());
		barra.add(view());
		barra.add(color());
		return barra;
	}

	// crea il menu file
	private JMenu file() {

		JMenuItem save = new JMenuItem("Save...");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean saved = true;
				FileDialog save = new FileDialog(MazeGUI.this, "Save state",
						FileDialog.SAVE);
				save.setVisible(true);
				if (save.getFile() != null)
					if (is3D)
						saved = labirinth3D.saveMaze(save.getDirectory()
								+ save.getFile());
					else
						saved = labirinth2D.saveMaze(save.getDirectory()
								+ save.getFile());
				else if (!saved) {
					JOptionPane.showMessageDialog(null,
							"Impossibile salvare il file");
				}
			}
		});
		JMenuItem load = new JMenuItem("Load...");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean loaded = true;
				FileDialog load = new FileDialog(MazeGUI.this, "Load state",
						FileDialog.LOAD);
				load.setVisible(true);
				if (is3D)
					loaded = labirinth3D.setMaze(load.getDirectory()
							+ load.getFile());
				else
					loaded = labirinth2D.setMaze(load.getDirectory()
							+ load.getFile());
				if (!loaded) {
					JOptionPane.showMessageDialog(null,
							"Impossibile caricare il file");
				}
			}
		});
		JMenuItem exit = new JMenuItem("Exit ");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.ALT_MASK));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JMenu file = new JMenu("File  ");
		file.setMnemonic(KeyEvent.VK_F);
		file.add(load);
		file.add(save);
		file.addSeparator();
		file.add(exit);

		return file;
	}

	// crea il menu action
	private JMenu action() {

		JMenuItem newMaze = new JMenuItem("New Maze...  ");
		newMaze.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK));
		newMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (is3D)
					new newMazeDialog(MazeGUI.this, "Set Dimension", true,
							labirinth3D);
				else
					new newMazeDialog(MazeGUI.this, "Set Dimension", true,
							labirinth2D);
			}
		});

		JMenuItem fromYou = new JMenuItem("From You");
		fromYou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MazeGUI.this.over();
				labirinth2D.findExit(false);
			}
		});

		JMenuItem fromBeginning = new JMenuItem("From Beginning");
		fromBeginning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MazeGUI.this.over();
				labirinth2D.findExit(true);
			}
		});

		JMenu solve = new JMenu("Find Exit  ");
		solve.add(fromYou);
		solve.add(fromBeginning);

		JMenu action = new JMenu("Action  ");
		action.setMnemonic(KeyEvent.VK_A);
		action.add(newMaze);
		action.add(solve);

		return action;
	}

	// crea il menu view
	private JMenu view() {
		JMenuItem overview = new JMenuItem("Overview ");
		overview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.ALT_MASK));
		overview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MazeGUI.this.over();
			}
		});
		JMenuItem insideview = new JMenuItem("Inside view ");
		insideview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));
		insideview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MazeGUI.this.inside();
			}
		});

		JMenu view = new JMenu("View  ");
		view.setMnemonic(KeyEvent.VK_V);
		view.add(overview);
		view.add(insideview);

		return view;
	}

	private JMenu color() {
		// ciascuno di questi menuItem se premuto provoca
		// la chiamata al metodo setBackground del graphicMaze
		// visualizzato in questo momento
		JMenuItem blue = new JMenuItem("Blue");
		blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!is3D)
					labirinth2D.setColor(Color.blue);
				else
					labirinth3D.setColor(Color.blue);
			}
		});
		JMenuItem white = new JMenuItem("White");
		white.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!is3D)
					labirinth2D.setColor(Color.white);
				else
					labirinth3D.setColor(Color.white);
			}
		});
		JMenuItem orange = new JMenuItem("orange");
		orange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!is3D)
					labirinth2D.setColor(Color.orange);
				else
					labirinth3D.setColor(Color.orange);
			}
		});
		JMenuItem yellow = new JMenuItem("Yellow");
		yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!is3D)
					labirinth2D.setColor(Color.yellow);
				else
					labirinth3D.setColor(Color.yellow);
			}
		});
		JMenuItem pink = new JMenuItem("Pink");
		pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!is3D)
					labirinth2D.setColor(Color.pink);
				else
					labirinth3D.setColor(Color.pink);
			}
		});

		JMenu color = new JMenu("Color");
		color.setMnemonic(KeyEvent.VK_C);
		color.add(blue);
		color.add(white);
		color.add(orange);
		color.add(yellow);
		color.add(pink);

		return color;
	}

	/** fa partire l'applicazione */
	public static void main(String arg[]) {
		new MazeGUI();
	}

}
