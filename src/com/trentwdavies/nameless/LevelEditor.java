package com.trentwdavies.nameless;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
	
//wheeeeeeeeeee
public class LevelEditor extends JFrame implements ActionListener {	
	private static LevelEditor instance;
	
	private LevelView level = new LevelView();
	
	private JComboBox typeCombo = new JComboBox(new Class<?>[] { Button.class, Line.class, Triangle.class, ExitPoint.class });
	
	private JToggleButton addremButton = new JToggleButton("Add/remove");
	private JToggleButton cloneButton = new JToggleButton("Clone");
	private JToggleButton animButton = new JToggleButton("Animate");
	
	private JButton loadButton = new JButton("Load");
	private JButton getCodeButton = new JButton("Get code");
	
	private JLabel statusLabel = new JLabel();
	private JLabel dragLabel = new JLabel();
	
	private ButtonGroup optionGroup = new ButtonGroup();
	
	public LevelEditor() {
		super("Level Editor");
		
		// set up toolbar buttons
		
		loadButton.addActionListener(this);
		getCodeButton.addActionListener(this);
		
		optionGroup.add(addremButton);
		optionGroup.add(cloneButton);
		optionGroup.add(animButton);
		optionGroup.setSelected(addremButton.getModel(), true);
		
		JPanel content = new JPanel(new BorderLayout());
		
		content.add(level, BorderLayout.CENTER);
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		toolbar.add(typeCombo);
		toolbar.addSeparator();
		toolbar.add(addremButton);
		toolbar.add(cloneButton);
		toolbar.add(animButton);
		toolbar.addSeparator();
		toolbar.add(loadButton);
		toolbar.add(getCodeButton);
		
		content.add(toolbar, BorderLayout.SOUTH);
		
		JPanel status = new JPanel();
		status.add(statusLabel);
		status.add(dragLabel);
		content.add(status, BorderLayout.NORTH);
		
		setContentPane(content);
		pack();
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Load")) {
			String code = JOptionPane.showInputDialog(this, "Enter level string");
			code = code.replace("\t", "").replace(" ", ""); // remove tabs and spaces
			level.decode(code);
		} else if(cmd.equals("Get code")) {
			StringSelection ss = new StringSelection(level.encodeJava());
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(ss, null);
			JOptionPane.showMessageDialog(this, "Code for level copied to clipboard.");
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
		statusLabel.setText(str);
	}
	
	public void setDragText(String str) {
		dragLabel.setText(str);
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
