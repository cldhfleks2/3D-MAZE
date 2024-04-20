package com.gunziluv.front.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.gunziluv.front.Configuration;
import com.gunziluv.front.RunGame;
import com.gunziluv.input.InputHandler;

public class Launcher extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;
	public Configuration config = new Configuration();
	
	
	private int width = 800;
	private int height = 400;
	protected int button_width = 80;
	protected int button_height = 40;
	boolean running = false;
	Thread thread;
	//JFrame frame = new JFrame();
	
	public Launcher(int id) {
		//버튼등의 UI를 시스템환경과 동일하게 만들어주기
		try {///////////////////////////////////////////////////////////////////
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {
			e.printStackTrace();
		}///////////////////////
		
		setUndecorated(true); //GUI를 내맘대로 꾸미게 설정. => 최소화버튼,종료버튼등이 사라짐
		setTitle("hoons Maze Game Launcher");
		setSize(new Dimension(width , height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		if(id == 0) drawButtons();
		
		InputHandler input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		startMenu();
		repaint(); //버튼이 안보이는경우 다시그리는 메소드를통해 보이게함.
	}
	
	public void updateFrame() {
		if(InputHandler.dragged) {
			Point p = getLocation(); 
			setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, 
					p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}
	
	public void startMenu() {
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}
	
	public void stopMenu() {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		requestFocus();
		while (running) {
			
			try {
				renderMenu();
			} catch (IllegalStateException e) {
				System.out.println("Handled, baby.");
			}
			updateFrame();
		}
	}
	
	private void renderMenu() throws IllegalStateException{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3); //3D 게임을 만들기위해 2D면 2개필요?
			return;
		}	
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,  0,  800,  400);
		
		try {
			g.drawImage(ImageIO.read(Launcher.class.getResource("/menu_image.jpg")), 0, 0, 800, 400, null);
			//마우스 포인터가 올라가면 이미지가바뀜
			if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 &&
					InputHandler.MouseY > 130 && InputHandler.MouseY < 130 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_on.png")), 690, 130, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 134, 22, 20, null);
				if(InputHandler.MouseButton == 1) {
					config.loadConfiguration("res/settings/config.xml");
					dispose(); 
					new RunGame();
				}
			}else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_off.png")), 690, 130, 80, 30, null);
			}
			
			if(InputHandler.MouseX > 641 && InputHandler.MouseX < 641 + 130 &&
					InputHandler.MouseY > 170 && InputHandler.MouseY < 170 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_on.png")), 641, 170, 130, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 174, 22, 20, null);
				if(InputHandler.MouseButton == 1) {
					new Options();
				}
			}else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_off.png")), 641, 170, 130, 30, null);
			}
			
			if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 &&
					InputHandler.MouseY > 210 && InputHandler.MouseY < 210 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_on.png")), 690, 210, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 214, 22, 20, null);
				if(InputHandler.MouseButton == 1) System.out.println("Help");
			}else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_off.png")), 690, 210, 80, 30, null);
			}
			
			if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690 + 80 &&
					InputHandler.MouseY > 250 && InputHandler.MouseY < 250 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_on.png")), 690, 250, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 690 + 80, 254, 22, 20, null);
				if(InputHandler.MouseButton == 1) System.out.println("Bye Bye!");
				if(InputHandler.MouseButton == 1) System.exit(0); //종료
			}else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/exit_off.png")), 690, 250, 80, 30, null);
			}
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		
		g.dispose();
		bs.show();
	}
	
	private void drawButtons() {
		play = new JButton("Single Game");
		rplay = new Rectangle((width/2) - (button_width/2) , 40 , button_width , button_height);  
		play.setBounds(rplay);
		window.add(play);
		
		options = new JButton("Options");
		roptions = new Rectangle((width/2) - (button_width/2) , 90 , button_width , button_height);  
		options.setBounds(roptions);
		window.add(options);
		
		help = new JButton("More Help...");
		rhelp = new Rectangle((width/2) - (button_width/2) , 140 , button_width , button_height);  
		help.setBounds(rhelp);
		window.add(help);
		
		quit = new JButton("Quit");
		rquit = new Rectangle((width/2) - (button_width/2) , 190 , button_width , button_height);  
		quit.setBounds(rquit);
		window.add(quit);
		
		play.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration("res/settings/config.xml");
				dispose(); //현재의 실행창(JFrame)을 종료함.
				new RunGame();
			}
		});
		
		options.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); 
				new Options();
			}
		});
		
		help.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Help!");
			}
		});

		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}


}
