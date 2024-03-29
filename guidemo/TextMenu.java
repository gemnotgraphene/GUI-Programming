package guidemo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;;


public class TextMenu extends JMenu {
	
	private final DrawPanel panel;   
	
	private JCheckBoxMenuItem bold;   // controls whether the text is bold or not.
	private JCheckBoxMenuItem italic; // controls whether the text is italic or not.
	private JRadioButtonMenuItem justifyLeft;
	private JRadioButtonMenuItem justifyRight;
	private JRadioButtonMenuItem justifyCenter;
	

	public TextMenu(DrawPanel owner) {
		super("Text");
		this.panel = owner;
		final JMenuItem change = new JMenuItem("Change Text...");
		change.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String currentText = panel.getTextItem().getText();
				String newText = GetTextDialog.showDialog(panel,currentText);
				if (newText != null && newText.trim().length() > 0) {
					panel.getTextItem().setText(newText);
					panel.repaint();
				}
			}
		});
		final JMenuItem size = new JMenuItem("Set Size...");
		size.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int currentSize = panel.getTextItem().getFontSize();
				String s = JOptionPane.showInputDialog(panel, "What font size do you want to use?",currentSize);
				if (s != null && s.trim().length() > 0) {
					try {
						int newSize = Integer.parseInt(s.trim()); // can throw NumberFormatException
						panel.getTextItem().setFontSize(newSize); // can throw IllegalArgumentException
						panel.repaint();
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(panel, s + " is not a legal text size.\n"
								+"Please enter a positive integer.");
					}
				}
			}
		});
		final JMenuItem lineSpacing = new JMenuItem("Set Line Spacing...");
		lineSpacing.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				double currentLineSpacing = panel.getTextItem().getLineHeightMultiplier();
				String s = JOptionPane.showInputDialog(panel, 
						"What line spacing do you want to use?", currentLineSpacing);
				if (s != null && s.trim().length() > 0) {
					try {
						double newLineSpacing = Double.parseDouble(s.trim()); // can throw NumberFormatException
						panel.getTextItem().setLineHeightMultiplier(newLineSpacing); // can throw IllegalArgumentException
						panel.repaint();
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(panel, s + " is not a legal line spacing.\n"
								+"Please enter a positive number.");
					}
				}
			}
		});
		final JMenuItem color = new JMenuItem("Set Color...");
		color.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Color currentColor = panel.getTextItem().getColor();
				Color newColor = JColorChooser.showDialog(panel, "Select Text Color", currentColor);
				if (newColor != null) {
					panel.getTextItem().setColor(newColor);
					panel.repaint();
				}
			}
		});
		italic = new JCheckBoxMenuItem("Italic");
		italic.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setItalic(italic.isSelected());
				panel.repaint();
			}
		});
		bold = new JCheckBoxMenuItem("Bold");
		bold.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setBold(bold.isSelected());
				panel.repaint();
			}
		});
		JMenu justifyTextMenu = new JMenu("Justify Text");
		ButtonGroup buttonGroup = new ButtonGroup();
		justifyLeft = new JRadioButtonMenuItem("Left");
		justifyRight = new JRadioButtonMenuItem("Right");
		justifyCenter = new JRadioButtonMenuItem("Center");
		justifyLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setJustify(TextItem.LEFT);
				panel.repaint();
			}
		});
		justifyRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setJustify(TextItem.RIGHT);
				panel.repaint();
			}
		});
		justifyCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setJustify(TextItem.CENTER);
				panel.repaint();
			}
		});
		buttonGroup.add(justifyLeft);
		buttonGroup.add(justifyRight);
		buttonGroup.add(justifyCenter);
		justifyTextMenu.add(justifyLeft);
		justifyTextMenu.add(justifyRight);
		justifyTextMenu.add(justifyCenter);
		
		add(change);
		addSeparator();
		add(size);
		add(lineSpacing);
		add(color);
		add(italic);
		add(bold);
		add(justifyTextMenu);
		addSeparator();
		add(makeFontNameSubmenu());
	}
	

	public void setDefaults() {
		italic.setSelected(false);
		bold.setSelected(false);
		justifyLeft.setSelected(true);
	}
	

	private JMenu makeFontNameSubmenu() {
		ActionListener setFontAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setFontName(evt.getActionCommand());
				panel.repaint();
			}
		};
		JMenu menu = new JMenu("Font Name");
		String[] basic = { "Serif", "SansSerif", "Monospace" };
		for (String f : basic) {
			JMenuItem m = new JMenuItem(f+ " Default");
			m.setActionCommand(f);
			m.addActionListener(setFontAction);
			m.setFont(new Font(f,Font.PLAIN,12));
			menu.add(m);
		}
		menu.addSeparator();
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		if (fonts.length <= 20) {
			for (String f : fonts) {
				JMenuItem m = new JMenuItem(f);
				m.addActionListener(setFontAction);
				m.setFont(new Font(f,Font.PLAIN,12));
				menu.add(m);
			}
		}
		else { //Too many items for one menu; divide them into several sub-sub-menus.
			char ch1 = 'A';
			char ch2 = 'A';
			JMenu m = new JMenu();
			int i = 0;
			while (i < fonts.length) {
				while (i < fonts.length && (Character.toUpperCase(fonts[i].charAt(0)) <= ch2 || ch2 == 'Z')) {
					JMenuItem item = new JMenuItem(fonts[i]);
					item.addActionListener(setFontAction);
					item.setFont(new Font(fonts[i],Font.PLAIN,12));
					m.add(item);
					i++;
				}
				if (i == fonts.length || (m.getMenuComponentCount() >= 12 && i < fonts.length-4)) {
					if (ch1 == ch2)
						m.setText("" + ch1);
					else
						m.setText(ch1 + " to " + ch2);
					menu.add(m);
					if (i < fonts.length)
						m = new JMenu();
					ch2++;
					ch1 = ch2;
				}
				else 
					ch2++;
			}
		}
		return menu;
	}
	

}
