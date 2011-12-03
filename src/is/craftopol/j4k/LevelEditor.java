package is.craftopol.j4k;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

// wheeeeeeeeeee
public class LevelEditor extends JFrame implements ActionListener {
	private static LevelEditor instance;
	
	private LevelView level = new LevelView();
	
	private JComboBox typeCombo = new JComboBox(new String[] { "Line", "Rectangle", "Circle", "Spawn point", "Exit point" });
	
	private JToggleButton addButton = new JToggleButton("Add");
	private JToggleButton delButton = new JToggleButton("Remove");
	
	private JToggleButton getCodeButton = new JToggleButton("Get code");
	
	private JLabel statusLabel = new JLabel();
	
	private ButtonGroup optionGroup = new ButtonGroup();
	
	public LevelEditor() {
		super("Level Editor");
		
		// set up toolbar buttons
		
		getCodeButton.addActionListener(this);
		
		optionGroup.add(addButton);
		optionGroup.add(delButton);
		optionGroup.setSelected(addButton.getModel(), true);
		
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(level, BorderLayout.CENTER);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		toolbar.add(typeCombo);
		toolbar.addSeparator();
		toolbar.add(addButton);
		toolbar.add(delButton);
		toolbar.addSeparator();
		toolbar.add(getCodeButton);
		
		content.add(toolbar, BorderLayout.SOUTH);
		content.add(statusLabel, BorderLayout.NORTH);
		
		setContentPane(content);
		pack();
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Get code")) {
			StringSelection ss = new StringSelection(level.encodeJava());
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(ss, null);
			JOptionPane.showMessageDialog(this, "Code for level copied to clipboard.");
		}
	}
	
	public String getSelectedMode() {
		ButtonModel mod = optionGroup.getSelection();
		if(mod.equals(addButton.getModel()))
			return "add";
		else if(mod.equals(delButton.getModel()))
			return "remove";
		else throw new RuntimeException("wat");
	}
	
	public String getSelectedTool() {
		return (String) typeCombo.getSelectedItem();
	}
	
	public void setStatusText(String str) {
		statusLabel.setText(str);
	}
	
	public static LevelEditor getInstance() {
		if(instance == null)
			instance = new LevelEditor();
		return instance;
	}
	
	public static void main(String[] args) {
		getInstance();
	}
}
