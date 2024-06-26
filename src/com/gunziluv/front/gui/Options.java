package com.gunziluv.front.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.gunziluv.front.Configuration;

public class Options extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	private JTextField twidth, theight;
	private JLabel lwidth, lheight;
	private JButton OK;
	private Rectangle rOK, rresolution;
	private Choice resolution = new Choice();
	Configuration config = new Configuration();
	
	int w = 0;
	int h = 0;
	private int button_width = 80;
	private int button_height = 40;
	
	JPanel window = new JPanel();
	
	public Options() {
		
		setTitle("Options - hoons Maze Game Launcher");
		setSize(new Dimension(width ,  height));
		add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawButtons();
		window.repaint();
	}
	
	private void drawButtons() {
		OK = new JButton("OK");
		rOK = new Rectangle((width - 100) , (height-70), button_width, button_height - 10);
		OK.setBounds(rOK);
		window.add(OK);
		
		rresolution = new Rectangle(20, 40, 100, 40);
		resolution.setBounds(rresolution);
		resolution.add("640, 480");
		resolution.add("800, 600");
		resolution.add("1024, 768");
		resolution.select(1);
		window.add(resolution);
		
		lwidth = new JLabel("Width: ");
		lwidth.setBounds(30, 150, 120, 20);
		lwidth.setFont(new Font("Verdana", 2, 13));
		window.add(lwidth);
		
		lheight = new JLabel("Height: ");
		lheight.setBounds(30, 180, 120, 20);
		lheight.setFont(new Font("Verdana", 2, 13));
		window.add(lheight);
		
		twidth = new JTextField();
		twidth.setBounds(80, 150, 60, 20);
		window.add(twidth);
		
		theight = new JTextField();
		theight.setBounds(80, 180, 60, 20);
		window.add(theight);
		
		
		
		//누를때마다 설정을 파일에저장함
		OK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Launcher(0);
				config.saveConfiguration("width", parseWidth());
				config.saveConfiguration("height", parseHeight());
			}
		});
	}
	
	private void drop() {
		int selection = resolution.getSelectedIndex(); //해상도설정
		int w = 0;
		int h = 0;
		if(selection == 0) {
			w = 640; h = 480;
		}
		if(selection == 1 || selection == -1) { 
			w = 800; h = 600;
		}
		if(selection == 2) {
			w = 1024; h = 768;
		}
		
		
		
	}
	
	private int parseWidth() {
		int w = 800;
		try {
			w = Integer.parseInt(twidth.getText());
			return w;
		} catch (NumberFormatException e) {
			drop();
			return w;
		}
	}
	
	private int parseHeight() {
		int h = 600;
		try {
			h = Integer.parseInt(theight.getText());
			return h;
		} catch (NumberFormatException e) {
			drop();
			return h;
		}
	}
	
	
}
