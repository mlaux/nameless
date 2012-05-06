package is.craftopol.j4k;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import netscape.javascript.JSObject;
	
//wheeeeeeeeeee
public class LevelEditor extends JApplet implements ActionListener {	
	private static LevelEditor instance;
	
	private LevelView level = new LevelView();
	
	private JComboBox typeCombo = new JComboBox(new Class<?>[] { Button.class, Line.class, Triangle.class, ExitPoint.class });
	
	private JToggleButton addremButton = new JToggleButton("Add/remove");
	private JToggleButton cloneButton = new JToggleButton("Clone");
	private JToggleButton animButton = new JToggleButton("Animate");
	
	private JButton loadButton = new JButton("Load");
	
	private JButton playButton = new JButton("Play level");
	private JButton getCodeButton = new JButton("Get code");
	
	private JLabel statusLabel = new JLabel();
	private JLabel dragLabel = new JLabel();
	
	private ButtonGroup optionGroup = new ButtonGroup();
	
	public LevelEditor() {
		// set up toolbar buttons
		setLayout(new BorderLayout());
		
		loadButton.addActionListener(this);
		playButton.addActionListener(this);
		getCodeButton.addActionListener(this);
		
		optionGroup.add(addremButton);
		optionGroup.add(animButton);
		optionGroup.setSelected(addremButton.getModel(), true);
		
		add(level, BorderLayout.CENTER);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		toolbar.add(typeCombo);
		toolbar.addSeparator();
		toolbar.add(addremButton);
		toolbar.add(animButton);
		toolbar.addSeparator();
		toolbar.add(loadButton);
		toolbar.add(playButton);
		toolbar.add(getCodeButton);
		
		add(toolbar, BorderLayout.SOUTH);
		
		JPanel status = new JPanel();
		status.add(statusLabel);
		status.add(dragLabel);
		
		add(status, BorderLayout.NORTH);
		
		level.registerListeners();
	}
	
	public void init() {
		instance = this;
	}
	
	public static void main(String[] args) {
		LevelEditor le = getInstance();
		
		JFrame frame = new JFrame("Nameless Level Editor");
		
		frame.add(le);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Load")) {
			String code = JOptionPane.showInputDialog(this, "Enter level string");
			code = code.replace("\t", "").replace(" ", ""); // remove tabs and spaces
			level.decode(code);
		} else if(cmd.equals("Play level")) {
			try {
				JSObject.getWindow(this).call("done", new Object[] { level.encodeJava() });
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} else if(cmd.equals("Get code")) {
			JDialog dialog = new JDialog((Frame) null, "Code");
			JTextPane pane = new JTextPane();
			pane.setEditable(false);
			pane.setText(level.encodeJava());
			
			dialog.add(new JScrollPane(pane));

			dialog.setSize(new Dimension(320, 200));
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		}
	}
	
	public String getSelectedMode() {
		ButtonModel mod = optionGroup.getSelection();
		if(mod.equals(addremButton.getModel()))
			return "addremove";
		else if(mod.equals(cloneButton.getModel()))
			return "clone";
		else if(mod.equals(animButton.getModel()))
			return "animate";
		else throw new RuntimeException("wat");
	}
	
	public Class<?> getSelectedTool() {
		return (Class<?>) typeCombo.getSelectedItem();
	}
	
	public void setStatusText(String str) {
		statusLabel.setText("Current: " + str);
	}
	
	public void setDragText(String str) {
		dragLabel.setText("Drag: " + str);
	}
	
	public static LevelEditor getInstance() {
		if(instance == null)
			instance = new LevelEditor();
		return instance;
	}
}
