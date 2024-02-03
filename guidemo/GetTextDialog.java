package guidemo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GetTextDialog extends JDialog {
	
	private boolean canceled = false;
	private JTextArea text;
	
	
	public static String showDialog(Component parent, String initialText) {
		GetTextDialog dialog = new GetTextDialog(frameAncestor(parent), initialText);
		dialog.setVisible(true);
		if (dialog.canceled)
			return null;
		else
			return dialog.text.getText();
	}
	
	private static Frame frameAncestor(Component c) {
		while (c != null && ! (c instanceof Frame))
			c = c.getParent();
		return (Frame)c;
	}
	

	private GetTextDialog(Frame parent, String initialText) {
		super(parent, "Input Your Text", true);
		JPanel content = new JPanel();
		setContentPane(content);
		content.setBackground(Color.LIGHT_GRAY);
		content.setLayout(new BorderLayout(3,3));
		text = new JTextArea(10,50);
		text.setMargin(new Insets(6,6,6,6));
		if (initialText != null)
			text.setText(initialText);
		content.add(text,BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		content.add(bottom,BorderLayout.SOUTH);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				canceled = true;
				dispose();
			}
		});
		JButton ok = new JButton("OK");
		ok.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});
		bottom.add(cancel);
		bottom.add(ok);
		pack();
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	

}
