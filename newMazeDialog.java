import Labirinto.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Questa classe crea un dialog in cui si possono inserire le dimesioni di un
 * labirinto che verranno passate al metodo newMaze del graphicMaze gm.
 */
public class newMazeDialog extends JDialog implements ActionListener {

	private TextField width = new TextField("40");
	private TextField height = new TextField("30");
	private Button ok = new Button("OK");
	private Button cancel = new Button("Cancel");
	private graphicMaze gm;

	// costruttore
	public newMazeDialog(Frame owner, String title, boolean modal,
			graphicMaze gm) {
		super(owner, title, modal);
		this.gm = gm;
		// setto il layout
		Container c = getContentPane();
		c.setLayout(new GridLayout(3, 2));

		// aggiungo i componenti al Dialog
		c.add(new Label("Set Width"));
		c.add(width);
		c.add(new Label("Set Height"));
		c.add(height);
		c.add(ok);
		c.add(cancel);

		// setto le funzioni dei componenti
		width.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				height.requestFocus();
			}
		});
		height.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok.requestFocus();
			}
		});
		ok.addActionListener(this);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newMazeDialog.this.dispose();
			}
		});

		// Setto i parametri iniziali del Dialog
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				newMazeDialog.this.dispose();
			}
		});
		setSize(400, 150);
		width.requestFocus();
		setVisible(true);
	}

	/**
	 * Controlla che le dimensioni inserite siano dei numeri compresi tra 3 e
	 * 250 e poi chiama il metodo gm.newMaze con queste dimensioni(se valide).
	 */
	public void actionPerformed(ActionEvent e) {
		String w = width.getText();
		int W;
		try {
			W = Integer.parseInt(w.trim());
		} catch (NumberFormatException n) {
			width.requestFocus();
			return;
		}
		if (W < 3 || W > 250) {
			width.requestFocus();
			return;
		}
		String h = height.getText();
		int H;
		try {
			H = Integer.parseInt(h.trim());
		} catch (NumberFormatException n) {
			height.requestFocus();
			return;
		}
		if (H < 3 || H > 250) {
			height.requestFocus();
			return;
		}
		gm.newMaze(W, H);
		dispose();
	}

}
