import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Maze.graphicMaze2D;
import Maze.graphicMaze3D;

/** questa classe e' un'applicazione che genera dei labirinti e li risolve. */

public class MazeGUI extends JFrame {

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
		labirinth3D.setFromMaze(labirinth2D);
		is3D = true;
	}

	// metto la visuale del labirinto dall'alto
	private void over() {
		if (!is3D) {
			return;
		}
		removeKeyListener(labirinth3D);
		addKeyListener(labirinth2D);
		cl.show(p, "2D");
		labirinth2D.setFromMaze(labirinth3D);
		is3D = false;
	}

	// questa funzione crea la MenuBar del Frame
	private JMenuBar creaMenuBar() {
		// aggiungo tutti i menu alla MenuBar
		JMenuBar barra = new JMenuBar();
		barra.add(action());
		barra.add(view());
		barra.add(color());		
		return barra;
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
		ColorChooserButton colorChooser = new ColorChooserButton(Color.WHITE);
		colorChooser.setText("Select your colour");
		colorChooser.addColorChangedListener(new ColorChooserButton.ColorChangedListener() {
		    @Override
		    public void colorChanged(Color newColor) {
				if (!is3D)
					labirinth2D.setColor(newColor);
				else
					labirinth3D.setColor(newColor);
		    }
		});
		
		JMenu color = new JMenu("Color");
		color.setMnemonic(KeyEvent.VK_C);
		color.add(colorChooser);

		return color;
	}

	/** fa partire l'applicazione */
	public static void main(String arg[]) {
		new MazeGUI();
	}

}
